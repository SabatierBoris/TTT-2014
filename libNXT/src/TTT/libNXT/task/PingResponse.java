package TTT.libNXT.task;

import java.util.Queue;

import TTT.commons.communication.Message;
import TTT.commons.communication.Ping;

import TTT.libNXT.communication.Connexion;
import TTT.libNXT.communication.MessageListener;

public class PingResponse extends Thread implements MessageListener{
	private Queue<Ping> queue;
	private Connexion conn;

	public PingResponse(){
		super();
		this.conn = Connexion.getInstance();
		this.conn.addMessageListener(this,Ping.ID);
		this.queue = new Queue<Ping>();
	}

	@Override
	public void run(){
		Ping p = null;
		synchronized(this.queue){
			try{
				while(!this.isInterrupted()){
					if(!this.queue.empty()){
						p = (Ping)this.queue.pop();
						if(p.isRecieved()){
							p.setSendBack();
							this.conn.send(p);
						}
					}
					this.queue.wait();
				}
			} catch(InterruptedException e){
				this.interrupt();
			}
		}
	}

	@Override
	public void messageReceived(Message m){
		synchronized(this.queue){
			this.queue.push((Ping) m);
			this.queue.notify();
		}
	}
}

