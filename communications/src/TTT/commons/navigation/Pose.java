package TTT.commons.navigation;

public class Pose {
	private Point location;
	private double heading;
	private long nbTurn;
	private double fullHeading;

	public Pose(double x, double y, double heading){
		this.location = new Point(x,y);
		this.setHeading(heading);
	}

	public Pose(Point p, double heading){
		this(p.getX(), p.getY(), heading);
	}

	public Pose(Pose p){
		this(p.location,p.fullHeading);
	}

	public Pose(Point p){
		this(p,0);
	}

	public Pose(double x, double y){
		this(x,y,0);
	}

	public Pose(){
		this(0,0,0);
	}

	public Point getLocation(){
		return this.location;
	}

	public double getHeading(){
		return this.heading;
	}

	public double getFullHeading(){
		return this.fullHeading;
	}

	public void setLocation(Point p){
		this.setLocation(p.getX(), p.getY());
	}

	public void setLocation(double x, double y){
		this.location.setX(x);
		this.location.setY(y);
	}

	public void setHeading(double data){
		this.fullHeading = data;
		this.nbTurn = Math.round(data)/360;
		this.heading = data%360;
	}

	public void move(float distance, double newHeading){
		this.setHeading(newHeading);
		this.move(distance);
	}

	public void move(float distance){
		if(distance != 0){
			double newX = this.location.getX();
			double newY = this.location.getY();
			double dx = 0;
			double dy = 0;
			double head = this.heading;
			if(head < 0){
				head+=360;
			}
			if(head == 0){
				dx = distance;
				dy = 0;
			} else if(head == 90){
				dx = 0;
				dy = distance;
			} else if(head == 180){
				dx = -distance;
				dy = 0;
			} else if(head == 270){
				dx = 0;
				dy = -distance;
			} else {
				double radian = Math.toRadians(head);
				double cos = Math.cos(radian);
				double sin = Math.sin(radian);
				dx = distance*cos;
				dy = distance*sin;
			}
			newX += dx;
			newY += dy;
			this.setLocation(newX,newY);
		}
	}

	public boolean equals(Pose p){
		return (this.heading == p.heading && this.location.equals(p.location));
	}

	public Pose substract(Pose p){
		return new Pose(this.location.substract(p.location), this.heading - p.heading);
	}

	public double getDistance(){
		return this.location.getDistance();
	}

	public String toString(){
		return this.location.toString() + "," + this.heading + "(" + this.fullHeading + ")";
	}

	public double getAngleTo(Pose p){
		return this.getAngleTo(p.getLocation());
	}

	public double getAngleTo(Point p){
		Point tmp = p.substract(this.getLocation());
		double x = tmp.getX();
		double y = tmp.getY();
		if(x == 0){
			if(y >= 0){
				return 0;
			} else {
				return 180;
			}
		}
		double angle = Math.toDegrees(Math.atan(y/x));
		if(x >= 0){
			if(y >= 0){
				return 90 - angle;
			} else {
				return 90 + angle;
			}
		} else {
			if(y >= 0){
				return -90 + angle;
			} else {
				return -90 - angle;
			}
		}
	}
}
