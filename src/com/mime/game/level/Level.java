package com.mime.game.level;

import java.util.Random;

import com.mime.game.graphics.Sprite;

public class Level {

	public Block[] blocks;

	public final int width, height;

	public Level(int width, int height) {
		this.width = width;
		this.height = height;
		blocks = new Block[width * height];
		Random random = new Random();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Block block = null;
				if (random.nextInt(8) == 0) {
					block = new SolidBlock();
				} else {
					block = new Block();
					block.addSprite(new Sprite(0, 0, 0));
				}
				blocks[x + y * width] = block;
			}
		}
	}

	public Block create(int x, int y) {
		if (x < 0 || y < 0 || x >= width || y >= height) {
			return Block.solidWall;
		}

		return blocks[x + y * width];
	}

}
