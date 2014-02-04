package TTT.libNXT.navigation;

import java.util.ArrayList;

import lejos.robotics.Tachometer;

import TTT.commons.navigation.Pose;
import TTT.commons.navigation.PoseListener;

public class BasicOdometry extends Thread {
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

	public BasicOdometry(Tachometer left, int leftRatio,
			   Tachometer right, int rightRatio, float coef_d, float coef_h){
		this.leftSensor = left;
		this.rightSensor = right;

		this.leftRatio = leftRatio;
		this.rightRatio = rightRatio;

		this.COEF_D = coef_d;
		this.COEF_H = coef_h;

		this.currentPosition = new Pose();

		this.codeursListeners = new ArrayList<CodeursListener>();
		this.poseListeners = new ArrayList<PoseListener>();

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

	private int getLeftCount(){
		return this.leftSensor.getTachoCount() * this.leftRatio;
	}
	private int getRightCount(){
		return this.rightSensor.getTachoCount() * this.rightRatio;
	}

	void calculation(){
		int currentLeftCount = this.getLeftCount();
		int currentRightCount = this.getRightCount();
		int prevDistance = this.distance;
		int prevOrient = this.orient;

		this.distance = (currentLeftCount + currentRightCount)/2;
		this.orient = (currentLeftCount - currentRightCount);
		this.fireCodeursChanged(this.distance,this.orient);
		if(currentLeftCount != this.lastLeftCount || currentRightCount != this.lastRightCount){
			Pose newPose, tmpPose;
			Pose current = this.getCurrentPose();
			newPose = new Pose(current);

			int delta_distance = this.distance - prevDistance;
			int delta_orient = (this.orient + prevOrient)/2;

			//newPose.move(delta_distance/COEF_D,newPose.getHeading()+Math.round(delta_orient/COEF_H));
			newPose.move(delta_distance/COEF_D);

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
		for(PoseListener listener : this.poseListeners){
			listener.poseChanged(p);
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
				Thread.sleep(10); //TODO fix sleep time
			} catch(InterruptedException e){
				this.interrupt();
			}
		}
	}


}
