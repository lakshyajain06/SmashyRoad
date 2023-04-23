import javax.swing.JFrame;


public class SmashyRoadMain {


	public static void main(String[] args) {
		JFrame window = new JFrame("SMASHYROAD");
		window.setSize(1000, 700);
		window.setLocation(100, 200);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SmashyRoadPanel smashPanel = new SmashyRoadPanel(window.getWidth(), window.getHeight());
		smashPanel.setDoubleBuffered(true);
		
		GameTimer smashTimer = new GameTimer(smashPanel); 
		smashPanel.setTimer(smashTimer);
		
		new SmashyRoadPanelListener(smashPanel, smashTimer);
		smashPanel.setDoubleBuffered(true);
		
		window.setContentPane(smashPanel);
		window.setVisible(true);

		smashPanel.requestFocusInWindow();
	}
}
