package TTT.PC.controllers;

import TTT.PC.models.communication.ConnexionModel;

import TTT.PC.models.task.TaskFixLinearAsserv;
import TTT.PC.models.task.TaskAsservInfo;
import TTT.PC.models.graph.GraphModel;

import TTT.commons.communication.FixLinearAsservMessage;
import TTT.commons.communication.FixAngularAsservMessage;

public class AsservController {
	private ConfigController config = null;
	private ConnexionController conn;
	private Thread task;
	private GraphModel linearGraphModel;
	private GraphModel angularGraphModel;
	private TaskAsservInfo info;

	public AsservController(ConfigController config, ConnexionController conn, GraphModel linearGraphModel, GraphModel angularGraphModel, TaskAsservInfo info){
		super();
		this.config = config;
		this.conn = conn;
		this.task = null;
		this.info = info;
		this.linearGraphModel = linearGraphModel;
		this.angularGraphModel = angularGraphModel;
	}

	public void launchFixLinearAsserv(){
		if(this.task == null){
			this.task = new TaskFixLinearAsserv(this,this.config,this.conn,this.linearGraphModel,this.angularGraphModel,this.info);
			this.task.setDaemon(true);
			this.task.start();
		}
	}

	public void finish(){
		this.task = null;
	}
}
