package com.mime.game;

import java.awt.event.KeyEvent;

import com.mime.game.input.Controller;

public class Game {

	private int time;
	public Controller controls;

	public Game() {
		controls = new Controller();
	}

	public void tick(boolean[] key) {
		setTime(getTime() + 1);

		boolean forward = key[KeyEvent.VK_W];
		boolean back = key[KeyEvent.VK_S];
		boolean left = key[KeyEvent.VK_A];
		boolean right = key[KeyEvent.VK_D];

		controls.tick(forward, back, left, right);
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

}
