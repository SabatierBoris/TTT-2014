package TTT.libNXT.navigation;

import lejos.nxt.NXTMotor;

public class TankAsservise extends AbstractAsservise{

	public TankAsservise(BasicOdometry odo, NXTMotor left, NXTMotor right){
		super(odo,left,right);
		this.reset();
	}

	@Override
	protected void speedsUpdate(int linearSpeed, int angularSpeed){
		int M1Speed=0;
		int M2Speed=0;

		if(this.linearIsLock()){
			M1Speed += linearSpeed;
			M2Speed += linearSpeed;
		}
		if(this.angularIsLock()){
			M1Speed += angularSpeed;
			M2Speed -= angularSpeed;
		}

		if(this.linearIsLock() || this.angularIsLock()){
			this.setM1Float(false);
			this.setM2Float(false);
		}else{
			this.setM1Float(true);
			this.setM2Float(true);
		}
		this.setM1Speed(linearSpeed+angularSpeed);
		this.setM2Speed(linearSpeed-angularSpeed);
	}
}
