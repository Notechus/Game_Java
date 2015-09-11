package com.mime.game.level;

public class Block {

	protected boolean solid = false; // if wall exist
	public static Block solidWall = new SolidBlock();
	public static Block nonsolidWall = new NonSolidWall();

	public boolean isSolid() {
		return solid;
	}

	public void setSolid(boolean solid) {
		this.solid = solid;
	}
}
