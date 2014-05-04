package TTT.commons.communication;

import TTT.commons.factory.ItemFactory;

@ItemFactory(factoryName="TTT.commons.communication.Messages")
public class USMonitorGetValue extends Message {
	public static final int ID = 11;

	private String sensorName;

	public USMonitorGetValue(){
		this("");
	}
	public USMonitorGetValue(String name){
		this.sensorName=name;
	}

	public String getSensorName(){
		return this.sensorName;
	}

	@Override
	public String toString(){
		return ID + Message.DELIMITER + this.sensorName;
	}

	@Override
	public void parse(String data) throws ParsingFailException {
		this.sensorName = data;
	}

	@Override
	public int getId(){
		return USMonitorGetValue.ID;
	}
}
