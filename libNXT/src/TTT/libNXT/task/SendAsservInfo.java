package TTT.libNXT.task;

import TTT.commons.communication.Message;
import TTT.commons.communication.AsservInfo;
import TTT.commons.communication.MessageListener;

import TTT.libNXT.communication.Connexion;

import TTT.libNXT.navigation.AbstractAsservise;

public class SendAsservInfo extends Thread implements MessageListener{
	private AbstractAsservise asserv;
	private Connexion conn;

	public SendAsservInfo(AbstractAsservise asserv){
		super();
		this.asserv = asserv;
		this.conn = Connexion.getInstance();
		this.conn.addMessageListener(this,AsservInfo.ID);
	}

	@Override
	public void run(){
		synchronized(this){
			try{
				while(!this.isInterrupted()){
					this.wait();
					this.conn.send(new AsservInfo(this.asserv.getTargetLinearSpeed(),
				  								  this.asserv.getCurrentLinearSpeed(),
												  this.asserv.getTargetAngularSpeed(),
												  this.asserv.getCurrentAngularSpeed()));
				}
			} catch(InterruptedException e){
				this.interrupt();
			}
		}
	}

	@Override
	public void messageReceived(Message m){
		synchronized(this){
			this.notify();
		}
	}
}
