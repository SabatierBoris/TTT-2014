package TTT.libNXT.navigation;

import lejos.nxt.NXTMotor;

public class DifferentialAsservise extends AbstractAsservise{
	public DifferentialAsservise(BasicOdometry odo, NXTMotor linear, NXTMotor rotation){
		super(odo,linear,rotation);
		this.reset();
	}

	@Override 
	protected void speedsUpdate(int linearSpeed, int angularSpeed){
		if(this.linearIsLock()){
			this.setM1Float(false);
			this.setM1Speed(linearSpeed);
		} else {
			this.setM1Float(true);
		}
		if(this.angularIsLock()){
			this.setM2Float(false);
			this.setM2Speed(-1*angularSpeed);
		} else {
			this.setM2Float(true);
		}
	}
}
