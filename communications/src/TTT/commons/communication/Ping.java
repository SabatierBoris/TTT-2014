package TTT.commons.communication;

import TTT.commons.factory.ItemFactory;

@ItemFactory(factoryName="TTT.commons.communication.Messages")
public class Ping extends Message {
	public static final int ID = 2;
	private String msg;

	public Ping(){
	}

	public Ping(String data){
		this.msg = data;
	}

	@Override
	public String toString(){
		return "Ping : " + msg;
	}

	@Override
	public void parse(String data) throws ParsingFailException {
		msg = data;
	}
}

