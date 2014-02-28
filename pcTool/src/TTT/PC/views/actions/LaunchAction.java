package TTT.PC.views.actions;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import java.awt.event.ActionEvent;

import TTT.PC.controllers.ConnexionController;

public class LaunchAction extends AbstractAction {
	static final long serialVersionUID = -6945893446370275518L;
	private ConnexionController controller;

	public LaunchAction(ConnexionController controller){
		super();
		this.controller = controller;
		this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("F6"));
		this.putValue(Action.NAME, "Avance");
	}
	public void actionPerformed(ActionEvent e){
		this.controller.sendForward();
	}
}
