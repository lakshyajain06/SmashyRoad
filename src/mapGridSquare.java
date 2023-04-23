import java.awt.*;

public class mapGridSquare {
	private final int lineGridSize = 100;
	
	private vector position;
	private int col, row, windowHeight, windowWidth, squareSizeX, squareSizeY;
	
	private NonLivingEntity[] obstacles;
	
	public mapGridSquare(int row, int col, int windowHeight, int windowWidth, vector mapPosition) {
		this.row = row;
		this.col = col;
		
		this.windowHeight = windowHeight;
		this.windowWidth = windowWidth;
		obstacles = new NonLivingEntity[0];
		position = new vector(mapPosition.getX() + (windowWidth * col), mapPosition.getY() + (windowHeight * row));
	
		squareSizeX = windowWidth;
		squareSizeY = windowHeight;
	}
	
	//updates position of grid
	public void update(vector mapPosition) {
		position.setX(mapPosition.getX() + (windowWidth * col));
		position.setY(mapPosition.getY() + (windowHeight * row));
		for(int i = 0; i < obstacles.length; i++) {
			getObstacles()[i].setPosition(this.getPosition());
			obstacles[i].moveHitBox();
		}
	}
	
	public void draw(Graphics g) {
		g.setColor(new Color(61, 217, 102));
		g.fillRect((int)position.getX(), (int)position.getY(), squareSizeX, squareSizeY);
		g.setColor(new Color(66, 194, 99));
		for(int i = 0; i <=(squareSizeX/lineGridSize); i++) {
			g.drawLine((int)position.getX() + (i*lineGridSize), (int)position.getY(), (int)position.getX() + (i*lineGridSize), (int)position.getY() + squareSizeY);
		}
		for(int i = 0; i <=(squareSizeY/lineGridSize); i++) {
			g.drawLine((int)position.getX(), (int)position.getY() + (i*lineGridSize), (int)position.getX() + squareSizeX, (int)position.getY() + (i*lineGridSize));
		}
		
		for(int i = 0; i < obstacles.length; i++) {
			obstacles[i].draw((Graphics2D)g);
		}
	}
	
	public boolean contains(vector posOnMap) {
		int x = (int)posOnMap.getX();
		int y = (int)posOnMap.getY();
		if(windowWidth*col < x && x < windowWidth*(col+1)) {
			if(windowHeight*row < y && y < windowHeight*(row+1)) {
				return true;
			}
		}
		return false;
	}

	public vector getPosition() {
		return position;
	}

	public void setPosition(vector position) {
		this.position = position;
	}

	public NonLivingEntity[] getObstacles() {
		return obstacles;
	}

	public void setObstacles(NonLivingEntity[] obstacles) {
		this.obstacles = obstacles;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getWindowHeight() {
		return windowHeight;
	}

	public void setWindowHeight(int windowHeight) {
		this.windowHeight = windowHeight;
	}

	public int getWindowWidth() {
		return windowWidth;
	}

	public void setWindowWidth(int windowWidth) {
		this.windowWidth = windowWidth;
	}
}

