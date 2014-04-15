package TTT.PC.views.actions;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import java.awt.event.ActionEvent;

import TTT.PC.controllers.ConnexionController;

import TTT.PC.models.graph.GraphModel;
import TTT.PC.models.task.TaskAsservInfo;

public class BatteryAction extends AbstractAction {
	static final long serialVersionUID = -6945893446370275518L;
	private ConnexionController controller;
	private int n;
	
	private TaskAsservInfo asservInfo;
	private GraphModel linearGraphModel;
	private GraphModel angularGraphModel;

	public BatteryAction(ConnexionController controller, TaskAsservInfo asservInfo, GraphModel linearGraphModel, GraphModel angularGraphModel){
		super();
		this.controller = controller;
		this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("F8"));
		this.putValue(Action.NAME, "Battery");
		this.asservInfo = asservInfo;
		this.linearGraphModel = linearGraphModel;
		this.angularGraphModel = angularGraphModel;
		this.n = 0;
	}
	public void actionPerformed(ActionEvent e){
		if(this.n%4 == 0){
			this.linearGraphModel.clear();
			this.angularGraphModel.clear();
			this.asservInfo.startSend();
		}else if(this.n%4 == 1){
			this.asservInfo.stopSend();
		}else if(this.n%4 == 2){
			this.linearGraphModel.clear();
			this.angularGraphModel.clear();
			this.asservInfo.startSend();
		}else{
			this.asservInfo.stopSend();
		}
		this.n++;
		this.controller.sendBattery();
	}
}
