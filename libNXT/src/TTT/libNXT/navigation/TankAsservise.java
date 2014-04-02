package TTT.libNXT.navigation;

import lejos.nxt.NXTMotor;

public class TankAsservise extends AbstractAsservise{

	public TankAsservise(BasicOdometry odo, NXTMotor left, NXTMotor right){
		super(odo,left,right);
		this.reset();
	}

	@Override
	protected void speedsUpdate(int linearSpeed, int angularSpeed){
		this.setM1Speed(linearSpeed+angularSpeed);
		this.setM2Speed(linearSpeed-angularSpeed);
	}
}
