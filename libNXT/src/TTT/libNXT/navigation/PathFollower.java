package TTT.libNXT.navigation;

import java.util.ArrayList;

import TTT.commons.navigation.Pose;
import TTT.commons.navigation.Point;

import TTT.libNXT.configuration.ConfigListener;
import TTT.libNXT.configuration.Configurateur;

import TTT.libNXT.navigation.BasicOdometry;
import TTT.libNXT.navigation.Navigator;
import TTT.libNXT.navigation.MovementListener;
import TTT.libNXT.navigation.MovingAction;

//TODO Remove
//import TTT.libNXT.communication.USBConnexion;
//import TTT.commons.communication.Error;

public class PathFollower extends Thread implements MovementListener, ConfigListener{
	private ArrayList<Pose> waypoints;
	private ArrayList<String> frontUS;
	private ArrayList<String> backUS;
	private Navigator nav;
	private BasicOdometry odo;

	private ArrayList<PathListener> listeners;

	private PathFollowAction previousAction;

	private double distancePrecision;
	private double anglePrecision;

	public PathFollower(Navigator nav, BasicOdometry odo){
		super();
		this.odo = odo;
		this.nav = nav;
		this.waypoints = new ArrayList<Pose>();
		this.previousAction = PathFollowAction.STOP;
		this.listeners = new ArrayList<PathListener>();

		Configurateur conf = Configurateur.getInstance();
		this.distancePrecision	= Double.parseDouble(conf.get("pathFollow.distancePrecision","10"));
		this.anglePrecision		= Double.parseDouble(conf.get("pathFollow.anglePrecision","3"));

		this.nav.addMovementListener(this);
		conf.addConfigListener(this,"pathFollow");
	}

	public void addListener(PathListener listener){
		this.listeners.add(listener);
	}

	public void changeAction(PathFollowAction action){
		this.previousAction = action;
		for(PathListener listener: this.listeners){
			listener.actionChanged(action);
		}
	}

	public void clearPath(){
		this.nav.stopMoving();
		synchronized(this.waypoints){
			this.waypoints.clear();
		}
	}

	public void addWayPoints(ArrayList<Pose> waypoints){
		//USBConnexion conn = USBConnexion.getInstance();
		for(Pose waypoint: waypoints){
			//conn.send(new Error("Add waypoint " + waypoint));
			this.addWayPoint(waypoint);
		}
	}

	public void addWayPoint(Pose waypoint){
		synchronized(this.waypoints){
			this.waypoints.add(waypoint);
			this.waypoints.notify();
		}
	}

	public boolean isEmpty(){
		return this.waypoints.isEmpty();
	}

	@Override
	public void configChanged(String key, String value){
		if(key.equals("pathFollow.distancePrecision")){
			this.distancePrecision	= Double.parseDouble(value);
		}else if(key.equals("pathFollow.anglePrecision")){
			this.anglePrecision		= Double.parseDouble(value);
		}
	}

	@Override
	public void movementChange(MovingAction newMovement){
		if(newMovement == MovingAction.STOP){
			synchronized(this.waypoints){
				this.waypoints.notify();
			}
		}
	}

	@Override
	public void run(){
		//USBConnexion conn = USBConnexion.getInstance();
		boolean wait;
		while(!this.isInterrupted()){
			try{
				synchronized(this.waypoints){
					wait = true;
					if(!this.waypoints.isEmpty()){
						Pose waypoint = this.waypoints.get(0);
						Pose current = this.odo.getCurrentPose();
						Pose sub = waypoint.substract(current);
						double distance = sub.getDistance();
						
						/*
						conn.send(new Error("==================================="));
						conn.send(new Error("PATH : waypoint " + waypoint));
						conn.send(new Error("PATH : current " + current));
						conn.send(new Error("PATH : previousAction " + this.previousAction));
						*/

						if(this.previousAction == PathFollowAction.STOP){
							this.changeAction(PathFollowAction.PRETURN);
							//SHOULD I PRETURN ?
							Point diff = sub.getLocation();
							double angle;
							if(diff.getX() == 0){
								if(diff.getY() > 0){
									angle = 90;
								}else if(diff.getY() < 0){
									angle = -90;
								}else{
									angle = 0;
								}
							}else{
								angle = Math.toDegrees(Math.atan(diff.getY()/diff.getX()));
								if(diff.getX() < 0){
									angle -= 180;
								}
							}
							angle = (angle+360)%360;
						//	conn.send(new Error("PATH : DIFFANGLE " + (angle-current.getHeading())));

							if(Math.abs(angle-current.getHeading()) > this.anglePrecision){
								//Je ne suis pas dans la bonne direction
								//TURN
								double diffAngle = angle-current.getHeading();
								if(diffAngle > 180){
									diffAngle -= 360;
								}else if(diffAngle < -180){
									diffAngle += 360;
								}
								if(diffAngle > 0){
						//			conn.send(new Error("PATH : 1-TurnLeft " + (int)Math.round(diffAngle)));
									this.nav.turnLeft((int)Math.round(diffAngle));
								}else{
						//			conn.send(new Error("PATH : 1-TurnRight " + (int)Math.round(-diffAngle)));
									this.nav.turnRight((int)Math.round(-diffAngle));
								}
							}else{
								wait=false;
							}
						}else if(this.previousAction == PathFollowAction.PRETURN){
							this.changeAction(PathFollowAction.MOVE);
							if(distance > this.distancePrecision){
								//Je suis dans la bonne direction
								// J'avance
						//		conn.send(new Error("PATH : Move " + (int)Math.round(distance)));
								this.nav.moveForward((int)Math.round(distance));
							}else{
								wait=false;
							}
						}else if(this.previousAction == PathFollowAction.MOVE){
							this.changeAction(PathFollowAction.POSTTURN);
							if(this.waypoints.size() == 1 && Math.abs(waypoint.getHeading()-current.getHeading()) > this.anglePrecision){
								//Si c'est mon dernier waypoint et que je n'ai pas le bon angle
								//Turn
								double diffAngle = waypoint.getHeading()-current.getHeading();
								if(diffAngle > 180){
									diffAngle -= 360;
								}else if(diffAngle < -180){
									diffAngle += 360;
								}
								if(diffAngle > 0){
//									conn.send(new Error("PATH : 2-TurnLeft " + (int)Math.round(diffAngle)));
									this.nav.turnLeft((int)Math.round(diffAngle));
								}else{
//									conn.send(new Error("PATH : 2-TurnRight " + (int)Math.round(-diffAngle)));
									this.nav.turnRight((int)Math.round(-diffAngle));
								}
							}else{
								wait=false;
							}
						}else{
//							conn.send(new Error("PATH : Pop"));
							this.waypoints.remove(0);
							this.changeAction(PathFollowAction.STOP);
							wait=false;
						}
					}else{
//						conn.send(new Error("PATH : Nothing to do"));
						this.changeAction(PathFollowAction.STOP);
					}
					if(wait == true){
						this.waypoints.wait();
					}
				}
			}catch(InterruptedException e){
				this.interrupt();
			}
		}
	}
}
