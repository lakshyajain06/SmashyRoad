import java.awt.event.*;
import java.util.ArrayList;

public class SmashyRoadPanelListener implements KeyListener{

	SmashyRoadPanel panel;
	ArrayList<Integer> keys = new ArrayList<Integer>();

	boolean alreadyPressed;		
	boolean paused = false;
	GameTimer theTimer;

	public SmashyRoadPanelListener(SmashyRoadPanel panel, GameTimer timer) {
		this.panel = panel;
		panel.addKeyListener(this);
		theTimer = timer;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		alreadyPressed = false;
		int key = e.getKeyCode();
		for(int i = 0; i < keys.size(); i++) {
			if(keys.get(i) == key) {
				alreadyPressed = true;
			}
		}

		if(!alreadyPressed) {
			keys.add(key);
		}
		for(int i = 0; i < keys.size(); i++) {
			if(keys.get(i) == KeyEvent.VK_DOWN) {
				panel.getPlayer().setAcceleration(-1 * panel.getPlayer().getPossibleAcceleration());
			}
			if(!(keys.contains(KeyEvent.VK_RIGHT) && keys.contains(KeyEvent.VK_LEFT))) {
				if(keys.get(i) == KeyEvent.VK_RIGHT) {
					panel.getPlayer().setTurnAcceleration(panel.getPlayer().getPossibleTurnAcceleration());
				}
				if(keys.get(i) == KeyEvent.VK_LEFT) {
					panel.getPlayer().setTurnAcceleration(-1 * panel.getPlayer().getPossibleTurnAcceleration());
				}
			}
			if(keys.get(i) == KeyEvent.VK_SPACE && panel.getTimer().isGameOver()) {
				panel.reset();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		int key = e.getKeyCode();
		if(keys.contains(key)) {
			keys.remove(keys.indexOf(key));
		}

		if(key == KeyEvent.VK_DOWN) {
			panel.getPlayer().setAcceleration(panel.getPlayer().getPossibleAcceleration());

		}
		if(key == KeyEvent.VK_RIGHT) {
			panel.getPlayer().setCurrentTurnSpeed(0);
			panel.getPlayer().setTurnAcceleration(0);
		}
		if(key == KeyEvent.VK_LEFT) {
			panel.getPlayer().setCurrentTurnSpeed(0);
			panel.getPlayer().setTurnAcceleration(0);
		}
	}

}
