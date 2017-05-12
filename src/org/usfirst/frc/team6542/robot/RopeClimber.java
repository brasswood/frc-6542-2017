package org.usfirst.frc.team6542.robot;

import edu.wpi.first.wpilibj.SpeedController;

public class RopeClimber extends MySafety {
	SpeedController climber;
	public RopeClimber(SpeedController climber) {
		this.climber = climber;
		motors[0] = climber;
	}
	
}
