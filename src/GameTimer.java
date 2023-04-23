import java.awt.event.*;
import javax.swing.Timer;

public class GameTimer implements ActionListener{

	SmashyRoadPanel panel;
	Timer myTimer = new Timer(17, this);

	private double secInGame = 0;

	private int totalScore = 0;
	private int killScore = 0;

	private boolean gameOver = false;

	private int numberOfCops = 0;

	public GameTimer(SmashyRoadPanel panel) {
		this.panel = panel;
		myTimer.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		panel.getPlayer().update();
		panel.getGameMap().update();
		for(int i = 0; i < panel.getCops().size(); i++) {
			panel.getCops().get(i).update();
		}
		manageCollisions();
		killCopWithZeroHealth();
		deleteNecessaryCops();
		
		panel.repaint();
		
		secInGame+= 0.017;
		calculateNumberOfCops();
		manageActualNumberOfCops();

		if(panel.getPlayer().getHP() <= 0) {
			panel.getPlayer().setAlive(false);
			gameOver = true;

		}
		if(!gameOver) {
			calculateScore();
		}

	}
	public void killCopWithZeroHealth() {
		for(int i = 0; i < panel.getCops().size(); i++) {
			if(panel.getCops().get(i).isAlive() && panel.getCops().get(i).getHP() <= 0) {
				killScore+=panel.getCops().get(i).getScorevalue();
				panel.getCops().get(i).setAlive(false);
			}
		}
	}
	public void deleteNecessaryCops() {
		for(int i = 0; i < panel.getCops().size(); i++) {
			if(panel.getCops().get(i).isShouldDelete()) {
				panel.getCops().remove(i);
			}
		}
	}
	public void manageCollisions() {
		for(int i = 0; i < panel.getCops().size(); i++) {
			if(panel.getPlayer().collides(panel.getCops().get(i))) {
				panel.getPlayer().knockBack(panel.getCops().get(i));
			}
		}
		for(int i = 0; i < panel.getCops().size(); i++) {
			for(int j = i+1; j < panel.getCops().size(); j++) {
				if(panel.getCops().get(i).collides(panel.getCops().get(j))) {
					panel.getCops().get(i).knockBack(panel.getCops().get(j));
				}
			}
		}
	}
	public void manageActualNumberOfCops() {
		if(panel.getCops().size() < numberOfCops) {
			createRandomCop();
		}
	}
	public void createRandomCop() {
		int rot = (int)(Math.random()*361);
		vector pos = new vector(Math.cos(Math.toRadians(rot))*1000, Math.sin(Math.toRadians(rot))*1000);
		panel.getCops().add(new cop(pos, 180+rot, panel.getPlayer(), panel.getGameMap()));

	}
	public void calculateScore() {
		totalScore = 999999999*((int)secInGame + killScore);
	}
	public void reset() {
		totalScore = 0;
		secInGame = 0;
		killScore = 0;
		gameOver = false;
		numberOfCops = 0;
	}
	public void calculateNumberOfCops() {
		numberOfCops = 1 + totalScore/20;
	}
	public void pauseTimer() {
		myTimer.stop();
	}
	public void unpauseTimer() {
		myTimer.start();
	}

	public double getsecInGame() {
		return secInGame;
	}

	public void setsecInGame(double secInGame) {
		this.secInGame = secInGame;
	}

	public int getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}
}
