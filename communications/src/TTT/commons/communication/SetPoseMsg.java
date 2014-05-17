package TTT.commons.communication;

import java.util.StringTokenizer;
import java.util.NoSuchElementException;

import TTT.commons.factory.ItemFactory;
import TTT.commons.navigation.Pose;

@ItemFactory(factoryName="TTT.commons.communication.Messages")
public class SetPoseMsg extends Message {
	public static final String DELIMITER = ":";
	public static final int ID = 18;
	private Pose data;

	public SetPoseMsg(){
		this(new Pose());
	}

	public SetPoseMsg(Pose data){
		this.data = data;
	}

	public Pose getPose(){
		return this.data;
	}

	@Override
	public String toString(){
		if(this.data == null){
			this.data = new Pose();
		}
		return ID + Message.DELIMITER + this.data.getLocation().getX() + SetPoseMsg.DELIMITER + this.data.getLocation().getY() + SetPoseMsg.DELIMITER + this.data.getHeading();
	}

	@Override
	public void parse(String data) throws ParsingFailException {
		double x,y,heading;
		try{
			StringTokenizer st = new StringTokenizer(data,SetConfig.DELIMITER);
			x = Double.parseDouble(st.nextToken());
			y = Double.parseDouble(st.nextToken());
			heading = Double.parseDouble(st.nextToken());
			this.data = new Pose(x,y,heading);
		} catch(NoSuchElementException e){
			throw new ParsingFailException("Too few data : " + data);
		}
	}

	@Override
	public int getId(){
		return SetPoseMsg.ID;
	}
}
