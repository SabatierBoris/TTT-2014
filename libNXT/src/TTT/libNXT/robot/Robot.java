package TTT.libNXT.robot;

import java.util.ArrayList;

import lejos.robotics.Touch;

public class Robot {
	private static final long TIME_LIMIT = 90000l; //TODO fix
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
		long currentTick,startedTick;
		while(!this.starter.isPressed()){
			Thread.yield();
		}
		for(Thread t : this.tasks){
			t.setDaemon(true);
			t.start();
		}
		while(this.starter.isPressed()){
			Thread.yield();
		}
		//TODO Start the IA
		startedTick = System.currentTimeMillis();
		currentTick = System.currentTimeMillis();
		while(!this.starter.isPressed() || currentTick-startedTick > Robot.TIME_LIMIT){
			currentTick = System.currentTimeMillis();
			Thread.yield();
		}
		for(Thread t : this.tasks){
			t.interrupt();
		}
	}
}
