package TTT.robots;

import lejos.nxt.Button;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;

import TTT.libNXT.robot.Robot;

import TTT.libNXT.communication.BluetoothConnexion;

import TTT.libNXT.task.RealUSMonitor;
import TTT.libNXT.task.ServeUSMonitor;
import TTT.libNXT.task.WatchConnexion;

public class US extends Robot{

	public US(){
		super(0l);

		byte[] pin = {(byte)'0', (byte)'4', (byte)'0', (byte)'5'};
		BluetoothConnexion bluetooth = BluetoothConnexion.getInstance();

		bluetooth.setPin(pin);
		bluetooth.setName("SENSORS");
		bluetooth.setSlave();

		RealUSMonitor usMon = new RealUSMonitor();
		usMon.addSensor("FRONT-LEFT",new UltrasonicSensor(SensorPort.S4));
		usMon.addSensor("FRONT-RIGHT",new UltrasonicSensor(SensorPort.S1));
		usMon.addSensor("BACK-LEFT",new UltrasonicSensor(SensorPort.S2));
		usMon.addSensor("BACK-RIGHT",new UltrasonicSensor(SensorPort.S3));
		
		ServeUSMonitor servMon = new ServeUSMonitor(usMon,bluetooth);

		this.addTask(bluetooth);
		this.addTask(new WatchConnexion(bluetooth));
		this.addTask(usMon);
		this.addTask(servMon);
	}

	@Override
	public void insertStarter(){
	}

	@Override
	public void removeStarter(){
	}

	@Override
	public boolean ARUIsPress(){
		return (Button.waitForAnyPress(10) != 0);
	}

	public static void main(String[] args){
		US bot = new US();
		bot.run();
	}
}
