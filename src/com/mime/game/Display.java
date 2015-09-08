package com.mime.game;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import com.mime.game.graphics.Render;
import com.mime.game.graphics.Screen;
import com.mime.game.input.InputHandler;

@SuppressWarnings("unused")
public class Display extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	public static final String TITLE = "Game Client Pre-Alpha 0.02";

	private Thread thread;
	private boolean running = false;

	// screen thingy
	private BufferedImage img;
	private Screen screen;
	private int[] pixels;

	// utils stuff
	private Game game;
	private InputHandler input;

	public Display() {
		Dimension size = new Dimension(WIDTH, HEIGHT);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);

		screen = new Screen(WIDTH, HEIGHT);
		img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
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
			while (unprocessedSeconds > secondsPerTick) {
				tick();
				unprocessedSeconds -= secondsPerTick;
				ticked = true;
				tickCount++;
				if (tickCount % 60 == 0) {
					System.out.println(frames + "fps");
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

		for (int i = 0; i < WIDTH * HEIGHT; i++) {
			pixels[i] = screen.pixels[i];
		}

		Graphics g = bs.getDrawGraphics();
		g.drawImage(img, 0, 0, WIDTH + 10, HEIGHT + 10, null);
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
		// frame.setSize(WIDTH, HEIGHT);
		frame.setResizable(false);
		frame.setLocation((WIDTH) / 2, (HEIGHT - 256) / 2); // middle
		frame.setVisible(true);

		// start game
		game.start();

	}

}
