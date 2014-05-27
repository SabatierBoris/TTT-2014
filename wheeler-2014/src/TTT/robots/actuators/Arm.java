package TTT.robots.actuators;

import lejos.robotics.RegulatedMotor;

public class Arm{
	private RegulatedMotor m;
	public Arm(RegulatedMotor m){
		this.m = m;
	}

	public void init(){
		this.m.setSpeed(50);
		this.m.forward();
		while(this.m.isMoving()){
		}
		this.m.flt();
		try{
			Thread.sleep(1500);
		} catch(InterruptedException e){
		}
		this.m.resetTachoCount();
		this.m.flt();
		this.goToMax();
		try{
			Thread.sleep(1500);
		} catch(InterruptedException e){
		}
		this.goToMin();
		try{
			Thread.sleep(1500);
		} catch(InterruptedException e){
		}
		this.goToZero();
	}

	public void goToZero(){
		//System.out.println("Goto Zero");
		this.goTo(-35);
	}
	
	public void goToMin(){
		//System.out.println("Goto Min");
		this.goTo(0);
	}

	public void goToMax(){
		//System.out.println("Goto Max");
		this.goTo(-350);
	}

	private void goTo(int pos){
		int currentPose = this.m.getTachoCount();
		boolean forward = (currentPose < pos);
		boolean exit = (currentPose == pos);
		this.m.setSpeed(375);
		if(forward){
			this.m.forward();
		} else {
			this.m.backward();
		}
		while(!exit){
			currentPose = this.m.getTachoCount();
			exit = (forward && currentPose >= pos) || (!forward && currentPose <= pos);
		}
		this.m.stop();
		this.m.flt();
	}
}
