package TTT.libNXT.navigation;

import lejos.robotics.RegulatedMotor;

public abstract class AbstractAsservise extends Thread implements CodeursListener {
	protected long lastUpdateTick;

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

	public AbstractAsservise(BasicOdometry odo, RegulatedMotor m1, RegulatedMotor m2){
		super();

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

		odo.addCodeursListener(this);
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
		int diffDistance = this.currentDistance - this.lastDistance;
		int diffOrient = this.currentOrient - this.lastOrient;
		long diffTime = currentTick - this.lastUpdateTick;

		this.currentLinearSpeed = (int)(diffDistance/diffTime);
		this.currentAngularSpeed = (int)(diffOrient/diffTime);

		this.lastDistance = this.currentDistance;
		this.lastOrient = this.currentOrient;
		this.lastUpdateTick = currentTick;
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

	public abstract void speedsCalculation();
	public abstract void reset();
}
