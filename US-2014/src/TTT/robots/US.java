package TTT.robots;

import lejos.nxt.Button;

import TTT.libNXT.robot.Robot;
import TTT.libNXT.communication.BluetoothConnexion;
import TTT.libNXT.task.WatchConnexion;

public class US extends Robot{

	public US(){
		super(0l);
		byte[] pin = {(byte)'0', (byte)'4', (byte)'0', (byte)'5'};
		BluetoothConnexion bluetooth = BluetoothConnexion.getInstance();
		bluetooth.setPin(pin);
		bluetooth.setName("SENSORS");
		bluetooth.setSlave();
		this.addTask(bluetooth);
		this.addTask(new WatchConnexion(bluetooth));
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
