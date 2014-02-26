package TTT.PC.views;

import javax.swing.JLabel;

import TTT.PC.models.communication.ConnexionListener;
import TTT.PC.models.communication.StatutChangedEvent;

import TTT.PC.controllers.ConnexionController;

public class ConnexionViewStatusBar extends JLabel implements ConnexionListener {
	static final long serialVersionUID = -1193997742742445922L;
	private ConnexionController controller = null;

	public ConnexionViewStatusBar(ConnexionController controller){
		super();
		this.controller = controller;
		this.controller.addView(this);
		this.setHorizontalAlignment(JLabel.RIGHT);
		this.setStatut(false);
	}

	public final ConnexionController getController(){
		return this.controller;
	}

	@Override
	public void statutChanged(StatutChangedEvent event){
		this.setStatut(event.getNewStatut());
	}

	public void setStatut(boolean connected){
		if(connected){
			this.setText("Connected");
		} else {
			this.setText("Disconnected");
		}
	}
}
