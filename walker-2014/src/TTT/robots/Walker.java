package TTT.robots;

import lejos.nxt.NXTMotor;
import lejos.nxt.MotorPort;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.Touch;

import lejos.robotics.MirrorMotor;

import TTT.libNXT.communication.Connexion;
import TTT.libNXT.communication.USBConnexion;
import TTT.libNXT.communication.BluetoothConnexion;
import TTT.libNXT.robot.Robot;
import TTT.libNXT.sensor.HitechnicSensorAngle;
import TTT.libNXT.task.SendPose;
import TTT.libNXT.configuration.Configurateur;
import TTT.libNXT.navigation.BasicOdometry;
import TTT.libNXT.navigation.TankAsservise;
import TTT.libNXT.navigation.Navigator;

import TTT.libNXT.actuator.MirrorNXTMotor;

import TTT.libNXT.task.USMonitor;
import TTT.libNXT.task.PingResponse;
import TTT.libNXT.task.FixLinearAsserv;
import TTT.libNXT.task.FixAngularAsserv;
import TTT.libNXT.task.SendAsservInfo;

import TTT.robots.actions.SligShot;

public class Walker extends Robot {

	private BasicOdometry odo;
	private Configurateur conf;
	private Touch starter;

	public Walker(){
		super(90000l);

		Connexion usb = USBConnexion.getInstance();
		BluetoothConnexion bluetooth = BluetoothConnexion.getInstance();

		this.starter = new TouchSensor(SensorPort.S3);

		this.conf = Configurateur.getInstance();
		this.conf.setConn(usb);
		this.conf.setName("Walker");

		this.odo = new BasicOdometry(new HitechnicSensorAngle(SensorPort.S1), new HitechnicSensorAngle(SensorPort.S2));
		TankAsservise asserv = new TankAsservise(this.odo, new NXTMotor(MotorPort.A), new MirrorNXTMotor(MotorPort.B));
		Navigator nav = new Navigator(this.odo,asserv);
		BluetoothConnexion blue = BluetoothConnexion.getInstance();
		byte[] pin = {(byte)'0',(byte)'4',(byte)'0',(byte)'5'};

		bluetooth.setMaster();
		bluetooth.setName("SENSORS");
		bluetooth.setPin(pin);
		
		this.addTask(this.conf);
		this.addTask(usb);
		this.addTask(bluetooth);

		this.addTask(this.odo);
		this.addTask(asserv);
		this.addTask(nav);

		this.addTask(new FixLinearAsserv(asserv,usb));
		this.addTask(new FixAngularAsserv(asserv,usb));
		this.addTask(new SendAsservInfo(asserv,usb));

		this.addTask(new TaskTest3(nav,usb));
		this.addTask(new TaskTestUS());

		/* TEST US */
		/*
		USMonitor usMon = new USMonitor();
		//usMon.addSensor("RIGHT",new UltrasonicSensor(SensorPort.S1));
		usMon.addSensor("LEFT",new UltrasonicSensor(SensorPort.S4));

		this.addTask(usMon);
		*/


		/* TEST SLIGSHOT */
/*
		SligShot s = new SligShot(Motor.C);


		System.out.println("Init");
		s.init();
		try{
			Thread.sleep(90000);
		}catch(InterruptedException e){
		}

		System.out.println("Throw Bottom");
		s.throwBottom();
		try{
			Thread.sleep(2000);
		}catch(InterruptedException e){
		}

		System.out.println("Throw Top");
		s.throwTop();
		try{
			Thread.sleep(2000);
		}catch(InterruptedException e){
		}

		System.out.println("Throw Net");
		s.throwNet();
		try{
			Thread.sleep(2000);
		}catch(InterruptedException e){
		}

		System.out.println("Move to Init");
		s.moveToInit();
		try{
			Thread.sleep(2000);
		}catch(InterruptedException e){
		}
*/
		/* END - TEST SLIGSHOT */

		//this.addTask(new TaskTest(nav));
		//this.addTask(new TaskTest(this.odo,asserv));
		//this.addTask(new SendPose(this.odo));
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
		Walker bot = new Walker();
		bot.run();
		Configurateur.getInstance().save();//TODO improve
	}
}
