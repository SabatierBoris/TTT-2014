package TTT.commons.communication;

import java.util.StringTokenizer;
import java.util.NoSuchElementException;
import java.util.ArrayList;

import TTT.commons.factory.ItemFactory;

@ItemFactory(factoryName="TTT.commons.communication.Messages")
public class USMonitorSensors extends Message {
	public static final String DELIMITER = ":";
	public static final int ID = 10;
	private ArrayList<String> sensorsNames;

	public USMonitorSensors(){
		this.sensorsNames = new ArrayList<String>();
	}

	public void addSensorName(String name){
		this.sensorsNames.add(name);
	}

	public ArrayList<String> getSensorsNames(){
		return this.sensorsNames;
	}

	@Override
	public String toString(){
		String ret = ID + Message.DELIMITER;
		int size = this.sensorsNames.size();
		for(int i=0;i<size;i++){
			if(i != 0){
				ret += USMonitorSensors.DELIMITER;
			}
			ret += this.sensorsNames.get(i);
		}
		return ret;
	}

	@Override
	public void parse(String data) throws ParsingFailException {
		try{
			StringTokenizer st = new StringTokenizer(data,SetConfig.DELIMITER);
			while(st.hasMoreElements()){
				this.addSensorName(st.nextToken());
			}
		} catch(NoSuchElementException e){
			throw new ParsingFailException("Too few data : " + data);
		}
	}

	@Override
	public int getId(){
		return USMonitorSensors.ID;
	}
}
