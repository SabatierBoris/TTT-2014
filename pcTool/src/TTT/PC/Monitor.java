package TTT.PC;

import TTT.PC.communication.Connexion;

public class Monitor{
	public static void main(String[] args){
		Connexion conn = Connexion.getInstance();
		conn.connect();
		String buff;
		while(conn.isConnected()){
			buff = conn.read();
			if(!buff.equals("")){
				System.out.println(buff);
			}
		}
	}
}
