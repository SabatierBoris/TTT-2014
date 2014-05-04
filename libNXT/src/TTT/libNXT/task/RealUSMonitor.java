package TTT.libNXT.task;

import java.util.Random;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.ArrayList;

import lejos.nxt.UltrasonicSensor;

public class RealUSMonitor extends Thread implements USMonitor{
	private Hashtable<String,UltrasonicSensor> sensors;
	private Hashtable<String,Integer> datas;
	private int maxSleep;
	private Random rand;

	public RealUSMonitor(){
		super();
		this.sensors  = new Hashtable<String, UltrasonicSensor>();
		this.datas = new Hashtable<String, Integer>();
		this.maxSleep = 10;
		this.rand = new Random();
	}

	public void addSensor(String name, UltrasonicSensor sensor){
		this.sensors.put(name,sensor);
		this.datas.put(name,255);
	}

	@Override
	public ArrayList<String> getNamesSensors(){
		ArrayList<String> ret = new ArrayList<String>();
		Enumeration<String> it = this.sensors.keys();
		while(it.hasMoreElements()){
			ret.add(it.nextElement());
		}
		return ret;
	}

	@Override
	public Integer getData(String name){
		//System.out.println("REAL : GETDATA " + name);
		return this.datas.get(name);
	}

	@Override
	public void run(){
		String key;
		Enumeration<String> it;
		UltrasonicSensor sensor;
		int val;

		synchronized(this){
			try{
				while(!this.isInterrupted()){
					it = this.sensors.keys();
					while(it.hasMoreElements()){
						key = it.nextElement();
						sensor = this.sensors.get(key);
						Thread.sleep(rand.nextInt(this.maxSleep));
						sensor.ping();
						val = sensor.getDistance();
						this.datas.put(key,val);
						//System.out.println(key + " = " + val);
					}
					Thread.sleep(100);
				}
			} catch(InterruptedException e){
				this.interrupt();
			}
		}
	}
}
