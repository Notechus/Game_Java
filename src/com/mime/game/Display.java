package com.mime.game;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import com.mime.game.graphics.Render;
import com.mime.game.graphics.Screen;

public class Display extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	public static final String TITLE = "Game Client Pre-Aplha 0.01";

	private Thread thread;
	private boolean running = false;

	// screen thingy
	private BufferedImage img;
	private Screen screen;
	private int[] pixels;

	public Display() {
		// render = new Render(WIDTH, HEIGHT);
		screen = new Screen(WIDTH, HEIGHT);
		img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();

	}

	private void start() {
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

	public void run() {
		while (running) {
			tick();
			render();
		}
	}

	public void tick() {

	}

	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		// render screen
		screen.render();

		for (int i = 0; i < WIDTH * HEIGHT; i++) {
			pixels[i] = screen.pixels[i];
		}

		Graphics g = bs.getDrawGraphics();
		g.drawImage(img, 0, 0, WIDTH, HEIGHT, null);
		g.dispose();
		bs.show();
	}

	public static void main(String[] args) {
		Display game = new Display();
		JFrame frame = new JFrame(TITLE);
		// add component display to jframe and pack
		frame.add(game);
		frame.pack();

		// init frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setSize(WIDTH, HEIGHT);
		frame.setResizable(false);
		frame.setVisible(true);

		// start game
		game.start();

	}

}
