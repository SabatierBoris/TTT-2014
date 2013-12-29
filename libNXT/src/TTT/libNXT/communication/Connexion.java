package TTT.libNXT.communication;

import lejos.nxt.comm.USB;
import lejos.nxt.comm.NXTConnection;

import TTT.commons.communication.Communicator;
import TTT.commons.communication.Message;

public class Connexion{
	private static Connexion instance = new Connexion();

	private Communicator comm;
	private NXTConnection connection;

	private Connexion(){
		this.comm = Communicator.getInstance();
		this.connection = null;
	}

	public boolean isConnected(){
		return (this.comm.isConnected() && this.connection != null);
	}

	public boolean connect(){
		System.out.println("Wait");
		this.connection = USB.waitForConnection();
		if(!this.comm.connect(this.connection.openInputStream(),this.connection.openOutputStream())){
			this.close();
			return false;
		}
		System.out.println("Connect");
		return true;
	}

	public void close(){
		this.comm.close();
		if(this.connection != null){
			this.connection.close();
			this.connection = null;
		}
	}

	public static Connexion getInstance(){
		return instance;
	}

	public String read(){
		Message in = this.comm.readMessage();
		if(in != null){
			return in.toString();
		} else {
			return "";
		}
	}

	public void send(Message m){
		this.comm.sendMessage(m);
	}
}



