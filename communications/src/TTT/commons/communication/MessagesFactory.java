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
			case SetConfig.ID:
				ret = new SetConfig();
				break;
			case Ping.ID:
				ret = new Ping();
				break;
			case Error.ID:
				ret = new Error();
				break;
			case FixLinearAsservMessage.ID:
				ret = new FixLinearAsservMessage();
				break;
			case GetConfig.ID:
				ret = new GetConfig();
				break;
		}
		return ret;
	}
}
