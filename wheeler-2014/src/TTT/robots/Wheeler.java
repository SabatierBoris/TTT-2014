package TTT.robots;

import lejos.nxt.SensorPort;
import lejos.nxt.Button;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTRegulatedMotor;

import TTT.libNXT.sensor.MindSensorAngle;

import TTT.robots.actions.Arm;

import TTT.libNXT.communication.Connexion;
import TTT.libNXT.task.PingResponse;
import TTT.libNXT.robot.Robot;
import TTT.libNXT.navigation.BasicOdometry;

public class Wheeler extends Robot {

	private BasicOdometry odo;

	public Wheeler(){
		super();
		this.odo = new BasicOdometry(new MindSensorAngle(SensorPort.S1), 1, 22,
									 new MindSensorAngle(SensorPort.S2), -1, 22, 4);
		this.addTask(Connexion.getInstance());
		this.addTask(this.odo);
	//	this.addTask(new PingResponse());
		this.addTask(new TaskTest(this.odo));
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
