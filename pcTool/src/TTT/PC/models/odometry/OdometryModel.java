package TTT.PC.models.odometry;

import javax.swing.event.EventListenerList;

import TTT.commons.navigation.Pose;

public class OdometryModel{
	private EventListenerList listeners;
	private Pose pose;

	public OdometryModel(){
		this.listeners = new EventListenerList();
		this.pose = new Pose();
	}

	public void addOdometryListener(OdometryListener listener){
		this.listeners.add(OdometryListener.class,listener);
	}

	public void removeConfigListener(OdometryListener listener){
		this.listeners.remove(OdometryListener.class,listener);
	}

	private void fireNewPose(Pose pose){
		OdometryListener[] listenerlist = this.listeners.getListeners(OdometryListener.class);
		for(OdometryListener listener : listenerlist){
			listener.newPose(pose);
		}
	}
	public void setCurrentPose(Pose pose){
		this.pose = pose;
		this.fireNewPose(pose);
	}

	public Pose getCurrentPose(){
		return this.pose;
	}
}


