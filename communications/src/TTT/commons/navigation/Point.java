package TTT.commons.navigation;

public class Point {
	private int x;
	private int y;

	public Point(int x, int y){
		this.x = x;
		this.y = y;
	}

	public Point(){
		this(0,0);
	}

	public Point(Point p){
		this(p.x,p.y);
	}

	public int getX(){
		return this.x;
	}

	public int getY(){
		return this.y;
	}

	public void setX(int x){
		this.x = x;
	}

	public void setY(int y){
		this.y = y;
	}

	public boolean equals(Point p){
		return (this.x == p.x && this.y == p.y);
	}

	public Point substract(Point p){
		return new Point(this.x-p.x,this.y-p.y);
	}

	public double getDistance(){
		return Math.sqrt((this.x*this.x) + (this.y*this.y));
	}

	public String toString(){
		return this.x + "," + this.y;
	}
}
