package TTT.robots;

import lejos.nxt.NXTMotor;
import lejos.nxt.MotorPort;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.Touch;

import TTT.commons.navigation.Pose;

import TTT.commons.communication.Error;

import TTT.libNXT.communication.Connexion;
import TTT.libNXT.communication.USBConnexion;
import TTT.libNXT.communication.BluetoothConnexion;
import TTT.libNXT.robot.Robot;
import TTT.libNXT.robot.Color;
import TTT.libNXT.sensor.HitechnicSensorAngle;
import TTT.libNXT.task.SendPose;
import TTT.libNXT.configuration.Configurateur;
import TTT.libNXT.navigation.BasicOdometry;
import TTT.libNXT.navigation.TankAsservise;
import TTT.libNXT.navigation.Navigator;
import TTT.libNXT.navigation.MovingAction;

import TTT.libNXT.navigation.PathFollower;
import TTT.libNXT.navigation.pathFinding.Ground;

import TTT.libNXT.ia.BasicIA;
import TTT.libNXT.ia.Action;
import TTT.libNXT.ia.EmptyAction;

import TTT.libNXT.actuator.MirrorNXTMotor;

import TTT.libNXT.task.WatchOpponent;
import TTT.libNXT.task.RemoteUSMonitor;
import TTT.libNXT.task.CacheUSMonitor;
//import TTT.libNXT.task.PingResponse;
import TTT.libNXT.task.FixLinearAsserv;
//import TTT.libNXT.task.FixAngularAsserv;
import TTT.libNXT.task.SendAsservInfo;

import TTT.robots.actuators.SligShot;
import TTT.robots.actions.FunnyAction;
import TTT.robots.actions.FirstWave;
import TTT.robots.actions.FresqueAction;
import TTT.robots.actions.SecondWave;

public class Walker extends Robot {

	private BasicOdometry odo;
	private Configurateur conf;
	private Touch starter;
	private Navigator nav;

	public Walker(){
		super(90000l);
		//this.selectColor();

		SligShot s = new SligShot(Motor.C);
		s.init();

		Connexion usb = USBConnexion.getInstance();
		BluetoothConnexion bluetooth = BluetoothConnexion.getInstance();
		byte[] pin = {(byte)'0',(byte)'4',(byte)'0',(byte)'5'};
		bluetooth.setMaster();
		bluetooth.setName("SENSORS");
		bluetooth.setPin(pin);

		this.starter = new TouchSensor(SensorPort.S3);

		this.conf = Configurateur.getInstance();
		this.conf.setConn(usb);
		this.conf.setName("Walker");

		RemoteUSMonitor usMon = new RemoteUSMonitor(bluetooth);
		CacheUSMonitor cacheUsMon = new CacheUSMonitor(usMon);

		this.odo = new BasicOdometry(new HitechnicSensorAngle(SensorPort.S1), new HitechnicSensorAngle(SensorPort.S2),usb);
		TankAsservise asserv = new TankAsservise(this.odo, new NXTMotor(MotorPort.A), new MirrorNXTMotor(MotorPort.B));
		this.nav = new Navigator(this.odo,asserv,usb);
		PathFollower pathFollow = new PathFollower(this.nav,this.odo);
		Ground ground = new Ground(this.odo);
		BasicIA ia = new BasicIA(this.odo,ground,pathFollow);
		ia.setFunnyAction(new FunnyAction(s));

		Action firstWave = new FirstWave(s);
		Action fresque = new FresqueAction(this.nav);
		Action secondWave = new SecondWave(s);
		Action goToNet = new EmptyAction("GoToNet");
		if(this.getColor() == Color.YELLOW){
			//YELLOW
			firstWave.addTarget(new Pose(600,800,90)); //TODO 
			fresque.addTarget(new Pose(150,1350,0)); //TODO
			secondWave.addTarget(new Pose(600,2100,0)); //TODO
			goToNet.addTarget(new Pose(600,2100,180)); //TODO
		}else{
			//RED
			firstWave.addTarget(new Pose(600,800,90));
			fresque.addTarget(new Pose(150,1350,0));
			secondWave.addTarget(new Pose(600,2100,0)); //TODO
			goToNet.addTarget(new Pose(600,2100,180)); //TODO
		}
		ia.addAction(firstWave);
		ia.addAction(fresque);
		ia.addAction(secondWave);
		ia.addAction(goToNet);

		WatchOpponent watch = new WatchOpponent(cacheUsMon,this.nav,pathFollow);

		watch.addFrontUS("FRONT-LEFT");
		watch.addFrontUS("FRONT-RIGHT");
		watch.addBackUS("BACK-LEFT");
		watch.addBackUS("BACK-RIGHT");
		watch.addLeftUS("FRONT-LEFT");
		watch.addLeftUS("BACK-LEFT");
		watch.addRightUS("FRONT-RIGHT");
		watch.addRightUS("BACK-RIGHT");

		this.addTask(this.conf);
		this.addTask(usb);
		this.addTask(bluetooth);

		this.addTask(this.odo);
		this.addTask(asserv);
		this.addTask(this.nav);
		this.addTask(pathFollow);
	
//		this.addTask(watch);
//		this.addTask(usMon);
//		this.addTask(cacheUsMon);
		
		this.setIA(ia);

//		this.addTask(new FixLinearAsserv(asserv,usb));
//		this.addTask(new FixAngularAsserv(asserv,usb));
//		this.addTask(new SendAsservInfo(asserv,usb));
//		this.addTask(new SendPose(this.odo,usb));
//		this.addTask(new TaskTest3(nav,usb,ground));
//		this.addTask(new TaskTest4(usb,pathFollow));
//		this.addTask(new TaskTestUS(cacheUsMon,usb));


		//this.addTask(new TaskTest(nav));
		//this.addTask(new TaskTest(this.odo,asserv));
	}

	private void waitFinishMove(){
		Pose current, newPose;
		newPose = this.odo.getCurrentPose();
		try{
			do{
				current = newPose;
				Thread.sleep(1000);
				newPose = this.odo.getCurrentPose();
			}while(current.substract(newPose).getDistance() > 0);
		}catch(InterruptedException e){
		}
	}

	@Override
	public void initPose(){
		/*
		this.nav.moveBackward(1000);
		this.waitFinishMove();
		this.nav.stopMoving();
		try{
			Thread.sleep(500);
		}catch(InterruptedException e){
		}
		this.nav.moveForward(100);
		this.waitFinishMove();
		this.nav.stopMoving();
		try{
			Thread.sleep(500);
		}catch(InterruptedException e){
		}
		//TODO Do the color difference
		this.nav.turnRight(90); //RED
		//END TODO
		this.waitFinishMove();
		this.nav.stopMoving();
		try{
			Thread.sleep(500);
		}catch(InterruptedException e){
		}
		this.nav.moveBackward(1000);
		this.waitFinishMove();
		this.nav.stopMoving();
		try{
			Thread.sleep(500);
		}catch(InterruptedException e){
		}
		*/

		//TODO Do the color difference
		if(this.getColor() == Color.YELLOW){
			this.odo.resetPose(new Pose(135,224,0));//TODO
		}else{
			//RED
			this.odo.resetPose(new Pose(135,224,0));//RED
		}
		//END TODO
	}

	@Override
	public void insertStarter(){
		//System.out.println("INSERT STARTER");
		while(!this.starter.isPressed()){
			Thread.yield();
		}
	}

	@Override
	public void removeStarter(){
		//System.out.println("REMOVE STARTER");
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
