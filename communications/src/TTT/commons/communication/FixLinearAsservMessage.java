package TTT.commons.communication;

import TTT.commons.factory.ItemFactory;

@ItemFactory(factoryName="TTT.commons.communication.Messages")
public class FixLinearAsservMessage extends Message {
	public static final String DELIMITER = ":";
	public static final int ID = 6;

	public FixLinearAsservMessage(){
	}

	@Override
	public String toString(){
		return FixLinearAsservMessage.ID + Message.DELIMITER + "FIX";
	}

	@Override
	public void parse(String data) throws ParsingFailException {
	}

	@Override
	public int getId(){
		return FixLinearAsservMessage.ID;
	}
}
