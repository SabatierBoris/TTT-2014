package TTT.robots.actions;

import TTT.libNXT.ia.Action;

import TTT.robots.actuators.Arm;

public class DropFire extends Action{
	private Arm arm;

	public DropFire(Arm arm){
		super("DropFire");
		this.arm = arm;
	}

	@Override
	public void run(){
		this.arm.goToMin();
		this.arm.goToZero();
	}
}
