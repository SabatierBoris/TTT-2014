package TTT.PC.views.actions;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import java.awt.event.ActionEvent;

import TTT.PC.controllers.OdometryController;

public class FixOdometry extends AbstractAction {
	static final long serialVersionUID = -6945893446370275518L;
	private OdometryController controller;

	public FixOdometry(OdometryController controller){
		super();
		this.controller = controller;
		this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("F6"));
		this.putValue(Action.NAME, "FixOdometry");
	}
	public void actionPerformed(ActionEvent e){
		this.controller.launchFixOdometry();
	}
}
