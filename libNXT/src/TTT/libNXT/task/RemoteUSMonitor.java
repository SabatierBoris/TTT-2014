package TTT.libNXT.task;

import java.util.Queue;
import java.util.ArrayList;

import TTT.libNXT.communication.Connexion;

import TTT.commons.communication.Message;
import TTT.commons.communication.USMonitorGetSensors;
import TTT.commons.communication.USMonitorSensors;
import TTT.commons.communication.USMonitorGetValue;
import TTT.commons.communication.USMonitorValue;
import TTT.commons.communication.MessageListener;

public class RemoteUSMonitor extends Thread implements USMonitor, MessageListener{
	private Connexion conn;
	private Queue<Message> queue;
	private USMonitorSensors sensors;
	private USMonitorValue value;

	public RemoteUSMonitor(Connexion conn){
		super();
		this.queue = new Queue<Message>();
		this.sensors = null;
		this.value = null;
		this.conn = conn;
		this.conn.addMessageListener(this,USMonitorSensors.ID);
		this.conn.addMessageListener(this,USMonitorValue.ID);
	}

	@Override
	public ArrayList<String> getNamesSensors(){
		this.sensors = null;
		synchronized(this){
			while(!this.isInterrupted() && this.sensors == null && this.conn.isConnected()){
				try{
					this.conn.send(new USMonitorGetSensors());
					this.wait(500);
				} catch(InterruptedException e){
					this.interrupt();
				}
			}
		}
		if(this.sensors == null){
			return null;
		}
		return this.sensors.getSensorsNames();
	}

	@Override
	public Integer getData(String name){
		this.value = null;
		synchronized(this){
			while(!this.isInterrupted() && this.value == null && this.conn.isConnected()){
				try{
					this.conn.send(new USMonitorGetValue(name));
					this.wait(500);
				} catch(InterruptedException e){
					this.interrupt();
				}
			}
		}
		if(this.value == null){
			return 255;
		}
		return this.value.getSensorValue();
	}

	@Override
	public void messageReceived(Message m){
		synchronized(this.queue){
			this.queue.push(m);
			this.queue.notify();
		}
	}

	@Override
	public void run(){
		Message m;
		synchronized(this.queue){
			try{
				while(!this.isInterrupted()){
					if(!this.queue.empty()){
						m = (Message)this.queue.pop();
						if(m.getId() == USMonitorSensors.ID){
							synchronized(this){
								this.sensors = (USMonitorSensors)m;
								this.notify();
							}
						}else if(m.getId() == USMonitorValue.ID){
							synchronized(this){
								this.value = (USMonitorValue)m;
								this.notify();
							}
						}
					}
					this.queue.wait();
				}
			} catch(InterruptedException e){
				this.interrupt();
			}
		}
	}
}

