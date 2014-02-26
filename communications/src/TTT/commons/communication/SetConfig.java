package TTT.commons.communication;

import java.util.StringTokenizer;
import java.util.NoSuchElementException;

import TTT.commons.factory.ItemFactory;

@ItemFactory(factoryName="TTT.commons.communication.Messages")
public class SetConfig extends Message {
	public static final int ID = 4;
	private String key;
	private String value;

	public SetConfig(String key, String value){
		this.key = key;
		this.value = value;
	}

	public SetConfig(){
		this("","");
	}

	@Override
	public String toString(){
		return SetConfig.ID + ":" + this.key + "-" + this.value;
	}

	@Override
	public void parse(String data) throws ParsingFailException {
		try{
			StringTokenizer st = new StringTokenizer(data,"-");
			this.key = st.nextToken();
			this.value = st.nextToken();
		} catch(NoSuchElementException e){
			throw new ParsingFailException("Too few data : " + data);
		}
	}

	public String getKey(){
		return this.key;
	}

	public String getValue(){
		return this.value;
	}

	@Override
	public int getId(){
		return SetConfig.ID;
	}
}
