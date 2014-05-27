package TTT.libNXT.navigation.pathFinding;

import java.util.ArrayList;

import TTT.commons.navigation.Point;
import TTT.commons.navigation.Pose;

import TTT.libNXT.configuration.ConfigListener;
import TTT.libNXT.configuration.Configurateur;

import TTT.libNXT.navigation.BasicOdometry;

import TTT.libNXT.task.OpponentListener;
import TTT.libNXT.task.Direction;

public class Ground implements ConfigListener, OpponentListener {
	private int groundLength;
	private int groundWidth;
	private int botSize;

	private ArrayList<Obstacle> obstacles;
	private Obstacle opponent;

	private Graph graph;

	private Node currentPose;
	private Node target;
	private BasicOdometry odo;

	public Ground(BasicOdometry odo){
		super();

		Configurateur conf = Configurateur.getInstance();

		this.groundLength	= Integer.parseInt(conf.get("ground.groundLength","3000")); 
		this.groundWidth	= Integer.parseInt(conf.get("ground.groundWidth","2000"));
		this.botSize		= Integer.parseInt(conf.get("ground.botSize","200"));

		conf.addConfigListener(this,"ground");

		this.odo = odo;

		this.obstacles = new ArrayList<Obstacle>();
		this.graph = new Graph();

		this.currentPose = new Node(this.odo.getCurrentPose().getLocation());
		this.graph.addNode(this.currentPose);

		this.opponent = new Obstacle(-500,-500,this.botSize,this.botSize,this.graph);

		this.target = new Node(new Point(-500,-500));
		this.graph.addNode(this.target);

		this.initObstacles();
		this.update();
	}

	@Override
	public void opponentFound(Direction dir){
		Pose p = this.odo.getCurrentPose();
		if(dir == Direction.FRONT){
			p.move(this.botSize);
		}else{
			p.move(-this.botSize);
		}
		this.opponent.moveCenter(p.getLocation());
	}

	@Override
	public void configChanged(String key, String value){
		if(key.equals("ground.groundLength")){
			this.groundLength = Integer.parseInt(value);
		}else if(key.equals("ground.groundWidth")){
			this.groundWidth = Integer.parseInt(value);
		}else if(key.equals("ground.botSize")){
			this.botSize = Integer.parseInt(value);
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
		this.addObstacle(new Obstacle(150,750,300+this.botSize,700+this.botSize,this.graph)); //yellow box
		this.addObstacle(new Obstacle(150,2250,300+this.botSize,700+this.botSize,this.graph)); //red box

		this.addObstacle(this.opponent);
	}

	public void update(){
		this.checkPoseLink();
		this.checkObstaclesLink();
	}

	public ArrayList<Pose> getPathTo(Pose p){
		if(p == null){
			return null;
		}
		this.target.updatePosition(p.getLocation());
		this.linkTargetToPose();
		this.linkANode(this.target);
		ArrayList<Node> path = this.graph.getPath(this.currentPose,this.target);
		if(path == null){
			return null;
		}
		path.remove(path.size()-1);//Remove the last one (target)
		ArrayList<Pose> res = new ArrayList<Pose>();
		for(Node n: path){
			res.add(new Pose(n.getPosition()));
		}
		res.add(p); //Add the target (with orientation)
		return res;
	}

	public void linkTargetToPose(){
		boolean intersect = false;
		for(Obstacle ob: this.obstacles){
			if(ob.isCrossedBy(this.currentPose, this.target)){
				intersect = true;
				break;
			}
		}
		if(intersect == true){
			this.currentPose.unlinkNode(this.target);
			this.target.unlinkNode(this.currentPose);
		}else{
			this.currentPose.linkNode(this.target);
			this.target.linkNode(this.currentPose);
		}
	}

	public void linkANode(Node n){
		Point p = n.getPosition();
		boolean intersect;
		for(Obstacle ob1: this.obstacles){
			if((!ob1.getNodes().contains(n) && ob1.haveInside(n)) || p.getX() < 0 || p.getX() > this.groundWidth || p.getY() < 0 || p.getY() > this.groundLength){
				for(Node n2: this.graph.getNodes()){
					n2.unlinkNode(n);
					n.unlinkNode(n2);
				}
			}else{
				for(Node n2: ob1.getNodes()){
					if(n2 != n){
						p = n2.getPosition();
						if(p.getX() < 0 || p.getX() > this.groundWidth || p.getY() < 0 || p.getY() > this.groundLength){
							intersect = true;
						}else{
							intersect = false;
							for(Obstacle ob2: this.obstacles){
								if(ob2.isCrossedBy(n,n2)){
									intersect = true;
									break;
								}
							}
						}
						if(intersect == false){
							intersect = (ob1.getNodes().contains(n) && !ob1.isSide(n,n2));
						}

						if(intersect == true){
							n2.unlinkNode(n);
							n.unlinkNode(n2);
						}else{
							n2.linkNode(n);
							n.linkNode(n2);
						}
					}
				}
			}
		}
	}

	/***
	 * Verification, ajout, suppression des liens entre le robot est les differents obstacle
	 */
	public void checkPoseLink(){
		this.currentPose.updatePosition(this.odo.getCurrentPose().getLocation());
		this.linkANode(this.currentPose);
	}

	/***
	 * Verification, ajout, suppression des liens entre les obstracles
	 */
	public void checkObstaclesLink(){
		for(Obstacle ob: this.obstacles){
			for(Node n: ob.getNodes()){
				this.linkANode(n);
			}
		}
	}
}
