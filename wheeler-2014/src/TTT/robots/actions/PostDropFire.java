package TTT.robots.actions;

import TTT.libNXT.ia.Action;

import TTT.robots.actuators.Arm;

public class PostDropFire extends Action{
	private Arm arm;

	public PostDropFire(Arm arm){
		super("PostDropFire");
		this.arm = arm;
	}

	@Override
	public void run(){
		this.arm.goToMax();
	}
}
