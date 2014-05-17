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
			case USMonitorGetValue.ID:
				ret = new USMonitorGetValue();
				break;
			case Battery.ID:
				ret = new Battery();
				break;
			case FixAngularAsservMessage.ID:
				ret = new FixAngularAsservMessage();
				break;
			case SetConfig.ID:
				ret = new SetConfig();
				break;
			case SendPoseMsg.ID:
				ret = new SendPoseMsg();
				break;
			case MoveMsg.ID:
				ret = new MoveMsg();
				break;
			case Ping.ID:
				ret = new Ping();
				break;
			case USMonitorGetSensors.ID:
				ret = new USMonitorGetSensors();
				break;
			case Error.ID:
				ret = new Error();
				break;
			case StopMsg.ID:
				ret = new StopMsg();
				break;
			case SetPoseMsg.ID:
				ret = new SetPoseMsg();
				break;
			case AsservInfo.ID:
				ret = new AsservInfo();
				break;
			case USMonitorSensors.ID:
				ret = new USMonitorSensors();
				break;
			case FixLinearAsservMessage.ID:
				ret = new FixLinearAsservMessage();
				break;
			case USMonitorValue.ID:
				ret = new USMonitorValue();
				break;
			case GetConfig.ID:
				ret = new GetConfig();
				break;
			case GetPose.ID:
				ret = new GetPose();
				break;
			case TurnMsg.ID:
				ret = new TurnMsg();
				break;
		}
		return ret;
	}
}
