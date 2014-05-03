package TTT.libNXT.communication;

import javax.bluetooth.RemoteDevice;
import lejos.nxt.comm.Bluetooth;

public class BluetoothConnexion extends Connexion{
	private static BluetoothConnexion instance = new BluetoothConnexion();

	private boolean master;
	private String name;
	private byte[] pin;

	private BluetoothConnexion(){
		super();
		this.master = false;
		this.name = "";
		this.pin = null;
	}

	public void setPin(byte[] pin){
		this.pin = pin;
	}

	public void setName(String name){
		this.name = name;
	}

	public void setMaster(){
		this.master = true;
	}

	public void setSlave(){
		this.master = false;
	}

	@Override
	public synchronized boolean connect(){
		try{
			boolean ret = false;
			this.setConnection(null);
			if(this.master){
				RemoteDevice btrd = Bluetooth.getKnownDevice(this.name);
				if(btrd != null){
					ret = this.setConnection(Bluetooth.connect(btrd));
				}
			}else{
				Bluetooth.reset();
				Bluetooth.setFriendlyName(this.name);
				Bluetooth.setPin(this.pin);
				Bluetooth.setPower(true);
				ret = this.setConnection(Bluetooth.waitForConnection());
			}
			if(!ret){
				this.close();
				return false;
			}
			return true;
		} catch(NullPointerException e){
			this.close();
			return false;
		}
	}

	public static BluetoothConnexion getInstance(){
		return instance;
	}
}
