package org.usfirst.frc.team6542.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	final String defaultAuto = "Default";
	final String customAuto = "My Auto";
	String autoSelected;
	SendableChooser<String> chooser = new SendableChooser<>();
	XboxController gamepad;
	ADXRS450_Gyro gyro;
	XboxDrive drive;
	Cannon cannon;
	RopeClimber ropeClimber;
	Spark sparkLeft, sparkRight;
	Talon shooter;
	Talon deJammer;
	Talon climber;
	Timer autonTimer = new Timer();
	final int[] channels = new int[] {0, 1, 13, 14, 15};
	PowerDistributionPanel pdp;
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		System.out.println("robotInit method started");
		chooser.addDefault("Default Auto", defaultAuto);
		chooser.addObject("My Auto", customAuto);
		SmartDashboard.putData("Auto choices", chooser);
		pdp = new PowerDistributionPanel();
		// See if Driver Station has a method to figure out
		// the port that Xbox Contoller is on
		gamepad = new XboxController(0);
		gyro = new ADXRS450_Gyro();
		System.out.println("Calibrating Gyro...");
		gyro.calibrate();
		sparkLeft = new Spark(0);
		sparkRight = new Spark(1);
		shooter = new Talon(2);
		deJammer = new Talon(3);
		climber = new Talon(4);
		drive = new XboxDrive(sparkLeft, sparkRight, gamepad, gyro);
		cannon = new Cannon(shooter, deJammer, gamepad);
		ropeClimber = new RopeClimber(climber, gamepad);
		CameraServer.getInstance().startAutomaticCapture();
		System.out.println("robotInit complete");
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		// autoSelected = chooser.getSelected();
		autoSelected = SmartDashboard.getString("Auto Selector",
		defaultAuto);
		System.out.println("Auto selected: " + autoSelected);
		autonTimer.reset();
		autonTimer.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		switch (autoSelected) {
		case customAuto:
			break;
		case defaultAuto:
		default:
			// Put default auto code here
			if (autonTimer.get() < 3.5) {
				drive.setLeftRightMotors(0.5, 0.5);
			} else {
				drive.setLeftRightMotors(0, 0);
			}
			break;
		}
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		
		SmartDashboard.putNumber("Gyro", gyro.getAngle());
		for (int ch : channels) {
			SmartDashboard.putNumber(Integer.toString(ch), pdp.getCurrent(ch));
		}
		
		if (ropeClimber.climb()) {
			cannon.setAToggle(false);
			gyro.reset();
			// quick n dirty system disabler
			return;
		} else if (drive.drive()) {
			cannon.setAToggle(false);
			ropeClimber.setClimberMotor(0.0);
		}
		if (cannon.shoot()) {
			gyro.reset();
		}
		if (cannon.deJam()) {
			gyro.reset();
		}
		

	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
	}
}

