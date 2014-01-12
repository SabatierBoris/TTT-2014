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

	private int leftWheelSize;
	private int rightWheelSize;

	private int capRatio;

	private ArrayList<PoseListener> poseListeners;

	public BasicOdometry(Tachometer left, int leftRatio, int leftWheelSize,
						 Tachometer right, int rightRatio, int rightWheelSize,
						 int capRatio, Pose current){
		this.leftSensor = left;
		this.rightSensor = right;

		this.leftRatio = leftRatio;
		this.rightRatio = rightRatio;

		this.leftWheelSize = leftWheelSize;
		this.rightWheelSize = rightWheelSize;

		this.capRatio = capRatio;

		this.poseListeners = new ArrayList<PoseListener>();

		this.initTachos();
		this.setCurrentPose(current);
	}

	public BasicOdometry(Tachometer left, int leftRatio, int leftWheelSize,
						 Tachometer right, int rightRatio, int rightWheelSize,
						 int capRatio){
		this(left,leftRatio,leftWheelSize,right,rightRatio,rightWheelSize,capRatio,new Pose());
	}

	protected void calculation(){
		int currentLeftCount = this.leftSensor.getTachoCount() * this.leftRatio;
		int currentRightCount = this.rightSensor.getTachoCount() * this.rightRatio;

		if(currentLeftCount != this.lastLeftCount || currentRightCount != this.lastRightCount){
			//On a bougé, donc on recalcule la position
			Pose newPose, tmpPose;
			Pose current = this.getCurrentPose();
			newPose = new Pose(current);

			// Calculate the wheels' distance traveled in mm
			float distanceLeft = (currentLeftCount - this.lastLeftCount) / (this.leftWheelSize * 1f);
			float distanceRight = (currentRightCount - this.lastRightCount) / (this.rightWheelSize * 1f);

			// Calculate the robot's distance traveled in mm
			float distance = (distanceLeft + distanceRight)/2;

			// Calculate the new robot's heading in degree
			int newHeading = current.getHeading() + Math.round((distanceLeft - distanceRight)/this.capRatio);

			// Move the position with the new heading and for the measured distance
			newPose.move(distance,newHeading);

			tmpPose = this.getCurrentPose();
			// If a new position is set by someone else during the calculation, we ignore the calculation
			if(!tmpPose.equals(current)){
				this.initTachos();
				return;
			}

			this.lastLeftCount = currentLeftCount;
			this.lastRightCount = currentRightCount;
			this.setCurrentPose(newPose);
		}
	}

	@Override 
	public void run(){
		System.out.println("Start Odo");
		while(!this.isInterrupted()){
			try{
				this.calculation();
				Thread.sleep(10); //TODO fix sleep time
			} catch(InterruptedException e){
				this.interrupt();
			}
		}
		System.out.println("End Odo");
	}

	private void initTachos(){
		this.leftSensor.resetTachoCount();
		this.rightSensor.resetTachoCount();
		this.lastLeftCount = this.leftSensor.getTachoCount();
		this.lastRightCount = this.rightSensor.getTachoCount();
	}

	private void firePoseChanged(Pose p){
		System.out.println(p);
		for(PoseListener listener : this.poseListeners){
			listener.poseChanged(p);
		}
	}

	public void addMessageListener(PoseListener listener){
		this.poseListeners.add(listener);
	}

	public synchronized void setCurrentPose(Pose p){
		this.currentPosition = new Pose(p);
		this.firePoseChanged(p);
	}
	public synchronized Pose getCurrentPose(){
		return new Pose(this.currentPosition);
	}
}

