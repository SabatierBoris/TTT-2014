package TTT.commons.communication;

/**
 * This is a generated file
 */
public class MessagesFactory extends AbstractMessagesFactory {
	public MessagesFactory(){
		super();
	}
	public Object getObject(int id){
		Object ret = null;
		switch(id){
			case Battery.ID:
				ret = new Battery();
				break;
			case Error.ID:
				ret = new Error();
				break;
		}
		return ret;
	}
}
