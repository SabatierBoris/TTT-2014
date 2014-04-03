package TTT.libNXT.actuator;

import lejos.nxt.NXTMotor;
import lejos.nxt.TachoMotorPort;

public class MirrorNXTMotor extends NXTMotor {
	public MirrorNXTMotor(TachoMotorPort port){
		super(port);
	}

	public MirrorNXTMotor(TachoMotorPort port, int PWMMode){
		super(port,PWMMode);
	}

	@Override
	public void setPower(int power){
		super.setPower(-power);
	}

	@Override
	public int getPower(){
		return -super.getPower();
	}

	@Override
	public int getTachoCount(){
		return -super.getTachoCount();
	}
}

