package TTT.robots;

import TTT.libNXT.communication.Connexion;
import TTT.commons.communication.Error;

public class TaskTest extends Thread {
	@Override
	public void run(){
		Connexion conn = Connexion.getInstance();
		Error p = new Error("bouboup");

		while(!this.isInterrupted()){
			try{
				conn.send(p);
				Thread.sleep(1500);
			} catch(InterruptedException e){
				this.interrupt();
			}
		}
	}
}
