package TTT.libNXT.task;

import java.util.ArrayList;

import TTT.libNXT.configuration.ConfigListener;
import TTT.libNXT.configuration.Configurateur;

import TTT.libNXT.navigation.MovementListener;
import TTT.libNXT.navigation.MovingAction;
import TTT.libNXT.navigation.Navigator;
import TTT.libNXT.navigation.PathFollower;

//TODO Remove
import TTT.libNXT.communication.USBConnexion;
import TTT.commons.communication.Error;

public class WatchOpponent extends Thread implements MovementListener, ConfigListener{
	private USMonitor mon;
	private PathFollower path;

	private int distanceDetection;

	private MovingAction movement;

	private ArrayList<String> frontUS;
	private ArrayList<String> backUS;
	private ArrayList<String> leftUS;
	private ArrayList<String> rightUS;
	private ArrayList<OpponentListener> listeners;

	public WatchOpponent(USMonitor mon, Navigator nav, PathFollower path){
		this.mon = mon;
		this.path= path;
		this.movement = MovingAction.STOP;
		this.frontUS = new ArrayList<String>();
		this.backUS = new ArrayList<String>();
		this.leftUS = new ArrayList<String>();
		this.rightUS = new ArrayList<String>();
		this.listeners = new ArrayList<OpponentListener>();

		nav.addMovementListener(this);

		Configurateur conf = Configurateur.getInstance();
		this.distanceDetection	= Integer.parseInt(conf.get("opponent.distanceDetection","15"));
		conf.addConfigListener(this,"opponent");
	}

	public void addListener(OpponentListener l){
		this.listeners.add(l);
	}

	public void addFrontUS(String name){
		this.frontUS.add(name);
	}

	public void addBackUS(String name){
		this.backUS.add(name);
	}

	public void addLeftUS(String name){
		this.leftUS.add(name);
	}

	public void addRightUS(String name){
		this.rightUS.add(name);
	}

	public void fireOpponentFound(Direction dir){
		for(OpponentListener l: this.listeners){
			l.opponentFound(dir);
		}
	}

	@Override
	public void configChanged(String key, String value){
		if(key.equals("opponent.distanceDetection")){
			this.distanceDetection	= Integer.parseInt(value);
		}
	}

	@Override
	public void movementChange(MovingAction movement){
		synchronized(this){
			this.movement = movement;
		}
	}

	@Override
	public void run(){
		//USBConnexion conn = USBConnexion.getInstance();
		Direction dir;
		ArrayList<String> sensors;
		while(!this.isInterrupted()){
			try{
				synchronized(this){
					sensors = null;
					dir = null;
					if(this.movement == MovingAction.STOP){
						//Nothing to do
						sensors = null;
					}else if(this.movement == MovingAction.FORWARD){
						dir = Direction.FRONT;
						sensors = this.frontUS;
					}else if(this.movement == MovingAction.BACKWARD){
						dir = Direction.BACK;
						sensors = this.backUS;
					}else if(this.movement == MovingAction.TURNLEFT){
						sensors = this.rightUS;
					}else{
						sensors = this.leftUS;
					}
					if(sensors != null){
						int val = 255;
						for(String name: sensors){
							int tmp = this.mon.getData(name);
							val = Math.min(val,tmp);
						}
						if(val < this.distanceDetection){
							if(dir == null){
								int val2 = 255;
								for(String name: this.frontUS){
									int tmp = this.mon.getData(name);
									val2 = Math.min(val2,tmp);
								}
								if(val2 <= val){
									dir = Direction.FRONT;
								}else{
									dir = Direction.BACK;
								}

							}
							this.fireOpponentFound(dir);
							//conn.send(new Error("STOP !!!"));
							//TODO Add in pathFollower
							this.path.clearPath();
						}
					}
				}
				Thread.sleep(500);
			}catch(InterruptedException e){
				this.interrupt();
			}
		}
	}
}
