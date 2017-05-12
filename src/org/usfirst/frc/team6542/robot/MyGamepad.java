package org.usfirst.frc.team6542.robot;

import edu.wpi.first.wpilibj.XboxController;

public class MyGamepad extends XboxController {
	private boolean aPrev = false;
	boolean aToggle = false;
	
	public MyGamepad(int port){
		super(port);
	}
	
	public boolean getAToggle() {
		boolean a = getAButton();
		if (!aPrev && a) {
			aToggle = !aToggle;
		}
		aPrev = a;
		return aToggle;
	}
}
