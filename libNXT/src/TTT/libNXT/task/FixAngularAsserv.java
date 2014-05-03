package TTT.libNXT.task;

import TTT.commons.communication.Message;
import TTT.commons.communication.FixAngularAsservMessage;
import TTT.commons.communication.MessageListener;

import TTT.libNXT.communication.Connexion;

import TTT.libNXT.navigation.AbstractAsservise;

public class FixAngularAsserv extends Thread implements MessageListener{
	private AbstractAsservise asserv;
	private Connexion conn;
	private FixAsservState state;
	private int i;

	public FixAngularAsserv(AbstractAsservise asserv, Connexion conn){
		super();
		this.i = 0;
		this.state = FixAsservState.STOP;
		this.asserv = asserv;
		this.conn = conn;
		this.conn.addMessageListener(this,FixAngularAsservMessage.ID);
	}

	@Override
	public void run(){
		synchronized(this){
			try{
				while(!this.isInterrupted()){
					if(this.state == FixAsservState.TURNLEFT){
						this.asserv.reset();
						this.asserv.setTarget(0,50);
						this.asserv.lockAngular();
					}else if(this.state == FixAsservState.TURNRIGHT){
						this.asserv.reset();
						this.asserv.setTarget(0,-50);
						this.asserv.lockAngular();
					}else{
						this.asserv.freeAngular();
						this.asserv.setTarget(0,0);
						this.asserv.reset();
					}
					this.wait();
				}
			}catch(InterruptedException e){
				this.interrupt();
			}
		}
	}

	@Override
	public void messageReceived(Message m){
		synchronized(this){
			if(m.getId() == FixAngularAsservMessage.ID){
				this.i++;
				this.i%=4;
				switch(i){
					case 0:
					case 2:
					default:
						this.state = FixAsservState.STOP;
						break;
					case 1:
						this.state = FixAsservState.TURNLEFT;
						break;
					case 3:
						this.state = FixAsservState.TURNRIGHT;
						break;
				}
				this.notify();
			}
		}
	}
}
