package TTT.libNXT.navigation;

//TODO Remove
import TTT.commons.communication.Error;
import TTT.libNXT.communication.Connexion;

import java.util.ArrayList;

import lejos.robotics.Tachometer;

import TTT.commons.navigation.Pose;
import TTT.commons.navigation.PoseListener;

import TTT.libNXT.configuration.Configurateur;
import TTT.libNXT.configuration.ConfigListener;

public class BasicOdometry extends Thread implements ConfigListener {
	private Pose currentPosition;

	private Tachometer leftSensor;
	private Tachometer rightSensor;

	private int leftRatio;
	private int rightRatio;

	private int lastLeftCount;
	private int lastRightCount;
	
	private int distance;
	private int orient;

	private ArrayList<CodeursListener> codeursListeners;
	private ArrayList<PoseListener> poseListeners;

	private float COEF_D;
	private float COEF_H;

	private int sleepTime;

	//TODO Remove
	private Connexion conn;

	public BasicOdometry(Tachometer left, Tachometer right){
		this.leftSensor = left;
		this.rightSensor = right;

		Configurateur conf = Configurateur.getInstance();

		this.leftRatio = Integer.parseInt(conf.get("odo.leftRatio","1"));
		this.rightRatio = Integer.parseInt(conf.get("odo.rightRatio","1"));

		this.COEF_D = Float.parseFloat(conf.get("odo.coefD","1"));
		this.COEF_H = Float.parseFloat(conf.get("odo.coefH","1"));

		this.sleepTime = Integer.parseInt(conf.get("odo.sleepTime","10"));

		this.currentPosition = new Pose();

		this.codeursListeners = new ArrayList<CodeursListener>();
		this.poseListeners = new ArrayList<PoseListener>();

		//TODO Remove
		this.conn = Connexion.getInstance();

		conf.addConfigListener(this,"odo");

		this.initTachos();
	}

	private void initTachos(){
		this.leftSensor.resetTachoCount();
		this.rightSensor.resetTachoCount();
		this.lastLeftCount = this.getLeftCount();
		this.lastRightCount = this.getRightCount();

		this.distance = 0;
		this.orient = 0;
	}

	private int getTachoCount(Tachometer tacho, int ratio){
		int value = tacho.getTachoCount();
		if(value == 0){
			value = tacho.getTachoCount();
		}
		return value*ratio;
	}


	private int getLeftCount(){
		return this.getTachoCount(this.leftSensor,this.leftRatio);
	}
	private int getRightCount(){
		return this.getTachoCount(this.rightSensor,this.rightRatio);
	}

	void calculation(){
		int currentLeftCount = this.getLeftCount();
		int currentRightCount = this.getRightCount();
		int prevDistance = this.distance;
		int prevOrient = this.orient;

//		System.out.println(currentLeftCount + " " + currentRightCount);

		this.distance = (currentLeftCount + currentRightCount)/2;
		//this.conn.send(new Error("Distance " + this.distance));
		this.orient = (currentLeftCount - currentRightCount);
		this.fireCodeursChanged(this.distance,this.orient);
		if(currentLeftCount != this.lastLeftCount || currentRightCount != this.lastRightCount){
			//this.conn.send(new Error(this.toString() + " - " + distance + " - " + orient));
			Pose newPose, tmpPose;
			Pose current = this.getCurrentPose();
			newPose = new Pose(current);

			int delta_distance = this.distance - prevDistance;
			int delta_orient = (this.orient + prevOrient)/2;

//			this.conn.send(new Error("Delta-O " + delta_orient/this.COEF_H));

			newPose.move(delta_distance/this.COEF_D,delta_orient/this.COEF_H);
			//newPose.move(delta_distance/COEF_D);

			tmpPose = this.getCurrentPose();
			// If a new position is set by someone else during the calculation, we ignore the calculation
			if(tmpPose.equals(current)){
				this.setCurrentPose(newPose);
			}

			this.lastLeftCount = currentLeftCount;
			this.lastRightCount = currentRightCount;
		}
	}

	private void fireCodeursChanged(int distance, int orient){
//		this.conn.send(new Error(this.toString() + " - " + distance + " - " + orient));
		for(CodeursListener listener : this.codeursListeners){
			listener.codeursChanged(distance,orient);
		}
	}

	public void addCodeursListener(CodeursListener listener){
		this.codeursListeners.add(listener);
	}

	public int getCurrentDistance(){
		return this.distance;
	}
	public int getCurrentOrient(){
		return this.orient;
	}

	private void firePoseChanged(Pose p){
		//this.conn.send(new Error(this.toString() + "-" + p.toString()));
		for(PoseListener listener : this.poseListeners){
			listener.poseChanged(new Pose(p));
		}
	}

	public void addPoseListener(PoseListener listener){
		this.poseListeners.add(listener);
	}

	public synchronized void setCurrentPose(Pose p){
		this.currentPosition = new Pose(p);
		this.firePoseChanged(p);
	}

	public synchronized Pose getCurrentPose(){
		return new Pose(this.currentPosition);
	}

	@Override 
	public void run(){
		while(!this.isInterrupted()){
			try{
				this.calculation();
				Thread.sleep(this.sleepTime);
			} catch(InterruptedException e){
				this.interrupt();
			}
		}
	}

	@Override
	public void configChanged(String key, String value){
		if(key.equals("odo.leftRatio")){
			this.leftRatio = Integer.parseInt(value);
		} else if(key.equals("odo.rightRatio")){
			this.rightRatio = Integer.parseInt(value);
		} else if(key.equals("odo.coefD")){
			this.COEF_D = Float.parseFloat(value);
		} else if(key.equals("odo.coefH")){
			this.COEF_H = Float.parseFloat(value);
		} else if(key.equals("odo.sleepTime")){
			this.sleepTime = Integer.parseInt(value);
		} else {
			this.conn.send(new Error("Unknow " + key));
		}
	}
}
