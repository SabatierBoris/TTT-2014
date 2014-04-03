package TTT.commons.communication;

import java.util.StringTokenizer;
import java.util.NoSuchElementException;

import TTT.commons.factory.ItemFactory;

@ItemFactory(factoryName="TTT.commons.communication.Messages")
public class AsservInfo extends Message {
	public static final String DELIMITER = ":";
	public static final int ID = 8;

	public int targetLinear;
	public int currentLinear;
	public int targetAngular;
	public int currentAngular;

	public AsservInfo(){
		this.targetLinear = 0;
		this.currentLinear = 0;
		this.targetAngular = 0;
		this.currentAngular = 0;
	}

	public AsservInfo(int targetLinear, int currentLinear, int targetAngular, int currentAngular){
		this.targetLinear = targetLinear;
		this.currentLinear = currentLinear;
		this.targetAngular = targetAngular;
		this.currentAngular = currentAngular;
	}

	@Override
	public String toString(){
		return AsservInfo.ID + Message.DELIMITER + this.targetLinear + AsservInfo.DELIMITER + this.currentLinear + AsservInfo.DELIMITER + this.targetAngular + AsservInfo.DELIMITER + this.currentAngular;
	}

	@Override
	public void parse(String data) throws ParsingFailException {
		try{
			StringTokenizer st = new StringTokenizer(data,AsservInfo.DELIMITER);
			this.targetLinear = Integer.parseInt(st.nextToken());
			this.currentLinear = Integer.parseInt(st.nextToken());
			this.targetAngular = Integer.parseInt(st.nextToken());
			this.currentAngular = Integer.parseInt(st.nextToken());
		} catch(NumberFormatException e){
			throw new ParsingFailException("Error for parsing : " + data);
		} catch(NoSuchElementException e){
			throw new ParsingFailException("Too few data : " + data);
		}
	}

	@Override
	public int getId(){
		return AsservInfo.ID;
	}
}

