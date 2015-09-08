package com.mime.game.input;

public class Controller {

	private double x;
	private double z;
	private double rotation;
	private double xa;
	private double za;
	private double rotationa;

	public void tick(boolean forward, boolean back, boolean left, boolean right, boolean turnLeft, boolean turnRight) {

		double rotationSpeed = 1.0;
		double walkSpeed = 1.0;
		double xMove = 0;
		double zMove = 0;

		if (forward) {
			zMove++;
		}
		if (back) {
			zMove--;
		}
		if (left) {
			xMove++;
		}
		if (right) {
			zMove--;
		}
		if (turnLeft) {
			zMove++;
		}
		if (turnLeft) {
			zMove++;
		}

		xa += (xMove * Math.cos(rotation) + zMove * Math.sin(rotation)) * walkSpeed;
		za += (zMove * Math.cos(rotation) - xMove * Math.sin(rotation)) * walkSpeed;

		x += xa;
		z += za;
	}
}
