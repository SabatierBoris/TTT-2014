package TTT.PC.controllers;


import TTT.PC.models.communication.ConnexionModel;

import TTT.PC.models.task.TaskFixOdometry;
import TTT.PC.models.task.TaskGetPose;
import TTT.PC.models.graph.GraphModel;
import TTT.PC.models.odometry.OdometryModel;

import TTT.commons.navigation.Pose;
import TTT.commons.communication.FixLinearAsservMessage;
import TTT.commons.communication.FixAngularAsservMessage;

public class OdometryController {
	private ConfigController config = null;
	private ConnexionController conn;
	private Thread task;
	private TaskGetPose info;
	private OdometryModel odoModel;

	public OdometryController(OdometryModel odoModel, ConfigController config, ConnexionController conn, TaskGetPose info){
		super();
		this.config = config;
		this.conn = conn;
		this.task = null;
		this.info = info;
		this.odoModel = odoModel;
	}

	public Pose getCurrentPose(){
		return this.odoModel.getCurrentPose();
	}

	public void setCurrentPose(double x, double y, double heading){
		this.conn.setCurrentPose(new Pose(x,y,heading));
	}

	public void launchFixOdometry(){
		if(this.task == null){
			this.task = new TaskFixOdometry(this,this.config,this.conn,this.info);
			this.task.setDaemon(true);
			this.task.start();
		}
	}

	public void finish(){
		this.task = null;
	}
}

