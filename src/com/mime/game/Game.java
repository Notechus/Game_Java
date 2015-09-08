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
		boolean jump = key[KeyEvent.VK_SPACE];
		boolean crouch = key[KeyEvent.VK_CONTROL];
		boolean walk = key[KeyEvent.VK_SHIFT]; // we implicitly run

		controls.tick(forward, back, left, right, jump, crouch, walk);
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

}
