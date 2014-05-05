package TTT.commons.communication;

import TTT.commons.factory.ItemFactory;

@ItemFactory(factoryName="TTT.commons.communication.Messages")
public class GetPose extends Message {
	public static final int ID = 13;

	@Override
	public String toString(){
		return ID + Message.DELIMITER;
	}

	@Override
	public void parse(String data) throws ParsingFailException {
	}

	@Override
	public int getId(){
		return GetPose.ID;
	}
}
