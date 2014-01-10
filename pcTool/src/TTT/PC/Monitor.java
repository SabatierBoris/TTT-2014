package TTT.PC;

import TTT.commons.communication.Ping;

import TTT.PC.communication.Connexion;

public class Monitor{
	public static void main(String[] args){
		Connexion conn = Connexion.getInstance();
		conn.setDaemon(true);
		conn.start();
		Ping p = new Ping("Pong");
		while(true){
			try{
				conn.send(p);
				Thread.sleep(50);
			}catch(InterruptedException e){
				conn.interrupt();
			}
		}
	}
}
