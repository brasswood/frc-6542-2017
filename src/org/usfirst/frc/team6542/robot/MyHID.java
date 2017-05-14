package org.usfirst.frc.team6542.robot;

import edu.wpi.first.wpilibj.GenericHID.*;
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
		XBCONTROLLER(),
		JOYSTICK(),
		GUITAR(),
		DRUMKIT(),
		WIIMOTE(),
		WIINUNCHUCK(),
		WIIBALANCE();
		
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
		return getRawAxis(type.driveForward);
	}
	public double getDriveBackward() {
		return getRawAxis(type.driveBackward);
	}
	public double getDriveX() {
		return getRawAxis(type.driveX);
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
		return getRawAxis(type.ropeY);
	}
}
