package TTT.libNXT.navigation;

import lejos.nxt.NXTMotor;

public class DifferentialAsservise extends AbstractAsservise{
	public DifferentialAsservise(BasicOdometry odo, NXTMotor linear, NXTMotor rotation){
		super(odo,linear,rotation);
		this.reset();
	}

	@Override 
	protected void speedsUpdate(int linearSpeed, int angularSpeed){
		if(this.linearIsLock() || this.angularIsLock()){
			this.setM1Float(false);
			this.setM2Float(false);
		}else{
			this.setM1Float(true);
			this.setM2Float(true);
		}


		if(this.linearIsLock()){
			this.setM1Speed(linearSpeed);
		}else{
			this.setM1Speed(0);
		}
		if(this.angularIsLock()){
			this.setM2Speed(-1*angularSpeed);
		}else{
			this.setM2Speed(0);
		}
	}
}
