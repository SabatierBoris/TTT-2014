package TTT.libNXT.navigation;

//TODO Remove
import TTT.commons.communication.Error;
import TTT.libNXT.communication.Connexion;

import TTT.libNXT.configuration.Configurateur;
import TTT.libNXT.configuration.ConfigListener;

import lejos.robotics.RegulatedMotor;

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

	private RegulatedMotor m1;
	private RegulatedMotor m2;

	private int m1Speed;
	private int m2Speed;

	private int lineP;
	private int angleP;

	private int lineI;
	private int angleI;
	
	private int lineD;
	private int angleD;

	private int previousLinearError;
	private int previousAngularError;

	private int sumLinearError;
	private int sumAngularError;

	private int previousLinearSpeed;
	private int previousAngularSpeed;

	//TODO Remove
	private Connexion conn;

	public AbstractAsservise(BasicOdometry odo, RegulatedMotor m1, RegulatedMotor m2){
		super();
		Configurateur conf = Configurateur.getInstance();

		this.lineP = Integer.parseInt(conf.get("asserv.lineP","1"));
		this.lineI = Integer.parseInt(conf.get("asserv.lineI","1"));
		this.lineD = Integer.parseInt(conf.get("asserv.lineD","1"));

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
			this.reset();
			this.notify();
		}
	}

	public void currentSpeedsCalculation(){
		long currentTick = System.currentTimeMillis();
		long diffTime = (currentTick - this.lastUpdateTick)/AbstractAsservise.ratioTick;

		if(diffTime != 0){
			int diffDistance = this.currentDistance - this.lastDistance;
			int diffOrient = this.currentOrient - this.lastOrient;

			this.currentLinearSpeed = (int)(diffDistance/diffTime);
			this.currentAngularSpeed = (int)(diffOrient/diffTime);

			//TODO Remove
			this.conn.send(new Error("LS "+this.currentLinearSpeed + " AS " + this.currentAngularSpeed));
//			this.conn.send(new Error("AS "+this.currentAngularSpeed));

			this.lastDistance = this.currentDistance;
			this.lastOrient = this.currentOrient;
			this.lastUpdateTick = currentTick;
		}
	}

	@Override
	public void run(){
		synchronized(this){
			try{
				while(!this.isInterrupted()){
					this.currentSpeedsCalculation();
					this.speedsCalculation();
					this.applySpeeds();
					this.wait();
				}
			} catch(InterruptedException e){
				this.interrupt();
			}
		}
	}

	public void applySpeeds(){
		this.m1.setSpeed(this.m1Speed);
		this.m2.setSpeed(this.m2Speed);

		if(this.m1Speed == 0){
			this.m1.stop();
		} else if(this.m1Speed > 0){
			this.m1.forward();
		} else {
			this.m1.backward();
		}
		if(this.m2Speed == 0){
			this.m2.stop();
		} else if(this.m2Speed > 0){
			this.m2.forward();
		} else {
			this.m2.backward();
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
		linearSpeed = this.previousLinearSpeed + regulatedLinearError;

		int changeAngularError = currentAngularError - this.previousAngularError;
		this.sumAngularError += currentAngularError;
		regulatedAngularError = Math.round((this.angleP * currentAngularError) + (this.angleI * this.sumAngularError) + (this.angleD * changeAngularError));
		angularSpeed = this.previousAngularSpeed + regulatedAngularError;

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
	}

	@Override
	public void configChanged(String key, String value){
		if(key.equals("asserv.lineP")){
			this.lineP = Integer.parseInt(value);
		} else if(key.equals("asserv.lineI")){
			this.lineI = Integer.parseInt(value);
		} else if(key.equals("asserv.lineD")){
			this.lineD = Integer.parseInt(value);
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

	protected abstract void speedsUpdate(int linearSpeed, int angularSpeed);
}
