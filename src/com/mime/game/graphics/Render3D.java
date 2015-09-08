package com.mime.game.graphics;

import java.util.Random;

import com.mime.game.Game;
import com.mime.game.input.Controller;

public class Render3D extends Render {

	public double[] zBuffer;
	private static final double renderDistance = 5000.0;

	// movement stuff
	private double forward;
	private double up;
	private double right;
	private double cosine;
	private double sine;

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

		// walking animation
		double walking = Math.sin(game.getTime() / 6.0) * 0.5;
		if (Controller.crouchWalk) {
			walking = Math.sin(game.getTime() / 6.0) * 0.25;
		}
		if (Controller.running) {
			walking = Math.sin(game.getTime() / 6.0) * 0.8;
		}

		// rotation stuff
		double rotation = game.controls.getRotation(); // Math.sin(game.getTime() % 1000.0 / 80);// game.controls.getRotation();
		cosine = Math.cos(rotation);
		sine = Math.sin(rotation);

		for (int y = 0; y < height; y++) {
			double ceiling = (y - height / 2.0) / height;

			double z = (floorPosition + up) / ceiling;
			if (Controller.walking) {
				z = (floorPosition + up + walking) / ceiling;
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

	public void walls(Game game) {
		Random random = new Random(100);

		for (int i = 0; i < 20000; i++) {
			double xx = random.nextDouble();
			double yy = random.nextDouble();
			double zz = 2;

			int xPixel = (int) (xx / zz * height / 2 + width / 2);
			int yPixel = (int) (yy / zz * height / 2 + height / 2);

			if (xPixel >= 0 && xPixel < width && yPixel >= 0 && yPixel < height) {
				pixels[xPixel + yPixel * width] = 0xfffff;
			}
		}

		for (int i = 0; i < 20000; i++) {
			double xx = random.nextDouble() - 1;
			double yy = random.nextDouble();
			double zz = 2;

			int xPixel = (int) (xx / zz * height / 2 + width / 2);
			int yPixel = (int) (yy / zz * height / 2 + height / 2);

			if (xPixel >= 0 && xPixel < width && yPixel >= 0 && yPixel < height) {
				pixels[xPixel + yPixel * width] = 0xfffff;
			}
		}

		for (int i = 0; i < 20000; i++) {
			double xx = random.nextDouble() - 1;
			double yy = random.nextDouble() - 1;
			double zz = 2;

			int xPixel = (int) (xx / zz * height / 2 + width / 2);
			int yPixel = (int) (yy / zz * height / 2 + height / 2);

			if (xPixel >= 0 && xPixel < width && yPixel >= 0 && yPixel < height) {
				pixels[xPixel + yPixel * width] = 0xfffff;
			}
		}

		for (int i = 0; i < 20000; i++) {
			double xx = random.nextDouble();
			double yy = random.nextDouble() - 1;
			double zz = 2;

			int xPixel = (int) (xx / zz * height / 2 + width / 2);
			int yPixel = (int) (yy / zz * height / 2 + height / 2);

			if (xPixel >= 0 && xPixel < width && yPixel >= 0 && yPixel < height) {
				pixels[xPixel + yPixel * width] = 0xfffff;
			}
		}
	}

	public void renderWall(double xLeft, double xRight, double zDistance, double yHeight) {

		// left side calculation
		double xcLeft = ((xLeft) - right) * 2; // xcLeft = calculation of left wall x
		double zcLeft = ((zDistance) - forward) * 2;

		double rotLeftSideX = xcLeft * cosine - zcLeft * sine;
		double yCornerTL = ((-yHeight) - up) * 2;// TL=top left
		double yCornerBL = ((+0.5 - yHeight) - up) * 2;// BL=bottom left
		double rotLeftSideZ = zcLeft * cosine + xcLeft * sine;

		// right side calculation
		double xcRight = ((xRight) - right) * 2;
		double zcRight = ((zDistance) - forward) * 2;

		double rotRightSideX = xcRight * cosine - zcRight * sine;
		double yCornerTR = ((-yHeight) - up) * 2;
		double yCornerBR = ((+0.5 - yHeight) - up) * 2;
		double rotRightSideZ = zcRight * cosine + xcRight * sine;

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

		double yPixelLeftTop = (int) (yCornerTL / rotLeftSideZ * height + height / 2);
		double yPixelLeftBottom = (int) (yCornerBL / rotLeftSideZ * height + height / 2);
		double yPixelRightTop = (int) (yCornerTR / rotRightSideZ * height + height / 2);
		double yPixelRightBottom = (int) (yCornerBR / rotRightSideZ * height + height / 2);

		for (int x = xPixelLeftInt; x < xPixelRightInt; x++) {
			double pixelRotation = (x - xPixelLeft) / (xPixelRight - xPixelLeft);

			double yPixelTop = yPixelLeftTop + (yPixelRightTop - yPixelLeftTop) * pixelRotation;
			double yPixelBottom = yPixelLeftBottom + (yPixelRightBottom - yPixelLeftBottom) * pixelRotation;

			int yPixelTopInt = (int) (yPixelTop);
			int yPixelBottomInt = (int) (yPixelBottom);

			if (yPixelTopInt < 0) {
				yPixelTopInt = 0;
			}
			if (yPixelTopInt > height) {
				yPixelTopInt = height;
			}

			for (int y = yPixelTopInt; y < yPixelBottomInt; y++) {
				pixels[x + y * width] = 0x1B91E0;
				zBuffer[x + y * width] = 0;
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
