import java.awt.*;

public class map {

	private vector position;
	private int windowHeight, windowWidth;
	private mapGridSquare[][] grid;

	private playerCar player;

	public map(vector position, int xGrids, int yGrids, int windowHeight, int windowWidth) {
		this.position = position;
		this.windowHeight = windowHeight;
		this.windowWidth = windowWidth;
		grid = new mapGridSquare[yGrids][xGrids];
		createMap();
	}

	public void createMap() {
		for(int i = 0; i < grid.length; i++) {
			for(int j = 0; j < grid[i].length; j++) {

				grid[i][j] = new mapGridSquare(i,j, windowHeight, windowWidth, position);
			}
		}
	}

	public void update() {
		for(int i = 0; i < grid.length; i++) {
			for(int j = 0; j < grid[i].length; j++) {
				grid[i][j].update(position);
			}
		}
	}
	public void draw(Graphics g) {
		int[] rows = rowsForPlayer();
		int[] cols = colsForPlayer();
		for(int i = 0; i < rows.length; i++) {
			for(int j = 0; j < cols.length; j++) {
				grid[rows[i]][cols[j]].draw(g);
			}
		}
	}
	public int[] rowsForPlayer() {
		int[] rows = new int[2];
		rows[0] = (int)((player.getPositionOnMap().getY()-windowHeight/2)/windowHeight);
		rows[1] = (int)((player.getPositionOnMap().getY()+windowHeight/2)/windowHeight);
		return rows;
	}
	public int[] colsForPlayer() {
		int[] cols = new int[2];
		cols[0] = (int)((player.getPositionOnMap().getX()-windowWidth/2)/windowWidth);
		cols[1] = (int)((player.getPositionOnMap().getX()+windowWidth/2)/windowWidth);
		return cols;
	}
	public vector getPosition() {
		return position;
	}

	public void setPosition(vector position) {
		this.position = position;
	}

	public playerCar getPlayer() {
		return player;
	}

	public void setPlayer(playerCar player) {
		this.player = player;
	}

	public mapGridSquare[][] getGrid() {
		return grid;
	}


}
