package TTT.robots;

import TTT.commons.navigation.Pose;

import lejos.robotics.navigation.DifferentialPilot;
import lejos.nxt.Motor;
import lejos.nxt.NXTMotor;

import lejos.nxt.addon.NXTMMX;
import lejos.nxt.addon.MMXRegulatedMotor;

import lejos.nxt.MotorPort;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.Button;
import lejos.nxt.TouchSensor;
import lejos.robotics.Touch;
import lejos.nxt.UltrasonicSensor;

import TTT.libNXT.sensor.MindSensorAngle;
import TTT.libNXT.sensor.MindSensorCompass;

import TTT.libNXT.ia.BasicIA;
import TTT.libNXT.ia.Action;

import TTT.libNXT.navigation.PathFollower;
import TTT.libNXT.navigation.pathFinding.Ground;

import TTT.robots.actuators.Arm;
import TTT.robots.actions.DropFire;
import TTT.robots.actions.PostDropFire;

import TTT.libNXT.robot.Robot;

import TTT.libNXT.communication.USBConnexion;

import TTT.libNXT.task.RealUSMonitor;

import TTT.libNXT.navigation.BasicOdometry;
import TTT.libNXT.navigation.DifferentialAsservise;
import TTT.libNXT.navigation.Navigator;

import TTT.libNXT.configuration.Configurateur;

import TTT.libNXT.task.PingResponse;
import TTT.libNXT.task.FixLinearAsserv;
import TTT.libNXT.task.FixAngularAsserv;
import TTT.libNXT.task.SendAsservInfo;
import TTT.libNXT.task.SendPose;

import TTT.libNXT.actuator.MirrorNXTMotor;

import lejos.nxt.addon.CompassMindSensor;

public class Wheeler extends Robot {

	private BasicOdometry odo;
	private Configurateur conf;
	private Touch starter;

	public Wheeler(){
		super(90000l);

		NXTMMX mmx = new NXTMMX(SensorPort.S4);
		MMXRegulatedMotor claw = new MMXRegulatedMotor(mmx,NXTMMX.MMX_MOTOR_2);
		Arm arm = new Arm(Motor.C);
		//TODO uncomment arm.init();

		this.starter = new TouchSensor(SensorPort.S3);

		USBConnexion usb = USBConnexion.getInstance();

		this.conf = Configurateur.getInstance();
		this.conf.setName("Wheeler");
		this.conf.setConn(usb);

		this.odo = new BasicOdometry(new MindSensorAngle(SensorPort.S1), new MindSensorAngle(SensorPort.S2),usb);
		DifferentialAsservise asserv = new DifferentialAsservise(this.odo, new MirrorNXTMotor(MotorPort.A),new NXTMotor(MotorPort.B));
		Navigator nav = new Navigator(this.odo,asserv,usb);

		Ground ground = new Ground(this.odo);
		PathFollower path = new PathFollower(nav,this.odo);

		BasicIA ia = new BasicIA(this.odo, ground, path);
		Action post = new PostDropFire(arm);
		post.addTarget(new Pose(50,0,0));
		ia.addAction(post);

		this.setIA(ia);

		RealUSMonitor usMon = new RealUSMonitor();
		usMon.addSensor("FRONT",new UltrasonicSensor(SensorPort.S4));

		this.addTask(this.conf);
		this.addTask(usb);

		this.addTask(this.odo);
		this.addTask(asserv);
		this.addTask(nav);
		this.addTask(path);
		this.addTask(usMon);

//		this.addTask(new FixLinearAsserv(asserv,usb));
//		this.addTask(new FixAngularAsserv(asserv,usb));
//		this.addTask(new SendAsservInfo(asserv,usb));

		//this.addTask(new TaskTest2(this.odo,asserv));
//		this.addTask(new TaskTest3(nav,usb));

		//this.addTask(new TaskTest1(this.odo,Motor.A,Motor.B));
//		this.addTask(new TaskTest1(this.odo,new NXTMotor(MotorPort.A),new NXTMotor(MotorPort.B)));

		//this.addTask(nav);
//		this.addTask(new TaskTest(this.odo,asserv));
//		this.addTask(new TaskTest1(this.odo,asserv));
//		this.addTask(new SendPose(this.odo,usb));
	}

	@Override
	public void initPose(){
		//TODO implement
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
