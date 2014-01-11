package TTT.libNXT.robot;

import java.util.ArrayList;

import lejos.nxt.Button;

public class Robot {
	private ArrayList<Thread> tasks;

	public Robot(){
		this.tasks = new ArrayList<Thread>();
	}


	public void addTask(Thread t){
		this.tasks.add(t);
	}

	public void run(){
		for(Thread t : this.tasks){
			t.setDaemon(true);
			t.start();
		}
		Button.waitForAnyPress();//TODO replace by the right thing
		for(Thread t : this.tasks){
			t.interrupt();
		}
	}
}
