package TTT.robots;

import TTT.commons.navigation.Pose;

import lejos.robotics.navigation.DifferentialPilot;
import lejos.nxt.Motor;

import lejos.nxt.MotorPort;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.Button;
import lejos.nxt.TouchSensor;

import TTT.libNXT.sensor.MindSensorAngle;
import TTT.libNXT.sensor.MindSensorCompass;

import TTT.robots.actions.Arm;

import TTT.libNXT.communication.Connexion;
import TTT.libNXT.task.PingResponse;
import TTT.libNXT.robot.Robot;
import TTT.libNXT.navigation.BasicOdometry;
import TTT.libNXT.navigation.Navigator;

import lejos.nxt.addon.CompassMindSensor;

public class Wheeler extends Robot {

	private BasicOdometry odo;

	public Wheeler(){
		super(new TouchSensor(SensorPort.S3));
		this.odo = new BasicOdometry(new MindSensorAngle(SensorPort.S1), 1,
									 new MindSensorAngle(SensorPort.S2), -1, 22.92f, 62.83f);
		DifferentialAsservise asserv = new DifferentialAsservise(this.odo, Motor.A, Motor.B,
																 1,0,0,1,0,0);
		//Navigator nav = new Navigator(this.odo,asserv);
		this.addTask(Connexion.getInstance());
		this.addTask(this.odo);
		this.addTask(asserv);
		//this.addTask(nav);
		//this.addTask(new TaskTest(nav));
		this.addTask(new TaskTest1(asserv));
		//this.addTask(new SendPose(this.odo));
	}


	public static void main(String[] args){
		Wheeler bot = new Wheeler();
		bot.run();
		/* Test compass
		MindSensorCompass c = new MindSensorCompass(SensorPort.S3);
		int i = 0;
		while(i<40){
			i++;
			System.out.println(c.getDegreesCartesian());
//			System.out.println(c.getX());
//			System.out.println(c.getY());
//			System.out.println(c.getZ());
			try{
				Thread.sleep(1000);
			} catch(Exception e){
			}
		}
		*/
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
