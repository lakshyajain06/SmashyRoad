import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.*;

public class Particle {
	private double speed = 3;
	private final double deceleration = .1;

	private double angularVelocity = 7;
	private final double angVelDec = .1;

	private double size = 7;
	private final double sizeIncrease = 1;

	private final int transparencyDec = 7;
	private int transparency = 255;

	private int x, y, forceRotation, rotation, colorNum;
	private vector speedV, position;

	private Rectangle2D.Double body = new Rectangle2D.Double(-size/2,-size/2, size, size);

	private map gameMap;


	public Particle(int x, int y, int rotation, map gameMap, int colorNum) {
		this.gameMap = gameMap;
		this.x = x;
		this.y = y;
		
		position = positionOnMap();
		
		this.forceRotation = rotation;
		this.rotation = rotation;
		speedV = new vector(Math.cos(Math.toRadians(forceRotation))*speed, Math.sin(Math.toRadians(forceRotation))*speed);
		
		this.colorNum = colorNum;
	}

	public void update() {

		controlSpeed();
		controlRotation();
		splitSpeedVector();
		move();


		size+= sizeIncrease;
		body = new Rectangle2D.Double(-size/2,-size/2, size, size);
		
		makeTransparent();
	}

	public int getRotation() {
		return rotation;
	}
	public void setRotation(double rotation) {
		rotation = rotation%360;
		if(0 > rotation) {
			rotation = 360 +rotation;
		}
		this.rotation = (int)rotation;
	}
	public void splitSpeedVector() {
		speedV.setX(Math.cos(Math.toRadians(forceRotation))*speed);
		speedV.setY(Math.sin(Math.toRadians(forceRotation))*speed);
	}
	public void controlSpeed() {
		if(speed > deceleration) {
			speed-=deceleration;
		}
		else if(speed < -deceleration) {
			speed+=deceleration;
		}
		else {
			speed = 0;
		}
	}
	public void controlRotation() {
		if(angularVelocity > angVelDec) {
			angularVelocity-=angVelDec;
		}
		else if(angularVelocity < -angVelDec) {
			angularVelocity+=angVelDec;
		}
		else {
			angularVelocity = 0;
		}
		rotation+=angularVelocity;
	}
	public void move() {
		position.add(speedV);
		x = (int)(gameMap.getPosition().getX() + position.getX());
		y = (int)(gameMap.getPosition().getY() + position.getY());
	}
	public void makeTransparent() {
		if(transparency >0) {
			transparency -= transparencyDec;
		}
	}
	public void draw(Graphics2D g) {
		AffineTransform oldPos = g.getTransform();
		//moves graphics to position of car
		g.translate(x,y);
		g.rotate(Math.toRadians(getRotation()));
		g.setColor(new Color(61, 61, 61, transparency));
		g.fill(body);

		g.setTransform(oldPos);
	}
	public void drawDead(Graphics2D g) {
		AffineTransform oldPos = g.getTransform();
		//moves graphics to position of car
		g.translate(x,y);
		g.rotate(Math.toRadians(getRotation()));
		if(colorNum == 0) {
			g.setColor(new Color(222, 51, 24, transparency));
		}
		else if(colorNum == 1) {
			g.setColor(new Color(237, 126, 36, transparency));
		}
		else if(colorNum == 2) {
			g.setColor(new Color(237, 195, 57, transparency));
		}
		else {
			g.setColor(new Color(61, 61, 61, transparency));
		}
		g.fill(body);

		g.setTransform(oldPos);
	}

	public int getTransparency() {
		return transparency;
	}

	public void setTransparency(int transparency) {
		this.transparency = transparency;
	}
	public vector positionOnMap() {
		return new vector((x - gameMap.getPosition().getX()), y - gameMap.getPosition().getY());
	}
}
