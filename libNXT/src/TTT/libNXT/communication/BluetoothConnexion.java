package TTT.libNXT.communication;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.bluetooth.RemoteDevice;

import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;

import TTT.commons.communication.Communicator;
import TTT.commons.communication.Message;
import TTT.commons.communication.MessageListener;

public class BluetoothConnexion extends Thread{
	private static BluetoothConnexion instance = new BluetoothConnexion();

	private final Hashtable<Integer,Collection<MessageListener>> messageListeners = new Hashtable<Integer,Collection<MessageListener>>();

	private Communicator comm;
	private NXTConnection connection;
	private boolean master;
	private String name;
	private byte[] pin;

	private BluetoothConnexion(){
		super();
		this.comm = new Communicator();
		this.connection = null;
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
			this.connection = null;
			if(this.master){
				RemoteDevice btrd = Bluetooth.getKnownDevice(this.name);
				if(btrd != null){
					this.connection = Bluetooth.connect(btrd);
				}
			}else{
				Bluetooth.reset();
				Bluetooth.setFriendlyName(this.name);
				Bluetooth.setPin(this.pin);
				Bluetooth.setPower(true);
				System.out.println("Wait");
				this.connection = Bluetooth.waitForConnection();
			}
			if(this.connection == null || !this.comm.connect(this.connection.openInputStream(),this.connection.openOutputStream())){
				this.close();
				return false;
			}
			System.out.println("Connected");
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

	public static BluetoothConnexion getInstance(){
		return instance;
	}

	public Message read(){
		Message in = this.comm.readMessage();
		System.out.println("<- " + in.toString());
		return in;
	}

	public synchronized void send(Message m){
		if(this.isConnected()){
			System.out.println("-> " + m.toString());
			if(!this.comm.sendMessage(m)){
				this.close();
			}
		}
	}
}



