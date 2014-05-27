package TTT.libNXT.robot;

import java.util.ArrayList;

import lejos.nxt.Button;
import lejos.nxt.LCD;

import TTT.libNXT.ia.BasicIA;

public abstract class Robot {
	private ArrayList<Thread> tasks;
	private BasicIA ia;
	private long timeLimit;
	private Color color;

	public Robot(long timeLimit){
		this.tasks = new ArrayList<Thread>();
		this.timeLimit = timeLimit;
		this.color = Color.UNKNOW;
		this.ia = null;
	}

	public void setIA(BasicIA ia){
		this.ia = ia;
	}

	abstract public void insertStarter();
	abstract public void removeStarter();
	abstract public boolean ARUIsPress();
	abstract public void initPose();

	public void addTask(Thread t){
		this.tasks.add(t);
	}

	public Color getColor(){
		return this.color;
	}

	public void selectColor(){
		int ret;
		System.out.println("<= RED | YELLOW =>");
		do{
			ret = Button.waitForAnyPress();
		}while(ret != Button.ID_LEFT && ret != Button.ID_RIGHT);
		if(ret == Button.ID_LEFT){
			this.color = Color.RED;
		}else{
			this.color = Color.YELLOW;
		}
	}

	public void run(){
		long currentTick,startedTick;
		for(Thread t : this.tasks){
			t.setDaemon(true);
			t.start();
		}
		if(this.ia != null){
			this.ia.setDaemon(true);
		}
		this.initPose();
		
		this.insertStarter();
		this.removeStarter();
		//Start the IA
		if(this.ia != null){
			this.ia.start();
		}

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
		if(this.ia != null){
			this.ia.interrupt();
			if(this.timeLimit != 0 && currentTick-startedTick >= this.timeLimit){
				this.ia.runFunny();
			}
		}

		System.out.println("STOP !!!");
		for(Thread t : this.tasks){
			t.interrupt();
		}
		System.out.println("STOPED !!");
	}
}
