package TTT.robots;

import TTT.libNXT.communication.Connexion;
import TTT.commons.communication.Error;
import TTT.commons.communication.Battery;
import TTT.commons.communication.MessageListener;
import TTT.commons.communication.Message;

import TTT.commons.navigation.Pose;
import TTT.commons.navigation.PoseListener;

import TTT.libNXT.navigation.BasicOdometry;
import TTT.libNXT.navigation.AbstractAsservise;

import TTT.libNXT.configuration.Configurateur;
import TTT.libNXT.configuration.ConfigListener;

public class TaskTest extends Thread implements PoseListener, ConfigListener, MessageListener {
	private AbstractAsservise asserv;

	private int currentLinearSpeed;
	private int currentAngularSpeed;

	private static final int maxLinearSpeed = 30; //TODO Fixe
	private static final int minLinearSpeed = 2; //TODO Fixe

	private static final int maxAngularSpeed = 300; //TODO Fixe
	private static final int minAngularSpeed = 1; //TODO Fixe

	private static final int linearAccel = 1;
	private static final int angularAccel = 1;

	private static final int refreshTime = 100;//TODO Fix Time

	private Pose targetPose;
	private Pose currentPose;
	private boolean walk;

	//TODO Remove
	private Connexion conn;

	public TaskTest(BasicOdometry odo, AbstractAsservise asserv, Connexion conn){
		super();
		this.walk = false;
		this.currentLinearSpeed = 0;
		this.currentAngularSpeed = 0;
		this.asserv = asserv;
		this.targetPose = new Pose(0,100,0);
		this.currentPose = new Pose();
		//TODO Remove
		this.conn = conn;
		this.conn.addMessageListener(this,Battery.ID);
		Configurateur conf = Configurateur.getInstance();
		conf.addConfigListener(this,"test");
		odo.addPoseListener(this);
	}

	@Override
	public void messageReceived(Message m){
		if(m.getId() == Battery.ID){
			this.walk = true;
		}
	}

	@Override
	public void configChanged(String key, String value){
		this.conn.send(new Error(key + " -> " + value));
		if(key.equals("test.lineSpeed")){
			this.currentLinearSpeed = Integer.parseInt(value);
		} else if (key.equals("test.angleSpeed")){
			this.currentAngularSpeed = Integer.parseInt(value);
		}
		this.applySpeed();
	}

	private void applySpeed(){
		int linear = this.currentLinearSpeed;
		int angular = this.currentAngularSpeed;
		/*
		if(linear > 0){
			linear = Math.min(Math.max(this.currentLinearSpeed,TaskTest1.minLinearSpeed),TaskTest1.maxLinearSpeed);
		}
		if(angular > 0){
			angular = Math.min(Math.max(this.currentAngularSpeed,TaskTest1.minAngularSpeed),TaskTest1.maxAngularSpeed);
		}
		*/
		this.asserv.setTarget(linear,angular);
	}

	@Override
	public void poseChanged(Pose p){
		synchronized(this.currentPose){
			this.currentPose = new Pose(p);
		}
	}

	@Override
	public void run(){
		Pose tmpCurrent;
		Pose tmpTarget;
		Pose diff;
		double diffDistance;
		int stopDistance;
		int tmp;
		try{
			while(true){
				Thread.sleep(1000);
				if(this.walk == true){
					this.currentLinearSpeed = 0;
					this.currentAngularSpeed = 0;
					this.applySpeed();
					Thread.sleep(100);
					this.asserv.lockLinear();
					this.conn.send(new Error("START WALK"));
					this.currentLinearSpeed = -100;
					this.currentAngularSpeed = 0;
					this.applySpeed();
					Thread.sleep(10000);
					this.conn.send(new Error("STOP WALK"));
					this.currentLinearSpeed = 0;
					this.currentAngularSpeed = 0;
					this.applySpeed();
					this.asserv.freeLinear();
					Thread.sleep(100);
					this.currentLinearSpeed = 0;
					this.currentAngularSpeed = 0;
					this.applySpeed();
					Thread.sleep(100);
					this.currentLinearSpeed = 0;
					this.currentAngularSpeed = 0;
					this.applySpeed();
					Thread.sleep(100);
					this.currentLinearSpeed = 0;
					this.currentAngularSpeed = 0;
					this.applySpeed();
					Thread.sleep(100);
					this.currentLinearSpeed = 0;
					this.currentAngularSpeed = 0;
					this.applySpeed();
					Thread.sleep(100);
					this.currentLinearSpeed = 0;
					this.currentAngularSpeed = 0;
					this.applySpeed();
					Thread.sleep(100);
					this.walk = false;
				}
			}
		/*
			Thread.sleep(5000);




			while(!this.isInterrupted()){
				synchronized(this.currentPose){
					tmpCurrent = new Pose(this.currentPose);
				}
				synchronized(this.targetPose){
					tmpTarget = new Pose(this.targetPose);
				}
				diffDistance = tmpTarget.getDistance() - tmpCurrent.getDistance();
				diff = tmpTarget.substract(tmpCurrent);
				//this.conn.send(new Error("DIFF POSE - " + tmpTarget.getAngleTo(tmpCurrent)));
				//this.conn.send(new Error("DIFF - " + diffDistance));
				//this.conn.send(new Error("Distance - " + diff.getDistance()));

				stopDistance = 0;
				tmp = this.currentLinearSpeed;
				while(tmp > 0){
					stopDistance += tmp;
					tmp -= TaskTest1.linearAccel;
				}

				if(diffDistance == 0){
					this.currentLinearSpeed = 0;
				}
				else if(stopDistance < diffDistance){
					//ACCEL OR STAY
					this.currentLinearSpeed += TaskTest1.linearAccel;
					if(this.currentLinearSpeed > TaskTest1.maxLinearSpeed){
						this.currentLinearSpeed = TaskTest1.maxLinearSpeed;
					}
				} else {
					//DECCEL
					this.currentLinearSpeed -= TaskTest1.linearAccel;
				}
				//this.conn.send(new Error("SPEED - " + this.currentLinearSpeed));
				this.currentAngularSpeed = 0;
				this.applySpeed();
				Thread.sleep(TaskTest1.refreshTime);
			}
			*/



			/*
			Thread.sleep(2000);
			this.currentLinearSpeed = 10;
			this.currentAngularSpeed = 0;
			this.applySpeed();

			Thread.sleep(3000);
			this.currentLinearSpeed = 0;
			this.currentAngularSpeed = 0;
			this.applySpeed();
			Thread.sleep(200);
			this.currentLinearSpeed = 0;
			this.currentAngularSpeed = 0;
			this.applySpeed();
			Thread.sleep(200);
			this.currentLinearSpeed = 0;
			this.currentAngularSpeed = 0;
			this.applySpeed();
			Thread.sleep(200);
			this.currentLinearSpeed = 0;
			this.currentAngularSpeed = 0;
			this.applySpeed();
			*/
/*			System.out.println("Accel");
			while(this.currentLinearSpeed < TaskTest1.maxLinearSpeed){
				Thread.sleep(10);
				this.currentLinearSpeed += TaskTest1.linearAccel;
				this.applySpeed();
			}
			System.out.println("STAY");
			Thread.sleep(2000);
			*/
			/*
			while(this.currentAngularSpeed < TaskTest1.maxAngularSpeed){
				Thread.sleep(10);
				this.currentAngularSpeed += TaskTest1.angularAccel;
				this.applySpeed();
			}
			*/
			/*
			System.out.println("DECELL");
			while(this.currentLinearSpeed > 0){
				Thread.sleep(10);
				this.currentLinearSpeed -= TaskTest1.linearAccel;
				this.applySpeed();
			}
			*/
			/*
			while(this.currentAngularSpeed > 0){
				Thread.sleep(10);
				this.currentAngularSpeed -= TaskTest1.angularAccel;
				this.applySpeed();
			}
			*/
		} catch(InterruptedException e){
			this.interrupt();
		}
	}
}
