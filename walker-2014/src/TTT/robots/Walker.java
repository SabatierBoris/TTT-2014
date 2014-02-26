package TTT.robots;

import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;

import lejos.robotics.MirrorMotor;

import TTT.libNXT.communication.Connexion;
import TTT.libNXT.robot.Robot;
import TTT.libNXT.navigation.BasicOdometry;
import TTT.libNXT.sensor.HitechnicSensorAngle;
import TTT.libNXT.task.SendPose;
import TTT.libNXT.configuration.Configurateur;

import TTT.libNXT.navigation.TankAsservise;

public class Walker extends Robot {

	private BasicOdometry odo;
	private Configurateur conf;

	public Walker(){
		super(new TouchSensor(SensorPort.S3));
		this.conf = Configurateur.getInstance();
		this.conf.setName("Walker");
		this.odo = new BasicOdometry(new HitechnicSensorAngle(SensorPort.S1), new HitechnicSensorAngle(SensorPort.S2));
		TankAsservise asserv = new TankAsservise(this.odo, MirrorMotor.invertMotor(Motor.A), Motor.B);
		//Navigator nav = new Navigator(this.odo,asserv);
		this.addTask(Configurateur.getInstance());
		this.addTask(Connexion.getInstance());
		this.addTask(this.odo);
		this.addTask(asserv);
		//this.addTask(nav);
		//this.addTask(new TaskTest(nav));
		this.addTask(new TaskTest(this.odo,asserv));
		//this.addTask(new SendPose(this.odo));
	}

	public static void main(String[] args){
		Walker bot = new Walker();
		bot.run();
		Configurateur.getInstance().save();//TODO improve
	}
}
