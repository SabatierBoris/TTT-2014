package TTT.libNXT.navigation.pathFinding;

import java.util.ArrayList;

import TTT.commons.navigation.Point;

//TODO Remove
import TTT.libNXT.communication.USBConnexion;
import TTT.commons.communication.Error;

public class Obstacle {
	private ArrayList<Node> nodes;
	private Node n1;
	private Node n2;
	private Node n3;
	private Node n4;

	private int xSize;
	private int ySize;


	public Obstacle(int x, int y, int xSize, int ySize, Graph graph){
		this.xSize = xSize;
		this.ySize = ySize;

		this.n1 = new Node(new Point(x+xSize/2,y+ySize/2));
		this.n2 = new Node(new Point(x+xSize/2,y-ySize/2));
		this.n3 = new Node(new Point(x-xSize/2,y-ySize/2));
		this.n4 = new Node(new Point(x-xSize/2,y+ySize/2));

		graph.addNode(this.n1);
		graph.addNode(this.n2);
		graph.addNode(this.n3);
		graph.addNode(this.n4);

		this.n1.linkNode(this.n2);
		this.n2.linkNode(this.n1);

		this.n2.linkNode(this.n3);
		this.n3.linkNode(this.n2);

		this.n3.linkNode(this.n4);
		this.n4.linkNode(this.n3);

		this.n4.linkNode(this.n1);
		this.n1.linkNode(this.n4);

		this.nodes = new ArrayList<Node>();
		this.nodes.add(this.n1);
		this.nodes.add(this.n2);
		this.nodes.add(this.n3);
		this.nodes.add(this.n4);
	}

	public void moveCenter(Point center){
		this.moveCenter(center.getX(),center.getY());
	}

	public void moveCenter(double x, double y){
		this.n1.updatePosition(new Point(x+this.xSize/2,y+this.ySize/2));
		this.n2.updatePosition(new Point(x+this.xSize/2,y-this.ySize/2));
		this.n3.updatePosition(new Point(x-this.xSize/2,y-this.ySize/2));
		this.n4.updatePosition(new Point(x-this.xSize/2,y+this.ySize/2));
	}

	public boolean isSide(Node nA, Node nB){
		return ((nA == this.n1 && nB == this.n2) ||
			    (nA == this.n2 && nB == this.n3) ||
			    (nA == this.n3 && nB == this.n4) ||
			    (nA == this.n4 && nB == this.n1) ||
			    (nA == this.n1 && nB == this.n4) ||
			    (nA == this.n4 && nB == this.n3) ||
			    (nA == this.n3 && nB == this.n2) ||
			    (nA == this.n2 && nB == this.n1));
	}
	public ArrayList<Node> getNodes(){
		return this.nodes;
	}

	public boolean haveInside(Node n){
		Point p = n.getPosition();
		Point A,B;
		Point[] points = {this.n1.getPosition(),
					 this.n2.getPosition(),
					 this.n3.getPosition(),
					 this.n4.getPosition()};
		int len = points.length;
		for(int i=0;i<len;i++){
			A =  points[i];
			if(i == len-1){
				B = points[0];
			}else{
				B = points[i+1];
			}
			double d_x = B.getX() - A.getX();
			double d_y = B.getY() - A.getY();
			double t_x = p.getX() - A.getX();
			double t_y = p.getY() - A.getY();
			if((d_x*t_y - d_y*t_x)>0){
				return false;
			}
		}
		return true;
	}

	public boolean isCrossedBy(Node start, Node end){
		Point pStart = start.getPosition();
		Point pEnd = end.getPosition();
		Point[] i = {this.intersect(start,end,this.n1,this.n2),
					 this.intersect(start,end,this.n2,this.n3),
					 this.intersect(start,end,this.n3,this.n4),
					 this.intersect(start,end,this.n4,this.n1)};

		//TODO Debug
		/*
		USBConnexion conn = USBConnexion.getInstance();
		conn.send(new Error("+++++++++++++++++++"));
		conn.send(new Error("<" + this + "> " + this.n1.getPosition() + " - " + this.n2.getPosition() + " = " + i[0]));
		conn.send(new Error("<" + this + "> " + this.n2.getPosition() + " - " + this.n3.getPosition() + " = " + i[1]));
		conn.send(new Error("<" + this + "> " + this.n3.getPosition() + " - " + this.n4.getPosition() + " = " + i[2]));
		conn.send(new Error("<" + this + "> " + this.n4.getPosition() + " - " + this.n1.getPosition() + " = " + i[3]));
		conn.send(new Error("+++++++++++++++++++"));
		*/

		for(Point p: i){
			//if(p != null && p.substract(start).getDistance() > 1 && p.substract(end).getDistance() > 1){
			if(p != null && !p.equals(pStart) && !p.equals(pEnd)){
				return true;
			}
		}

		/*
		if(i2 != null && !i2.equals(start) && !i2.equals(end)){
			return true;
		}
		if(i3 != null && !i3.equals(start) && !i3.equals(end)){
			return true;
		}
		if(i4 != null && !i4.equals(start) && !i4.equals(end)){
			return true;
		}
		*/
		return false;
/*

		if(this.intersect(start,end,this.n1,this.n2) || this.intersect(start,end,this.n2,this.n3) || this.intersect(start,end,this.n3,this.n4) || this.intersect(start,end,this.n4,this.n1)){
			return true;
		}
		return false;
*/
	}

	public Point intersect(Node n0, Node n1, Node n2, Node n3){
		Point p0 = n0.getPosition();
		Point p1 = n1.getPosition();
		Point p2 = n2.getPosition();
		Point p3 = n3.getPosition();

		double s10_x = p1.getX() - p0.getX();
		double s10_y = p1.getY() - p0.getY();

		double s32_x = p3.getX() - p2.getX();
		double s32_y = p3.getY() - p2.getY();

		double denom = (s10_x * s32_y) - (s32_x * s10_y);

		if(denom == 0){
			return null;
		}

		boolean denom_is_positive = (denom > 0);

		double s02_x = p0.getX() - p2.getX();
		double s02_y = p0.getY() - p2.getY();

		double s_numer = (s10_x*s02_y)-(s02_x*s10_y);

		if((s_numer < 0) == denom_is_positive){
			return null;
		}

		double t_numer = (s32_x*s02_y)-(s02_x*s32_y);

		if((t_numer < 0) == denom_is_positive){
			return null;
		}

		if(((s_numer > denom) == denom_is_positive) || ((t_numer > denom) == denom_is_positive)){
			return null;
		}

		double t = t_numer / denom;

		return new Point(p0.getX() + t*s10_x,p0.getY() + t*s10_y);
	}
}
