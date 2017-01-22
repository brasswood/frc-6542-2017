package org.usfirst.frc.team6542.robot;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.XboxController;

//like RobotDrive
public class XboxDrive {
	
	SpeedController frontLeft;
	SpeedController frontRight;
	SpeedController backLeft;
	SpeedController backRight;
	XboxController controller;
	/**
	 * Constructs XboxDrive given two SpeedControllers and an XboxController
	 * @param left			The controller you want to be the left side of the robot
	 * @param right			The controller you want to be the right side of the robot
	 * @param controller	The XboxController object you would like to use
	 */
	public XboxDrive(SpeedController left, SpeedController right, XboxController controller) {
		frontLeft = left;
		frontRight = right;
		this.controller = controller;
	}
	
	// Just In Case (TM)
	/**
	 * Constructs XboxDrive given four SpeedControllers (starting from front right and going counterclockwise) and an XboxController
	 * @param frontRight	The controller for the front right side of the robot
	 * @param frontLeft 	The controller for the front left side of the robot
	 * @param backLeft		The controller for the back left side of the robot
	 * @param backRight		The controller for the back right side of the robot
	 * @param controller	The XboxController object you would like to use
	 */
	public XboxDrive(SpeedController frontRight, SpeedController frontLeft, SpeedController backLeft, SpeedController backRight, XboxController controller) {
		// merge controllers on same side
		this.frontRight = frontRight;
		this.frontLeft = frontLeft;
		this.backLeft = backLeft;
		this.backRight = backRight;
	}
	
	//called every TeleopPeriod in IterativeRobot?
	public void drive() {
		// PWMSpeedController.set() accepts between -1 and 1.
		// getTriggerAxis returns between 0 and 1.
		double speed = controller.getTriggerAxis(GenericHID.Hand.kRight) - controller.getTriggerAxis(GenericHID.Hand.kLeft);
		double x = controller.getX(GenericHID.Hand.kLeft);
		// whatever rTrigger is, scale
		// left and right side proportional to that
		
		
		

	}
	
	protected void setLeftRightMotors(double leftOutput, double rightOutput) {
		// if only two sides were specified in constructor, fronts were used
		frontLeft.set(leftOutput);
		if (backLeft != null) {
			backLeft.set(leftOutput);
		}
		
		frontRight.set(rightOutput);
		if (backRight != null) {
				backRight.set(rightOutput);
		}
	}

	
}
