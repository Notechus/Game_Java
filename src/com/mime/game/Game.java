package com.mime.game;

import java.awt.event.KeyEvent;

import com.mime.game.input.Controller;
import com.mime.game.level.Level;

public class Game {

	private int time;
	public static int stime;
	public Controller controls;
	public Level level;

	public Game() {
		controls = new Controller();
		level = new Level(80, 80);
	}

	public void tick(boolean[] key) {
		setTime(getTime() + 1);
		stime++;

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
