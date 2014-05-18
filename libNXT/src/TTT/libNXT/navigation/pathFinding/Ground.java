package TTT.libNXT.navigation.pathFinding;

import java.util.ArrayList;

import TTT.commons.navigation.Point;

import TTT.libNXT.configuration.ConfigListener;
import TTT.libNXT.configuration.Configurateur;

import TTT.libNXT.navigation.BasicOdometry;

//TODO Remove
import TTT.libNXT.communication.USBConnexion;
import TTT.commons.communication.Error;

public class Ground implements ConfigListener {
	private int groundLength;
	private int groundWidth;
	private int botSize;
	private int opponentSize;
//TODO	private int nbOpponent;

	private ArrayList<Obstacle> obstacles;
	private Obstacle opponent;

	private Graph graph;

	private Node currentPose;
	private BasicOdometry odo;

	public Ground(BasicOdometry odo){
		super();

		Configurateur conf = Configurateur.getInstance();

		this.groundLength	= Integer.parseInt(conf.get("ground.groundLength","3000")); 
		this.groundWidth	= Integer.parseInt(conf.get("ground.groundWidth","2000"));
		this.botSize		= Integer.parseInt(conf.get("ground.botSize","200"));
		this.opponentSize	= Integer.parseInt(conf.get("ground.opponentSize","200"));
//TODO		this.nbOpponent		= Integer.parseInt(conf.get("ground.nbOpponent","1"));

		conf.addConfigListener(this,"ground");

		this.odo = odo;

		this.obstacles = new ArrayList<Obstacle>();
		this.graph = new Graph();

		//this.currentPose = new Node(this.odo.getCurrentPose().getLocation());
		//this.graph.addNode(this.currentPose);

		this.initObstacles();
		this.checkPoseLink();
		this.checkObstaclesLink();
	}

	@Override
	public void configChanged(String key, String value){
		if(key.equals("ground.groundLength")){
			this.groundLength = Integer.parseInt(value);
		}else if(key.equals("ground.groundWidth")){
			this.groundWidth = Integer.parseInt(value);
		}else if(key.equals("ground.botSize")){
			this.botSize = Integer.parseInt(value);
		}else if(key.equals("ground.opponentSize")){
			this.opponentSize = Integer.parseInt(value);
//TODO		}else if(key.equals("ground.nbOpponent")){
//TODO			this.nbOpponent = Integer.parseInt(value);
		}else{
			//TODO Erreur
		}
	}

	public Graph getGraph(){
		return this.graph;
	}

	public ArrayList<Obstacle> getObstacles(){
		return this.obstacles;
	}

	public void addObstacle(Obstacle obstacle){
		this.obstacles.add(obstacle);
	}

	public void initObstacles(){
		this.addObstacle(new Obstacle(150,750,300,700,this.graph)); //yellow box
		this.addObstacle(new Obstacle(150,2250,300,700,this.graph)); //red box
		this.addObstacle(new Obstacle(350,2250,600,300,this.graph)); //red box

		/*
		 * TODO
		this.opponent = new Obstacle(-500,-500,this.opponentSize,this.opponentSize,this.graph);
		this.addObstacle(this.opponent);
		*/
	}

	public void update(){
		/*
		Point current = this.odo.getCurrentPose().getLocation();
		if(!current.equals(this.currentPose.getPosition())){
			this.currentPose.updatePosition(current);
			this.checkPoseLink();
		}
		*/
	}

	/***
	 * Verification, ajout, suppression des liens entre le robot est les differents obstacle
	 */
	public void checkPoseLink(){
		//USBConnexion conn = USBConnexion.getInstance();
		//Add interconnected link
		for(Obstacle ob1: this.obstacles){
			for(Obstacle ob2: this.obstacles){
				if(ob2 != ob1){
					for(Node n1: ob1.getNodes()){
						for(Node n2: ob2.getNodes()){
							boolean intersect = false;
							for(Obstacle ob3: this.obstacles){
								if(ob3.isCrossedBy(n1,n2)){
									intersect = true;
									break;
								}
							}
							//TODO Debug
							if(intersect == true){
								//conn.send(new Error("KO " + n1.getPosition() + " - " + n2.getPosition()));
								//Remove Link
								n1.unlinkNode(n2);
								n2.unlinkNode(n1);
							}else{
								//conn.send(new Error("OK " + n1.getPosition() + " - " + n2.getPosition()));
								//Add Link
								n1.linkNode(n2);
								n2.linkNode(n1);
							}
						}
					}
				}
			}
		}
		//Remove link with inside node
		for(Obstacle ob1: this.obstacles){
			for(Obstacle ob2: this.obstacles){
				if(ob2 != ob1){
					for(Node n1: ob1.getNodes()){
						if(ob2.haveInside(n1)){
							//conn.send(new Error(n1.getPosition() + " is inside"));
							for(Node n2: ob2.getNodes()){
								//Remove Link
								n1.unlinkNode(n2);
								n2.unlinkNode(n1);
							}
						}
					}
				}
			}
		}
	}

	/***
	 * Verification, ajout, suppression des liens entre les obstracles
	 */
	public void checkObstaclesLink(){
		//TODO
	}
}
