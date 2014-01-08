package TTT.robots;

import lejos.nxt.Button;

import lejos.nxt.MotorPort;
import lejos.nxt.NXTRegulatedMotor;

import TTT.robots.actions.Arm;


public class Wheeler {
	public static void main(String[] args){
		/* Test Arm
		Arm a = new Arm(new NXTRegulatedMotor(MotorPort.A));
		System.out.println("Init");
		a.init();
		Button.waitForAnyPress();
		*/

		TaskTest t = new TaskTest();
		t.setDaemon(true);
		t.start();
		Button.waitForAnyPress();
		t.interrupt();
	}
}
