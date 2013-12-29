package TTT.commons.communication;

import TTT.commons.factory.ItemFactory;

@ItemFactory(factoryName="TTT.commons.communication.Messages")
public class Battery extends Message {
	public static final int ID = 3;
	private int value;

	@Override
	public String toString(){
		return "Battery : " + value;
	}

	@Override
	public void parse(String data) throws ParsingFailException {
		try {
			this.value = Integer.parseInt(data);
		} catch(NumberFormatException e){
			throw new ParsingFailException("Wrong data format : " + data);
		}
	}
}
