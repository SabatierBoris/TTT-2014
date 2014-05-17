package TTT.commons.communication;

import java.util.StringTokenizer;
import java.util.NoSuchElementException;

import TTT.commons.factory.ItemFactory;

@ItemFactory(factoryName="TTT.commons.communication.Messages")
public class StopMsg extends Message {
	public static final int ID = 17;

	@Override
	public String toString(){
		return StopMsg.ID + Message.DELIMITER;
	}

	@Override
	public void parse(String data) throws ParsingFailException {
	}

	@Override
	public int getId(){
		return StopMsg.ID;
	}
}
