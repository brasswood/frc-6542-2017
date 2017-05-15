package org.usfirst.frc.team6542.robot;

import edu.wpi.first.wpilibj.XboxController;

public class MyHID extends XboxController {

	private boolean cannonPrev = false;
	private boolean cannonToggle = false;
	private final Type type;
	
	public MyHID(int port, Type type) {
		super(port);
		this.type = type;
	}
	
	public enum Type {
		// This enum is simply for mapping what buttons you want
		// to trigger what functions
		// TODO: map
		XBCONTROLLER(3, 2, 0, 1, 1, 2, 6, 4, 5),
		// JOYSTICK(),
		GUITAR(0, 180,-1, -1, 4, 3, 5, -1, 2),
		// DRUMKIT(),
		WIIMOTE(2, 1, 0, 1, 5, 3, 4, -1, 1),
		WIINUNCHUCK(2, 1, 0, 1, 3, 4, 2, -1, 1),
		// WIIBALANCE()
		;
		
		private final int driveForward, driveBackward, driveX, driveY, cannonButton, deJamButton, ropeButton, ropeX, ropeY;
		Type(int driveForward, int driveBackward, int driveX, int driveY, int cannonButton, int deJamButton, int ropeButton, int ropeX, int ropeY) {
			this.driveForward = driveForward;
			this.driveBackward = driveBackward;
			this.driveX = driveX;
			this.driveY = driveY;
			this.cannonButton = cannonButton;
			this.deJamButton = deJamButton;
			this.ropeButton = ropeButton;
			this.ropeX = ropeX;
			this.ropeY = ropeY;
		}
	}
	
	public double getDriveForward() {
		double r;
		switch (type) {
		case GUITAR: r = (getPOV(0) == type.driveForward) ? 0.5 : 0;
		break;
		default: r = getRawAxis(type.driveForward);
		break;
		}
		
		return r;
	}
	public double getDriveBackward() {
		double r;
		switch (type) {
		case GUITAR: r = (getPOV(0) == type.driveBackward) ? 0.5 : 0;
		break;
		default: r = getRawAxis(type.driveBackward);
		break;
		}
		return r;
	}
	public double getDriveX() {
		double r;
		if (type == Type.GUITAR) {
			if (getRawButton(1)) {
				r = -1;
			} else if (getRawButton(2)) {
				r = 1;
			} else {
				r = 0;
			}
		} else {
			r = getRawAxis(type.driveX);
		}
		return r;
	}
	public double getDriveY() {
		return getRawAxis(type.driveY);
	}
	public boolean getCannonButton() {
		return getRawButton(type.cannonButton);
	}
	public boolean getCannonToggle() {
		boolean c = getCannonButton();
		if (!cannonPrev && c) {
			cannonToggle = !cannonToggle;
		}
		cannonPrev = c;
		return cannonToggle;
	}
	public boolean getDeJamButton() {
		return getRawButton(type.deJamButton);
	}
	public boolean getRopeButton() {
		return getRawButton(type.ropeButton);
	}
	public double getRopeX() {
		return getRawAxis(type.ropeX);
	}
	public double getRopeY() {
		double r;
		switch (type) {
		case GUITAR: r = (getRawAxis(type.ropeY) - 0.5) * 2;
		break;
		default: r = getRawAxis(type.ropeY);
		}
		return r;
	}
	public boolean getRawButton(int button) {
		if (button == -1) {return false;}
		else {return super.getRawButton(button);}
	}
	public double getRawAxis(int axis) {
		if (axis == -1) {return 0;}
		else {return super.getRawAxis(axis);}
	}
}
