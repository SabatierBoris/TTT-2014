package TTT.robots;

import lejos.nxt.SensorPort;

import TTT.libNXT.communication.Connexion;
import TTT.commons.communication.Error;

import TTT.libNXT.sensor.MindSensorAngle;

public class TaskTest extends Thread {
	@Override
	public void run(){
		MindSensorAngle s = new MindSensorAngle(SensorPort.S1);
		Connexion conn = Connexion.getInstance();
		int i = 0;
		while(!this.isInterrupted()){
			try{
				i++;
				if(i>10){
					i=0;
					s.resetTachoCount();
					System.out.println("Reset");
				}
				System.out.println("Tacho : " + s.getAngle());
				System.out.println("Raw : " + s.getRawAngle());
				System.out.println("RPM : " + s.getRPM());
				Thread.sleep(2000);
			} catch(InterruptedException e){
				this.interrupt();
			}
			//conn.send(new Error("A : " + s.getTachoCount()));
		}
	}
}
