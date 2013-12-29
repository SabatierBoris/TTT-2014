package TTT.robots;

import lejos.nxt.Button;

import TTT.libNXT.communication.Connexion;

import TTT.commons.communication.Ping;

public class Walker {
	public static void main(String[] args){
		Connexion conn = Connexion.getInstance();
		conn.connect();
		Button.waitForAnyPress();
		System.out.println("Send M1");
		conn.send(new Ping("ping"));
		Button.waitForAnyPress();
		System.out.println("Send M2");
		conn.send(new Ping("pong"));
		Button.waitForAnyPress();

	}
}
