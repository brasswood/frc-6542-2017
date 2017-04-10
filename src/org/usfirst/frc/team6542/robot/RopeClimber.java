package org.usfirst.frc.team6542.robot;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;

public class RopeClimber {
	SpeedController climber;
	XboxController gamepad;
	public RopeClimber(SpeedController climber, XboxController gamepad) {
		this.climber = climber;
		this.gamepad = gamepad;
	}
	public boolean climb() {
		if (gamepad.getBumper(Hand.kRight)){
			double x = gamepad.getX(Hand.kRight);
			double y = gamepad.getY(Hand.kRight);
			if (y < 0) {
				setClimberMotor(-Math.hypot(x, y));
			} else {setClimberMotor(Math.hypot(x, y));}
			return true;
		} else {
			setClimberMotor(0.0);
			return false;
		}
	}
	public void setClimberMotor(double value) {
		climber.set(value);
	}
	
}
