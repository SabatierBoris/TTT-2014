package TTT.PC.models.task;

import TTT.PC.models.communication.ConnexionModel;
import TTT.PC.models.odometry.OdometryModel;

import TTT.commons.communication.MessageListener;
import TTT.commons.communication.SendPoseMsg;
import TTT.commons.communication.GetPose;
import TTT.commons.communication.Message;

import TTT.commons.navigation.Pose;

public class TaskGetPose extends Thread implements MessageListener {
	private ConnexionModel conn;
	private boolean started;
	private OdometryModel model;

	public TaskGetPose(ConnexionModel conn, OdometryModel model){
		super();
		this.conn = conn;
		this.conn.addMessageListener(this,SendPoseMsg.ID);
		this.started = false;
		this.model = model;
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
					this.conn.send(new GetPose());
				}
				Thread.sleep(50);
			} catch(InterruptedException e){
				this.interrupt();
			}
		}
	}

	@Override
	public void messageReceived(Message m){
		if(m.getId() == SendPoseMsg.ID){
			this.model.setCurrentPose(((SendPoseMsg)m).getPose());
		}
	}
}

