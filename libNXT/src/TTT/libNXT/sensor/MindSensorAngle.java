package TTT.libNXT.sensor;

import lejos.nxt.I2CPort;
import lejos.nxt.I2CSensor;

import lejos.util.EndianTools;

import lejos.robotics.Tachometer;

public class MindSensorAngle extends I2CSensor implements Tachometer {

	protected static final int DEFAULT_ADDRESS = 0x30;
	protected static final int CMD_REG = 0x41;
	protected static final int ANGLE_REG = 0x42; // 4 bytes
	protected static final int RAW_ANGLE_REG = 0x46; // 4 bytes
	protected static final int SPEED_REG = 0x4A; // 2 bytes
	protected static final char RESET_CMD = 'r';

	public MindSensorAngle(I2CPort port, int address){
		super(port, address, I2CPort.LEGO_MODE, TYPE_LOWSPEED);
		this.resetTachoCount();
	}

	public MindSensorAngle(I2CPort port){
		this(port,DEFAULT_ADDRESS);
	}

	public int getAngle(){
		byte buf[] = new byte[4];
		this.getData(ANGLE_REG, buf, 4);
		return EndianTools.decodeIntLE(buf,0);
	}

	public int getRawAngle(){
		byte buf[] = new byte[4];
		this.getData(RAW_ANGLE_REG, buf, 4);
		return EndianTools.decodeIntLE(buf,0);
	}

	public int getRPM(){
		byte buf[] = new byte[2];
		this.getData(SPEED_REG, buf, 2);
		return EndianTools.decodeShortLE(buf,0);
	}

	@Override
	public int getTachoCount(){
//		return this.getRawAngle();
		return this.getAngle();
	}

	@Override
	public void resetTachoCount(){
		this.sendData(CMD_REG, (byte)RESET_CMD);
	}

	@Override
	public int getRotationSpeed(){
		return this.getRPM();
	}
}
