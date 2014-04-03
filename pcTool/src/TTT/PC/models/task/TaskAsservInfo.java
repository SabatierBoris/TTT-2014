package TTT.PC.models.task;

import TTT.PC.models.communication.ConnexionModel;

import TTT.commons.communication.MessageListener;
import TTT.commons.communication.AsservInfo;
import TTT.commons.communication.Message;

public class TaskAsservInfo extends Thread implements MessageListener {
	private ConnexionModel conn;

	public TaskAsservInfo(ConnexionModel conn){
		super();
		this.conn = conn;
		this.conn.addMessageListener(this,AsservInfo.ID);
	}

	@Override
	public void run(){
		while(!this.isInterrupted()){
			try{
				if(this.conn.isConnected()){
					this.conn.send(new AsservInfo());
				}
				Thread.sleep(100);
			} catch(InterruptedException e){
				this.interrupt();
			}
		}
	}

	@Override
	public void messageReceived(Message m){
		if(m.getId() == AsservInfo.ID){
			AsservInfo c = (AsservInfo)m;
			System.out.println(c.targetLinear + "    " + c.currentLinear);
		}

	}
}
