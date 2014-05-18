package TTT.libNXT.task;

import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Enumeration;

public class CacheUSMonitor extends Thread implements USMonitor{
	private USMonitor master;
	private Hashtable<String,Integer> sensorsValue;

	public CacheUSMonitor(USMonitor master){
		this.master = master;
		this.sensorsValue = new Hashtable<String,Integer>();
	}

	@Override
	public ArrayList<String> getNamesSensors(){
		ArrayList<String> ret = new ArrayList<String>();
		Enumeration<String> it = this.sensorsValue.keys();
		while(it.hasMoreElements()){
			ret.add(it.nextElement());
		}
		return ret;
	}

	@Override
	public Integer getData(String name){
		Integer ret = this.sensorsValue.get(name);
		if(ret == null){
			ret = 255;
		}
		return ret;
	}

	@Override
	public void run(){
		try{
			String name;
			ArrayList<String> names = null;
			while(names == null){
				Thread.sleep(500);
				names = this.master.getNamesSensors();
			}

			for(String n: names){
				if(n != null){
					this.sensorsValue.put(n,255);
				}
			}
			while(!this.isInterrupted()){
				Enumeration<String> it = this.sensorsValue.keys();
				while(it.hasMoreElements()){
					name = it.nextElement();
					this.sensorsValue.put(name,this.master.getData(name));
				}
				Thread.sleep(500);
			}
		} catch(InterruptedException e){
			this.interrupt();
		}
	}
}

