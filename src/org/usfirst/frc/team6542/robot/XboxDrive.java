package org.usfirst.frc.team6542.robot;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.GyroBase;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.XboxController;

//like RobotDrive
public class XboxDrive {
	
	SpeedController frontLeft;
	SpeedController frontRight;
	SpeedController backLeft;
	SpeedController backRight;
	XboxController controller;
	GyroBase gyro;
	double leftCorrect, rightCorrect;
	
	public XboxDrive(SpeedController left, SpeedController right, XboxController controller, GyroBase gyro) {
		this(left, right, controller);
		this.gyro = gyro;
	}
	
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
	
	public void drive() {
		// PWMSpeedController.set() accepts between -1 and 1.
		// getTriggerAxis returns between 0 and 1.
		double speed = controller.getTriggerAxis(GenericHID.Hand.kRight) - controller.getTriggerAxis(GenericHID.Hand.kLeft);
		double x = controller.getX(GenericHID.Hand.kLeft);
		double leftOutput = 0;
		double rightOutput = 0;
		if (x == 0.0) {
			leftOutput = speed;
			rightOutput = -speed;
			if (gyro != null && speed != 0) {
				setLeftRightMotors(leftOutput, rightOutput, gyro);
				return;
			}
		} else if (x > 0.0) {
			leftOutput = speed;
			rightOutput = (x - 0.5) * 2 * speed;
			
		} else {
			leftOutput = (x + 0.5) * 2 * speed;
			rightOutput = -speed;
		}
		setLeftRightMotors(leftOutput, rightOutput);			
	}
	
	public void setLeftRightMotors(double leftOutput, double rightOutput) {
		// if only two sides were specified in constructor, fronts were used and rears were null.
		frontLeft.set(leftOutput);
		if (backLeft != null) {
			backLeft.set(leftOutput);
		}
		
		frontRight.set(rightOutput);
		if (backRight != null) {
				backRight.set(rightOutput);
		}
	}
	
	public void setLeftRightMotors(double leftOutput, double rightOutput, GyroBase gyro) {
		double heading = gyro.getAngle();
		if (heading < -2) {
			leftCorrect -= 0.01;
		} else if (heading > 2) {
			rightCorrect -= 0.01;
		}
		this.setLeftRightMotors((leftOutput + leftCorrect), (rightOutput + rightCorrect));
	}
}
