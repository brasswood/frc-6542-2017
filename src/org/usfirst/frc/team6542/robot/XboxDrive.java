package org.usfirst.frc.team6542.robot;
import edu.wpi.first.wpilibj.GyroBase;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.*;

// like RobotDrive
public class XboxDrive {
	
	SpeedController frontLeft;
	SpeedController frontRight;
	SpeedController backLeft;
	SpeedController backRight;
	XboxController controller;
	boolean headingIsSet;
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
		headingIsSet = true;
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
		double speed = controller.getTriggerAxis(Hand.kRight) - controller.getTriggerAxis(Hand.kLeft);
		// square speed
		if (speed < 0) {
			speed = -Math.pow(speed, 2);
		} else {
			speed = Math.pow(speed, 2);
		}
		double x = controller.getX(Hand.kLeft);
		// square x
		if (x < 0) {
			x = -Math.pow(x, 2);
		} else {
			x = Math.pow(x, 2);
		}
		double leftSpeed = 0;
		double rightSpeed = 0;
		System.out.println(speed);
		if (Math.abs(x) <= 0.15) {
			leftSpeed = speed;
			rightSpeed = -speed;
		} else {
			if (x > 0.0) {
				leftSpeed = speed;
				rightSpeed = (x - 0.5) * 2 * speed;
				
			} else {
				leftSpeed = (x + 0.5) * 2 * speed;
				rightSpeed = -speed;
			}
		}

		SmartDashboard.putNumber("leftSpeed", leftSpeed);
		SmartDashboard.putNumber("rightSpeed", rightSpeed);
		if (gyro != null) {setLeftRightMotors(leftSpeed, rightSpeed, gyro);}
		else {setLeftRightMotors(leftSpeed, rightSpeed);}
		
		// debug
		SmartDashboard.putBoolean("headingIsSet", headingIsSet);
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
		if (leftSpeed == -rightSpeed) {
			if (headingIsSet) {
				if (heading > 1) {
					//leftSpeed is our base, if leftSpeed is forward than the robot should go forward
					if (leftSpeed > 0) {
						leftOutput = (0.7 * leftOutput);
						System.out.println("Adjust leftOutput");
					} else {
						rightOutput = (0.7 * rightOutput);
						System.out.println("Adjust rightOutput");
					}
				} else if (heading < -1) {
					if (leftSpeed > 0) {
						rightOutput = (0.7 * rightSpeed);
						System.out.println("Adjust rightOutput");
					} else {
						leftOutput = (0.7 * leftSpeed);
						System.out.println("Adjust leftOutput");
					}
				}
			} else {
				System.out.println("heading not set yet");
				resetGyro();
				leftOutput = 0;
				rightOutput = 0;
			}
		} else {headingIsSet = false;}
		
		// debug
		SmartDashboard.putNumber("leftOutput", leftOutput);
		SmartDashboard.putBoolean("lAdjust", leftOutput != leftSpeed);
		SmartDashboard.putNumber("rightOutput", rightOutput);
		SmartDashboard.putBoolean("rAdjust", rightOutput != rightSpeed);
		System.out.println(leftOutput + ", " + rightOutput + ", " + heading + ", " + gyro.getRate());
		
		this.setLeftRightMotors(leftOutput, rightOutput);
	}
	
	private boolean resetGyro() {
		if (Math.abs(gyro.getRate()) < 1) {
			gyro.reset();
			headingIsSet = true;
			return true;
		} else {
			headingIsSet = false;
			return false;
		}
	}
}