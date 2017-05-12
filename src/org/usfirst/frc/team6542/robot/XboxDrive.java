package org.usfirst.frc.team6542.robot;
import edu.wpi.first.wpilibj.GyroBase;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.*;
import edu.wpi.first.wpilibj.Timer;

// like RobotDrive
public class XboxDrive extends MySafety {
	
	SpeedController frontLeft;
	SpeedController frontRight;
	SpeedController backLeft;
	SpeedController backRight;
	XboxController gamepad;
	GyroBase gyro;
	Timer timer;
	public final double limit = 0.7;
	
	public XboxDrive(SpeedController left, SpeedController right, XboxController gamepad, GyroBase gyro) {
		this(left, right, gamepad);
		this.gyro = gyro;
		this.timer = new Timer();

	}
	
	/**
	 * Constructs XboxDrive given two SpeedControllers and an XboxController
	 * @param left			The controller you want to be the left side of the robot
	 * @param right			The controller you want to be the right side of the robot
	 * @param controller	The XboxController object you would like to use
	 */
	public XboxDrive(SpeedController left, SpeedController right, XboxController gamepad) {
		frontLeft = left;
		motors[0] = left;
		frontRight = right;
		motors[1] = right;
		this.gamepad = gamepad;
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
	public XboxDrive(SpeedController frontRight, SpeedController frontLeft, SpeedController backLeft, SpeedController backRight, XboxController gamepad) {
		// merge controllers on same side
		this.frontRight = frontRight;
		motors[1] = frontRight;
		this.frontLeft = frontLeft;
		motors[0] = frontLeft;
		this.backLeft = backLeft;
		motors[2] = backLeft;
		this.backRight = backRight;
		motors[3] = backRight;
		this.gamepad = gamepad;
	}
	public boolean drive() {
		// the return of our function to check if the robot is driving
		// so that we can disable the ball cannon if it is
		boolean r = false;
		// PWMSpeedController.set() accepts between -1 and 1.
		// getTriggerAxis returns between 0 and 1.
		double speed = gamepad.getTriggerAxis(Hand.kRight) - gamepad.getTriggerAxis(Hand.kLeft);
		// square speed
		if (speed < 0) {
			speed = -Math.pow(speed, 2);
		} else {
			speed = Math.pow(speed, 2);
		}
		double x = gamepad.getX(Hand.kLeft);
		// square x
		if (x < 0) {
			x = -Math.pow(x, 2);
		} else {
			x = Math.pow(x, 2);
		}
		double leftSpeed = 0;
		double rightSpeed = 0;
		System.out.println(speed);
		if (gyro != null && timer.get() >= 0.5) {
			gyro.reset();
			timer.stop();
			timer.reset();
		}
		if (Math.abs(x) <= 0.15) {
			leftSpeed = speed;
			rightSpeed = -speed;
			if (gyro != null) {
				if (timer.get() < 0.5) {
					r = setLeftRightMotors(leftSpeed, rightSpeed);
				} else {
					r = setLeftRightMotors(leftSpeed, rightSpeed, gyro);
				}
			}
		} else {
			if (x > 0.0) {
				leftSpeed = speed;
				rightSpeed = (x - 0.5) * 2 * speed;
				
			} else {
				leftSpeed = (x + 0.5) * 2 * speed;
				rightSpeed = -speed;
			}
			r = setLeftRightMotors(leftSpeed, rightSpeed);
			if (gyro != null) {
					System.out.println("reset gyro");
					timer.reset();
					timer.start();
			}
		}

		SmartDashboard.putNumber("leftSpeed", leftSpeed);
		SmartDashboard.putNumber("rightSpeed", rightSpeed);
		if (gyro != null) {r = setLeftRightMotors(leftSpeed, rightSpeed, gyro);}
		else {r = setLeftRightMotors(leftSpeed, rightSpeed);}
		// debug
		return r;
	}
	
	public boolean setLeftRightMotors(double leftOutput, double rightOutput) {
		// if only two sides were specified in constructor, fronts were used and rears were null.
		leftOutput = limit * leftOutput;
		rightOutput = limit * rightOutput;
		set(frontLeft, leftOutput);
		if (backLeft != null) {
			set(backLeft, leftOutput);
		}
		
		set(frontRight, rightOutput);
		if (backRight != null) {
				set(backRight, rightOutput);
		}
		return (leftOutput != 0 && rightOutput != 0);
	}
	// TODO: Clean this up
	public boolean setLeftRightMotors(double leftSpeed, double rightSpeed, GyroBase gyro) {
		double heading = gyro.getAngle();
		double leftOutput = leftSpeed;
		double rightOutput = rightSpeed;
		if (heading > 1) {
			//leftSpeed is our base, if leftSpeed is forward than the robot should go forward
			if (leftSpeed > 0) {
				leftOutput = ((360 - heading) / 360) * leftSpeed;
				System.out.println("Adjust leftOutput");
			} else {
				rightOutput = ((360 - heading) / 360) * rightSpeed;
				System.out.println("Adjust rightOutput");
			}
		} else if (heading < -1) {
			if (leftSpeed > 0) {
				rightOutput = ((360 - heading) / 360) * rightSpeed;
				System.out.println("Adjust rightOutput");
			} else {
				leftOutput = ((360 - heading) / 360) * leftSpeed;
				System.out.println("Adjust leftOutput");
			}
		}
		// debug
		SmartDashboard.putNumber("leftOutput", leftOutput);
		SmartDashboard.putBoolean("lAdjust", leftOutput != leftSpeed);
		SmartDashboard.putNumber("rightOutput", rightOutput);
		SmartDashboard.putBoolean("rAdjust", rightOutput != rightSpeed);
		System.out.println(leftOutput + ", " + rightOutput + ", " + heading + ", " + gyro.getRate());
		
		return this.setLeftRightMotors(leftOutput, rightOutput);
	}
}
