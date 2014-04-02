package TTT.libNXT.navigation;

import TTT.commons.navigation.Pose;
import TTT.commons.navigation.PoseListener;

public class Navigator extends Thread implements PoseListener{
	private final int MIN_SPEED = 10;
	private final int MAX_SPEED = 100;
	private final int ACCEL = 2;
	private Pose current;
	private Pose target;
	private AbstractAsservise asserv;
	private int phase; // 0->Stop, 1->turning, 2->moving, 3->turning

	private int previousAngleSpeed;
	private int previousLinearSpeed;

	private int currentAngleSpeed;
	private int currentLinearSpeed;

	private long lastUpdate;

	public Navigator(BasicOdometry odo, AbstractAsservise asserv){
		super();
		odo.addPoseListener(this);
		this.current = new Pose(odo.getCurrentPose());
		this.target = new Pose(current);
		this.phase = 0;
		this.asserv = asserv;
		this.previousAngleSpeed = 0;
		this.previousLinearSpeed = 0;
		this.currentAngleSpeed = 0;
		this.currentLinearSpeed = 0;
		this.lastUpdate = 0;
		this.reset();
	}

	public void setTarget(Pose p){
		synchronized(this){
			this.target = new Pose(p);
			this.reset();
			this.phase = 1;
			this.notify();
		}
	}

	@Override
	public void poseChanged(Pose p){
		synchronized(this){
			this.current = new Pose(p);
			this.notify();
		}
	}

	@Override
	public void run(){
		synchronized(this){
			try{
				while(!this.isInterrupted()){
					this.wait();
					if(this.phase != 0){
						this.calculation();
						this.applySpeeds();
					}
				}
			}catch(InterruptedException e){
				this.interrupt();
			}
		}
	}

	private void applySpeeds(){
		this.asserv.setTarget(this.currentLinearSpeed,this.currentAngleSpeed);
	}

	private void reset(){
		this.previousAngleSpeed = 0;
		this.previousLinearSpeed = 0;
		this.currentAngleSpeed = 0;
		this.currentLinearSpeed = 0;
		this.lastUpdate = -1;
		this.applySpeeds();
	}

	private void calculation(){
		long distance = 0;
		int angle = 0;
		if(this.phase == 1){
			angle = (int)Math.round(current.getAngleTo(target));
			if(angle == 0){
				this.reset();
				this.phase = 2;
			}
		}
		if(this.phase == 2){
			Pose error = target.substract(current);
			distance = Math.round(error.getDistance());
			if(distance == 0){
				this.reset();
				this.phase = 3;
			}
		}
		if(this.phase == 3){
			Pose error = target.substract(current);
			angle = (int)Math.round(error.getHeading());
			if(angle == 0){
				this.reset();
				this.phase = 0;
			}
		}
		long currentTime = System.currentTimeMillis();
		if(this.lastUpdate == -1){
			this.previousAngleSpeed = 0;
			this.previousLinearSpeed = 0;
			if(angle != 0){
				this.currentAngleSpeed = MIN_SPEED;
				this.currentLinearSpeed = 0;
			} else {
				this.currentAngleSpeed = 0;
				this.currentLinearSpeed = MIN_SPEED;
			}
		}else{
			this.previousAngleSpeed = this.currentAngleSpeed;
			this.previousLinearSpeed = this.currentLinearSpeed;
			long diff = (currentTime - this.lastUpdate)/100;
			this.currentAngleSpeed = 0;//TODO
			this.currentLinearSpeed = 0;//TODO
		}
		this.lastUpdate = currentTime;
	}
}
