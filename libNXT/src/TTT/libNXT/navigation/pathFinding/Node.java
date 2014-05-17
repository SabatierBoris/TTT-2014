package TTT.libNXT.navigation.pathFinding;

import java.util.ArrayList;

import TTT.commons.navigation.Point;

public class Node {
	private Point point;
	private ArrayList<Node> nodesLinked;
	private Integer travel;
	private Node previous;

	public Node(Point point){
		this.point = new Point(point);
		this.nodesLinked = new ArrayList<Node>();
		this.travel = null;
		this.previous = null;
	}

	public void resetPathVariables(){
		this.travel = null;
		this.previous = null;
	}

	public void updatePosition(Point p){
		this.point = new Point(p);
	}

	public Point getPosition(){
		return this.point;
	}

	public void unlinkNode(Node node){
		this.nodesLinked.remove(node);
	}

	public void linkNode(Node node){
		this.nodesLinked.add(node);
	}

	public void setTravel(Integer value){
		this.travel = value;
	}

	public Integer getTravel(){
		return this.travel;
	}

	public void setPrevious(Node previous){
		this.previous = previous;
	}

	public Node getPrevious(){
		return this.previous;
	}

	public ArrayList<Node> getLinkedNodes(){
		return this.nodesLinked;
	}

	public Integer getDistance(Node node){
		return Integer.valueOf((int)Math.round(this.point.substract(node.point).getDistance()));
	}
}
