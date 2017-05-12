package org.usfirst.frc.team6542.robot;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.XboxController;


public class Cannon extends MySafety {
	SpeedController shooter;
	SpeedController deJammer;
	XboxController gamepad;
	boolean aPrev = false;
	boolean aToggle = false;
	public Cannon(SpeedController shooter) {
		this.shooter = shooter;
		motors[0] = shooter;
	}
	public Cannon(SpeedController shooter, SpeedController deJammer) {
		this(shooter);
		this.deJammer = deJammer;
		motors[1] = deJammer;
	}

	public boolean deJam() {
		if (deJammer != null) {
			return set(deJammer, -0.5);
		} else {
			return false;
		}
	}
}
