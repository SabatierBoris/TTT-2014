package TTT.libNXT.navigation.pathFinding;

import java.util.ArrayList;

public class Graph{
	private ArrayList<Node> nodes;

	public Graph(){
		this.nodes = new ArrayList<Node>();
	}

	public void addNode(Node node){
		this.nodes.add(node);
	}

	public void remove(Node node){
		//TODO
		//remove Node
		//Remove link
	}

	public ArrayList<Node> getNodes(){
		return this.nodes;
	}

	public ArrayList<Node> getPath(Node start, Node target){
		ArrayList<Node> notSeen = new ArrayList<Node>();
		ArrayList<Node> path = new ArrayList<Node>();
		Node nodeWork;
		Integer tmp_i, tmp_i_2,dist;
		if(start == null || target == null || !this.nodes.contains(start) || !this.nodes.contains(target)){
			//param error
			return null;
		}
		//Init nodes
		for(Node node: this.nodes){
			node.resetPathVariables();
			notSeen.add(node);
		}

		start.setTravel(0);
		while(!notSeen.isEmpty()){
			//Get the nodeWork
			nodeWork = null;
			for(Node node: notSeen){
				tmp_i = node.getTravel();
				if(tmp_i != null && (nodeWork == null || nodeWork.getTravel() > tmp_i)){
					nodeWork = node;
				}
			}
			if(nodeWork == null){
				nodeWork = notSeen.get(0);
			}
			notSeen.remove(nodeWork);

			tmp_i = nodeWork.getTravel();
			if(tmp_i != null){
				for(Node node: nodeWork.getLinkedNodes()){
					tmp_i_2 = node.getTravel();
					dist = nodeWork.getDistance(node);
					if(tmp_i_2 == null || tmp_i_2 > tmp_i + dist){
						node.setTravel(tmp_i + dist);
						node.setPrevious(nodeWork);
					}
				}
			}
		}
		if(target.getPrevious() == null){
			//Can't reach target from start
			return null;
		}
		
		nodeWork = target;
		while(nodeWork != start){
			path.add(0,nodeWork);
			nodeWork = nodeWork.getPrevious();
		}
		path.add(0,start);
		return path;
	}
}
