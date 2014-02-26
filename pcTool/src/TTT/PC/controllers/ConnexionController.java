package TTT.PC.controllers;

import TTT.PC.models.communication.ConnexionModel;
import TTT.PC.models.communication.ConnexionListener;

public class ConnexionController {
	private ConnexionModel model = null;

	public ConnexionController(ConnexionModel model){
		super();
		this.model = model;
	}

	public void addView(ConnexionListener view){
		this.model.addConnexionListener(view);
	}

	public void toogleConnexion(){
		if(this.model.isConnected()){
			this.model.close();
		} else {
			this.model.connect();
		}
	}
}
