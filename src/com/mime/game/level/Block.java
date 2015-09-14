package com.mime.game.level;

import java.util.ArrayList;
import java.util.List;

import com.mime.game.graphics.Sprite;

public class Block {

	protected boolean solid = false; // if wall exist
	public static Block solidWall = new SolidBlock();
	public static Block nonsolidWall = new NonSolidWall();

	public List<Sprite> sprites = new ArrayList<Sprite>();

	public void addSprite(Sprite sprite) {
		sprites.add(sprite);
	}

	public boolean isSolid() {
		return solid;
	}

	public void setSolid(boolean solid) {
		this.solid = solid;
	}
}
