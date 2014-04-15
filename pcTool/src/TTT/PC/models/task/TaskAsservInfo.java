package TTT.PC.models.task;

import TTT.PC.models.communication.ConnexionModel;

import TTT.commons.communication.MessageListener;
import TTT.commons.communication.AsservInfo;
import TTT.commons.communication.Message;
import TTT.PC.models.graph.GraphModel;

public class TaskAsservInfo extends Thread implements MessageListener {
	private ConnexionModel conn;
	private GraphModel linearGraphModel;
	private GraphModel angularGraphModel;
	private boolean started;

	public TaskAsservInfo(ConnexionModel conn, GraphModel linearGraphModel, GraphModel angularGraphModel){
		super();
		this.linearGraphModel = linearGraphModel;
		this.angularGraphModel = angularGraphModel;
		this.conn = conn;
		this.conn.addMessageListener(this,AsservInfo.ID);
		this.started = false;
	}

	public void startSend(){
		this.started = true;
	}

	public void stopSend(){
		this.started = false;
	}

	@Override
	public void run(){
		while(!this.isInterrupted()){
			try{
				if(this.started && this.conn.isConnected()){
					this.conn.send(new AsservInfo());
				}
				Thread.sleep(50);
			} catch(InterruptedException e){
				this.interrupt();
			}
		}
	}

	@Override
	public void messageReceived(Message m){
		if(m.getId() == AsservInfo.ID){
			AsservInfo c = (AsservInfo)m;
			linearGraphModel.addTarget(c.targetLinear);
			linearGraphModel.addData(c.currentLinear);

			angularGraphModel.addTarget(c.targetAngular);
			angularGraphModel.addData(c.currentAngular);
		}

	}
}
