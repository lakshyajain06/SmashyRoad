import java.awt.*;
import java.awt.geom.AffineTransform;

public class standardCar extends playerCar{

	//Stats of Car
	private final static int totalHP = 1000;
	private final static double maxTurnSpeed = 6;
	private final static double turnAcceleration = .3;
	private final static double acceleration = .075;
	private final static int maxSpeed = 12;
	private final static int xSize = 76;
	private final static int ySize = 38;




	public standardCar(map x, vector position) {
		super(x, position, xSize, ySize, totalHP, totalHP, maxTurnSpeed, turnAcceleration, 270, acceleration,maxSpeed, 0);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void draw(Graphics2D g2) {


		//prevents rotation or shift of any other drawings after this
		AffineTransform oldPos = g2.getTransform();
		//moves graphics to position of car
		g2.translate(getPosition().getX(),getPosition().getY());


		g2.rotate(Math.toRadians(getRotation()));
		if(isAlive()) {
			//this draws car
			g2.setColor(new Color(50,50,50));
			g2.fillRect(getTranslatedX(), getTranslatedY(), getxSize(), getySize());
			g2.setColor(Color.BLACK);
			g2.fillRect(getTranslatedX()+getxSize()*3/20, getTranslatedY(), getxSize()/2, getySize());
			g2.setColor(Color.CYAN);
			g2.fillRect(getTranslatedX()+getxSize()*13/20, getTranslatedY() + getySize()/10, getxSize()/20, getySize()*8/10);
		}
		else {
			g2.setColor(Color.BLACK);	
			g2.fillRect(getTranslatedX(), getTranslatedY(), getxSize(), getySize());
		}

		//prevents rotation or shift of any other drawings after this
		g2.setTransform(oldPos);
		drawHealthBar(g2);
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

}
