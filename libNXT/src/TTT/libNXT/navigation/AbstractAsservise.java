package TTT.libNXT.navigation;

//TODO Remove
import TTT.commons.communication.Error;
import TTT.libNXT.communication.Connexion;

import TTT.libNXT.configuration.Configurateur;
import TTT.libNXT.configuration.ConfigListener;

import lejos.nxt.NXTMotor;

public abstract class AbstractAsservise extends Thread implements CodeursListener, ConfigListener {
	protected long lastUpdateTick;

	private static final int ratioTick = 5;

	protected int lastDistance;
	protected int lastOrient;

	protected int currentDistance;
	protected int currentOrient;

	protected int targetLinearSpeed;
	protected int targetAngularSpeed;

	protected int currentLinearSpeed;
	protected int currentAngularSpeed;

	private NXTMotor m1;
	private NXTMotor m2;

	private int m1Speed;
	private int m2Speed;

	private float lineP;
	private int angleP;

	private float lineI;
	private int angleI;
	
	private float lineD;
	private int angleD;

	private int previousLinearError;
	private int previousAngularError;

	private int sumLinearError;
	private int sumAngularError;

	private int previousLinearSpeed;
	private int previousAngularSpeed;

	private boolean linearLock;
	private boolean angularLock;
	private boolean m1Float;
	private boolean m2Float;

	//TODO Remove
	private Connexion conn;

	public AbstractAsservise(BasicOdometry odo, NXTMotor m1, NXTMotor m2){
		super();
		Configurateur conf = Configurateur.getInstance();

		this.linearLock = false;
		this.angularLock = false;
		this.m1Float = true;
		this.m2Float = true;

		this.lineP = Float.parseFloat(conf.get("asserv.lineP","1"));
		this.lineI = Float.parseFloat(conf.get("asserv.lineI","1"));
		this.lineD = Float.parseFloat(conf.get("asserv.lineD","1"));

		this.angleP = Integer.parseInt(conf.get("asserv.angleP","1"));
		this.angleI = Integer.parseInt(conf.get("asserv.angleI","1"));
		this.angleD = Integer.parseInt(conf.get("asserv.angleD","1"));

		this.m1Speed = 0;
		this.m2Speed = 0;

		this.m1 = m1;
		this.m2 = m2;

		this.currentDistance = odo.getCurrentDistance();
		this.currentOrient = odo.getCurrentDistance();

		this.lastDistance = this.currentDistance;
		this.lastOrient = this.currentOrient;

		this.targetLinearSpeed = 0;
		this.targetAngularSpeed = 0;

		this.currentLinearSpeed = 0;
		this.currentAngularSpeed = 0;

		this.lastUpdateTick = System.currentTimeMillis();

		//TODO Remove
		this.conn = Connexion.getInstance();


		odo.addCodeursListener(this);
		conf.addConfigListener(this,"asserv");
	}

	public int getCurrentLinearSpeed(){
		return this.currentLinearSpeed;
	}

	public int getCurrentAngularSpeed(){
		return this.currentAngularSpeed;
	}

	public void setM1Speed(int s){
		this.m1Speed = s;
	}
	public void setM2Speed(int s){
		this.m2Speed = s;
	}

	@Override
	public void codeursChanged(int distance, int orient){
		synchronized(this){
			this.currentDistance = distance;
			this.currentOrient = orient;
			this.notify();
		}
	}

	public void setTarget(int linearSpeed, int angularSpeed){
		synchronized(this){
			this.targetLinearSpeed = linearSpeed;
			this.targetAngularSpeed = angularSpeed;
			//this.reset();
			this.notify();
		}
	}

	public boolean currentSpeedsCalculation(){
		long currentTick = System.currentTimeMillis();
		long diffTime = (currentTick - this.lastUpdateTick)/AbstractAsservise.ratioTick;

		if(diffTime > 10){ //TODO Param
			int diffDistance = this.currentDistance - this.lastDistance;
			int diffOrient = this.currentOrient - this.lastOrient;
//			this.conn.send(new Error("Diff time = " + diffTime));

//			this.currentLinearSpeed = (int)(diffDistance/diffTime);
			this.currentLinearSpeed = diffDistance;
//			this.currentAngularSpeed = (int)(diffOrient/diffTime);
			this.currentAngularSpeed = diffOrient;

			//TODO Remove
//			this.conn.send(new Error("Distance " + diffDistance + " Speed " + this.currentLinearSpeed));
//			this.conn.send(new Error("LS "+this.currentLinearSpeed + " AS " + this.currentAngularSpeed));
//			this.conn.send(new Error("AS "+this.currentAngularSpeed));

			this.lastDistance = this.currentDistance;
			this.lastOrient = this.currentOrient;
			this.lastUpdateTick = currentTick;
			return true;
		}else{
			return false;
		}
	}

	@Override
	public void run(){
		synchronized(this){
			try{
				while(!this.isInterrupted()){
					if(this.currentSpeedsCalculation()){
						this.speedsCalculation();
						this.applySpeeds();
					}
					this.wait();
				}
			} catch(InterruptedException e){
				this.interrupt();
			}
		}
	}

	public void applySpeeds(){
		if(this.m1Float == false){
			this.m1.setPower(this.m1Speed);
			if(this.m1Speed == 0){
				this.m1.stop();
			} else {
				this.m1.forward();
			}
		} else {
			this.m1.flt();
		}
		if(this.m2Float == false){
			this.m2.setPower(this.m2Speed);
			if(this.m2Speed == 0){
				this.m2.stop();
			} else {
				this.m2.forward();
			}
		} else {
			this.m2.flt();
		}
	}

	public void speedsCalculation(){
		int regulatedLinearError = 0;
		int regulatedAngularError = 0;
		int linearSpeed = 0;
		int angularSpeed = 0;

		int currentLinearError = this.targetLinearSpeed - this.currentLinearSpeed;
		int currentAngularError = this.targetAngularSpeed - this.currentAngularSpeed;

		int changeLinearError = currentLinearError - this.previousLinearError;
		this.sumLinearError += currentLinearError;
		regulatedLinearError = Math.round((this.lineP * currentLinearError) + (this.lineI * this.sumLinearError) + (this.lineD * changeLinearError));
		linearSpeed = regulatedLinearError;

		//this.conn.send(new Error(this.targetLinearSpeed + ";" + this.currentLinearSpeed + " ; " + linearSpeed + "=" + this.lineP + "*" + currentLinearError + " + " + this.lineI + "*" + this.sumLinearError + " + " + this.lineD + "*" + changeLinearError));
		//this.conn.send(new Error(this.currentLinearSpeed + " ; " + linearSpeed + "=" + this.lineP + "*" + currentLinearError));
		
		this.conn.send(new Error(this.targetLinearSpeed + "    -     " + this.currentLinearSpeed + "    -   " + this.sumLinearError));

		int changeAngularError = currentAngularError - this.previousAngularError;
		this.sumAngularError += currentAngularError;
		regulatedAngularError = Math.round((this.angleP * currentAngularError) + (this.angleI * this.sumAngularError) + (this.angleD * changeAngularError));
		angularSpeed = regulatedAngularError;

//		this.conn.send(new Error(this.targetAngularSpeed + "    -     " + this.currentAngularSpeed + "    -   " + angularSpeed));





//		this.conn.send(new Error("PERROR " + this.lineP*currentLinearError +
//								 " IERROR " + this.lineI*this.sumLinearError +
//								 " DERROR " + this.lineD*changeLinearError));
//		this.conn.send(new Error("Target " + this.targetLinearSpeed + 
//								 " Current " + this.currentLinearSpeed +
//								 " Error " + currentLinearError +
//								 " Previous " + this.previousLinearSpeed +
//								 " RegulatedError " + regulatedLinearError +
//								 " NextSpeed " + linearSpeed));
/*
		this.conn.send(new Error("" + this.targetLinearSpeed + 
								 ";" + this.currentLinearSpeed +
								 ";" + currentLinearError +
								 ";" + this.previousLinearSpeed +
								 ";" + regulatedLinearError +
								 ";" + linearSpeed));
*/
		//this.conn.send(new Error("Target " + this.targetAngularSpeed + " Current " + this.currentAngularSpeed + " Error " + currentAngularError + " RegulatedError " + regulatedAngularError + " NextSpeed " + angularSpeed));
		
		if((linearSpeed < 0 && this.targetLinearSpeed > 0) || (linearSpeed > 0 && this.targetLinearSpeed < 0)){
			linearSpeed = this.targetLinearSpeed;
		}

		this.speedsUpdate(linearSpeed,angularSpeed);

		this.previousLinearSpeed = linearSpeed;
		this.previousAngularSpeed = angularSpeed;

		this.previousLinearError = currentLinearError;
		this.previousAngularError = currentAngularError;
	}

	public void reset(){
		this.previousLinearError = 0;
		this.previousAngularError = 0;
		this.sumLinearError = 0;
		this.sumAngularError = 0;
		if(this.targetLinearSpeed == 0){
			this.previousLinearSpeed = 0;
		}
		if(this.targetAngularSpeed == 0){
			this.previousAngularSpeed = 0;
		}
//		this.conn.send(new Error("Target;Current;Error;Previous;RegulatedError;NextSpeed"));
	}

	@Override
	public void configChanged(String key, String value){
		if(key.equals("asserv.lineP")){
			this.lineP = Float.parseFloat(value);
		} else if(key.equals("asserv.lineI")){
			this.lineI = Float.parseFloat(value);
		} else if(key.equals("asserv.lineD")){
			this.lineD = Float.parseFloat(value);
		} else if(key.equals("asserv.angleP")){
			this.angleP = Integer.parseInt(value);
		} else if(key.equals("asserv.angleI")){
			this.angleI = Integer.parseInt(value);
		} else if(key.equals("asserv.angleD")){
			this.angleD = Integer.parseInt(value);
		} else {
			this.conn.send(new Error("Unknow " + key));
		}
	}

	public boolean linearIsLock(){
		return this.linearLock;
	}
	public boolean angularIsLock(){
		return this.angularLock;
	}
	public void lockLinear(){
		this.linearLock = true;
	}
	public void freeLinear(){
		this.linearLock = false;
	}
	public void lockAngular(){
		this.angularLock = true;
	}
	public void freeAngular(){
		this.angularLock = false;
	}

	public void setM1Float(boolean value){
		this.m1Float = value;
	}
	public void setM2Float(boolean value){
		this.m2Float = value;
	}

	protected abstract void speedsUpdate(int linearSpeed, int angularSpeed);
}
