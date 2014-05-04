package TTT.libNXT.task;

import java.util.Queue;
import java.util.Iterator;

import TTT.libNXT.communication.Connexion;

import TTT.commons.communication.Message;
import TTT.commons.communication.USMonitorGetSensors;
import TTT.commons.communication.USMonitorSensors;
import TTT.commons.communication.USMonitorGetValue;
import TTT.commons.communication.USMonitorValue;
import TTT.commons.communication.MessageListener;

public class ServeUSMonitor extends Thread implements MessageListener{
	private Connexion conn;
	private Queue<Message> queue;
	private USMonitor mon;

	public ServeUSMonitor(USMonitor mon, Connexion conn){
		super();
		this.queue = new Queue<Message>();
		this.mon = mon;
		this.conn = conn;
		this.conn.addMessageListener(this,USMonitorGetSensors.ID);
		this.conn.addMessageListener(this,USMonitorGetValue.ID);
	}

	@Override
	public void run(){
		Message m;
		synchronized(this.queue){
			try{
				while(!this.isInterrupted()){
					if(!this.queue.empty()){
						m = (Message)this.queue.pop();
						if(m.getId() == USMonitorGetSensors.ID){
							this.sendSensors();
						}else if(m.getId() == USMonitorGetValue.ID){
							this.sendValue(((USMonitorGetValue)m).getSensorName());
						}
					}
					this.queue.wait();
				}
			} catch(InterruptedException e){
				this.interrupt();
			}
		}
	}

	private void sendSensors(){
		USMonitorSensors msg = new USMonitorSensors();
		Iterator<String> it = this.mon.getNamesSensors().iterator();
		while(it.hasNext()){
			msg.addSensorName(it.next());
		}
		this.conn.send(msg);
	}
	
	private void sendValue(String sensorName){
		//System.out.println("SERVE : " + sensorName);
		Integer val = this.mon.getData(sensorName);
		//System.out.println("SERVE : SendValue " + val);
		if(val == null){
			val = 255;
		}
		this.conn.send(new USMonitorValue(sensorName,val));
	}

	@Override
	public void messageReceived(Message m){
		synchronized(this.queue){
			this.queue.push(m);
			this.queue.notify();
		}
	}
}
