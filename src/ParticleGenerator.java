import java.awt.Graphics2D;
import java.util.ArrayList;

public class ParticleGenerator {

	private final int numOfParticle = 100;
	private int pause = 40;

	private int rotation, x, y;
	private ArrayList<Particle> particles = new ArrayList<Particle>();

	private int counter = 0;
	
	private map gameMap;

	public ParticleGenerator(int x, int y, int rotation, map gameMap) {
		this.x = x;
		this.y = y;
		this.rotation = rotation;
		this.gameMap = gameMap;
	}

	public void draw(Graphics2D g2) {
		update();
		for(int i = 0; i < particles.size(); i++) {
			particles.get(i).draw(g2);
		}
	}
	public void drawDead(Graphics2D g2) {
		update();
		for(int i = 0; i < particles.size(); i++) {
			particles.get(i).drawDead(g2);
		}
	}
	public void update() {
		int ranRot = (int)(Math.random()*21) - 10;
		for(int i = 0; i < particles.size(); i++) {
			particles.get(i).update();
			if(particles.get(i).getTransparency() <= 0) {
				particles.remove(i);
				i--;
			}
		}
		if(counter > pause) {
			counter = 0;
		}
		if(counter == 0) {
			if(particles.size() < numOfParticle) {

				int randNum = (int)(Math.random()*6);
				particles.add(new Particle(x, y, rotation + ranRot, gameMap, randNum));
			}
		}
		counter++;
	}
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
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

	public int getPause() {
		return pause;
	}
	public void setPause(int pause) {
		this.pause = pause;
	}
}

