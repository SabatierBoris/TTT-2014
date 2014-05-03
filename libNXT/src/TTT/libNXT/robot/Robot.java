package TTT.libNXT.robot;

import java.util.ArrayList;

import lejos.nxt.LCD;

public abstract class Robot {
	private ArrayList<Thread> tasks;
	private long timeLimit;

	public Robot(long timeLimit){
		this.tasks = new ArrayList<Thread>();
		this.timeLimit = timeLimit;
	}

	abstract public void insertStarter();
	abstract public void removeStarter();
	abstract public boolean ARUIsPress();

	public void addTask(Thread t){
		this.tasks.add(t);
	}

	public void run(){
		long currentTick,startedTick;
		for(Thread t : this.tasks){
			t.setDaemon(true);
			t.start();
		}
		this.insertStarter();
		this.removeStarter();
		//TODO Start the IA
		LCD.clear();
		startedTick = System.currentTimeMillis();
		currentTick = System.currentTimeMillis();
		while(!this.ARUIsPress() && (this.timeLimit == 0 || currentTick-startedTick < this.timeLimit)){
			currentTick = System.currentTimeMillis();
			if(this.timeLimit != 0){
				LCD.drawString((this.timeLimit-(currentTick-startedTick))/1000 + " Secondes",0,0);
			}
			Thread.yield();
		}
		System.out.println("STOP !!!");
		for(Thread t : this.tasks){
			t.interrupt();
		}
		if(this.timeLimit != 0 && currentTick-startedTick >= this.timeLimit){
			//TODO Funny action
		}
		System.out.println("STOPED !!");
	}
}
