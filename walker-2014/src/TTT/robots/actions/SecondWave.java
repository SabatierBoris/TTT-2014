package TTT.robots.actions;

import TTT.libNXT.ia.Action;

import TTT.robots.actuators.SligShot;

public class SecondWave extends Action {
	private SligShot sligShot;

	public SecondWave(SligShot s){
		super("SecondWave");
		this.sligShot = s;
	}

	@Override
	public void run(){
		this.sligShot.throwTop();
		this.sligShot.moveToInit();
	}
}

