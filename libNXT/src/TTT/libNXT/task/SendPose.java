package TTT.libNXT.task;

import TTT.commons.communication.Error;

import TTT.libNXT.communication.Connexion;

import TTT.commons.navigation.Pose;
import TTT.commons.navigation.PoseListener;

import TTT.libNXT.navigation.BasicOdometry;

public class SendPose extends Thread implements PoseListener {
	private Pose current;

	public SendPose(BasicOdometry odo){
		super();
		odo.addPoseListener(this);
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
		Connexion conn = Connexion.getInstance();
		synchronized(this){
			try{
				while(!this.isInterrupted()){
					this.wait();
					if(conn.isConnected()){
						conn.send(new Error(this.current.toString()));
					}
				}
			} catch(InterruptedException e){
				this.interrupt();
			}
		}
	}
}
