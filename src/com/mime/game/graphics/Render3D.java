package com.mime.game.graphics;

import com.mime.game.Game;
import com.mime.game.input.Controller;

public class Render3D extends Render {

	public double[] zBuffer;
	private static final double renderDistance = 5000.0;

	// movement stuff
	private double forward; // stores z value of movement
	private double up; // stores y value of movement
	private double right; // stores x value of movement
	private double cosine; // used in rotation
	private double sine; // used in rotation
	private double walking; // walking - "bumping" thingy using sine function

	public Render3D(int width, int height) {
		super(width, height);
		zBuffer = new double[width * height];
	}

	public void floor(Game game) {

		// borders of floor and ceiling displayed
		double floorPosition = 8;
		double ceilingPosition = 8;

		// movement, we can add this to xx and yy to get movement or to z to jump
		forward = game.controls.getZ();
		right = game.controls.getX();
		up = game.controls.getY();
		walking = 0;
		// rotation stuff
		double rotation = game.controls.getRotation(); // Math.sin(game.getTime() % 1000.0 / 80);// game.controls.getRotation();
		cosine = Math.cos(rotation);
		sine = Math.sin(rotation);

		for (int y = 0; y < height; y++) {
			double ceiling = (y - height / 2.0) / height;

			double z = (floorPosition + up) / ceiling;
			// "bumping" thingy
			if (Controller.walking) {
				// walking animation
				walking = Math.sin(game.getTime() / 6.0) * 0.5;
				z = (floorPosition + up + walking) / ceiling;
			}
			if (Controller.crouchWalk) {
				// walking = Math.sin(game.getTime() / 6.0) * 0.25;
				z = (floorPosition + up + walking) / ceiling;
			}
			if (Controller.running) {
				// walking = Math.sin(game.getTime() / 6.0) * 0.8;
				z = (floorPosition + up + walking) / ceiling;
			}
			if (Controller.jumping) {
				walking = 0;
				z = (floorPosition + up) / ceiling;
			}

			if (ceiling < 0) {
				z = (ceilingPosition - up) / -ceiling;
				if (Controller.walking) {
					z = (ceilingPosition - up - walking) / -ceiling;
				}
			}

			for (int x = 0; x < width; x++) {
				double depth = (x - width / 2.0) / height;
				depth *= z;
				double xx = depth * cosine + z * sine; // rotation thingy(sin & cos)
				double yy = z * cosine - depth * sine;

				// xx and yy casted to int and added movement
				int xPix = (int) (xx + right);
				int yPix = (int) (yy + forward);

				zBuffer[x + y * width] = z;
				pixels[x + y * width] = Texture.floor.pixels[(xPix & 7) + (yPix & 7) * 8];

				if (z > 500) {
					pixels[x + y * width] = 0;
				}
			}
		}
	}

	public void renderWall(double xLeft, double xRight, double zDistanceLeft, double zDistanceRight, double yHeight) {

		double upCorrect = 0.062; // walls doesnt jump with us
		double rightCorrect = 0.062;// could be just correct but we make 3 in case it screws up
		double forwardCorrect = 0.062;
		double walkCorrect = -0.062;

		// left side calculation
		double xcLeft = ((xLeft) - (right * rightCorrect)) * 2; // xcLeft = calculation of left wall x
		double zcLeft = ((zDistanceLeft) - (forward * forwardCorrect)) * 2;

		double rotLeftSideX = xcLeft * cosine - zcLeft * sine;
		double yCornerTL = ((-yHeight - (-up * upCorrect + (walking * walkCorrect)))) * 2;// TL=top left
		double yCornerBL = ((+0.5 - yHeight - (-up * upCorrect + (walking * walkCorrect)))) * 2;// BL=bottom left
		double rotLeftSideZ = zcLeft * cosine + xcLeft * sine;

		// right side calculation
		double xcRight = ((xRight) - (right * rightCorrect)) * 2;
		double zcRight = ((zDistanceRight) - (forward * forwardCorrect)) * 2;

		double rotRightSideX = xcRight * cosine - zcRight * sine;
		double yCornerTR = ((-yHeight - (-up * upCorrect + (walking * walkCorrect)))) * 2;
		double yCornerBR = ((+0.5 - yHeight - (-up * upCorrect + (walking * walkCorrect)))) * 2;
		double rotRightSideZ = zcRight * cosine + xcRight * sine;

		double xPixelLeft = (rotLeftSideX / rotLeftSideZ * height + width / 2);
		double xPixelRight = (rotRightSideX / rotRightSideZ * height + width / 2);

		double tex30 = 0;
		double tex40 = 8;
		double clip = 0.5; // for clipping fix

		if (rotLeftSideZ < clip && rotRightSideZ < clip) {
			return; // if it already happened, return
		}

		if (rotLeftSideZ < clip) {
			// fix clipping - Cohen-Sutherland algorithm, left side

			double clip0 = (clip - rotLeftSideZ) / (rotRightSideZ - rotLeftSideZ);
			rotLeftSideZ = rotLeftSideZ + (rotRightSideZ - rotLeftSideZ) * clip0;
			rotLeftSideX = rotLeftSideX + (rotRightSideX - rotLeftSideX) * clip0;
			tex30 = tex30 + (tex40 - tex30) * clip0;
		}

		if (rotRightSideZ < clip) {
			double clip0 = (clip - rotLeftSideZ) / (rotRightSideZ - rotLeftSideZ);
			rotRightSideZ = rotLeftSideZ + (rotRightSideZ - rotLeftSideZ) * clip0;
			rotRightSideX = rotLeftSideX + (rotRightSideX - rotLeftSideX) * clip0;
			tex30 = tex30 + (tex40 - tex30) * clip0;
		}

		if (xPixelLeft >= xPixelRight) {
			return;
		}

		int xPixelLeftInt = (int) (xPixelLeft);
		int xPixelRightInt = (int) (xPixelRight);

		if (xPixelLeftInt < 0) {
			xPixelLeftInt = 0;
		}
		if (xPixelRightInt > width) {
			xPixelRightInt = width;
		}

		double yPixelLeftTop = yCornerTL / rotLeftSideZ * height + height / 2.0;
		double yPixelLeftBottom = yCornerBL / rotLeftSideZ * height + height / 2.0;
		double yPixelRightTop = yCornerTR / rotRightSideZ * height + height / 2.0;
		double yPixelRightBottom = yCornerBR / rotRightSideZ * height + height / 2.0;

		// texture thing
		double tex1 = 1 / rotLeftSideZ;
		double tex2 = 1 / rotRightSideZ;
		double tex3 = tex30 / rotLeftSideZ;
		double tex4 = tex40 / rotRightSideZ - tex3;

		for (int x = xPixelLeftInt; x < xPixelRightInt; x++) {
			double pixelRotation = (x - xPixelLeft) / (xPixelRight - xPixelLeft);

			int xTexture = (int) ((tex3 + tex4 * pixelRotation) / (tex1 + (tex2 - tex1) * pixelRotation));

			double yPixelTop = yPixelLeftTop + (yPixelRightTop - yPixelLeftTop) * pixelRotation;
			double yPixelBottom = yPixelLeftBottom + (yPixelRightBottom - yPixelLeftBottom) * pixelRotation;

			int yPixelTopInt = (int) (yPixelTop);
			int yPixelBottomInt = (int) (yPixelBottom);

			if (yPixelTopInt < 0) {
				yPixelTopInt = 0;
			}
			if (yPixelBottomInt > height) {
				yPixelBottomInt = height;
			}

			for (int y = yPixelTopInt; y < yPixelBottomInt; y++) {
				try {
					double pixelRotationY = (y - yPixelTop) / (yPixelBottom - yPixelTop);
					int yTexture = (int) (8 * pixelRotationY);
					// pixels[x + y * width] = 0x1B91E0;
					// pixels[x + y * width] = xTexture * 100 + yTexture * 100 * 256;
					pixels[x + y * width] = Texture.wall.pixels[(xTexture & 7) + (yTexture & 7) * 8];
					zBuffer[x + y * width] = 1 / (tex1 + (tex2 - tex1) * pixelRotation) * 12;
				} catch (ArrayIndexOutOfBoundsException ex) {
					ex.printStackTrace();
					continue;
				}

			}
		}

	}

	public void renderDistanceLimiter() {
		for (int i = 0; i < width * height; i++) {
			int color = pixels[i];
			int brightness = (int) (renderDistance / (zBuffer[i]));

			if (brightness < 0) {
				brightness = 0;
			}

			if (brightness > 255) {
				brightness = 255;
			}

			int r = ((color >> 16) & 0xff) * brightness / 255;
			int g = ((color >> 8) & 0xff) * brightness / 255;
			int b = ((color) & 0xff) * brightness / 255;

			pixels[i] = r << 16 | g << 8 | b;
		}
	}
}
