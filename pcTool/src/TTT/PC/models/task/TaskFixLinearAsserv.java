package TTT.PC.models.task;

import java.util.Queue;
import java.util.ArrayDeque;

import TTT.PC.models.config.ConfigModel;
import TTT.PC.models.config.Config;
import TTT.PC.models.config.ConfigListener;
import TTT.PC.models.graph.GraphModel;

import TTT.PC.controllers.AsservController;
import TTT.PC.controllers.ConnexionController;
import TTT.PC.controllers.ConfigController;

import javax.swing.JOptionPane;








//import TTT.commons.communication.AsservInfo;
//import TTT.commons.communication.GetConfig;
//import TTT.commons.communication.SetConfig;

//import TTT.commons.communication.FixLinearAsservMessage;
//import TTT.commons.communication.Message;

public class TaskFixLinearAsserv extends Thread implements ConfigListener {
	private ConfigController config;
	private AsservController controller;
	private ConnexionController conn;
	private Queue<Config> configQueue;
	private GraphModel linearGraphModel;
	private GraphModel angularGraphModel;
	private TaskAsservInfo info;


	public TaskFixLinearAsserv(AsservController controller, ConfigController config, ConnexionController conn, GraphModel linearGraphModel, GraphModel angularGraphModel, TaskAsservInfo info){
		super();

		this.controller = controller;

		this.config = config;
		this.config.addView(this);

		this.configQueue = new ArrayDeque<Config>();

		this.conn = conn;
		this.info = info;

		this.linearGraphModel = linearGraphModel;
		this.angularGraphModel = angularGraphModel;
	}

	private String getValue(String key){
		Config m = null;
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

	private void fixValue(String valueKey, float minValue, float maxValue, float precision){
		float current;
		float min = minValue;
		float max = maxValue;
		Integer prevVal;
		Integer currentVal;
		int state = 0;
		ArrayDeque<Integer> values = new ArrayDeque<Integer>();

		current = (max+min)/2;
		do{
			switch(state){
				case 0: //Start UP
					break;
				case 1: //Stable => Up the min value
					min = current;
					break;
				case 2: //Unstable => Down the max value
					max = current;
					break;
			}
			current = (max+min)/2;
			System.out.println("=======================");
			System.out.println("state : " + state);
			System.out.println("Min : " + min);
			System.out.println("Current : " + current);
			System.out.println("Max : " + max);
			this.setValue(valueKey,""+current);

			try{
				Thread.sleep(500);
			} catch(InterruptedException e){
				this.interrupt();
			}

			this.linearGraphModel.clear();
			this.angularGraphModel.clear();
			this.info.startSend();
			this.conn.sendFixLinearAsserv();
			try{
				Thread.sleep(3000);
			} catch(InterruptedException e){
				this.interrupt();
			}
			this.conn.sendFixLinearAsserv();
			this.info.stopSend();

			if(max-min < precision){
				try{
					System.out.println("Finish");
					Thread.sleep(2000);
				} catch(InterruptedException e){
					this.interrupt();
				}
				state = 3;
			}else if(JOptionPane.showConfirmDialog(null, "Stable ?", "Stable", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == 1){
				state = 2;
			}else {
				state = 1;
			}

		}while(state != 3);
	}

	@Override
	public void run(){
		String angleP = this.getValue("asserv.angleP");
		String angleI = this.getValue("asserv.angleI");
		String angleD = this.getValue("asserv.angleD");

		System.out.println("Save Angle PID : (" + angleP + "|" + angleI + "|" + angleD + ")");

		this.setValue("asserv.angleP","0");
		this.setValue("asserv.angleI","0");
		this.setValue("asserv.angleD","0");

		this.setValue("asserv.lineP","0");
		this.setValue("asserv.lineI","0");
		this.setValue("asserv.lineD","0");

		this.fixValue("asserv.lineP",0f,10f,0.001f);
		this.fixValue("asserv.lineI",0f,10f,0.001f);
		this.fixValue("asserv.lineD",0f,10f,0.001f);

		this.setValue("asserv.angleP",angleP);
		this.setValue("asserv.angleI",angleI);
		this.setValue("asserv.angleD",angleD);

		this.finish();
	}

	private void finish(){
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

