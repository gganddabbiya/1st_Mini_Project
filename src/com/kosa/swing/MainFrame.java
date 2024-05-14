package com.kosa.swing;

import java.awt.CardLayout;
import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame extends JFrame {
	private static MainFrame instance;

	private static CardLayout cardLayout = new CardLayout();
	private static JPanel cardPanel = new JPanel(cardLayout);

	private MainFrame() {
		setTitle("First Project");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 500);

		cardPanel.add(new LoginUI(), "loginUI");

		add(cardPanel);

		setVisible(true);
	}

	public static MainFrame getInstance() {
		if (instance == null) {
			instance = new MainFrame();
		}
		return instance;
	}

	public static void addCardPanel(JPanel panel, String panelName) {
		cardPanel.add(panel, panelName);
	}

	public static void showCardPanel(String panelName) {
		cardLayout.show(cardPanel, panelName);
	}

	public static boolean isCardPanel(JPanel panel) {
		Component[] components = cardPanel.getComponents();
		for (Component component : components) {
			if (component == panel) {
				return true;
			}
		}
		return false;
	}

}
