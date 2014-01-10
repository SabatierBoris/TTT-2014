package TTT.robots;

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
		Ping p = new Ping("Toto");
		while(!this.isInterrupted()){
			if(!this.queue.empty()){
				this.queue.pop();
				this.conn.send(p);
			}
		}
	}

	@Override
	public void messageReceived(Message m){
		this.queue.push((Ping) m);
	}
}
