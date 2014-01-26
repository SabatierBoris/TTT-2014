package TTT.robots;

import lejos.robotics.RegulatedMotor;

import TTT.libNXT.navigation.BasicOdometry;
import TTT.libNXT.navigation.AbstractAsservise;

public class DifferentialAsservise extends AbstractAsservise{

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

	public DifferentialAsservise(BasicOdometry odo, RegulatedMotor linear, RegulatedMotor rotation, 
			                     int lineP, int lineI, int lineD,
								 int angleP, int angleI, int angleD){
		super(odo,linear,rotation);

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
		int linearSpeed = 0;
		int angularSpeed = 0;

		int currentLinearError = this.targetLinearSpeed - this.currentLinearSpeed;
		int currentAngularError = this.targetAngularSpeed - this.currentAngularSpeed;

//		System.out.println(currentLineError + " | " + currentAngleError);

		int changeLinearError = currentLinearError - this.previousLinearError;
		this.sumLinearError += currentLinearError;
		linearSpeed = (int)Math.round((this.lineP * currentLinearError) + (this.lineI * this.sumLinearError) + (this.lineD * changeLinearError));

		int changeAngularError = currentAngularError - this.previousAngularError;
		this.sumAngularError += currentAngularError;
		angularSpeed = (int)Math.round((this.angleP * currentAngularError) + (this.angleI * this.sumAngularError) + (this.angleD * changeAngularError));

		System.out.println(linearSpeed + " | " + angularSpeed);

		this.setM1Speed(linearSpeed);
		this.setM2Speed(angularSpeed);

		this.previousLinearError = currentLinearError;
		this.previousAngularError = currentAngularError;
	}

	@Override
	public void reset(){
		this.previousLinearError = 0;
		this.previousAngularError = 0;
		this.sumLinearError = 0;
		this.sumAngularError = 0;
	}
}
