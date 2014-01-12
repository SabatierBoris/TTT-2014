package TTT.robots;

import lejos.nxt.MotorPort;
import lejos.nxt.NXTRegulatedMotor;

import lejos.nxt.SensorPort;

import TTT.libNXT.communication.Connexion;
import TTT.commons.communication.Error;

import TTT.libNXT.sensor.MindSensorAngle;

import TTT.commons.navigation.Pose;
import TTT.commons.navigation.PoseListener;

import TTT.libNXT.navigation.BasicOdometry;

public class TaskTest extends Thread implements PoseListener {
	public Pose current;

	public TaskTest(BasicOdometry odo){
		super();
		current = odo.getCurrentPose();
		odo.addMessageListener(this);
	}


	@Override
	public void poseChanged(Pose p){
		synchronized(this){
			this.current = p;
			this.notify();
		}
	}


	@Override
	public void run(){
//		Connexion conn = Connexion.getInstance();
		NXTRegulatedMotor move = new NXTRegulatedMotor(MotorPort.A);
		NXTRegulatedMotor rotation = new NXTRegulatedMotor(MotorPort.B);
		move.setSpeed(50);
		rotation.setSpeed(20);
		move.stop();
		rotation.stop();
		Pose target = new Pose(0,100,0);
		Pose diff;

		synchronized(this){
			try{
				while(!this.isInterrupted()){
					if(this.current != null){
						diff = current.substract(target);
						System.out.println("Diff : " + diff);
						if(diff.getHeading() != 0){
							move.stop();
							if(diff.getHeading() < 180){
								rotation.backward();
							} else {
								rotation.forward();
							}
						} else {
							rotation.stop();
							int y = diff.getLocation().getY();
							System.out.println("Y : " + y);
							if(y == 0){
								move.stop();
							} else if(y > 0){
								move.backward();
							} else {
								move.forward();
							}
						}
					}
					this.wait();
				}
			} catch(InterruptedException e){
				this.interrupt();
			}
		}
//		int i = 0;
/*		while(!this.isInterrupted()){
			try{
				i++;
				if(i>10){
					i=0;
				//	s.resetTachoCount();
				//	System.out.println("Reset");
				}
				System.out.println("Tacho : " + s.getTachoCount());
				//System.out.println("Raw : " + s.getRawAngle());
				//System.out.println("RPM : " + s.getRPM());
				Thread.sleep(2000);
			} catch(InterruptedException e){
				this.interrupt();
			}
			//conn.send(new Error("A : " + s.getTachoCount()));
		}

		/*
		NXTRegulatedMotor m1 = new NXTRegulatedMotor(MotorPort.A);
		NXTRegulatedMotor m2 = new NXTRegulatedMotor(MotorPort.B);
		m2.stop();
		m1.setSpeed(1000);
		m1.forward();
		try{
			Thread.sleep(3000);
		} catch(InterruptedException e){
		}
		m1.stop();
		m1.flt();
		m2.stop();
		m2.flt();

		*/
	}
}
