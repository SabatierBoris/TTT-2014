package TTT.PC.models.communication;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.io.IOException;

import javax.swing.event.EventListenerList;

import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTInfo;

import TTT.commons.communication.Communicator;
import TTT.commons.communication.Message;

public class ConnexionModel extends Thread{
	private Communicator comm;
	private NXTComm connection;
	private EventListenerList listeners;

	public ConnexionModel(){
		super();
		this.comm = Communicator.getInstance();
		this.connection = null;
		this.listeners = new EventListenerList();
	}

	@Override
	public void run(){
		String m;//TODO Change to Message
		while(!this.isInterrupted()){
			try{
				if(this.isConnected()){
					m = this.read();
					System.out.println(m.toString());
				} else{
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
		NXTInfo[] infos;
		try{
			this.connection = NXTCommFactory.createNXTComm(NXTCommFactory.USB);
			infos = this.connection.search("NXT");
			if(infos.length>=1){
				this.connection.open(infos[0]);
				this.comm.connect(this.connection.getInputStream(),this.connection.getOutputStream());
			} else {
				this.close();
				return false;
			}
		} catch(NXTCommException e){
			this.close();
			return false;
		}
		this.fireStatutChanged();
		return true;
	}

	public synchronized void close(){
		this.comm.close();
		if(this.connection != null){
			try{
				this.connection.close();
			} catch(IOException e){
			}
			this.connection = null;
		}
	}

	public void addConnexionListener(ConnexionListener listener){
		this.listeners.add(ConnexionListener.class,listener);
	}

	public void removeConnexionListener(ConnexionListener listener){
		this.listeners.remove(ConnexionListener.class,listener);
	}

	private void fireStatutChanged(){
		ConnexionListener[] listenerlist = this.listeners.getListeners(ConnexionListener.class);
		System.out.println("Fire Status Changed to " + this.isConnected());
		for(ConnexionListener listener : listenerlist){
			listener.statutChanged(new StatutChangedEvent(this,this.isConnected()));
		}
	}

	public String read(){
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
