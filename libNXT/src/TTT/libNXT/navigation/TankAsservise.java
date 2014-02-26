package TTT.libNXT.navigation;

import lejos.robotics.RegulatedMotor;

import TTT.libNXT.navigation.BasicOdometry;
import TTT.libNXT.navigation.AbstractAsservise;

public class TankAsservise extends AbstractAsservise{

	public TankAsservise(BasicOdometry odo, RegulatedMotor left, RegulatedMotor right){
		super(odo,left,right);
		this.reset();
	}

	@Override
	protected void speedsUpdate(int linearSpeed, int angularSpeed){
		this.setM1Speed(linearSpeed+angularSpeed);
		this.setM2Speed(linearSpeed-angularSpeed);
	}
}
