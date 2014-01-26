package TTT.libNXT.robot;

import java.util.ArrayList;

import lejos.robotics.Touch;

public class Robot {
	private ArrayList<Thread> tasks;
	private Touch starter;

	public Robot(Touch starter){
		this.tasks = new ArrayList<Thread>();
		this.starter = starter;
	}


	public void addTask(Thread t){
		this.tasks.add(t);
	}

	public void run(){
		//while(!this.starter.isPressed()){
		//	Thread.yield();
		//}
		for(Thread t : this.tasks){
			t.setDaemon(true);
			t.start();
		}
		while(this.starter.isPressed()){
			Thread.yield();
		}
		//TODO Start the IA
		while(!this.starter.isPressed()){
			Thread.yield();
		}
		for(Thread t : this.tasks){
			t.interrupt();
		}
	}
}
