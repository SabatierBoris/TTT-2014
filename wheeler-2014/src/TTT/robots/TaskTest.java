package TTT.robots;

import TTT.libNXT.communication.Connexion;
import TTT.commons.communication.Ping;

public class TaskTest extends Thread {
	@Override
	public void run(){
		Connexion conn = Connexion.getInstance();
		conn.setDaemon(true);
		conn.start();
		Ping p = new Ping("ping");

		while(!this.isInterrupted()){
			try{
				conn.send(p);
				Thread.sleep(3000);
			} catch(InterruptedException e){
				conn.interrupt();
				this.interrupt();
			}
		}
	}
}
