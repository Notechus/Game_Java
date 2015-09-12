package com.mime.game.gui;

import java.awt.Choice;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.mime.game.Display;

public class Options extends Launcher {

	private static final long serialVersionUID = 1L;

	private int width = 540;
	private int height = 440;

	private JButton OK;
	private Rectangle rOK, rresolution;
	private Choice resolution = new Choice();

	public Options() {
		super(1);
		setTitle("Options - Game Launcher");
		setSize(new Dimension(width, height));
		setLocationRelativeTo(null);

		drawButtons();
	}

	private void drawButtons() {
		OK = new JButton("OK");
		rOK = new Rectangle((width - 100), (height - 70), button_width, button_height - 10);
		OK.setBounds(rOK);
		window.add(OK);

		// list of resolutions
		rresolution = new Rectangle(50, 80, 80, 25);
		resolution.setBounds(rresolution);
		resolution.add("640x480");
		resolution.add("800x600");
		resolution.add("1024x768");
		resolution.add("1280x720");
		resolution.add("1366x768");
		resolution.add("1600x900");
		resolution.add("1920x1080");
		resolution.select(1);
		window.add(resolution);

		OK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Display.setSelection(resolution.getSelectedIndex());
				int w = Display.getGameWidth();
				int h = Display.getGameHeight();
				getConfig().saveConfiguration("height", h);
				getConfig().saveConfiguration("width", w);
				dispose();
				new Launcher(0);
			}
		});
	}
}
