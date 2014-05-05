package TTT.libNXT.task;

import java.util.Queue;

import TTT.libNXT.communication.Connexion;

import TTT.commons.communication.Message;
import TTT.commons.communication.SendPoseMsg;
import TTT.commons.communication.GetPose;
import TTT.commons.communication.MessageListener;
import TTT.commons.navigation.Pose;
import TTT.commons.navigation.PoseListener;

import TTT.libNXT.navigation.BasicOdometry;

public class SendPose extends Thread implements PoseListener, MessageListener {
	private Pose current;
	private Queue<GetPose> queue;
	private Connexion conn;

	public SendPose(BasicOdometry odo, Connexion conn){
		super();
		this.conn = conn;
		this.queue = new Queue<GetPose>();
		odo.addPoseListener(this);
		this.conn.addMessageListener(this,GetPose.ID);
	}

	@Override
	public void poseChanged(Pose p){
		synchronized(this){
			this.current = new Pose(p);
			this.notify();
		}
	}

	@Override
	public void run(){
		synchronized(this.queue){
			try{
				while(!this.isInterrupted()){
					if(!this.queue.empty()){
						this.queue.pop();
						this.conn.send(new SendPoseMsg(this.current));
					}else{
						this.queue.wait();
					}
				}
			} catch(InterruptedException e){
				this.interrupt();
			}
		}
	}

	@Override
	public void messageReceived(Message m){
		synchronized(this.queue){
			this.queue.push((GetPose) m);
			this.queue.notify();
		}
	}
}
