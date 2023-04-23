import java.awt.*;


public abstract class Entity {
	private int xSize,ySize;
	private vector position;

	public Entity(vector position, int xSize, int ySize) {
		this.position = position;
		this.ySize = ySize;
		this.xSize = xSize;
	}
	
	public abstract void draw(Graphics2D g);
	
	public int getxSize() {
		return xSize;
	}
	public void setxSize(int xSize) {
		this.xSize = xSize;
	}
	public int getySize() {
		return ySize;
	}
	public void setySize(int ySize) {
		this.ySize = ySize;
	}
	public vector getPosition() {
		return position;
	}

	public void setPosition(vector position) {
		this.position = position;
	}
	
}
