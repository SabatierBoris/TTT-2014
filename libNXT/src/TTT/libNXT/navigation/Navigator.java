package TTT.libNXT.navigation;

import TTT.commons.navigation.Pose;
import TTT.commons.navigation.PoseListener;

import TTT.libNXT.configuration.ConfigListener;
import TTT.libNXT.configuration.Configurateur;

public class Navigator extends Thread implements PoseListener, ConfigListener {
	private MovingAction state;
	private MovingState accelState;

	private Pose currentPose;
	private Pose prevPose;
	private double distanceLeft;
	private double angleLeft;

	private AbstractAsservise asserv;

	private int stopDistance;
	private int fullStopDistance;
	private int maxLinearSpeed;
	private int minLinearSpeed;
	private double linearAccelSpeed;

	private int stopAngle;
	private int fullStopAngle;
	private int maxAngularSpeed;
	private int minAngularSpeed;
	private double angularAccelSpeed;

	private long prevTime;

	public Navigator(BasicOdometry odo, AbstractAsservise asserv){
		this.asserv = asserv;
		odo.addPoseListener(this);
		this.currentPose = new Pose(odo.getCurrentPose());
		this.prevPose = new Pose(this.currentPose);
		this.distanceLeft = 0;
		this.angleLeft = 0;

		Configurateur conf = Configurateur.getInstance();

		stopDistance		= Integer.parseInt(conf.get("nav.stopDistance","25")); 
		fullStopDistance	= Integer.parseInt(conf.get("nav.fullStopDistance","10"));
		maxLinearSpeed		= Integer.parseInt(conf.get("nav.maxLinearSpeed","100"));
		minLinearSpeed		= Integer.parseInt(conf.get("nav.minLinearSpeed","50"));
		linearAccelSpeed	= Double.parseDouble(conf.get("nav.linearAccepSpeed","0.1"));

		stopAngle			= Integer.parseInt(conf.get("nav.stopAngle","25")); 
		fullStopAngle		= Integer.parseInt(conf.get("nav.fullStopAngle","10"));
		maxAngularSpeed		= Integer.parseInt(conf.get("nav.maxAngularSpeed","100"));
		minAngularSpeed		= Integer.parseInt(conf.get("nav.minAngularSpeed","50"));
		angularAccelSpeed	= Double.parseDouble(conf.get("nav.angularAccepSpeed","0.1"));

		conf.addConfigListener(this,"nav");
	}

	@Override
	public void poseChanged(Pose p){
		synchronized(this){
			this.prevPose = this.currentPose;
			this.currentPose = new Pose(p);
			this.notify();
		}
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
		}else if(key.equals("nav.linearAccepSpeed")){
			linearAccelSpeed = Double.parseDouble(value);
		}else if(key.equals("nav.stopAngle")){
			stopAngle = Integer.parseInt(value);
		}else if(key.equals("nav.fullStopAngle")){
			fullStopAngle = Integer.parseInt(value);
		}else if(key.equals("nav.maxAngularSpeed")){
			maxAngularSpeed = Integer.parseInt(value);
		}else if(key.equals("nav.minAngularSpeed")){
			minAngularSpeed = Integer.parseInt(value);
		}else if(key.equals("nav.angularAccepSpeed")){
			angularAccelSpeed = Double.parseDouble(value);
		}else{
			//TODO Erreur
		}
	}

	public void stopMoving(){
		synchronized(this){
			this.state = MovingAction.STOP;
			this.accelState = MovingState.STOP;
			this.notify();
		}
	}

	public void moveForward(int distance){
		synchronized(this){
			this.state = MovingAction.FORWARD;
			this.accelState = MovingState.ACCEL;
			this.distanceLeft = distance;
			this.prevTime = System.currentTimeMillis();
			this.notify();
		}
	}

	public void moveBackward(int distance){
		synchronized(this){
			this.state = MovingAction.BACKWARD;
			this.accelState = MovingState.ACCEL;
			this.distanceLeft = distance;
			this.prevTime = System.currentTimeMillis();
			this.notify();
		}
	}

	public void turnRight(int angle){
		synchronized(this){
			this.state = MovingAction.TURNRIGHT;
			this.accelState = MovingState.ACCEL;
			this.angleLeft = angle;
			this.prevTime = System.currentTimeMillis();
			this.notify();
		}
	}

	public void turnLeft(int angle){
		synchronized(this){
			this.state = MovingAction.TURNLEFT;
			this.accelState = MovingState.ACCEL;
			this.angleLeft = angle;
			this.prevTime = System.currentTimeMillis();
			this.notify();
		}
	}

	private void move(MovingAction direction, long diffTime, double distance, double angle){
		int linearSpeed = 0;
		int currentLinearSpeed = 0;

		if(direction != MovingAction.FORWARD && direction != MovingAction.BACKWARD){
			return;
		}

		if(distance <= this.stopDistance){
			this.accelState = MovingState.DECEL;
		}

		currentLinearSpeed = Math.abs(this.asserv.getTargetLinearSpeed());

		if(this.accelState == MovingState.ACCEL && currentLinearSpeed < this.maxLinearSpeed){
			linearSpeed = currentLinearSpeed + (int)Math.round(diffTime*this.linearAccelSpeed);
		}else if(this.accelState == MovingState.DECEL){
			linearSpeed = currentLinearSpeed - (int)Math.round(diffTime*this.linearAccelSpeed);
		}else{
			linearSpeed = this.maxLinearSpeed;
		}

		//Limit maximum Speed
		if(linearSpeed > this.maxLinearSpeed){
			linearSpeed = this.maxLinearSpeed;
		}
		
		//Limit minimum Speed
		if(linearSpeed < this.minLinearSpeed){
			linearSpeed = this.minLinearSpeed;
		}

		if(direction == MovingAction.BACKWARD){
			linearSpeed *= -1;
		}

		this.asserv.lockLinear();
		this.asserv.setTarget(linearSpeed,0);

		if(distance < this.fullStopDistance){
			this.state = MovingAction.STOP;
		}
	}

	private void turn(MovingAction direction, long diffTime, double distance, double angle){
		int angularSpeed;
		int currentAngularSpeed;
		if(direction != MovingAction.TURNLEFT && direction != MovingAction.TURNRIGHT){
			return;
		}

		if(angle <= this.stopAngle){
			this.accelState = MovingState.DECEL;
		}

		currentAngularSpeed = Math.abs(this.asserv.getTargetAngularSpeed());

		if(this.accelState == MovingState.ACCEL && currentAngularSpeed < this.maxAngularSpeed){
			angularSpeed = currentAngularSpeed + (int)Math.round(diffTime*this.angularAccelSpeed);
		}else if(this.accelState == MovingState.DECEL){
			angularSpeed = currentAngularSpeed - (int)Math.round(diffTime*this.angularAccelSpeed);
		}else{
			angularSpeed = this.maxAngularSpeed;
		}

		//Limit maximum Speed
		if(angularSpeed > this.maxAngularSpeed){
			angularSpeed = this.maxAngularSpeed;
		}
		//Limit minumun Speed
		if(angularSpeed < this.minAngularSpeed){
			angularSpeed = this.minAngularSpeed;
		}

		if(direction == MovingAction.TURNLEFT){
			angularSpeed *= -1;
		}

		this.asserv.lockAngular();
		this.asserv.setTarget(0,angularSpeed);

		if(angle < this.fullStopAngle){
			this.state = MovingAction.STOP;
		}

	}

	@Override
	public void run(){
		long currentTime = System.currentTimeMillis();;
		long diffTime = System.currentTimeMillis();;
		while(!this.isInterrupted()){
			try{
				synchronized(this){
					currentTime = System.currentTimeMillis();
					diffTime = currentTime - this.prevTime;

					this.angleLeft -= Math.abs(this.prevPose.getHeading() - this.currentPose.getHeading());


					this.distanceLeft -= this.currentPose.substract(this.prevPose).getDistance();
					//System.out.println(distance);

					if(this.state == MovingAction.FORWARD || this.state == MovingAction.BACKWARD){
						this.move(this.state,diffTime,this.distanceLeft,this.angleLeft);
					}else if(this.state == MovingAction.TURNLEFT || this.state == MovingAction.TURNRIGHT){
						this.turn(this.state,diffTime,this.distanceLeft,this.angleLeft);
					}else{
						this.angleLeft = 0;
						this.distanceLeft = 0;
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
