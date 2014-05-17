package TTT.libNXT.navigation.pathFinding;

import java.util.ArrayList;

import TTT.commons.navigation.Point;

public class Obstacle {
	private ArrayList<Node> nodes;
	private Node n1;
	private Node n2;
	private Node n3;
	private Node n4;

	public Obstacle(int x, int y, int xSize, int ySize, Graph graph){
		this.n1 = new Node(new Point(x+xSize/2,y+ySize/2));
		this.n2 = new Node(new Point(x+xSize/2,y-ySize/2));
		this.n3 = new Node(new Point(x-xSize/2,y-ySize/2));
		this.n4 = new Node(new Point(x-xSize/2,y+ySize/2));

		graph.addNode(this.n1);
		graph.addNode(this.n2);
		graph.addNode(this.n3);
		graph.addNode(this.n4);

		/*
		this.n1.linkNode(this.n2);
		this.n2.linkNode(this.n1);

		this.n2.linkNode(this.n3);
		this.n3.linkNode(this.n2);

		this.n3.linkNode(this.n4);
		this.n4.linkNode(this.n3);

		this.n4.linkNode(this.n1);
		this.n1.linkNode(this.n4);
		*/

		this.nodes = new ArrayList<Node>();
		this.nodes.add(this.n1);
		this.nodes.add(this.n2);
		this.nodes.add(this.n3);
		this.nodes.add(this.n4);
	}

	public ArrayList<Node> getNodes(){
		return this.nodes;
	}

	public boolean isCrossedBy(Node start, Node end){
		if(this.intersect(start,end,this.n1,this.n2) || this.intersect(start,end,this.n2,this.n3) || this.intersect(start,end,this.n3,this.n4) || this.intersect(start,end,this.n4,this.n1)){
			return true;
		}
		return false;
	}

	public boolean intersect(Node n0, Node n1, Node n2, Node n3){
		Point p0 = n0.getPosition();
		Point p1 = n1.getPosition();
		Point p2 = n2.getPosition();
		Point p3 = n3.getPosition();

		long s10_x = Math.round(p1.getX() - p0.getX());
		long s10_y = Math.round(p1.getY() - p0.getY());

		long s32_x = Math.round(p3.getX() - p2.getX());
		long s32_y = Math.round(p3.getY() - p2.getY());

		long denom = (s10_x * s32_y) - (s32_x * s10_y);

		if(denom == 0){
			return false;
		}

		boolean denom_is_positive = (denom > 0);

		long s02_x = Math.round(p0.getX() - p2.getX());
		long s02_y = Math.round(p0.getY() - p2.getY());

		long s_numer = (s10_x*s02_y)-(s02_x*s10_y);

		if((s_numer < 0) == denom_is_positive){
			return false;
		}

		long t_numer = (s32_x*s02_y)-(s02_x*s32_y);

		if((t_numer < 0) == denom_is_positive){
			return false;
		}

		if(((s_numer > denom) == denom_is_positive) || ((t_numer > denom) == denom_is_positive)){
			return false;
		}

		return true;
	}
}
