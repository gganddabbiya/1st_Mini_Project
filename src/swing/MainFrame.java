package swing;
import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame extends JFrame{
	private static MainFrame instance;
	
	private static CardLayout cardLayout = new CardLayout();
	private static JPanel cardPanel = new JPanel(cardLayout);
   
    
	public MainFrame () {
        setTitle("First Project");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        setSize(800, 500);
        
        cardPanel.add(new MainPanel(cardLayout, cardPanel), "mainPanel");
        cardPanel.add(new MyProjectPanel(), "myProjectPanel");
        cardPanel.add(new AddProjectPanel(cardLayout, cardPanel), "addProjectPanel");
        cardPanel.add(new ProjectPanel(), "projectPanel");
        
        add(cardPanel);
        
        setVisible(true);
    }
	
	public static MainFrame getInstance() {
        if (instance == null) {
            instance = new MainFrame();
        }
        return instance;
    }
	
}
