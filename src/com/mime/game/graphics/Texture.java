package com.mime.game.graphics;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Texture {
	public static Render floor = loadBitmap("/textures/floor.png");
	public static Render wall = loadBitmap("/textures/wall.png");

	public static Render loadBitmap(String fileName) {

		try {
			BufferedImage image = ImageIO.read(Texture.class.getResource(fileName));
			int width = image.getWidth();
			int height = image.getHeight();
			Render result = new Render(width, height);
			image.getRGB(0, 0, width, height, result.pixels, 0, width);

			return result;
		} catch (Exception ex) {
			System.err.println("CRASH");
			throw new RuntimeException(ex);
		}
	}
}
