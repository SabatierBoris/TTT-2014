package TTT.robots.actions;

import TTT.libNXT.ia.Action;

import TTT.robots.actuators.SligShot;

public class FirstWave extends Action {
	private SligShot sligShot;

	public FirstWave(SligShot s){
		super("FirstWave");
		this.sligShot = s;
	}

	@Override
	public void run(){
		this.sligShot.throwBottom();
		this.sligShot.moveToInit();
	}
}
