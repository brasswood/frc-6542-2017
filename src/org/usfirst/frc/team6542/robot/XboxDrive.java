package org.usfirst.frc.team6542.robot;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.XboxController;

//like RobotDrive
public class XboxDrive {
	
	SpeedController left;
	SpeedController right;
	XboxController controller;
	
	public XboxDrive(SpeedController left, SpeedController right, XboxController controller) {
		this.left = left;
		this.right = right;
		this.controller = controller;
	}
	
	// Just In Case (TM)
	public XboxDrive(SpeedController one, SpeedController two, SpeedController three, SpeedController four, XboxController controller) {
		
	}
	
	//called every TeleopPeriod in IterativeRobot?
	public void drive() {
		// PWMSpeedController.set() accepts between -1 and 1.
		// getTriggerAxis returns between 0 and 1.
		double speed = controller.getTriggerAxis(GenericHID.Hand.kRight) - controller.getTriggerAxis(GenericHID.Hand.kLeft);
		double x = controller.getX(GenericHID.Hand.kLeft);
		double[] sides = getEachSide(x);
		// whatever rTrigger is, scale
		// left and right side proportional to that
		left.set(sides[0] * speed);
		right.set(sides[1] * speed);
		
		

	}
	// left comma right
	private double[] getEachSide(double steeringFactor) {
		double[] sides = new double[5];
		sides[0] = 1 - steeringFactor;
		sides[1] = steeringFactor;
		return sides;
	}
	
}
