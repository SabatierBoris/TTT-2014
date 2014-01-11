package TTT.robots;

import lejos.nxt.Button;

import lejos.nxt.MotorPort;
import lejos.nxt.NXTRegulatedMotor;

import TTT.robots.actions.Arm;

import TTT.libNXT.communication.Connexion;
import TTT.libNXT.task.PingResponse;
import TTT.libNXT.robot.Robot;

public class Wheeler extends Robot {

	public Wheeler(){
		super();
		this.addTask(Connexion.getInstance());
	//	this.addTask(new PingResponse());
		this.addTask(new TaskTest());
	}


	public static void main(String[] args){

		Wheeler bot = new Wheeler();
		bot.run();
		/* Test Arm
		Arm a = new Arm(new NXTRegulatedMotor(MotorPort.A));
		System.out.println("Init");
		a.init();
		Button.waitForAnyPress();
		*/

//		Connexion conn = Connexion.getInstance();
//		conn.setDaemon(true);
//		conn.start();

//		TaskTest t = new TaskTest();
//		PingResponse p = new PingResponse();
//		p.setDaemon(true);
//		p.start();
//		t.setDaemon(true);
//		t.start();
//		Button.waitForAnyPress();
//		p.interrupt();
//		t.interrupt();
//		conn.interrupt();
	}
}
