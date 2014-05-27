package TTT.robots.actuators;

import lejos.robotics.RegulatedMotor;

public class SligShot{
	private RegulatedMotor m;

	public SligShot(RegulatedMotor m){
		this.m = m;
	}

	public void init(){
		this.m.stop();
		this.m.resetTachoCount();
	}

	public void moveToInit(){
		this.goTo(0,50);
	}

	public void throwBottom(){
		this.goTo(60,50);
		this.moveToInit();
	}

	public void throwTop(){
		this.goTo(120,50);
		this.moveToInit();
	}

	public void throwNet(){
		this.goTo(-75,1000);
	}

	private void goTo(int pos, int speed){
		int currentPos = this.m.getTachoCount();
		boolean forward = (currentPos < pos);
		boolean exit = (currentPos == pos);
		this.m.setSpeed(speed);
		if(forward){
			this.m.forward();
		}else{
			this.m.backward();
		}
		while(!exit){
			currentPos = this.m.getTachoCount();
			exit = (forward && currentPos >= pos) || (!forward && currentPos <= pos);
		}
		this.m.stop();
	}
}
