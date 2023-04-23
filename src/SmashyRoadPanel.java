import java.awt.*;
import java.util.ArrayList;

import javax.swing.JPanel;

public class SmashyRoadPanel extends JPanel{

	private map gameMap;
	private ArrayList<cop> cops;
	private playerCar player;
	private GameTimer timer;
	
	private int width, height;
	
	
	public SmashyRoadPanel(int width, int height) {
		this.width = width;
		this.height = height;
		
		//creates player at center
		gameMap = new map(new vector(-30000,-21000), 60, 60, height, width);
		player = new standardCar(gameMap, new vector(width/2, height/2));
		gameMap.setPlayer(player);
		cops = new ArrayList<cop>();
	}
	
	public void reset() {
		gameMap = new map(new vector(-10000,-7000), 20, 20, height, width);
		player = new standardCar(gameMap, new vector(width/2, height/2));
		gameMap.setPlayer(player);
		cops = new ArrayList<cop>();
		timer.reset(); 
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		gameMap.draw(g2d);
		for(int i = 0; i < cops.size(); i++) {
			cops.get(i).draw(g2d);
		}
		player.draw(g2d);
		
		g2d.setColor(Color.BLACK);
		g2d.drawString("Score: " + timer.getTotalScore(), 50, 50);
		
		if(timer.isGameOver()) {
			g2d.setColor(new Color(0,0,0,100));
			g2d.fillRect(0,0,width, height);
			g2d.setFont(new Font("SansSerif", Font.PLAIN, 18));
			g2d.setColor(Color.WHITE);
			g2d.drawString("GAME OVER", 400,300);
			g2d.setFont(new Font("SansSerif", Font.PLAIN, 16));
			g2d.drawString("Your score was: " + timer.getTotalScore(), 400,350);
			g2d.drawString("Press space to restart", 400,400);
		}
	}
	
	public ArrayList<cop> getCops() {
		return cops;
	}

	public playerCar getPlayer() {
		return player;
	}

	public map getGameMap() {
		return gameMap;
	}
	public void setTimer(GameTimer timer) {
		this.timer = timer;
	}

	public GameTimer getTimer() {
		return timer;
	}
}
