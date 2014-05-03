package TTT.PC.views.actions;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import java.awt.event.ActionEvent;

import TTT.PC.models.communication.ConnexionListener;
import TTT.PC.models.communication.StatutChangedEvent;

import TTT.PC.controllers.ConnexionController;

public class ToogleConnectAction extends AbstractAction implements ConnexionListener {
	static final long serialVersionUID = -6945893446370275518L;
	private ConnexionController controller;

	public ToogleConnectAction(ConnexionController controller){
		super();
		this.controller = controller;
		this.controller.addView(this);
		this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("F5"));
		this.setName(false);
	}
	public void actionPerformed(ActionEvent e){
		System.out.println("F5");
		this.controller.toogleConnexion();
	}

	@Override
	public void statutChanged(StatutChangedEvent event){
		this.setName(event.getNewStatut());
	}

	public void setName(boolean connected){
		if(connected){
			this.putValue(Action.NAME, "Disconnect");
		} else {
			this.putValue(Action.NAME, "Connect");
		}
	}
}

