package TTT.PC.controllers;

import TTT.PC.models.communication.ConnexionModel;
import TTT.PC.models.communication.ConnexionListener;

import TTT.commons.communication.AsservInfo;
import TTT.commons.communication.Battery;
import TTT.commons.communication.FixLinearAsservMessage;
import TTT.commons.communication.FixAngularAsservMessage;
import TTT.commons.communication.MoveMsg;
import TTT.commons.communication.TurnMsg;
import TTT.commons.communication.StopMsg;
import TTT.commons.communication.SetPoseMsg;
import TTT.commons.communication.MessageListener;

import TTT.commons.navigation.Pose;

public class ConnexionController {
	private ConnexionModel model = null;

	public ConnexionController(ConnexionModel model){
		super();
		this.model = model;
	}

	public void addView(ConnexionListener view){
		this.model.addConnexionListener(view);
	}

	public void addMessageView(MessageListener view, Integer messageId){
		this.model.addMessageListener(view,messageId);
	}
	public void removeMessageView(MessageListener view, Integer messageId){
		this.model.removeMessageListener(view,messageId);
	}

	public void toogleConnexion(){
		if(this.model.isConnected()){
			this.model.close();
		} else {
			this.model.connect();
		}
	}

	public void sendFixLinearAsserv(){
		this.model.send(new FixLinearAsservMessage());
	}

	public void sendFixAngularAsserv(){
		this.model.send(new FixAngularAsservMessage());
	}

	public void sendAsservInfo(){
		this.model.send(new AsservInfo());
	}

	public void sendBattery(){
		this.model.send(new Battery());
	}

	public void sendMove(int distance){
		this.model.send(new MoveMsg(distance));
	}

	public void sendTurn(int angle){
		this.model.send(new TurnMsg(angle));
	}

	public void sendStop(){
		this.model.send(new StopMsg());
	}

	public void setCurrentPose(Pose pose){
		this.model.send(new SetPoseMsg(pose));
	}
}
