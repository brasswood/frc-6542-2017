package org.usfirst.frc.team6542.robot;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.XboxController;


public class Cannon {
	SpeedController shooter;
	SpeedController deJammer;
	XboxController gamepad;
	boolean aPrev = false;
	boolean aToggle = false;
	public Cannon(SpeedController shooter, XboxController gamepad) {
		this.shooter = shooter;
		this.gamepad = gamepad;
	}
	public Cannon(SpeedController shooter, SpeedController deJammer, XboxController gamepad) {
		this(shooter, gamepad);
		this.deJammer = deJammer;
	}
	
	public void setAToggle(boolean setting) {
		aToggle = setting;
	}
	public boolean shoot() {
		if (aToggle) {shooter.set(1);}
		else {shooter.set(0);}
		
		boolean a = gamepad.getAButton();
		if (a && !aPrev) {
			aToggle= !aToggle;
		}
		// NOTE: The following line must go after getAButton
		// is compared to aPrev
		aPrev = a;
		return aToggle;
	}
	public boolean deJam() {
		if (deJammer != null) {
			if (gamepad.getBButton()) {
				deJammer.set(0.1);
				return true;
			} else {
				deJammer.set(0);
				return false;
			}
		} else {
			return false;
		}
	}
}
