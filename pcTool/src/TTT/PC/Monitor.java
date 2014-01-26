package TTT.PC;

import TTT.commons.communication.Ping;

import TTT.PC.communication.Connexion;

public class Monitor{
	public static void main(String[] args){
		Connexion conn = Connexion.getInstance();
		conn.setDaemon(true);
		conn.start();
		while(true){
			try{
			//	conn.send(new Ping());
				Thread.sleep(500);
			}catch(InterruptedException e){
				conn.interrupt();
			}
		}
	}
}
