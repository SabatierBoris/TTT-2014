package TTT.robots;

import TTT.libNXT.communication.Connexion;
import TTT.commons.communication.Error;

import TTT.commons.navigation.Pose;

import TTT.libNXT.navigation.Navigator;

public class TaskTest extends Thread {
	private Navigator nav;

	public TaskTest(Navigator nav){
		super();
		this.nav = nav;
	}

	@Override
	public void run(){
		try{
			Thread.sleep(5000);
		} catch(InterruptedException e){
			this.interrupt();
		}
//		this.nav.setTarget(new Pose(0,50,0));
		/*

		try{
			Thread.sleep(45000);
		} catch(InterruptedException e){
			this.interrupt();
		}
		this.asserv.setTarget(50,30);
		*/
	}
}
