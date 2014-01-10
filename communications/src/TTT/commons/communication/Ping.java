package TTT.commons.communication;

import java.util.StringTokenizer;
import java.util.NoSuchElementException;

import TTT.commons.factory.ItemFactory;

@ItemFactory(factoryName="TTT.commons.communication.Messages")
public class Ping extends Message {
	public static final int ID = 2;
	public static int count = 0;
	private int num;
	private int direction;

	public Ping(){
		this.num = Ping.count++;
		this.direction = 0;
	}

	@Override
	public String toString(){
		return Ping.ID + ":" + this.num + "-" + this.direction;
	}

	@Override
	public void parse(String data) throws ParsingFailException {
		try{
			StringTokenizer st = new StringTokenizer(data,"-");
			this.num = Integer.parseInt(st.nextToken());
			this.direction = Integer.parseInt(st.nextToken());
		} catch(NumberFormatException e){
			throw new ParsingFailException("Error for parsing : " + data);
		} catch(NoSuchElementException e){
			throw new ParsingFailException("Too few data : " + data);
		}
	}

	@Override
	public int getId(){
		return Ping.ID;
	}

	public boolean isRecieved(){
		return (this.direction == 0);
	}

	public void setSendBack(){
		this.direction = 1;
	}
}
