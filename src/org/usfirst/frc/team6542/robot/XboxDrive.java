package org.usfirst.frc.team6542.robot;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.PWMSpeedController;
import edu.wpi.first.wpilibj.XboxController;

//like RobotDrive
public class XboxDrive {
	
	PWMSpeedController left;
	PWMSpeedController right;
	XboxController controller;
	
	public XboxDrive(PWMSpeedController left, PWMSpeedController right, XboxController controller) {
		this.left = left;
		this.right = right;
		this.controller = controller;
	}
	
	// Just In Case (TM)
	public XboxDrive(PWMSpeedController one, PWMSpeedController two, PWMSpeedController three, PWMSpeedController four, XboxController controller) {
		
	}
	
	//called every TeleopPeriod in IterativeRobot?
	public void drive() {
		// PWMSpeedController.set() accepts between -1 and 1.
		// getTriggerAxis returns between 0 and 1.
		double lTrigger = controller.getTriggerAxis(GenericHID.Hand.kLeft);
		double rTrigger = controller.getTriggerAxis(GenericHID.Hand.kRight);
		
		// Right trigger should take priority here if both are pressed at
		// the same time. It's bedtime, I'll think of something better
		// later.
		if (rTrigger > 0) {
			driveForward(rTrigger);
		} else if (lTrigger > 0) {
			driveBackward(lTrigger);
		}
		else {
			left.set(0);
			right.set(0);
		}

	}
	
	private void driveForward(double speed) {
		//idk what the front of the robot is so we'll just go with this
		left.set(speed);
		right.set(-speed);
	}
	
	private void driveBackward(double speed) {
		left.set(-speed);
		right.set(speed);
	}
}
