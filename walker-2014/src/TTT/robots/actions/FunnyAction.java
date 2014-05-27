package TTT.robots.actions;

import TTT.libNXT.ia.Action;

import TTT.robots.actuators.SligShot;

public class FunnyAction extends Action {
	private SligShot sligShot;

	public FunnyAction(SligShot s){
		super("FunnyAction");
		this.sligShot = s;
	}

	@Override
	public void run(){
		this.sligShot.throwNet();
		this.sligShot.moveToInit();
	}
}

