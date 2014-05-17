package TTT.libNXT.navigation;

import java.util.ArrayList;

import TTT.libNXT.navigation.CodeursListener;

import TTT.libNXT.configuration.ConfigListener;
import TTT.libNXT.configuration.Configurateur;

import TTT.libNXT.communication.Connexion;
import TTT.commons.communication.MessageListener;
import TTT.commons.communication.Message;
import TTT.commons.communication.MoveMsg;
import TTT.commons.communication.TurnMsg;
import TTT.commons.communication.StopMsg;

//TODO Remove
import TTT.libNXT.communication.USBConnexion;
import TTT.commons.communication.Error;

public class Navigator extends Thread implements CodeursListener, ConfigListener, MessageListener {
	private MovingAction state;
	private MovingState accelState;

	private int startedDistance;
	private int startedAngle;
	private int currentDistance;
	private int currentAngle;
	private int targetDistance;
	private int targetAngle;

	private AbstractAsservise asserv;

	private int stopDistance;
	private int fullStopDistance;
	private int maxLinearSpeed;
	private int minLinearSpeed;
	private double linearAccelSpeed;
	private double linearDeccelSpeed;

	private int stopAngle;
	private int fullStopAngle;
	private int maxAngularSpeed;
	private int minAngularSpeed;
	private double angularAccelSpeed;
	private double angularDeccelSpeed;

	private BasicOdometry odo;

	private long prevTime;

	private ArrayList<MovementListener> movementListeners;

	public Navigator(BasicOdometry odo, AbstractAsservise asserv, Connexion conn){
		this.asserv = asserv;
		this.odo = odo;
		this.odo.addCodeursListener(this);

		this.currentDistance = this.odo.getCurrentDistance();
		this.currentAngle = this.odo.getCurrentDistance();
		this.startedDistance = this.currentDistance;
		this.startedAngle = this.currentAngle;
		this.targetDistance = this.currentDistance;
		this.targetAngle = this.currentAngle;

		Configurateur conf = Configurateur.getInstance();

		stopDistance		= Integer.parseInt(conf.get("nav.stopDistance","25")); 
		fullStopDistance	= Integer.parseInt(conf.get("nav.fullStopDistance","10"));
		maxLinearSpeed		= Integer.parseInt(conf.get("nav.maxLinearSpeed","100"));
		minLinearSpeed		= Integer.parseInt(conf.get("nav.minLinearSpeed","50"));
		linearAccelSpeed	= Double.parseDouble(conf.get("nav.linearAccelSpeed","0.1"));
		linearDeccelSpeed	= Double.parseDouble(conf.get("nav.linearDeccelSpeed","0.1"));

		stopAngle			= Integer.parseInt(conf.get("nav.stopAngle","25")); 
		fullStopAngle		= Integer.parseInt(conf.get("nav.fullStopAngle","10"));
		maxAngularSpeed		= Integer.parseInt(conf.get("nav.maxAngularSpeed","100"));
		minAngularSpeed		= Integer.parseInt(conf.get("nav.minAngularSpeed","50"));
		angularAccelSpeed	= Double.parseDouble(conf.get("nav.angularAccelSpeed","0.1"));
		angularAccelSpeed	= Double.parseDouble(conf.get("nav.angularDeccelSpeed","0.1"));

		this.movementListeners = new ArrayList<MovementListener>();

		conf.addConfigListener(this,"nav");

		conn.addMessageListener(this,MoveMsg.ID);
		conn.addMessageListener(this,TurnMsg.ID);
		conn.addMessageListener(this,StopMsg.ID);
	}

	private void fireMovementChange(MovingAction movement){
		for(MovementListener listener : this.movementListeners){
			listener.movementChange(movement);
		}
	}

	public void addMovementListener(MovementListener listener){
		this.movementListeners.add(listener);
	}

	@Override
	public void codeursChanged(int distance, int orient){
		synchronized(this){
			this.currentDistance = distance;
			this.currentAngle = orient;
			this.notify();
		}
	}

	private void changeState(MovingAction newState){
		this.state = newState;
		this.fireMovementChange(newState);
	}

	@Override
	public void configChanged(String key, String value){
		if(key.equals("nav.stopDistance")){
			stopDistance = Integer.parseInt(value);
		}else if(key.equals("nav.fullStopDistance")){
			fullStopDistance = Integer.parseInt(value);
		}else if(key.equals("nav.maxLinearSpeed")){
			maxLinearSpeed = Integer.parseInt(value);
		}else if(key.equals("nav.minLinearSpeed")){
			minLinearSpeed = Integer.parseInt(value);
		}else if(key.equals("nav.linearAccelSpeed")){
			linearAccelSpeed = Double.parseDouble(value);
		}else if(key.equals("nav.linearDeccelSpeed")){
			linearDeccelSpeed = Double.parseDouble(value);
		}else if(key.equals("nav.stopAngle")){
			stopAngle = Integer.parseInt(value);
		}else if(key.equals("nav.fullStopAngle")){
			fullStopAngle = Integer.parseInt(value);
		}else if(key.equals("nav.maxAngularSpeed")){
			maxAngularSpeed = Integer.parseInt(value);
		}else if(key.equals("nav.minAngularSpeed")){
			minAngularSpeed = Integer.parseInt(value);
		}else if(key.equals("nav.angularAccelSpeed")){
			angularAccelSpeed = Double.parseDouble(value);
		}else if(key.equals("nav.angularDeccelSpeed")){
			angularDeccelSpeed = Double.parseDouble(value);
		}else{
			//TODO Erreur
		}
	}

	@Override
	public void messageReceived(Message m){
		if(m.getId() == MoveMsg.ID){
			MoveMsg msg = (MoveMsg)m;
			if(msg.getValue() > 0){
				this.moveForward(msg.getValue());
			}else{
				this.moveBackward(-msg.getValue());
			}
		}else if(m.getId() == TurnMsg.ID){
			TurnMsg msg = (TurnMsg)m;
			if(msg.getValue() > 0){
				this.turnLeft(msg.getValue());
			}else{
				this.turnRight(-msg.getValue());
			}
		}else if(m.getId() == StopMsg.ID){
			this.stopMoving();
		}
	}

	public void stopMoving(){
		synchronized(this){
			this.changeState(MovingAction.STOP);
			this.accelState = MovingState.STOP;
			this.notify();
		}
	}

	public void moveForward(int distance){
		synchronized(this){
			this.changeState(MovingAction.FORWARD);
			this.accelState = MovingState.ACCEL;
			this.targetDistance += distance*this.odo.getCoefD();
			this.prevTime = System.currentTimeMillis();
			this.notify();
		}
	}

	public void moveBackward(int distance){
		synchronized(this){
			this.changeState(MovingAction.BACKWARD);
			this.accelState = MovingState.ACCEL;
			this.targetDistance -= distance*this.odo.getCoefD();
			this.prevTime = System.currentTimeMillis();
			this.notify();
		}
	}

	public void turnRight(int angle){
		synchronized(this){
			this.changeState(MovingAction.TURNRIGHT);
			this.accelState = MovingState.ACCEL;
			this.targetAngle -= angle*this.odo.getCoefH();
			this.prevTime = System.currentTimeMillis();
			this.notify();
		}
	}

	public void turnLeft(int angle){
		synchronized(this){
			this.changeState(MovingAction.TURNLEFT);
			this.accelState = MovingState.ACCEL;
			this.targetAngle += angle*this.odo.getCoefH();
			this.prevTime = System.currentTimeMillis();
			this.notify();
		}
	}

	private void move(MovingAction direction, long diffTime, double distance, double angle){
		int linearSpeed = 0;
		int angularSpeed= (int)Math.round(angle);
		int currentLinearSpeed = 0;
		int signe = 1;

		if(direction != MovingAction.FORWARD && direction != MovingAction.BACKWARD){
			return;
		}

		if(direction == MovingAction.BACKWARD){
			signe = -1;
		}

		if(signe*distance < this.fullStopDistance){
			this.stopMoving();
			return;
		}

		if(signe*distance <= this.stopDistance){
			this.accelState = MovingState.DECEL;
		}

		currentLinearSpeed = Math.abs(this.asserv.getTargetLinearSpeed());

		if(this.accelState == MovingState.ACCEL && currentLinearSpeed < this.maxLinearSpeed){
			linearSpeed = currentLinearSpeed + (int)Math.round(diffTime*this.linearAccelSpeed);
		}else if(this.accelState == MovingState.DECEL){
			linearSpeed = currentLinearSpeed - (int)Math.round(diffTime*this.linearDeccelSpeed);
		}else{
			linearSpeed = this.maxLinearSpeed;
		}

		//Limit maximum Speed
		linearSpeed = Math.min(linearSpeed,this.maxLinearSpeed);
		
		//Limit minimum Speed
		linearSpeed = signe*Math.max(linearSpeed,this.minLinearSpeed);

		this.asserv.lockLinear();
		this.asserv.lockAngular();
		this.asserv.setTarget(linearSpeed,angularSpeed);

	}

	private void turn(MovingAction direction, long diffTime, double distance, double angle){
		int angularSpeed;
		int linearSpeed = (int)Math.round(distance);
		int currentAngularSpeed;
		int signe = 1;

		if(direction != MovingAction.TURNLEFT && direction != MovingAction.TURNRIGHT){
			return;
		}
		if(direction == MovingAction.TURNRIGHT){
			signe = -1;
		}
		if(signe*angle < this.fullStopAngle){
			this.stopMoving();
			return;
		}

		if(signe*angle <= this.stopAngle){
			this.accelState = MovingState.DECEL;
		}

		currentAngularSpeed = Math.abs(this.asserv.getTargetAngularSpeed());

		if(this.accelState == MovingState.ACCEL && currentAngularSpeed < this.maxAngularSpeed){
			angularSpeed = currentAngularSpeed + (int)Math.round(diffTime*this.angularAccelSpeed);
		}else if(this.accelState == MovingState.DECEL){
			angularSpeed = currentAngularSpeed - (int)Math.round(diffTime*this.angularDeccelSpeed);
		}else{
			angularSpeed = this.maxAngularSpeed;
		}

		//Limit maximum Speed
		angularSpeed = Math.min(angularSpeed,this.maxAngularSpeed);

		//Limit minumun Speed
		angularSpeed = signe*Math.max(angularSpeed,this.minAngularSpeed);

		this.asserv.lockLinear();
		this.asserv.lockAngular();
		this.asserv.setTarget(linearSpeed,angularSpeed);
	}

	@Override
	public void run(){
		long currentTime = System.currentTimeMillis();;
		long diffTime = System.currentTimeMillis();;
		int distanceLeft;
		int angleLeft;
		USBConnexion conn = USBConnexion.getInstance();
		while(!this.isInterrupted()){
			try{
				synchronized(this){
					currentTime = System.currentTimeMillis();
					diffTime = currentTime - this.prevTime;

					distanceLeft = this.targetDistance - this.currentDistance;
					angleLeft= this.targetAngle - this.currentAngle;

//					conn.send(new Error("ANGLE : " + angleLeft));
//					conn.send(new Error("DISTANCE : " + distanceLeft));
/*
					if(this.accelState == MovingState.ACCEL){
						conn.send(new Error("ACCEL"));
					}else if(this.accelState == MovingState.DECEL){
						conn.send(new Error("DECCEL"));
					}
*/
					//System.out.println(distance);

					if(this.state == MovingAction.FORWARD || this.state == MovingAction.BACKWARD){
						this.move(this.state,diffTime,distanceLeft,angleLeft);
					}else if(this.state == MovingAction.TURNLEFT || this.state == MovingAction.TURNRIGHT){
						this.turn(this.state,diffTime,distanceLeft,angleLeft);
					}else{
						this.startedDistance = this.currentDistance;
						this.startedAngle = this.currentAngle;
						this.targetDistance = this.currentDistance;
						this.targetAngle = this.currentAngle;
						this.asserv.setTarget(0,0);
						this.asserv.freeAngular();
						this.asserv.freeLinear();
						this.asserv.reset();
					}
					this.prevTime = currentTime;
					this.wait();
				}
			}catch(InterruptedException e){
				this.interrupt();
			}
		}
	}
}
