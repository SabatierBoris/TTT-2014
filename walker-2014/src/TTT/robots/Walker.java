package TTT.robots;

import lejos.nxt.Button;

import TTT.robots.communication.Connexion;

public class Walker {
	public static void main(String[] args){
		System.out.println("Waiting");
		Connexion conn = Connexion.getInstance();
		System.out.println("Connected");
		Button.waitForAnyPress();
		System.out.println("Send M1");
		conn.send("1:96AA");
		Button.waitForAnyPress();
		System.out.println("Send M2");
		conn.send("2:Message 2");
		Button.waitForAnyPress();

	}
}
