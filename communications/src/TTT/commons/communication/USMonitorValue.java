package TTT.commons.communication;

import java.util.StringTokenizer;
import java.util.NoSuchElementException;

import TTT.commons.factory.ItemFactory;

@ItemFactory(factoryName="TTT.commons.communication.Messages")
public class USMonitorValue extends Message {
	public static final String DELIMITER = ":";
	public static final int ID = 12;

	private String sensorName;
	private int sensorValue;

	public USMonitorValue(){
		this("",0);
	}
	
	public USMonitorValue(String name, int value){
		this.sensorName = name;
		this.sensorValue = value;
	}

	public String getSensorName(){
		return this.sensorName;
	}

	public int getSensorValue(){
		return this.sensorValue;
	}

	@Override
	public String toString(){
		return ID + Message.DELIMITER + this.sensorName + USMonitorValue.DELIMITER + this.sensorValue;
	}

	@Override
	public void parse(String data) throws ParsingFailException {
		try{
			StringTokenizer st = new StringTokenizer(data,SetConfig.DELIMITER);
			this.sensorName = st.nextToken();
			this.sensorValue = Integer.parseInt(st.nextToken());
		} catch(NoSuchElementException e){
			throw new ParsingFailException("Too few data : " + data);
		} catch(NumberFormatException e){
			throw new ParsingFailException("Error for parsing : " + data);
		}
	}

	@Override
	public int getId(){
		return USMonitorValue.ID;
	}
}
