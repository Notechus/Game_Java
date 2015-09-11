package com.mime.game;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class RunGame {

	public RunGame() {
		// cursor image
		BufferedImage cursor = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blank = Toolkit.getDefaultToolkit().createCustomCursor(cursor, new Point(0, 0), "blank");

		Display game = new Display();
		JFrame frame = new JFrame(Display.TITLE);
		// add component display to frame and pack
		frame.add(game);
		frame.pack();

		// set cursor to blank
		frame.getContentPane().setCursor(blank);

		// init frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setSize(Display.width, Display.height);
		frame.setResizable(false);
		frame.setLocation((Display.width) / 2, (Display.height) / 2); // middle
		frame.setVisible(true);

		// start game
		game.start();
	}
}
