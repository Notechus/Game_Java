package com.mime.game;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class Configuration {

	Properties properties = new Properties();
	private String width = "";
	private String height = "";

	public void saveConfiguration(String key, int value) {
		String path = "res/settings/config.xml";
		try {
			File file = new File(path);
			boolean exist = file.exists();
			if (!exist) {
				file.createNewFile();
			}
			OutputStream write = new FileOutputStream(path);
			properties.setProperty(key, Integer.toString(value));
			properties.storeToXML(write, "Resolution");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void loadConfiguration(String path) {
		try {
			InputStream read = new FileInputStream(path);
			properties.loadFromXML(read);
			height = properties.getProperty("height");
			width = properties.getProperty("width");
			setResolution(Integer.parseInt(width), Integer.parseInt(height));
		} catch (FileNotFoundException ex) {
			saveConfiguration("width", 800);
			saveConfiguration("height", 600);
			loadConfiguration(path);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void setResolution(int width, int height) {
		if (width == 640 && height == 480) {
			Display.setSelection(0);
		}
		if (width == 800 && height == 600) {
			Display.setSelection(1);
		}
		if (width == 1024 && height == 768) {
			Display.setSelection(2);
		}
		if (width == 1280 && height == 720) {
			Display.setSelection(3);
		}
		if (width == 1366 && height == 768) {
			Display.setSelection(4);
		}
		if (width == 1600 && height == 900) {
			Display.setSelection(5);
		}
		if (width == 1920 && height == 1080) {
			Display.setSelection(6);
		}
	}
}
