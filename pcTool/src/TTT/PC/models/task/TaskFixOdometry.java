package TTT.PC.models.task;

import java.util.Queue;
import java.util.ArrayDeque;

import TTT.PC.models.config.ConfigModel;
import TTT.PC.models.config.Config;
import TTT.PC.models.config.ConfigListener;
import TTT.PC.models.graph.GraphModel;

import TTT.PC.controllers.OdometryController;
import TTT.PC.controllers.ConnexionController;
import TTT.PC.controllers.ConfigController;

import TTT.commons.navigation.Pose;

import javax.swing.JOptionPane;

public class TaskFixOdometry extends Thread implements ConfigListener {
	private ConfigController config;
	private OdometryController controller;
	private ConnexionController conn;
	private Queue<Config> configQueue;
	private TaskGetPose info;

	public TaskFixOdometry(OdometryController controller, ConfigController config, ConnexionController conn, TaskGetPose info){
		super();

		this.controller = controller;

		this.config = config;
		this.config.addView(this);

		this.configQueue = new ArrayDeque<Config>();

		this.conn = conn;
		this.info = info;
	}

	private String getValue(String key){
		Config m = null;
		this.configQueue.clear();
		this.config.getConfig(key);
		for(int i=0;i<5;i++){
			synchronized(this.configQueue){
				try{
					this.configQueue.wait();
					m = this.configQueue.poll();
					if(m.getKey().equals(key)){
						return m.getValue();
					}
				}catch(InterruptedException e){
					this.interrupt();
				}
			}
		}
		return "";
	}

	private void setValue(String key, String value){
		this.config.updateConfig(new Config(key,value),value);
	}

	private void sleep(int ms){
		try{
			Thread.sleep(ms);
		}catch(InterruptedException e){
			this.interrupt();
		}
	}

	private boolean waitFinishMove(){
		Pose origine,current, newPose;
		origine = this.controller.getCurrentPose();
		newPose = origine;
		do{
			current = newPose;
			this.sleep(1000);
			newPose = this.controller.getCurrentPose();
		}while(current.substract(newPose).getDistance() > 0);
		return (origine.substract(newPose).getDistance() > 10);
	}

	private void fixLinear(int distance){
		System.out.println("Start FixLinear");
		double coefD = Double.parseDouble(this.getValue("odo.coefD"));
		Pose origine = new Pose();
		this.conn.sendMove(distance);
		this.waitFinishMove();
		this.conn.sendStop();
		Pose finish = this.controller.getCurrentPose();
		Pose sub = finish.substract(origine);
		double currentDistance = sub.getDistance();
		String buff = JOptionPane.showInputDialog("Distance réelement parcouru ? ("+currentDistance+")");
		if(buff != null){
			double realDistance = Double.parseDouble(buff);
			double newCoefD = (currentDistance/realDistance)*coefD;

			System.out.println("=======================");
			System.out.println("Origine : " + origine);
			System.out.println("Finish : " + finish);
			System.out.println("Sub : " + sub);
			System.out.println("Distance : " + currentDistance);
			System.out.println("Real Distance : " + realDistance);
			System.out.println("Current CoefD : " + coefD);
			System.out.println("New CoefD : " + newCoefD);
			System.out.println("=======================");

			this.setValue("odo.coefD",newCoefD+"");
		}
		System.out.println("Stop FixLinear");
	}

	private void fixAngular(int degree){
		Pose origine = new Pose();
		double coefH = Double.parseDouble(this.getValue("odo.coefH"));
		this.conn.sendMove(150);
		this.waitFinishMove();
		this.conn.sendTurn(degree);
		this.waitFinishMove();
		this.sleep(500);
		do{
			this.conn.sendMove(-100);
		}while(this.waitFinishMove());
		this.conn.sendStop();

		Pose finish = this.controller.getCurrentPose();
		double currentAngle = finish.getFullHeading();

		double newCoefH = (currentAngle/degree)*coefH;

		System.out.println("=======================");
		System.out.println("Origine : " + origine);
		System.out.println("Finish : " + finish);
		System.out.println("Angle : " + currentAngle);
		System.out.println("Current CoefH : " + coefH);
		System.out.println("New CoefH : " + newCoefH);
		System.out.println("=======================");

		this.setValue("odo.coefH",newCoefH+"");
	}

	private void resetWall(){
		System.out.println("Start RESET WALL");
		do{
			this.conn.sendMove(-100);
		}while(this.waitFinishMove());
		this.controller.setCurrentPose(0.0,0.0,0.0);
		this.conn.sendStop();
		this.sleep(1000);
		System.out.println("Stop RESET WALL");
	}

	@Override
	public void run(){
		this.info.startSend();
		this.sleep(100);
		this.resetWall();
		/*
		this.fixLinear(1000);
		*/
		this.fixAngular(360*5);

		this.info.stopSend();
		this.finish();
	}

	private void finish(){
		System.out.println("Finish");
		this.controller.finish();
	}

	@Override
	public void newConfig(Config conf){
		synchronized(this.configQueue){
			this.configQueue.add(conf);
			this.configQueue.notify();
		}
	}
}


