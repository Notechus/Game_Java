package com.mime.game.graphics;

import com.mime.game.Game;

public class Render3D extends Render {

	public double[] zBuffer;
	private static final double renderDistance = 5000.0;

	public Render3D(int width, int height) {
		super(width, height);
		zBuffer = new double[width * height];
	}

	public void floor(Game game) {

		// borders of floor and ceiling displayed
		double floorPosition = 8;
		double ceilingPosition = 8;

		// movement, we can add this to xx and yy to get movement
		double forward = game.controls.getZ();
		double right = game.controls.getX();

		// rotation stuff
		double rotation = game.controls.getRotation();
		double cosine = Math.cos(rotation);
		double sine = Math.sin(rotation);

		for (int y = 0; y < height; y++) {
			double ceiling = (y - height / 2.0) / height;

			double z = floorPosition / ceiling;

			if (ceiling < 0) {
				z = ceilingPosition / -ceiling; // allows us to control height of ceiling
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
				pixels[x + y * width] = ((xPix & 15) * 16) | ((yPix & 15) * 16) << 8;

				if (z > renderDistance) {
					pixels[x + y * width] = 0;
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
