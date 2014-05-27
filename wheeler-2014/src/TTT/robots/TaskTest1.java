package TTT.robots;

import lejos.robotics.RegulatedMotor;
import lejos.nxt.NXTMotor;

import TTT.libNXT.communication.Connexion;
import TTT.commons.communication.Battery;
import TTT.commons.communication.Message;
import TTT.commons.communication.Error;
import TTT.commons.communication.MessageListener;

import TTT.commons.navigation.Pose;
import TTT.commons.navigation.PoseListener;

import TTT.libNXT.navigation.BasicOdometry;
import TTT.libNXT.configuration.Configurateur;
import TTT.libNXT.configuration.ConfigListener;
import TTT.libNXT.navigation.AbstractAsservise;

public class TaskTest1 extends Thread implements PoseListener, MessageListener, ConfigListener {
	private MovingAction state;

	private Pose currentPose;
	private Pose target;

	private NXTMotor m1;
	private NXTMotor m2;
	//TODO Remove
	private Connexion conn;

	private double travelDistance;
	private double lastDistance;
	private double lastAngle;
	private int nbTurn;

	private double limitAngleUp;
	private double limitAngleDown;
	private int accelDistance;
	private int stopDistance;
	private int fullStopDistance;
	private int minLinearSpeed;
	private int minLinearAccelSpeed;
	private int correctionAngleSpeed;

/*
	private int linearAccel;
	private int angularAccel;
	private int linearMaxSpeed;
	private int angularMaxSpeed;
*/

	private int n;

	public TaskTest1(BasicOdometry odo, NXTMotor m1, NXTMotor m2, Connexion conn){
		super();
		this.n = 0;
		this.travelDistance = 0;
		this.nbTurn = 0;
		this.lastDistance = -1;
		this.lastAngle = -1;
		this.state = MovingAction.STOP;
		this.m1 = m1;
		this.m2 = m2;
		this.conn = conn;
		this.conn.addMessageListener(this,Battery.ID);

		Configurateur conf = Configurateur.getInstance();

		this.limitAngleUp = Double.parseDouble(conf.get("test.limitAngleUp","0.5"));
		this.limitAngleDown = Double.parseDouble(conf.get("test.limitAngleDown","0.25"));
		this.accelDistance = Integer.parseInt(conf.get("test.accelDistance","25"));
		this.stopDistance = Integer.parseInt(conf.get("test.stopDistance","25"));
		this.fullStopDistance = Integer.parseInt(conf.get("test.fullStopDistance","10"));
		this.minLinearSpeed = Integer.parseInt(conf.get("test.minLinearSpeed","50"));
		this.minLinearAccelSpeed = Integer.parseInt(conf.get("test.minLinearAccelSpeed","50"));
		this.correctionAngleSpeed = Integer.parseInt(conf.get("test.correctionAngleSpeed","5"));








/*
		this.linearAccel = Integer.parseInt(conf.get("test.linearAccel","100"));
		this.angularAccel = Integer.parseInt(conf.get("test.angularAccel","100"));
		this.linearMaxSpeed = Integer.parseInt(conf.get("test.linearMaxSpeed","400"));
		this.angularMaxSpeed = Integer.parseInt(conf.get("test.angularMaxSpeed","20"));
*/
		conf.addConfigListener(this,"test");

		odo.addPoseListener(this);
		this.currentPose = new Pose(odo.getCurrentPose());
		this.target = new Pose(this.currentPose);

		//this.m1.setAcceleration(this.linearAccel);
		//this.m2.setAcceleration(this.angularAccel);
	}

	@Override
	public void configChanged(String key, String value){
		if(key.equals("test.limitAngleUp")){
			this.limitAngleUp = Double.parseDouble(value);
		} else if(key.equals("test.limitAngleDown")){
			this.limitAngleDown = Double.parseDouble(value);
		} else if(key.equals("test.accelDistance")){
			this.accelDistance = Integer.parseInt(value);
		} else if(key.equals("test.stopDistance")){
			this.stopDistance = Integer.parseInt(value);
		} else if(key.equals("test.fullStopDistance")){
			this.fullStopDistance = Integer.parseInt(value);
		} else if(key.equals("test.minLinearSpeed")){
			this.minLinearSpeed = Integer.parseInt(value);
		} else if(key.equals("test.minLinearAccelSpeed")){
			this.minLinearAccelSpeed = Integer.parseInt(value);
		} else if(key.equals("test.correctionAnglespeed")){
			this.correctionAngleSpeed = Integer.parseInt(value);
		} else {
			this.conn.send(new Error("Inknow " + key));
		}
		/*
		if(key.equals("test.linearAccel")){
			this.linearAccel = Integer.parseInt(value);
		} else if(key.equals("test.angularAccel")){
			this.angularAccel = Integer.parseInt(value);
		} else if(key.equals("test.linearMaxSpeed")){
			this.linearMaxSpeed = Integer.parseInt(value);
		} else if(key.equals("test.angularMaxSpeed")){
			this.angularMaxSpeed = Integer.parseInt(value);
		} else {
			this.conn.send(new Error("Inknow " + key));
		}
		*/
	}

	@Override
	public void poseChanged(Pose p){
		synchronized(this){
			this.currentPose = new Pose(p);
			this.notify();
		}
	}

	public void stop(){
		synchronized(this){
			this.state = MovingAction.STOP;
		}
	}

	public void moveForward(int distance){
		synchronized(this){
			this.travelDistance = 0;
			this.lastDistance = -1;
			this.lastAngle = -1;
			this.nbTurn = 0;
			this.state = MovingAction.FORWARD;
			this.target = new Pose(this.currentPose);
			this.target.move(distance);
			this.notify();
		}
	}

	public void moveBackward(int distance){
		synchronized(this){
			this.travelDistance = 0;
			this.lastDistance = -1;
			this.lastAngle = -1;
			this.nbTurn = 0;
			this.state = MovingAction.BACKWARD;
			this.target = new Pose(this.currentPose);
			this.target.move(-distance);
			this.notify();
		}
	}

	public void turnLeft(int angle){
		synchronized(this){
			this.lastDistance = -1;
			this.lastAngle = -1;
			this.state = MovingAction.TURNLEFT;
			this.target = new Pose(this.currentPose);
			this.target.setHeading(this.target.getHeading()+angle);
			this.nbTurn = angle/360;
			this.notify();
		}
	}

	public void turnRight(int angle){
		//TODO
	}

	private void move(MovingAction direction,double distance, double angle){
		int minSpeed = 100;
		int maxSpeed = 100;
		int speed = 0;
		int angleSpeed;

		if(direction != MovingAction.FORWARD && direction != MovingAction.BACKWARD){
			return;
		}

		//Acceleration
		if(this.travelDistance <= this.accelDistance){
			minSpeed = this.minLinearAccelSpeed+(100-this.minLinearAccelSpeed)*(int)Math.round(this.travelDistance)/this.accelDistance;
		}
		if(speed < minSpeed){
			speed = minSpeed;
		}

		//Decceleration
		if(distance <= this.stopDistance){
			maxSpeed = this.minLinearSpeed+(100-this.minLinearSpeed)*(int)Math.round(distance)/this.stopDistance;
		}
		if(speed > maxSpeed){
			speed = maxSpeed;
		}

		//Minimum Speed
		if(speed < this.minLinearSpeed){
			speed = this.minLinearSpeed;
		}

		//Angle omogeneisation
		if(angle < -180){
			angle += 360;
		}

		this.conn.send(new Error("D " + distance + " A " + angle));

		angleSpeed = (int)Math.round(this.correctionAngleSpeed*Math.abs(angle));
		this.m2.setPower(angleSpeed);

		this.m1.setPower(speed);
		if(direction == MovingAction.BACKWARD){
			this.m1.backward();
		} else {
			this.m1.forward();
		}

		if(angle < this.limitAngleDown && angle > -this.limitAngleDown){
			this.conn.send(new Error("STOP ANGLE"));
			this.m2.stop();
		} else if(angle > this.limitAngleUp){
			this.conn.send(new Error("TURN BACKWARD " + angleSpeed));
			this.m2.backward();
		} else if(angle < -this.limitAngleUp){
			this.conn.send(new Error("TURN FORWARD " + angleSpeed));
			this.m2.forward();
		}

		if(distance < this.fullStopDistance){
			this.conn.send(new Error("STOP !!!"));
			this.m1.stop();
			this.m2.stop();
			this.state = MovingAction.STOP;
		}
	}

	@Override
	public void run(){
		double distance;
		double angle;

		while(!this.isInterrupted()){
			try{
				synchronized(this){
					angle = target.getHeading() - this.currentPose.getHeading();

					distance = this.target.substract(this.currentPose).getDistance();
					if((this.state == MovingAction.FORWARD || this.state == MovingAction.BACKWARD) && (this.lastDistance != -1 && this.lastDistance < distance)){
						this.conn.send(new Error("Wrong direction !!!"));
						this.state = MovingAction.STOP;
					}
					if(this.lastDistance != -1){
						this.travelDistance += this.lastDistance - distance;
					}
					this.lastDistance = distance;

					//this.conn.send(new Error("nbTurn " + this.nbTurn + " T " + this.target + " C " + this.currentPose));
					//this.conn.send(new Error(" C " + this.currentPose));

					switch(this.state){
						case FORWARD:
						case BACKWARD:
							this.move(this.state,distance,angle);
							break;
						case TURNLEFT:
							angle = angle + this.nbTurn  * 360;
							if(this.lastAngle != -1 && (angle > this.lastAngle || angle < 0) && this.nbTurn == 0){
								this.m2.stop();
								this.state = MovingAction.STOP;
							}
							if(this.lastAngle != -1 && angle > this.lastAngle && this.nbTurn > 0){
								this.nbTurn--;
							}
							//if(this.m2.isStalled()){
							//	this.conn.send(new Error("CALLER !!!"));
							//	this.m2.stop();
							//}
							this.lastAngle = angle;
							this.conn.send(new Error("D " + distance + " A " + angle));
							//this.m2.setAcceleration(200);
							//this.m2.setSpeed(300);
							this.m2.setPower(100);
							this.m2.backward();
							//TODO
							break;
						case TURNRIGHT:
							//TODO
							break;
						case STOP:
						default:
							this.m1.flt();
							this.m2.flt();
							break;
					}
					this.wait();
				}
			} catch(InterruptedException e){
				this.interrupt();
			}
		}
	}

	@Override
	public void messageReceived(Message m){
		if(m.getId() == Battery.ID){
			if(this.n%4 == 0){
				this.moveBackward(120);
			} else if(this.n%4 == 1){
				this.stop();
			} else if(this.n%4 == 2){
				this.moveForward(120);
			} else {
				this.stop();
			}
			/*
			if(this.n%2 == 0){
				this.turnLeft(450);
			} else {
				this.turnRight(450);
			}
			*/
			this.n++;
		}
	}
}
