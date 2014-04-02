package TTT.commons.communication;

import TTT.commons.factory.ItemFactory;

@ItemFactory(factoryName="TTT.commons.communication.Messages")
public class Error extends Message {
	public static final int ID = 1;
	private String msg;

	public Error(){
		this("");
	}

	public Error(String data){
		super();
		this.msg = data;
	}

	@Override
	public String toString(){
		return ID + Message.DELIMITER + msg;
	}

	@Override
	public void parse(String data) throws ParsingFailException {
		msg = data;
	}

	@Override
	public int getId(){
		return Error.ID;
	}
}
