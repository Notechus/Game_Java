package com.mime.game.input;

public class Controller {

	private double x;
	private double z;
	private double rotation;

	private double xa;
	private double za;
	private double rotationa;

	// mouse
	public static boolean turnLeft = false;
	public static boolean turnRight = false;

	public void tick(boolean forward, boolean back, boolean left, boolean right) {

		double rotationSpeed = 0.025;
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
			xMove--;
		}

		if (right) {
			xMove++;
		}

		if (turnLeft) {
			rotationa -= rotationSpeed;
		}

		if (turnRight) {
			rotationa += rotationSpeed;
		}

		xa += (xMove * Math.cos(rotation) + zMove * Math.sin(rotation)) * walkSpeed;
		za += (zMove * Math.cos(rotation) - xMove * Math.sin(rotation)) * walkSpeed;

		x += xa;
		z += za;
		xa *= 0.1;
		za *= 0.1;
		rotation += rotationa;
		rotationa *= 0.2;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public double getRotation() {
		return rotation;
	}

	public void setRotation(double rotation) {
		this.rotation = rotation;
	}

	public double getXa() {
		return xa;
	}

	public void setXa(double xa) {
		this.xa = xa;
	}

	public double getZa() {
		return za;
	}

	public void setZa(double za) {
		this.za = za;
	}

	public double getRotationa() {
		return rotationa;
	}

	public void setRotationa(double rotationa) {
		this.rotationa = rotationa;
	}

}
