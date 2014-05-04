package TTT.PC.models.communication;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.Hashtable;
import java.util.Collection;
import java.util.ArrayList;

import java.util.concurrent.ExecutionException;

import javax.swing.event.EventListenerList;

import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTInfo;

import TTT.commons.communication.Communicator;
import TTT.commons.communication.Message;
import TTT.commons.communication.MessageListener;

public class ConnexionModel extends Thread{
	private Communicator comm;
	private NXTComm connection;
	private EventListenerList listeners;
	private final Hashtable<Integer,Collection<MessageListener>> messageListeners = new Hashtable<Integer,Collection<MessageListener>>();
	private InputStream in;
	private OutputStream out;

	public ConnexionModel(){
		super();
		this.comm = new Communicator();
		this.connection = null;
		this.listeners = new EventListenerList();
		this.in = null;
		this.out = null;
	}

	private void fireMessagereceived(Message m){
		//System.out.println(m);
		if(this.messageListeners != null && this.messageListeners.get(m.getId()) != null){
			for(MessageListener listener : this.messageListeners.get(m.getId())){
				listener.messageReceived(m);
			}
		}
	}

	public void removeMessageListener(MessageListener listener, Integer t){
		Collection<MessageListener> tmp = this.messageListeners.get(t);
		if(tmp != null){
			tmp.remove(listener);
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
					m = null;
					m = this.read();
					if(m != null){
						this.fireMessagereceived(m);
					}
				} else{
					Thread.sleep(100);
				}
			} catch(InterruptedException e){
				this.close();
			}
		}
	}

	public boolean isConnected(){
		return (this.comm.isConnected() && this.connection != null);
	}

	public synchronized boolean connect(){
		NXTInfo[] infos;
		System.out.println("start connect");
		try{
			this.connection = NXTCommFactory.createNXTComm(NXTCommFactory.USB);
			infos = this.connection.search(null);
			if(infos.length>=1){
				this.connection.open(infos[0]);
				this.in = this.connection.getInputStream();
				this.out = this.connection.getOutputStream();
				this.comm.connect(this.in,this.out);
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
		if(this.in != null){
			try{
				this.in.close();
			} catch(IOException e){
				this.in = null;
			}
		}
		if(this.out != null){
			try{
				this.out.close();
			} catch(IOException e){
				this.out = null;
			}
		}
		this.comm.close();
		if(this.connection != null){
			try{
				this.connection.close();
			} catch(IOException e){
			}
			this.connection = null;
		}
		this.fireStatutChanged();
	}

	public void addConnexionListener(ConnexionListener listener){
		this.listeners.add(ConnexionListener.class,listener);
	}

	public void removeConnexionListener(ConnexionListener listener){
		this.listeners.remove(ConnexionListener.class,listener);
	}

	private void fireStatutChanged(){
		ConnexionListener[] listenerlist = this.listeners.getListeners(ConnexionListener.class);
		for(ConnexionListener listener : listenerlist){
			listener.statutChanged(new StatutChangedEvent(this,this.isConnected()));
		}
	}

	public Message read(){
		Message in = this.comm.readMessage();
		System.out.println("<- " + in);
		return in;
	}

	public synchronized void send(Message m){
		if(this.isConnected()){
			if(!this.comm.sendMessage(m)){
				this.close();
			} else {
				System.out.println("->" + m);
			}
		}
	}
}
