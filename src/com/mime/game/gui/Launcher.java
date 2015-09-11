package com.mime.game.gui;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.mime.game.RunGame;

public class Launcher extends JFrame {

	private static final long serialVersionUID = 1L;

	protected JPanel window = new JPanel();
	private JButton play, options, help, quit;
	private Rectangle rplay, roptions, rhelp, rquit;

	private int width = 320;
	private int height = 480;
	protected int button_width = 80;
	protected int button_height = 40;

	public Launcher(int id) {
		// set system look and feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		setTitle("Game Launcher");
		setSize(new Dimension(width, height));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().add(getWindow());
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		getWindow().setLayout(null);

		if (id == 0) drawButtons();
	}

	private void drawButtons() {
		// play buton
		play = new JButton("Play");
		rplay = new Rectangle((width / 2) - (button_width / 2), 200, button_width, button_height);
		play.setBounds(rplay);
		getWindow().add(play);

		// options button
		options = new JButton("Options");
		roptions = new Rectangle((width / 2) - (button_width / 2), 250, button_width, button_height);
		options.setBounds(roptions);
		getWindow().add(options);

		// help button
		help = new JButton("Help");
		rhelp = new Rectangle((width / 2) - (button_width / 2), 300, button_width, button_height);
		help.setBounds(rhelp);
		getWindow().add(help);

		// quit button
		quit = new JButton("Quit");
		rquit = new Rectangle((width / 2) - (button_width / 2), 350, button_width, button_height);
		quit.setBounds(rquit);
		getWindow().add(quit);

		// action listeners

		play.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				new RunGame();

			}
		});

		options.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				new Options();
			}
		});

		help.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});

		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

	}

	public JPanel getWindow() {
		return window;
	}

	public void setWindow(JPanel window) {
		this.window = window;
	}
}
