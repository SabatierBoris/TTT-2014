package TTT.libNXT.communication;

import lejos.nxt.comm.USB;

public class USBConnexion extends Connexion{
	private static USBConnexion instance = new USBConnexion();

	private USBConnexion(){
		super();
	}

	@Override
	public synchronized boolean connect(){
		try{
			if(!this.setConnection(USB.waitForConnection())){
				this.close();
				return false;
			}
			return true;
		} catch(NullPointerException e){
			this.close();
			return false;
		}
	}

	public static USBConnexion getInstance(){
		return instance;
	}
}
