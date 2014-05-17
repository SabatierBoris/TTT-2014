package TTT.commons.communication;

import java.util.StringTokenizer;
import java.util.NoSuchElementException;

import TTT.commons.factory.ItemFactory;

@ItemFactory(factoryName="TTT.commons.communication.Messages")
public class MoveMsg extends Message {
	public static final int ID = 15;
	private int value;

	public MoveMsg(int value){
		this.value = value;
	}
	public MoveMsg(){
		this(0);
	}

	@Override
	public String toString(){
		return MoveMsg.ID + Message.DELIMITER + this.value;
	}

	@Override
	public void parse(String data) throws ParsingFailException {
		this.value = Integer.parseInt(data);
	}

	public void setValue(int value){
		this.value = value;
	}

	public int getValue(){
		return this.value;
	}

	@Override
	public int getId(){
		return MoveMsg.ID;
	}
}
