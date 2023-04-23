import java.awt.*;
import java.awt.geom.AffineTransform;

public class cop extends LivingEntity{

	private final static int totalHP = 200;
	private final static double maxTurnSpeed = 2;
	private final static double turnAcceleration = .3;
	private final static double acceleration = .055;
	private final static int maxSpeed = 14;
	private final static int copXSize = 76;
	private final static int copYSize = 38;

	private final static int scoreValue = 5;

	private final int stunTime = 60;

	private boolean stun = false;
	private int stunCounter = 0;
	
	private final int deathTime = 120;
	
	private boolean shouldDelete = false;
	private int deadCounter = 0;

	private playerCar player;

	public cop(vector position, int rotation, playerCar car, map gameMap) {
		super(gameMap, position, copXSize, copYSize, totalHP, totalHP, maxTurnSpeed, turnAcceleration, rotation, acceleration, maxSpeed, 10);

		player = car;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		if(!isAlive()) {
			deadCounter++;
		}
		if(deadCounter >= deathTime) {
			shouldDelete = true;
		}
		updateParticleGenerator();
		controlRotation();
		moveCop();
		splitSpeedVector();
		controlSpeed();
		moveHitBox();
	}

	@Override
	public void draw(Graphics2D g2) {

		//prevents rotation or shift of any other drawings after this
		AffineTransform oldPos = g2.getTransform();
		//moves graphics to position of car
		g2.translate(getPosition().getX(),getPosition().getY());
		g2.rotate(Math.toRadians(getRotation()));

		if(isAlive()) {
			g2.setColor(new Color(2, 37, 110));
			g2.fillRect(getTranslatedX(), getTranslatedY(), getxSize(), getySize());
			g2.setColor(Color.WHITE);
			g2.fillRect((int)(getTranslatedX()+getxSize()/4), getTranslatedY(), getxSize()/2, getySize());
			g2.setColor(Color.BLACK);
			g2.fillRect((int)(getTranslatedX()+getxSize()*5/8), getTranslatedY() + getySize()*2/20, getxSize()/8, getySize()*17/20);
			g2.setColor(Color.RED);
			g2.fillRect((int)(getTranslatedX()+getxSize()*6/16), getTranslatedY() + getySize()*2/20, getxSize()/8, getySize()*17/40);
			g2.setColor(new Color(20, 180, 245));
			g2.fillRect((int)(getTranslatedX()+getxSize()*6/16), getTranslatedY() + getySize()*21/40, getxSize()/8, getySize()*17/40);
		}
		else {
			g2.setColor(Color.BLACK);	
			g2.fillRect(getTranslatedX(), getTranslatedY(), getxSize(), getySize());
		}


		//prevents rotation or shift of any other drawings after this
		g2.setTransform(oldPos);
		if(getHP()/getTotalHP() <= .5) {
			if(getHP()/getTotalHP() <= 0) {
				getSmokeLeft().setPause(5);
				getSmokeRight().setPause(5);
			}
			else if(getHP()/getTotalHP() <= .25) {
				getSmokeLeft().setPause(20);
				getSmokeRight().setPause(20);
			}
			if(isAlive()) {
				getSmokeLeft().draw(g2);
				getSmokeRight().draw(g2);
			}
			else {
				getSmokeLeft().drawDead(g2);
				getSmokeRight().drawDead(g2);
			}
		}
	}
	public int angleToCar(){
		int angle = (int)(Math.toDegrees(Math.atan2(player.getPosition().getY()-this.getPosition().getY(),player.getPosition().getX()-this.getPosition().getX())));
		angle = angle%360;
		if(0 > angle) {
			angle = 360 +angle;
		}
		return angle;
	}
	//return -1 turn left return 0 don't turn return 1 right
	public int directionToTurn() {
		//makes angle range from -180 to 180
		int shiftedAngleToCar = angleToCar()- getRotation();
		while(-180 > shiftedAngleToCar){
			shiftedAngleToCar += 360;
		}
		while(180 < shiftedAngleToCar){
			shiftedAngleToCar -= 360;
		}
		if(shiftedAngleToCar > 20) {
			return 1;
		}
		else if(shiftedAngleToCar < -20) {
			return -1;
		}
		else {
			return 0;
		}

	}
	public void moveCop() {
		getSpeedVector().subtract(getKnockBackVector());
		getPositionOnMap().add(getSpeedVector());
		this.setPosition(new vector(getGameMap().getPosition().getX() + getPositionOnMap().getX(), getGameMap().getPosition().getY() + getPositionOnMap().getY()));
	}
	public void controlSpeed() {
		//sets speed back to normal after knockback
		if(-getFriction() > getKnockBackVector().getX()) {
			getKnockBackVector().setX(getKnockBackVector().getX() + getFriction());
		}
		else if(getFriction() < getKnockBackVector().getX()) {
			getKnockBackVector().setX(getKnockBackVector().getX() - getFriction());
		}
		else {
			getKnockBackVector().setX(0);
		}
		if(-getFriction() > getKnockBackVector().getY()) {
			getKnockBackVector().setY(getKnockBackVector().getY() + getFriction());
		}
		else if(getFriction() < getKnockBackVector().getY()) {
			getKnockBackVector().setY(getKnockBackVector().getY() - getFriction());
		}
		else {
			getKnockBackVector().setY(0);
		}
		if(isAlive()) {
			if(!stun) {
				//slows down or accelerates car to half max speed when turning
				if(isTurning()) {
					if(getSpeed() < getMaxSpeed()/2) {
						setSpeed(getSpeed() + getAcceleration());
					}
					else if(getSpeed() > getMaxSpeed()/2) {
						setSpeed(getSpeed() - getAcceleration());
					}
				}
				//if it is not turning or drifting, speeds up to max speed
				else {
					if(getSpeed() < getMaxSpeed()) {
						setSpeed(getSpeed() + getAcceleration());
					}
					else if(getSpeed() > getMaxSpeed()) {
						setSpeed(getMaxSpeed());
					}
				}
			}
			else {
				stunCounter++;
				if(getSpeed() > getAcceleration()) {
					setSpeed(getSpeed() - getAcceleration());
				}
			}
			if(stunCounter >= stunTime) {
				stunCounter = 0;
				stun = false;
			}
		}
		else {
			if(getSpeed() > getAcceleration()) {
				setSpeed(getSpeed() - getAcceleration());
			}
			else if(getSpeed() < -getAcceleration()) {
				setSpeed(getSpeed() + getAcceleration());
			}
			else {
				setSpeed(0);
			}
		}
	}
	public void controlRotation() {
		if(isAlive()) {
			if(directionToTurn() == -1) {
				setTurnAcceleration(-getPossibleTurnAcceleration());
			}
			else if(directionToTurn() == 1) {
				setTurnAcceleration(getPossibleTurnAcceleration());
			}
			else {
				setCurrentTurnSpeed(0);
				setTurnAcceleration(0);
			}

			setRotation(getRotation() + getCurrentTurnSpeed());

			//changes rate of rotation
			if(Math.abs(getCurrentTurnSpeed()) < getMaxTurnSpeed()) {
				setCurrentTurnSpeed(getCurrentTurnSpeed()+getTurnAcceleration());
			}
			else if(getCurrentTurnSpeed() > getMaxTurnSpeed()) {
				setCurrentTurnSpeed(getMaxTurnSpeed());
			}
			else if(getCurrentTurnSpeed() < -getMaxTurnSpeed()) {
				setCurrentTurnSpeed(-getMaxTurnSpeed());
			}
		}
	}

	public double calculateDamage(vector impactVector) {
		return impactVector.getMagnitude();
	}

	public int getScorevalue() {
		return scoreValue;
	}

	public boolean isStun() {
		return stun;
	}

	public void setStun(boolean stun) {
		this.stun = stun;
	}

	public boolean isShouldDelete() {
		return shouldDelete;
	}

	public void setShouldDelete(boolean shouldDelete) {
		this.shouldDelete = shouldDelete;
	}

}
