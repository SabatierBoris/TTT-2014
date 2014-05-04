package TTT.libNXT.task;

import lejos.nxt.Sound;

import TTT.libNXT.communication.Connexion;

public class WatchConnexion extends Thread{
	private Connexion conn;

	public WatchConnexion(Connexion conn){
		super();
		this.conn = conn;
	}

	@Override
	public void run(){
		Sound.setVolume(100);
		while(!this.isInterrupted()){
			try{
				if(!this.conn.isConnected()){
					Sound.playTone(440,500,30);
				}
				Thread.sleep(1000);
			}catch(InterruptedException e){
				this.interrupt();
			}
		}
	}
}
