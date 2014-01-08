package TTT.PC;

import TTT.PC.communication.Connexion;

public class Monitor{
	public static void main(String[] args){
		Connexion conn = Connexion.getInstance();
		conn.setDaemon(true);
		conn.start();
		while(true){
		}
	}
}
