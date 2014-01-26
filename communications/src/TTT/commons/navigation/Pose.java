package TTT.commons.navigation;

public class Pose {
	private Point location;
	private int heading;

	public Pose(int x, int y, int heading){
		this.location = new Point(x,y);
		this.setHeading(heading);
	}

	public Pose(Point p, int heading){
		this(p.getX(), p.getY(), heading);
	}

	public Pose(Pose p){
		this(p.location,p.heading);
	}

	public Pose(Point p){
		this(p,0);
	}

	public Pose(int x, int y){
		this(x,y,0);
	}

	public Pose(){
		this(0,0,0);
	}

	public Point getLocation(){
		return this.location;
	}

	public int getHeading(){
		return this.heading;
	}

	public void setLocation(Point p){
		this.setLocation(p.getX(), p.getY());
	}

	public void setLocation(int x, int y){
		this.location.setX(x);
		this.location.setY(y);
	}

	public void setHeading(int heading){
		heading %= 360;
		if(heading < 0){
			heading += 360;
		}
		this.heading = heading;
	}

	public void move(float distance, int newHeading){
		this.setHeading(newHeading);
		this.move(distance);
	}

	public void move(float distance){
		if(distance != 0){
			int newX = this.location.getX();
			int newY = this.location.getY();
			double dx = 0;
			double dy = 0;
			if(this.heading == 0){
				dx = 0;
				dy = distance;
			} else if(this.heading == 90){
				dx = distance;
				dy = 0;
			} else if(this.heading == 180){
				dx = 0;
				dy = -distance;
			} else if(this.heading == 270){
				dx = -distance;
				dy = 0;
			} else {
				double radian = Math.toRadians(this.heading);
				double cos = Math.cos(radian);
				double sin = Math.sin(radian);
				dx = distance*sin;
				dy = distance*cos;
			}
			newX += Math.round(dx);
			newY += Math.round(dy);
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
		return this.location.toString() + "," + this.heading;
	}

	public int getAngleTo(Pose p){
		return this.getAngleTo(p.getLocation());
	}

	public int getAngleTo(Point p){
		Point tmp = p.substract(this.getLocation());
		int x = tmp.getX();
		int y = tmp.getY();
		if(x == 0){
			if(y >= 0){
				return 0;
			} else {
				return 180;
			}
		}
		int angle = (int)Math.round(Math.toDegrees(Math.atan(y/x)));
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
