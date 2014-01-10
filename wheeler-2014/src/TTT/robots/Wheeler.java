package TTT.robots;

import lejos.nxt.Button;

import lejos.nxt.MotorPort;
import lejos.nxt.NXTRegulatedMotor;

import TTT.robots.actions.Arm;

import TTT.libNXT.communication.Connexion;
import TTT.libNXT.task.PingResponse;

public class Wheeler {
	public static void main(String[] args){
		/* Test Arm
		Arm a = new Arm(new NXTRegulatedMotor(MotorPort.A));
		System.out.println("Init");
		a.init();
		Button.waitForAnyPress();
		*/

		Connexion conn = Connexion.getInstance();
		conn.setDaemon(true);
		conn.start();

//		TaskTest t = new TaskTest();
		PingResponse p = new PingResponse();
		p.setDaemon(true);
		p.start();
//		t.setDaemon(true);
//		t.start();
		Button.waitForAnyPress();
		p.interrupt();
//		t.interrupt();
		conn.interrupt();
	}
}
