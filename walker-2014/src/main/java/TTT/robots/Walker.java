package TTT.robots;

import lejos.nxt.Button;

import TTT.commons.Hello;

public class Walker {
	public static void main(String[] args){
		Hello h = new Hello();
		h.play();
		Button.waitForAnyPress();
	}
}
