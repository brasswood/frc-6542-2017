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
	// TODO: wait a fraction of a second to reset gyro to account for momentum
	public void drive() {
		// PWMSpeedController.set() accepts between -1 and 1.
		// getTriggerAxis returns between 0 and 1.
		double speed = controller.getTriggerAxis(GenericHID.Hand.kRight) - controller.getTriggerAxis(GenericHID.Hand.kLeft);
		double x = controller.getX(GenericHID.Hand.kLeft);
		double leftSpeed = 0;
		double rightSpeed = 0;
		System.out.println(speed);
		if (Math.abs(x) <= 0.15) {
			leftSpeed = speed;
			rightSpeed = -speed;
			if (gyro != null) {
				setLeftRightMotors(leftSpeed, rightSpeed, gyro);
			}
		} else {
			if (x > 0.0) {
				leftSpeed = speed;
				rightSpeed = (x - 0.5) * 2 * speed;
				
			} else {
				leftSpeed = (x + 0.5) * 2 * speed;
				rightSpeed = -speed;
			}
			setLeftRightMotors(leftSpeed, rightSpeed);
			if (gyro != null) {
					System.out.println("reset gyro");
					gyro.reset();
			}
		}
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
	// TODO: Clean this up
	public void setLeftRightMotors(double leftSpeed, double rightSpeed, GyroBase gyro) {
		double heading = gyro.getAngle();
		double leftOutput = leftSpeed;
		double rightOutput = rightSpeed;
		if (heading > 1) {
			if (leftSpeed > 0) {
				leftOutput = (0.7 * leftOutput);
				System.out.println("Adjust leftOutput");
			} else {
				rightOutput = (0.7 * rightOutput);
				System.out.println("Adjust rightOutput");
			}
		} else if (heading < -1) {
			if (leftSpeed > 0) {
				rightOutput = (0.7 * rightOutput);
				System.out.println("Adjust rightOutput");
			} else {
				leftOutput = (0.7 * leftOutput);
				System.out.println("Adjust leftOutput");
			}
		}
		System.out.println(leftOutput + ", " + rightOutput + ", " + heading);
		this.setLeftRightMotors((leftOutput), (rightOutput));
	}
}