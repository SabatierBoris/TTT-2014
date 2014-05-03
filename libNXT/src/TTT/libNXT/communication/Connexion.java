package TTT.libNXT.communication;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Hashtable;

import lejos.nxt.comm.USB;
import lejos.nxt.comm.NXTConnection;

import TTT.commons.communication.Communicator;
import TTT.commons.communication.Message;
import TTT.commons.communication.MessageListener;

public class Connexion extends Thread{
	private static Connexion instance = new Connexion();

	private final Hashtable<Integer,Collection<MessageListener>> messageListeners = new Hashtable<Integer,Collection<MessageListener>>();

	private Communicator comm;
	private NXTConnection connection;

	private Connexion(){
		super();
		this.comm = new Communicator();
		this.connection = null;
	}

	private void fireMessageReceived(Message m){
		if(this.messageListeners != null && this.messageListeners.get(m.getId()) != null){
			for(MessageListener listener : this.messageListeners.get(m.getId())){
				listener.messageReceived(m);
			}
		}
	}

	public void addMessageListener(MessageListener listener, Integer t){
		Collection<MessageListener> tmp = this.messageListeners.get(t);
		if(tmp == null){
			tmp = new ArrayList<MessageListener>();
		}
		tmp.add(listener);
		this.messageListeners.put(t,tmp);
	}

	@Override
	public void run(){
		Message m;
		while(!this.isInterrupted()){
			try{
				if(this.isConnected()){
					m = this.read();
					if(m != null){
						this.fireMessageReceived(m);
					}
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
		try{
			this.connection = USB.waitForConnection();
			if(!this.comm.connect(this.connection.openInputStream(),this.connection.openOutputStream())){
				this.close();
				return false;
			}
			return true;
		} catch(NullPointerException e){
			this.close();
			return false;
		}
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

	public Message read(){
		Message in = this.comm.readMessage();
		return in;
	}

	public synchronized void send(Message m){
		if(this.isConnected()){
			if(!this.comm.sendMessage(m)){
				this.close();
			}
		}
	}
}



