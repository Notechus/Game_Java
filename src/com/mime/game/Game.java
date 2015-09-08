package com.mime.game;

public class Game {

	private int time;

	public void tick() {
		setTime(getTime() + 2);
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

}
