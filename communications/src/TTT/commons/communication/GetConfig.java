package TTT.commons.communication;

import java.util.StringTokenizer;
import java.util.NoSuchElementException;

import TTT.commons.factory.ItemFactory;

@ItemFactory(factoryName="TTT.commons.communication.Messages")
public class GetConfig extends Message {
	public static final String DELIMITER = ":";
	public static final int ID = 5;
	private String key;
	private String value;

	public GetConfig(String key, String value){
		this.key = key;
		this.value = value;
	}
	public GetConfig(String key){
		this(key,"");
	}

	public GetConfig(){
		this("","");
	}

	@Override
	public String toString(){
		return GetConfig.ID + Message.DELIMITER + this.key + GetConfig.DELIMITER + this.value;
	}

	@Override
	public void parse(String data) throws ParsingFailException {
		try{
			StringTokenizer st = new StringTokenizer(data,GetConfig.DELIMITER);
			this.key = st.nextToken();
			this.value = st.nextToken();
		} catch(NoSuchElementException e){
			throw new ParsingFailException("Too few data : " + data);
		}
	}

	public void setValue(String value){
		this.value = value;
	}

	public String getKey(){
		return this.key;
	}

	public String getValue(){
		return this.value;
	}

	@Override
	public int getId(){
		return GetConfig.ID;
	}
}
