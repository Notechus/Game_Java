package com.mime.game.graphics;

import com.mime.game.Game;
import com.mime.game.input.Controller;
import com.mime.game.level.Block;
import com.mime.game.level.Level;

public class Render3D extends Render {

	public double[] zBuffer;
	public double[] zBufferWall;
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
		zBufferWall = new double[width];
	}

	public void floor(Game game) {

		for (int x = 0; x < width; x++) {
			zBufferWall[x] = 0.0;
		}

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

		Level level = game.level;
		int size = 20;

		for (int xBlock = -size; xBlock <= size; xBlock++) {
			for (int zBlock = -size; zBlock <= size; zBlock++) {
				Block block = level.create(xBlock, zBlock);
				Block east = level.create(xBlock + 1, zBlock);
				Block south = level.create(xBlock, zBlock + 1);

				if (block.isSolid()) {
					if (!east.isSolid()) {
						renderWall(xBlock + 1, xBlock + 1, zBlock, zBlock + 1, 0);
						renderWall(xBlock + 1, xBlock + 1, zBlock, zBlock + 1, 0.5);
					}
					if (!south.isSolid()) {
						renderWall(xBlock + 1, xBlock, zBlock + 1, zBlock + 1, 0);
						renderWall(xBlock + 1, xBlock, zBlock + 1, zBlock + 1, 0.5);
					}
				} else {
					if (east.isSolid()) {
						renderWall(xBlock + 1, xBlock + 1, zBlock + 1, zBlock, 0);
						renderWall(xBlock + 1, xBlock + 1, zBlock + 1, zBlock, 0.5);
					}
					if (south.isSolid()) {
						renderWall(xBlock, xBlock + 1, zBlock + 1, zBlock + 1, 0);
						renderWall(xBlock, xBlock + 1, zBlock + 1, zBlock + 1, 0.5);
					}
				}
			}
		}
		for (int xBlock = -size; xBlock <= size; xBlock++) {
			for (int zBlock = -size; zBlock <= size; zBlock++) {
				Block block = level.create(xBlock, zBlock);
				for (int s = 0; s < block.sprites.size(); s++) {
					Sprite sprite = block.sprites.get(s);
					renderSprite(xBlock + sprite.getX(), sprite.getY(), zBlock + sprite.getZ());
				}
			}
		}
	}

	public void renderSprite(double x, double y, double z) {

		double upCorrect = 0.0625; // walls doesnt jump with us
		double rightCorrect = 0.0625;// could be just correct but we make 3 in case it screws up
		double forwardCorrect = 0.0625;
		double walkCorrect = -0.0625;

		double xc = ((x / 2) - (right * rightCorrect)) * 2; // xcLeft = calculation of left wall x
		double yc = ((y / 2) - (up * upCorrect)) * 2;
		double zc = ((z / 2) - (forward * forwardCorrect)) * 2;

		double rotX = xc * cosine - zc * sine; // rotation x
		double rotY = yc;
		double rotZ = zc * cosine - xc * sine;

		double xCentre = 400.0;
		double yCentre = 300.0;

		double xPixel = rotX / rotZ * height + xCentre;
		double yPixel = rotY / rotZ * height + yCentre;

		double xPixelL = xPixel - 16 / zc;
		double xPixelR = xPixel + 16 / zc;

		double yPixelL = yPixel - 16 / zc;
		double yPixelR = yPixel + 16 / zc;

		// cast to int

		int xpl = (int) xPixelL;
		int xpr = (int) xPixelR;
		int ypl = (int) yPixelL;
		int ypr = (int) yPixelR;

		// clipping fix
		if (xpl < 0) {
			xpl = 0;
		}
		if (xpr > width) {
			xpr = width;
		}
		if (ypl < 0) {
			ypl = 0;
		}
		if (ypr > height) {
			ypr = height;
		}

		for (int yp = ypl; yp < ypr; yp++) {
			for (int xp = xpl; xp < xpr; xp++) {
				if (zBuffer[xp + yp * width] > rotZ) {
					pixels[xp + yp * width] = 0xFF0000;
					zBuffer[xp + yp * width] = rotZ;
				}
			}
		}

	}

	public void renderWall(double xLeft, double xRight, double zDistanceLeft, double zDistanceRight, double yHeight) {

		double upCorrect = 0.0625; // walls doesnt jump with us
		double rightCorrect = 0.0625;// could be just correct but we make 3 in case it screws up
		double forwardCorrect = 0.0625;
		double walkCorrect = -0.0625;

		// left side calculation
		double xcLeft = ((xLeft / 2) - (right * rightCorrect)) * 2; // xcLeft = calculation of left wall x
		double zcLeft = ((zDistanceLeft / 2) - (forward * forwardCorrect)) * 2;

		double rotLeftSideX = xcLeft * cosine - zcLeft * sine;
		double yCornerTL = ((-yHeight - (-up * upCorrect + (walking * walkCorrect)))) * 2;// TL=top left
		double yCornerBL = ((+0.5 - yHeight - (-up * upCorrect + (walking * walkCorrect)))) * 2;// BL=bottom left
		double rotLeftSideZ = zcLeft * cosine + xcLeft * sine;

		// right side calculation
		double xcRight = ((xRight / 2) - (right * rightCorrect)) * 2;
		double zcRight = ((zDistanceRight / 2) - (forward * forwardCorrect)) * 2;

		double rotRightSideX = xcRight * cosine - zcRight * sine;
		double yCornerTR = ((-yHeight - (-up * upCorrect + (walking * walkCorrect)))) * 2;
		double yCornerBR = ((+0.5 - yHeight - (-up * upCorrect + (walking * walkCorrect)))) * 2;
		double rotRightSideZ = zcRight * cosine + xcRight * sine;

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
			tex40 = tex30 + (tex40 - tex30) * clip0;
		}

		double xPixelLeft = (rotLeftSideX / rotLeftSideZ * height + width / 2);
		double xPixelRight = (rotRightSideX / rotRightSideZ * height + width / 2);

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
			double zWall = (tex1 + (tex2 - tex1) * pixelRotation);

			if (zBufferWall[x] > zWall) {
				continue;
			}
			zBufferWall[x] = zWall;

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
