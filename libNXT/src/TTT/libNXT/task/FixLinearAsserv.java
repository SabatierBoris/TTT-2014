package TTT.libNXT.task;

import TTT.commons.communication.Message;
import TTT.commons.communication.FixLinearAsservMessage;
import TTT.commons.communication.MessageListener;

import TTT.libNXT.communication.Connexion;

import TTT.libNXT.navigation.AbstractAsservise;

public class FixLinearAsserv extends Thread implements MessageListener{
	private AbstractAsservise asserv;
	private Connexion conn;
	private FixAsservState state;
	private int i;

	public FixLinearAsserv(AbstractAsservise asserv){
		super();
		this.i = 0;
		this.state = FixAsservState.STOP;
		this.asserv = asserv;
		this.conn = Connexion.getInstance();
		this.conn.addMessageListener(this,FixLinearAsservMessage.ID);
	}

	@Override
	public void run(){
		/*
		synchronized(this.state){
			try{
				while(!this.isInterrupted()){
					switch(this.state){
						case FORWARD:
							this.asserv.reset();
							this.asserv.setTarget(50,0);
							this.asserv.lockLinear();
							break;
						case BACKWARD:
							this.asserv.reset();
							this.asserv.setTarget(-50,0);
							this.asserv.lockLinear();
							break;
						case STOP:
						default:
							this.asserv.freeLinear();
							this.asserv.setTarget(0,0);
							this.asserv.reset();
					}
					this.state.wait();
				}
			}catch(InterruptedException e){
				this.interrupt();
			}
		}
		*/
	}

	@Override
	public void messageReceived(Message m){
		synchronized(this.state){
			this.i++;
			this.i%=4;
			switch(i){
				case 0:
				case 2:
				default:
					this.state = FixAsservState.STOP;
					break;
				case 1:
					this.state = FixAsservState.FORWARD;
					break;
				case 3:
					this.state = FixAsservState.BACKWARD;
					break;
			}
			this.state.notify();
		}
	}
}
