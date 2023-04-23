
public abstract class playerCar extends LivingEntity{

	//the speed that holding drift will bring you down to
	private final int driftingMaxSpeed = 3;
	//the magnitude of the sliding that happens due to the drift
	private final int amountOfDriftSlide = -5;

	//used to center drawing of car

	private boolean alreadyDrifting = false;
	private boolean switchToDrifting = true;

	public playerCar(map x, vector position, int xSize, int ySize, int HP, int totalHP, double maxTurnSpeed, double turnAcceleration, int rotation, double acceleration,int maxSpeed, double speed) {
		super(x, position, xSize, ySize, HP, totalHP, maxTurnSpeed, turnAcceleration, rotation, acceleration,maxSpeed, speed);
	}

	//updates position,speed, rotation, etc.
	public void update() {
		updateParticleGenerator();
		controlRotation();
		movePlayer();
		splitSpeedVector();
		controlSpeed();
		shiftRotationPointForDrifting();
		moveHitBox();
	}

	//shifts center of rotation of car
	public void shiftRotationPointForDrifting() {
		if(isDrifting()) {
			alreadyDrifting = true;
			addDriftSlide();
		}
		else {
			if(!switchToDrifting) {
				switchToDrifting = true;
			}
			alreadyDrifting = false;
		}
	}

	//checks for drift
	public boolean isDrifting() {
		if(Math.abs(getCurrentTurnSpeed()) > (int)(getMaxTurnSpeed()*7/8) && (Math.abs(getSpeed()) > ((int)(getMaxSpeed()*3/4))|| alreadyDrifting)) {
			return true;
		}
		return false;
	}
	//adds slide perpendicular to the car when drifting
	public void addDriftSlide() {
		if(getCurrentTurnSpeed() > 0) {
			getGameMap().setPosition(new vector(getGameMap().getPosition().getX() - Math.cos(Math.toRadians(getRotation() + 90))*amountOfDriftSlide, 
					getGameMap().getPosition().getY() - Math.sin(Math.toRadians(getRotation() + 90))*amountOfDriftSlide));
		}
		else {
			getGameMap().setPosition(new vector(getGameMap().getPosition().getX() - Math.cos(Math.toRadians(getRotation() - 90))*amountOfDriftSlide, 
					getGameMap().getPosition().getY() - Math.sin(Math.toRadians(getRotation() - 90))*amountOfDriftSlide));
		}
	}
	public void controlRotation() {
		if(isAlive()) {
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
			//slows down car while drifting to the max drifting speed
			if(getAcceleration() >=0) {
				if(isDrifting()) {
					if(getSpeed() < driftingMaxSpeed) {
						setSpeed(getSpeed() + 3*getAcceleration());
					}
					else if(getSpeed() > driftingMaxSpeed) {
						setSpeed(getSpeed() - 3*getAcceleration());
					}
				}
				//slows down car while turning to 
				else if(isTurning()) {
					if(getSpeed() < getMaxSpeed()/2) {
						setSpeed(getSpeed() + getAcceleration());
					}
					else if(getSpeed() > getMaxSpeed()/2) {
						setSpeed(getSpeed() - getAcceleration());
					}
				}
				//if it is nor turning or drifting, speeds up to max speed
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
				if(getSpeed() > -getMaxSpeed()/2) {
					setSpeed(getSpeed()+getAcceleration());
				}
				else if(getSpeed() < -getMaxSpeed()/2) {
					setSpeed(-getMaxSpeed()/2);
				}
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
	public void movePlayer() {
		//makes car seem like its moving(moves background)
		getSpeedVector().subtract(getKnockBackVector());
		getGameMap().getPosition().subtract(getSpeedVector());

		setPositionOnMap(positionOnMap());
	}
	public double calculateDamage(vector impactVector) {
		return impactVector.getMagnitude();
	}	
	public boolean isAlreadyDrifting() {
		return alreadyDrifting;
	}
	public void setAlreadyDrifting(boolean alreadyDrifting) {
		this.alreadyDrifting = alreadyDrifting;
	}
}
