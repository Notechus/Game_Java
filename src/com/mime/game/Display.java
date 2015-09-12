package com.mime.game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import com.mime.game.graphics.Render;
import com.mime.game.graphics.Screen;
import com.mime.game.gui.Launcher;
import com.mime.game.input.Controller;
import com.mime.game.input.InputHandler;

@SuppressWarnings("unused")
public class Display extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;

	public static int width = 800;
	public static int height = 600;
	public static final String TITLE = "Game Client Pre-Alpha 0.05";

	private Thread thread;
	private boolean running = false;

	// screen thingy
	private BufferedImage img;
	private Screen screen;
	private int[] pixels;
	private String fps = "";

	// mouse pos
	private int newX = 0;
	private int newY = 0;
	private int oldX = 0;
	private int oldY = 0;
	public static int mouseSpeed;

	private static int selection;

	// utils stuff
	private Game game;
	private InputHandler input;

	public Display() {
		setGameWidth();
		setGameHeight();

		Dimension size = new Dimension(width, height);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);

		screen = new Screen(width, height);
		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();

		game = new Game();

		// input init
		input = new InputHandler();
		addKeyListener(input);
		addFocusListener(input);
		addMouseListener(input);
		addMouseMotionListener(input);

	}

	public void init() {

	}

	public static void setWidth(String width_) {
		width = Integer.getInteger(width_);
	}

	public static void setHeight(String height_) {
		height = Integer.getInteger(height_);
	}

	public static int setGameWidth() {
		if (selection == 0) {
			width = 640;
		}
		if (selection == 1 || selection == -1) {
			width = 800;
		}
		if (selection == 2) {
			width = 1024;
		}
		if (selection == 3) {
			width = 1280;
		}
		if (selection == 4) {
			width = 1366;
		}
		if (selection == 5) {
			width = 1600;
		}
		if (selection == 6) {
			width = 1900;
		}
		return width;
	}

	public static int setGameHeight() {
		if (selection == 0) {
			height = 480;
		}
		if (selection == 1 || selection == -1) {
			height = 600;
		}
		if (selection == 2) {
			height = 768;
		}
		if (selection == 3) {
			height = 720;
		}
		if (selection == 4) {
			height = 768;
		}
		if (selection == 5) {
			height = 900;
		}
		if (selection == 6) {
			height = 1080;
		}
		return height;
	}

	public static int getGameWidth() {
		return width;
	}

	public static int getGameHeight() {
		return height;
	}

	public void start() {
		if (running) return;
		running = true;
		thread = new Thread(this, "Game Thread");
		thread.start();

		System.out.println(thread.getName() + " initialised");
	}

	private void stop() {
		if (!running) return;
		running = false;
		try {
			thread.join();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(1);
		}
	}

	public void mouseInput() {
		newX = input.mouseX;
		newY = input.mouseY;

		if (newX > oldX) {
			Controller.turnRight = true;
		}

		if (newX < oldX) {
			Controller.turnLeft = true;
		}

		if (newX == oldX) {
			Controller.turnLeft = false;
			Controller.turnRight = false;
		}
		mouseSpeed = Math.abs(newX - oldX);
		oldX = newX;

	}

	public void run() {
		// init fps
		int frames = 0;
		double unprocessedSeconds = 0;
		long previousTime = System.nanoTime();
		double secondsPerTick = 1 / 60.0;
		int tickCount = 0;
		boolean ticked = false;

		init();
		while (running) {
			long currentTime = System.nanoTime();
			long passedTime = currentTime - previousTime;
			previousTime = currentTime;
			unprocessedSeconds += passedTime / 1000000000.0;
			requestFocus();

			while (unprocessedSeconds > secondsPerTick) {
				tick();
				unprocessedSeconds -= secondsPerTick;
				ticked = true;
				tickCount++;
				if (tickCount % 60 == 0) {
					fps = frames + " FPS";
					previousTime += 1000;
					frames = 0;
				}
			}
			if (ticked) {
				render();
				frames++;
			}
			render();
			frames++;
			mouseInput();
		}
	}

	public void tick() {
		game.tick(input.key);
	}

	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		// render screen
		screen.render(game);

		for (int i = 0; i < width * height; i++) {
			pixels[i] = screen.pixels[i];
		}

		Graphics g = bs.getDrawGraphics();
		g.drawImage(img, 0, 0, width + 10, height + 10, null);
		g.setFont(new Font("Verdana", 0, 24));
		g.setColor(Color.YELLOW);
		g.drawString(fps, 15, 20);

		g.dispose();
		bs.show();
	}

	public static void main(String[] args) {
		new Launcher(0);
	}

	public static int getSelection() {
		return selection;
	}

	public static void setSelection(int selection) {
		Display.selection = selection;
	}

}
