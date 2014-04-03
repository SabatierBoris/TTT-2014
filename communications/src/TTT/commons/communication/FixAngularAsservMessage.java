package TTT.commons.communication;

import TTT.commons.factory.ItemFactory;

@ItemFactory(factoryName="TTT.commons.communication.Messages")
public class FixAngularAsservMessage extends Message {
	public static final int ID = 7;

	@Override
	public String toString(){
		return FixAngularAsservMessage.ID + Message.DELIMITER + "FIX";
	}

	@Override
	public void parse(String data) throws ParsingFailException {
	}

	@Override
	public int getId(){
		return FixAngularAsservMessage.ID;
	}
}
