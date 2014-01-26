package TTT.robots;

import TTT.libNXT.communication.Connexion;
import TTT.commons.communication.Error;

import TTT.commons.navigation.Pose;

import TTT.libNXT.navigation.AbstractAsservise;

public class TaskTest1 extends Thread {
	private AbstractAsservise asserv;

	public TaskTest1(AbstractAsservise asserv){
		super();
		this.asserv = asserv;
	}

	@Override
	public void run(){
		try{
			Thread.sleep(5000);
		} catch(InterruptedException e){
			this.interrupt();
		}
		this.asserv.setTarget(50,0);
		try{
			Thread.sleep(2000);
		} catch(InterruptedException e){
			this.interrupt();
		}
		this.asserv.setTarget(150,0);
		try{
			Thread.sleep(2000);
		} catch(InterruptedException e){
			this.interrupt();
		}
		this.asserv.setTarget(300,0);

		try{
			Thread.sleep(2000);
		} catch(InterruptedException e){
			this.interrupt();
		}
		this.asserv.setTarget(200,100);

		try{
			Thread.sleep(2000);
		} catch(InterruptedException e){
			this.interrupt();
		}
		this.asserv.setTarget(0,0);

	}
}
