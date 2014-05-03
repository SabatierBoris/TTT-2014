package TTT.libNXT.task;

import java.util.Queue;

import TTT.commons.communication.Message;
import TTT.commons.communication.Ping;
import TTT.commons.communication.MessageListener;

import TTT.libNXT.communication.Connexion;

public class PingResponse extends Thread implements MessageListener{
	private Queue<Ping> queue;
	private Connexion conn;

	public PingResponse(Connexion conn){
		super();
		this.queue = new Queue<Ping>();
		this.conn = conn;
		this.conn.addMessageListener(this,Ping.ID);
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
							Thread.yield();
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

