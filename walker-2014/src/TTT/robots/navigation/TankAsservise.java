package TTT.robots.navigation;

import lejos.robotics.RegulatedMotor;

import TTT.libNXT.navigation.BasicOdometry;
import TTT.libNXT.navigation.AbstractAsservise;

public class TankAsservise extends AbstractAsservise{

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

	public TankAsservise(BasicOdometry odo, RegulatedMotor left, RegulatedMotor right, 
			                     int lineP, int lineI, int lineD,
								 int angleP, int angleI, int angleD){
		super(odo,left,right);

		this.lineP = lineP;
		this.lineI = lineI;
		this.lineD = lineD;

		this.angleP = angleP;
		this.angleI = angleI;
		this.angleD = angleD;

		this.reset();
	}

	@Override
	public void speedsCalculation(){
		int regulatedLinearError = 0;
		int regulatedAngularError = 0;
		int linearSpeed = 0;
		int angularSpeed = 0;

		int currentLinearError = this.targetLinearSpeed - this.currentLinearSpeed;
		int currentAngularError = this.targetAngularSpeed - this.currentAngularSpeed;

		//TODO Remove
//		this.conn.send(new Error(this.targetLinearSpeed + " - " + this.currentLinearSpeed + " = " + currentLinearError));

//		System.out.println(currentLineError + " | " + currentAngleError);

		int changeLinearError = currentLinearError - this.previousLinearError;
		this.sumLinearError += currentLinearError;
		regulatedLinearError = (int)Math.round((this.lineP * currentLinearError) + (this.lineI * this.sumLinearError) + (this.lineD * changeLinearError));
		linearSpeed = this.previousLinearSpeed + regulatedLinearError;
		//this.conn.send(new Error(this.targetLinearSpeed + " - " + regulatedLinearError + " - " + linearSpeed));

		int changeAngularError = currentAngularError - this.previousAngularError;
		this.sumAngularError += currentAngularError;
		regulatedAngularError = (int)Math.round((this.angleP * currentAngularError) + (this.angleI * this.sumAngularError) + (this.angleD * changeAngularError));
		angularSpeed = this.previousAngularSpeed + regulatedAngularError;

		this.setM1Speed(linearSpeed+angularSpeed);
		this.setM2Speed(linearSpeed-angularSpeed);
		this.previousLinearSpeed = linearSpeed;
		this.previousAngularSpeed = angularSpeed;

		this.previousLinearError = currentLinearError;
		this.previousAngularError = currentAngularError;
	}

	@Override
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
}
