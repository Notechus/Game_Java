package com.mime.game.graphics;

import com.mime.game.Display;

public class Render {

	private final int width;
	private final int height;
	public final int[] pixels;

	public Render(int width, int height) {
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	// method to draw object
	public void draw(Render render, int xOffset, int yOffset) {
		for (int y = 0; y < render.getHeight(); y++) {
			int yPix = y + yOffset;
			if (yPix < 0 || yPix >= Display.HEIGHT) {
				continue;
			}
			for (int x = 0; x < render.getWidth(); x++) {
				int xPix = x + xOffset;
				if (xPix < 0 || xPix >= Display.WIDTH) {
					continue;
				}
				// alpha is used to track updates
				int alpha = render.pixels[x + y * render.getWidth()];
				if (alpha > 0) { // if alpha > 0 then render
					pixels[xPix + yPix * width] = render.pixels[x + y * render.getWidth()];
					// System.out.println("x: " + x + " y: " + y);
				}
			}
		}
	}

}
