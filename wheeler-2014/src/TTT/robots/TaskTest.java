package TTT.robots;

import TTT.libNXT.communication.Connexion;
import TTT.commons.communication.Ping;

public class TaskTest extends Thread {
	@Override
	public void run(){
		Connexion conn = Connexion.getInstance();
		Ping p = new Ping("ping");

		while(!this.isInterrupted()){
			try{
				conn.send(p);
				Thread.sleep(10000);
			} catch(InterruptedException e){
				this.interrupt();
			}
		}
	}
}
