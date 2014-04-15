package TTT.libNXT.robot;

import java.util.ArrayList;

import lejos.robotics.Touch;
import lejos.nxt.LCD;

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
		for(Thread t : this.tasks){
			t.setDaemon(true);
			t.start();
		}
		System.out.println("INSERT STARTER");
		while(!this.starter.isPressed()){
			Thread.yield();
		}
		System.out.println("REMOVE STARTER");
		while(this.starter.isPressed()){
			Thread.yield();
		}
		//TODO Start the IA
		LCD.clear();
		startedTick = System.currentTimeMillis();
		currentTick = System.currentTimeMillis();
		while(!this.starter.isPressed() && currentTick-startedTick < Robot.TIME_LIMIT){
			currentTick = System.currentTimeMillis();
			LCD.drawString((currentTick-startedTick)/1000 + " Secondes",0,0);
			Thread.yield();
		}
		System.out.println("STOP !!!");
		for(Thread t : this.tasks){
			t.interrupt();
		}
		if(currentTick-startedTick >= Robot.TIME_LIMIT){
			//TODO Funny action
		}
		System.out.println("STOPED !!");
	}
}
