package com.mime.game;

import java.awt.event.KeyEvent;

import com.mime.game.input.Controller;

public class Game {

	private int time;
	private Controller controls;

	public Game() {

	}

	public void tick(boolean[] key) {
		setTime(getTime() + 1);

		boolean forward = key[KeyEvent.VK_W];
		boolean back = key[KeyEvent.VK_S];
		boolean left = key[KeyEvent.VK_A];
		boolean right = key[KeyEvent.VK_D];
		boolean turnLeft = key[KeyEvent.VK_LEFT];
		boolean turnRight = key[KeyEvent.VK_RIGHT];

		controls.tick(forward, back, left, right, turnLeft, turnRight);
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

}
