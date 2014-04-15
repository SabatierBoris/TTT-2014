package TTT.PC.models.graph;

import java.util.ArrayList;

import javax.swing.event.EventListenerList;

import TTT.PC.models.communication.ConnexionModel;

import TTT.commons.communication.GetConfig;
import TTT.commons.communication.SetConfig;
import TTT.commons.communication.Message;
import TTT.commons.communication.MessageListener;

public class GraphModel{
	private EventListenerList listeners;

	private ArrayList<Integer> data = null;
	private ArrayList<Integer> target = null;

	public GraphModel(){
		this.listeners = new EventListenerList();
		this.target = new ArrayList<Integer>();
		this.data = new ArrayList<Integer>();
	}

	public void clear(){
		this.data.clear();
		this.target.clear();
		this.fireNewData();
	}

	public void addTarget(int target){
		this.target.add(target);
		this.fireNewData();
	}

	public void addData(int data){
		this.data.add(data);
		this.fireNewData();
	}

	public void addGraphListener(GraphListener listener){
		this.listeners.add(GraphListener.class,listener);
	}

	public void removeGraphListener(GraphListener listener){
		this.listeners.remove(GraphListener.class,listener);
	}

	private void fireNewData(){
		GraphListener[] listenerlist = this.listeners.getListeners(GraphListener.class);
		for(GraphListener listener : listenerlist){
			listener.newData();
		}
	}

	public int getDataSize(){
		return this.data.size();
	}

	public int getTarget(int i){
		return this.target.get(i);
	}

	public int getData(int i){
		return this.data.get(i);
	}
}


