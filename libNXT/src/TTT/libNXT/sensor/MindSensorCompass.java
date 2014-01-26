package TTT.libNXT.sensor;

import lejos.nxt.I2CPort;
import lejos.nxt.I2CSensor;

import lejos.util.EndianTools;

import lejos.robotics.DirectionFinder;

public class MindSensorCompass extends I2CSensor implements DirectionFinder {

	private int zero;

	protected static final int DEFAULT_ADDRESS = 0x22;
	protected static final int CMD_REG = 0x41;

	protected static final int COMPASS_HEADING_REG = 0x4B; // 2 bytes
	protected static final int COMPASS_X_REG = 0x4D; // 2 bytes
	protected static final int COMPASS_Y_REG = 0x4F; // 2 bytes
	protected static final int COMPASS_Z_REG = 0x51; // 2 bytes

	protected static final int START_CALIBRATION_CMD = 0x43;
	protected static final int STOP_CALIBRATION_CMD = 0x63;
	protected static final int SENSITIVITY_2G_CMD = 0x31;
	protected static final int SENSITIVITY_4G_CMD = 0x32;
	protected static final int SENSITIVITY_8G_CMD = 0x33;
	protected static final int SENSITIVITY_16G_CMD = 0x34;

	public MindSensorCompass(I2CPort port, int address){
		super(port, address, I2CPort.LEGO_MODE, TYPE_LOWSPEED);
		this.resetCartesianZero();
	}

	public MindSensorCompass(I2CPort port){
		this(port,DEFAULT_ADDRESS);
	}

	public int getHeading(){
		byte buf[] = new byte[2];
		this.getData(COMPASS_HEADING_REG, buf, 2);
		return EndianTools.decodeShortLE(buf,0);
	}

	public int getX(){
		byte buf[] = new byte[4];
		this.getData(COMPASS_X_REG, buf, 2);
		return EndianTools.decodeShortLE(buf,0);
	}

	public int getY(){
		byte buf[] = new byte[4];
		this.getData(COMPASS_Y_REG, buf, 2);
		return EndianTools.decodeShortLE(buf,0);
	}

	public int getZ(){
		byte buf[] = new byte[4];
		this.getData(COMPASS_Z_REG, buf, 2);
		return EndianTools.decodeShortLE(buf,0);
	}

	@Override
	public float getDegreesCartesian(){
		return (float)((this.getHeading() - this.zero + 360)%360);
	}

	@Override
	public void resetCartesianZero(){
		this.zero = this.getHeading();
	}

	@Override
	public void startCalibration(){
		this.sendData(CMD_REG, (byte)START_CALIBRATION_CMD);
	}

	@Override
	public void stopCalibration(){
		this.sendData(CMD_REG, (byte)STOP_CALIBRATION_CMD);
	}
}
