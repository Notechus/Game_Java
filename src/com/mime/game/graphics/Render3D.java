package com.mime.game.graphics;

import com.mime.game.Game;

public class Render3D extends Render {

	public Render3D(int width, int height) {
		super(width, height);
	}

	public void floor(Game game) {

		// borders of floor and ceiling displayed
		double floorPosition = 8;
		double ceilingPosition = 8;

		// movement, we can add this to xx and yy to get movement
		double forward = game.getTime() / 5.0;
		double right = game.getTime() / 5.0;

		// rotation stuff
		double rotation = 0;// = game.getTime() / 100.0;
		double cosine = Math.cos(rotation);
		double sine = Math.sin(rotation);

		for (int y = 0; y < height; y++) {
			double ceiling = (y - height / 2.0) / height;

			double z = floorPosition / ceiling;

			if (ceiling < 0) {
				// ceiling = -ceiling;
				z = ceilingPosition / -ceiling; // allows us to control height of ceiling
			}

			for (int x = 0; x < width; x++) {
				double depth = (x - width / 2.0) / height;
				depth *= z;
				double xx = depth * cosine + z * sine; // rotation thingy(sin & cos)
				double yy = z * cosine - depth * sine;
				int xPix = (int) (xx);
				int yPix = (int) (yy);
				pixels[x + y * width] = ((xPix & 15) * 16) | ((yPix & 15) * 16) << 8;
			}
		}
	}
}
