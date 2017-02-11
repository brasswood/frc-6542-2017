package org.usfirst.frc.team6542.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.TalonSRX;
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
	boolean aToggle;
	boolean aPrev;
	ADXRS450_Gyro gyro;
	XboxDrive drive;
	Spark sparkLeft, sparkRight;
	TalonSRX ballCannon;
	Timer autonTimer = new Timer();
	final int[] channels = new int[] {0, 1, 14, 15};
	PowerDistributionPanel pdp;
	// NetworkTable myTable;
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
		// myTable = NetworkTable.getTable("datatable");
		// CameraServer.getInstance().startAutomaticCapture(0);
		// See if Driver Station has a method to figure out
		// the port that Xbox Contoller is on
		gamepad = new XboxController(0);
		gyro = new ADXRS450_Gyro();
		System.out.println("Calibrating Gyro...");
		gyro.calibrate();
		sparkLeft = new Spark(0);
		sparkRight = new Spark(1);
		ballCannon = new TalonSRX(2);
		drive = new XboxDrive(sparkLeft, sparkRight, gamepad, gyro);
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
			if (autonTimer.get() < 3) {
				drive.setLeftRightMotors(0.5, 0.5);
			} else {
				drive.setLeftRightMotors(0, 0);
			}
			break;
		case defaultAuto:
		default:
			// Put default auto code here
			break;
		}
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		drive.drive();
		
		if (gamepad.getAButton() && !aPrev) {
			aToggle = !aToggle;
		}
		// NOTE: The following line must go after getAButton
		// is compared to aPrev
		aPrev = gamepad.getAButton();
		
		if (aToggle) {
			double speed = Math.hypot(gamepad.getX(Hand.kRight), gamepad.getY(Hand.kRight));
			ballCannon.set(speed);
		}
		
		SmartDashboard.putNumber("Gyro", gyro.getAngle());
		for (int ch : channels) {
			SmartDashboard.putNumber(Integer.toString(ch), pdp.getCurrent(ch));
		}
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
	}
}

