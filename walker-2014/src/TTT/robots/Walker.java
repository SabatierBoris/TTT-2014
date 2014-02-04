package TTT.robots;

import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;

import TTT.libNXT.communication.Connexion;
import TTT.libNXT.robot.Robot;
import TTT.libNXT.navigation.BasicOdometry;
import TTT.libNXT.sensor.HitechnicSensorAngle;

import TTT.robots.navigation.TankAsservise;

public class Walker extends Robot {

	private BasicOdometry odo;

	public Walker(){
		super(new TouchSensor(SensorPort.S3));
		this.odo = new BasicOdometry(new HitechnicSensorAngle(SensorPort.S1), 1,
									 new HitechnicSensorAngle(SensorPort.S2), -1, 1f, 1f);
		TankAsservise asserv = new TankAsservise(this.odo, Motor.A, Motor.B,
																 1,0,0,1,0,0);
		//Navigator nav = new Navigator(this.odo,asserv);
		this.addTask(Connexion.getInstance());
		this.addTask(this.odo);
		this.addTask(asserv);
		//this.addTask(nav);
		//this.addTask(new TaskTest(nav));
		this.addTask(new TaskTest(this.odo,asserv));
//		this.addTask(new SendPose(this.odo));
	}


	public static void main(String[] args){
		Walker bot = new Walker();
		bot.run();
	}
}
