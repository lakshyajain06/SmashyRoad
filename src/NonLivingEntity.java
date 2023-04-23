import java.awt.*;
public class NonLivingEntity extends Entity{

	Rectangle hitBox;
	Color color;
	
	public NonLivingEntity(vector position, int xSize, int ySize, Color color) {
		super(position, xSize, ySize);
		hitBox = new Rectangle((int)position.getX(), (int)position.getY(), xSize, ySize);
		this.color = color;
	}
	
	public void moveHitBox() {
		hitBox.setLocation((int)getPosition().getX(), (int)getPosition().getY());
	}

	public void draw(Graphics2D g) {
		g.setColor(color);
		g.fill(hitBox);
	}

	public Rectangle getHitBox() {
		return hitBox;
	}

	public void setHitBox(Rectangle hitBox) {
		this.hitBox = hitBox;
	}
	
}
