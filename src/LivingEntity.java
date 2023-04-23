import java.awt.geom.Line2D;
import java.awt.*;


public abstract class LivingEntity extends Entity{

	private final int invincibilityFrames = 5;
	private final double friction = .3;

	private int invincibilityFramesCounter = 0;
	

	private final int translatedX = -getxSize()/2;
	private final int translatedY = -getySize()/2;

	private int rotation, maxSpeed;
	private double HP, totalHP, maxTurnSpeed, currentTurnSpeed, turnAcceleration, acceleration, speed, possibleAcceleration, possibleTurnAcceleration;
	private vector speedVector, knockBackVector, positionOnMap;

	private Line2D.Double[] bounds = new Line2D.Double[4];

	private double distance;
	private double originalAngle;
	
	private boolean alive = true;
	
	private map gameMap;
	
	private ParticleGenerator smokeLeft;
	private ParticleGenerator smokeRight;

	public LivingEntity(map gameMap, vector position, int xSize, int ySize, int HP, int totalHP, double maxTurnSpeed, double turnAcceleration, int rotation, double acceleration, int maxSpeed, double speed) {
		super(position, xSize, ySize);
		
		this.gameMap = gameMap;
		smokeLeft = new ParticleGenerator((int)getPosition().getX(), (int)getPosition().getY(), getRotation(), gameMap);
		smokeRight = new ParticleGenerator((int)getPosition().getX(), (int)getPosition().getY(), getRotation(), gameMap);

		
		this.HP = HP;
		this.totalHP = HP;
		this.maxTurnSpeed = maxTurnSpeed;
		this.turnAcceleration = 0;
		possibleTurnAcceleration = turnAcceleration;
		this.setRotation(rotation);
		this.maxSpeed = maxSpeed;
		speedVector = new vector(Math.cos(Math.toRadians(rotation))*speed, Math.sin(Math.toRadians(rotation))*speed);
		knockBackVector = new vector(0,0);
		this.speed = speed;
		currentTurnSpeed = 0;
		this.acceleration = acceleration;
		possibleAcceleration = acceleration;

		distance = Math.sqrt(Math.pow(getxSize()/2,2) + Math.pow(getySize()/2,2));
		originalAngle = Math.atan((double)getySize()/getxSize());
		bounds[0] = new Line2D.Double(getPosition().getX() + Math.cos(-originalAngle + Math.toRadians(rotation)) * distance, getPosition().getY() + Math.sin(-originalAngle + Math.toRadians(rotation)) * distance,getPosition().getX() + Math.cos(originalAngle + Math.toRadians(rotation)) * distance, getPosition().getY() + Math.sin(originalAngle + Math.toRadians(rotation)) * distance);
		bounds[1] = new Line2D.Double(getPosition().getX() - Math.cos(-originalAngle + Math.toRadians(rotation)) * distance, getPosition().getY() - Math.sin(-originalAngle + Math.toRadians(rotation)) * distance,getPosition().getX() - Math.cos(originalAngle + Math.toRadians(rotation)) * distance, getPosition().getY() - Math.sin(originalAngle + Math.toRadians(rotation)) * distance);
		bounds[2] = new Line2D.Double(getPosition().getX() + Math.cos(-originalAngle + Math.toRadians(rotation)) * distance, getPosition().getY() + Math.sin(-originalAngle + Math.toRadians(rotation)) * distance,getPosition().getX() - Math.cos(originalAngle + Math.toRadians(rotation)) * distance, getPosition().getY() - Math.sin(originalAngle + Math.toRadians(rotation)) * distance);
		bounds[3] = new Line2D.Double(getPosition().getX() + Math.cos(originalAngle + Math.toRadians(rotation)) * distance, getPosition().getY() + Math.sin(originalAngle + Math.toRadians(rotation)) * distance,getPosition().getX() - Math.cos(-originalAngle + Math.toRadians(rotation)) * distance, getPosition().getY() - Math.sin(-originalAngle + Math.toRadians(rotation)) * distance);

		
		positionOnMap = positionOnMap();
	}

	//update position, speed, etc.
	public abstract void update();
	public abstract double calculateDamage(vector impactVector);

	public void drawHealthBar(Graphics2D g) {
		g.setColor(Color.RED);
		g.fillRect(750,10, (int)(getHP()/getTotalHP()*200),20);
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(5));
		g.drawRect(750,10, 200,20);
	}
	public void moveHitBox() {
		bounds[0] = new Line2D.Double(getPosition().getX() + Math.cos(-originalAngle + Math.toRadians(rotation)) * distance, getPosition().getY() + Math.sin(-originalAngle + Math.toRadians(rotation)) * distance,getPosition().getX() + Math.cos(originalAngle + Math.toRadians(rotation)) * distance, getPosition().getY() + Math.sin(originalAngle + Math.toRadians(rotation)) * distance);
		bounds[1] = new Line2D.Double(getPosition().getX() - Math.cos(-originalAngle + Math.toRadians(rotation)) * distance, getPosition().getY() - Math.sin(-originalAngle + Math.toRadians(rotation)) * distance,getPosition().getX() - Math.cos(originalAngle + Math.toRadians(rotation)) * distance, getPosition().getY() - Math.sin(originalAngle + Math.toRadians(rotation)) * distance);
		bounds[2] = new Line2D.Double(getPosition().getX() + Math.cos(-originalAngle + Math.toRadians(rotation)) * distance, getPosition().getY() + Math.sin(-originalAngle + Math.toRadians(rotation)) * distance,getPosition().getX() - Math.cos(originalAngle + Math.toRadians(rotation)) * distance, getPosition().getY() - Math.sin(originalAngle + Math.toRadians(rotation)) * distance);
		bounds[3] = new Line2D.Double(getPosition().getX() + Math.cos(originalAngle + Math.toRadians(rotation)) * distance, getPosition().getY() + Math.sin(originalAngle + Math.toRadians(rotation)) * distance,getPosition().getX() - Math.cos(-originalAngle + Math.toRadians(rotation)) * distance, getPosition().getY() - Math.sin(-originalAngle + Math.toRadians(rotation)) * distance);

	}
	public void splitSpeedVector() {
		speedVector.setX(Math.cos(Math.toRadians(getRotation()))*getSpeed());
		speedVector.setY(Math.sin(Math.toRadians(getRotation()))*getSpeed());
	}
	public boolean isTurning() {
		if(Math.abs(currentTurnSpeed) > 0) {
			return true;
		}
		else {
			return false;
		}
	}
	public boolean collides(Entity x) {
		//checks for collision with cops
		if(invincibilityFramesCounter == 0) {
			if(x instanceof LivingEntity) {
				for(int i = 0; i < ((LivingEntity)x).getBounds().length; i++) {
					for(int j = 0; j < this.bounds.length; j++) {
						if(((LivingEntity)x).getBounds()[i].intersectsLine(this.bounds[j])) {
							return true;
						}
					}
				}
			}
			//checks for collision with obstacles
			else {
				for(int i = 0; i < this.bounds.length; i++) {
					if(this.bounds[i].intersects(((NonLivingEntity)x).getHitBox())) {
						return true;
					}
				}
			}
		}
		else {
			invincibilityFramesCounter++;
		}
		if(invincibilityFramesCounter >= invincibilityFrames) {
			invincibilityFramesCounter = 0;
		}
		return false;

	}
	public void knockBack(LivingEntity car2) {
		double differenceInXVelocity = this.getSpeedVector().getX() - car2.getSpeedVector().getX();
		double differenceInYVelocity = this.getSpeedVector().getY() - car2.getSpeedVector().getY();
		knockBackVector = new vector(differenceInXVelocity, differenceInYVelocity);
		this.setHP(this.getHP() - this.calculateDamage(knockBackVector));
		car2.setHP(car2.getHP() - car2.calculateDamage(knockBackVector));
		knockBackVector.divide(1.5);
		car2.setKnockBackVector(knockBackVector.negativeVector());
		invincibilityFramesCounter++;
		car2.setInvincibilityFramesCounter(1);
		if(this instanceof playerCar) {
			((cop)car2).setStun(true);
		}
	}
	public void updateParticleGenerator() {
		getSmokeLeft().setX((int)(getPosition().getX() + Math.cos(Math.toRadians(getRotation()))*3/8*getxSize()));
		getSmokeLeft().setY((int)(getPosition().getY() + Math.sin(Math.toRadians(getRotation()))*3/8*getxSize()));
		getSmokeLeft().setRotation(getRotation()-90);
		
		getSmokeRight().setX((int)(getPosition().getX() + Math.cos(Math.toRadians(getRotation()))*3/8*getxSize()));
		getSmokeRight().setY((int)(getPosition().getY() + Math.sin(Math.toRadians(getRotation()))*3/8*getxSize()));
		getSmokeRight().setRotation(90+getRotation());
	}
	public vector positionOnMap() {
		return new vector((int)(getPosition().getX() - getGameMap().getPosition().getX()), (int)(getPosition().getY() - getGameMap().getPosition().getY()));
	}
	public double getHP() {
		return HP;
	}
	public void setHP(double hP) {
		HP = hP;
	}
	public double getTotalHP() {
		return totalHP;
	}
	public double getCurrentTurnSpeed() {
		return currentTurnSpeed;
	}
	public void setCurrentTurnSpeed(double currentTurnSpeed) {
		this.currentTurnSpeed = currentTurnSpeed;
	}
	public double getSpeed() {
		return speed;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	public double getMaxTurnSpeed() {
		return maxTurnSpeed;
	}
	public void setMaxTurnSpeed(double maxTurnSpeed) {
		this.maxTurnSpeed = maxTurnSpeed;
	}
	public double getTurnAcceleration() {
		return turnAcceleration;
	}
	public void setTurnAcceleration(double turnAcceleration) {
		this.turnAcceleration = turnAcceleration;
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
	public double getAcceleration() {
		return acceleration;
	}
	public void setAcceleration(double acceleration) {
		this.acceleration = acceleration;
	}
	public int getMaxSpeed() {
		return maxSpeed;
	}
	public void setMaxSpeed(int maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public vector getSpeedVector() {
		return speedVector;
	}

	public void setSpeedVector(vector speedVector) {
		this.speedVector = speedVector;
	}

	public double getPossibleAcceleration() {
		return possibleAcceleration;
	}
	public double getPossibleTurnAcceleration() {
		return possibleTurnAcceleration;
	}
	public Line2D.Double[] getBounds() {
		return bounds;
	}

	public vector getKnockBackVector() {
		return knockBackVector;
	}

	public void setKnockBackVector(vector knockBackVector) {
		this.knockBackVector = knockBackVector;
	}

	public int getInvincibilityFramesCounter() {
		return invincibilityFramesCounter;
	}

	public void setInvincibilityFramesCounter(int invincibilityFramesCounter) {
		this.invincibilityFramesCounter = invincibilityFramesCounter;
	}

	public double getFriction() {
		return friction;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public ParticleGenerator getSmokeLeft() {
		return smokeLeft;
	}

	public void setSmokeLeft(ParticleGenerator smokeLeft) {
		this.smokeLeft = smokeLeft;
	}

	public ParticleGenerator getSmokeRight() {
		return smokeRight;
	}

	public void setSmokeRight(ParticleGenerator smokeRight) {
		this.smokeRight = smokeRight;
	}

	public map getGameMap() {
		return gameMap;
	}

	public void setGameMap(map gameMap) {
		this.gameMap = gameMap;
	}

	public vector getPositionOnMap() {
		return positionOnMap;
	}

	public void setPositionOnMap(vector positionOnMap) {
		this.positionOnMap = positionOnMap;
	}

	public int getTranslatedX() {
		return translatedX;
	}

	public int getTranslatedY() {
		return translatedY;
	}

	
}
