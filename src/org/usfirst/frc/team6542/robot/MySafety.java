package org.usfirst.frc.team6542.robot;

import edu.wpi.first.wpilibj.SpeedController;

public class MySafety {
	boolean safe;
	// This motor array is a way to set all motors that are unsafe to zero 
	// without necessarily knowing what they are
	SpeedController[] motors = new SpeedController[4];
	public void setSafe(boolean b) {
		this.safe = b;
		if (!b) {
			for (SpeedController i : motors) {
				i.set(0.0);
			}
		}
	}
	public boolean set(SpeedController motor, double value) {
		if (safe) {
			motor.set(value);
			return true;
		} else {
			motor.set(0);
			return false;
		}
	}
}
