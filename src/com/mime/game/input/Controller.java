package com.mime.game.input;

public class Controller {

	private double x; // left right
	private double z; // depth
	private double y; // up and down

	private double rotation;

	private double xa;
	private double za;
	private double rotationa;

	// for walking animation
	public static boolean walking = false;
	public static boolean crouchWalk = false;
	public static boolean running = false;

	// mouse
	public static boolean turnLeft = false;
	public static boolean turnRight = false;

	public void tick(boolean forward, boolean back, boolean left, boolean right, boolean jump, boolean crouch,
			boolean walk) {

		double rotationSpeed = 0.025;
		double walkSpeed = 1.0;
		double jumpHeight = 0.5;
		double crouchHeight = 0.3;
		// double proneHeight = 0.8;
		double xMove = 0;
		double zMove = 0;

		if (forward) {
			zMove++;
			walking = true;
		}

		if (back) {
			zMove--;
			walking = true;
		}

		if (left) {
			xMove--;
			walking = true;
		}

		if (right) {
			xMove++;
			walking = true;
		}

		if (turnLeft) {
			rotationa -= rotationSpeed;
		}

		if (turnRight) {
			rotationa += rotationSpeed;
		}

		if (jump) {
			y += jumpHeight;
			walk = true;
		}
		if (crouch) {
			y -= crouchHeight;
			walk = true;
			crouchWalk = true;
			walkSpeed = 0.35;
		}
		if (walk) {
			walkSpeed = 0.5;
			walking = true;
			running = false;
		}

		if (!forward && !back && !left && !right && !turnRight && !turnLeft) {
			walking = false;
		}
		if (!crouch) {
			crouchWalk = false;
		}
		if (!walk) {
			running = true;
		}

		xa += (xMove * Math.cos(rotation) + zMove * Math.sin(rotation)) * walkSpeed;
		za += (zMove * Math.cos(rotation) - xMove * Math.sin(rotation)) * walkSpeed;

		x += xa;
		y *= 0.9;
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

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
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
