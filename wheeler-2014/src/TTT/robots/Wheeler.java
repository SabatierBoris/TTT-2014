package TTT.robots;

import TTT.commons.navigation.Pose;

import lejos.robotics.navigation.DifferentialPilot;
import lejos.nxt.Motor;
import lejos.nxt.NXTMotor;

import lejos.nxt.MotorPort;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.Button;
import lejos.nxt.TouchSensor;
import lejos.robotics.Touch;

import TTT.libNXT.sensor.MindSensorAngle;
import TTT.libNXT.sensor.MindSensorCompass;

import TTT.robots.actions.Arm;

import TTT.libNXT.robot.Robot;

import TTT.libNXT.communication.Connexion;

import TTT.libNXT.navigation.BasicOdometry;
import TTT.libNXT.navigation.DifferentialAsservise;
import TTT.libNXT.navigation.Navigator;

import TTT.libNXT.configuration.Configurateur;

import TTT.libNXT.task.PingResponse;
import TTT.libNXT.task.FixLinearAsserv;
import TTT.libNXT.task.FixAngularAsserv;
import TTT.libNXT.task.SendAsservInfo;

import TTT.libNXT.actuator.MirrorNXTMotor;

import lejos.nxt.addon.CompassMindSensor;

public class Wheeler extends Robot {

	private BasicOdometry odo;
	private Configurateur conf;
	private Touch starter;

	public Wheeler(){
		super(90000l);
		this.starter = new TouchSensor(SensorPort.S3);

		this.conf = Configurateur.getInstance();
		this.conf.setName("Wheeler");

		this.odo = new BasicOdometry(new MindSensorAngle(SensorPort.S1), new MindSensorAngle(SensorPort.S2));
		DifferentialAsservise asserv = new DifferentialAsservise(this.odo, new MirrorNXTMotor(MotorPort.A),new NXTMotor(MotorPort.B));
		Navigator nav = new Navigator(this.odo,asserv);

		this.addTask(this.conf);
		this.addTask(Connexion.getInstance());

		this.addTask(this.odo);
		this.addTask(asserv);
		this.addTask(nav);

		this.addTask(new FixLinearAsserv(asserv));
		this.addTask(new FixAngularAsserv(asserv));
		this.addTask(new SendAsservInfo(asserv));

		//this.addTask(new TaskTest2(this.odo,asserv));
		this.addTask(new TaskTest3(nav));

		//this.addTask(new TaskTest1(this.odo,Motor.A,Motor.B));
//		this.addTask(new TaskTest1(this.odo,new NXTMotor(MotorPort.A),new NXTMotor(MotorPort.B)));

		//this.addTask(nav);
//		this.addTask(new TaskTest(this.odo,asserv));
//		this.addTask(new TaskTest1(this.odo,asserv));
//		this.addTask(new SendPose(this.odo));
	}

	@Override
	public void insertStarter(){
		System.out.println("INSERT STARTER");
		while(!this.starter.isPressed()){
			Thread.yield();
		}
	}

	@Override
	public void removeStarter(){
		System.out.println("REMOVE STARTER");
		while(this.starter.isPressed()){
			Thread.yield();
		}
	}

	@Override
	public boolean ARUIsPress(){
		return this.starter.isPressed();
	}


	public static void main(String[] args){
		Wheeler bot = new Wheeler();
		bot.run();
		Configurateur.getInstance().save();//TODO improve
	}
}
