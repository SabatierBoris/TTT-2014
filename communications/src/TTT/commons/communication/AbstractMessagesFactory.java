package TTT.commons.communication;

import java.util.StringTokenizer;
import java.util.NoSuchElementException;

import TTT.commons.factory.AbstractFactory;

public abstract class AbstractMessagesFactory extends AbstractFactory{
	public AbstractMessagesFactory(){
		super();
	}

	public Message getMessage(String data) throws ParsingFailException {
		int type;
		String buff;
		Message mes;
		try {
			StringTokenizer st = new StringTokenizer(data,":");
			type = Integer.parseInt(st.nextToken());
			buff = st.nextToken();
			if(st.hasMoreElements()){
				throw new ParsingFailException("Too many data : " + data);
			}
		} catch(NumberFormatException e){
			throw new ParsingFailException("Error for parsing : " + data);
		} catch(NoSuchElementException e){
			throw new ParsingFailException("Too few data : " + data);
		}
		mes = (Message)this.getObject(type);
		mes.parse(buff);
		return mes;
	}
}
