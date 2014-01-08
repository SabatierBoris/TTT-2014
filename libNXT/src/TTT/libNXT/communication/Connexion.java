package TTT.libNXT.communication;

import lejos.nxt.comm.USB;
import lejos.nxt.comm.NXTConnection;

import TTT.commons.communication.Communicator;
import TTT.commons.communication.Message;

public class Connexion extends Thread{
	private static Connexion instance = new Connexion();

	private Communicator comm;
	private NXTConnection connection;

	private Connexion(){
		super();
		this.comm = Communicator.getInstance();
		this.connection = null;
	}

	@Override
	public void run(){
		while(!this.isInterrupted()){
			try{
				if(this.isConnected()){
					//System.out.println(this.read());
				} else if(!this.connect()){
					Thread.sleep(100);
				}
			} catch(InterruptedException e){
				this.interrupt();
			}
		}
	}

	public boolean isConnected(){
		return (this.comm.isConnected() && this.connection != null);
	}

	public synchronized boolean connect(){
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

	public synchronized String read(){
		Message in = this.comm.readMessage();
		if(in != null){
			return in.toString();
		} else {
			return "";
		}
	}

	public synchronized void send(Message m){
		if(this.isConnected()){
			if(!this.comm.sendMessage(m)){
				this.close();
			} else {
				System.out.println(m.toString());
			}
		}
	}
}



