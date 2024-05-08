package swing;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import dao.AccountDAO;
import dao.ProjectDAO;
import db.DBConnection;
import dto.AccountDTO;
import oracle.jdbc.OracleTypes;
import session.Session;

public class MainPanel extends JPanel {
	private CardLayout cardLayout;
    private JPanel cardPanel;
    private AccountDAO accountDAO = new AccountDAO();
    private ProjectDAO projectDAO = new ProjectDAO();

    public MainPanel(CardLayout cardLayout, JPanel cardPanel) {
    	this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
    	
        setLayout(new BorderLayout());

        //로고
        JLabel logoLabel = new JLabel("JJOIN");
        logoLabel.setHorizontalAlignment(JLabel.CENTER);
        add(logoLabel, BorderLayout.NORTH);

        //로그아웃
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton logoutButton = new JButton("로그아웃");
        logoutButton.addActionListener(e -> logout());
        logoutPanel.add(logoutButton);
        add(logoutPanel, BorderLayout.EAST);

        //프로젝트 목록 버튼
        add(createProjectButtonPanel(), BorderLayout.CENTER);

    }

    private JPanel createProjectButtonPanel() {
    	JPanel buttonPanel = new JPanel();
    	buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
    	buttonPanel.setBackground(Color.black);
    	
    	buttonPanel.add(Box.createVerticalGlue());
    	
        for (String projectName:projectDAO.getProjectNameByUserId(Session.getUserId())) {
            JButton projectButton = new JButton(projectName);
            projectButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            projectButton.addActionListener(e -> switchToMyProjectPanel());
            
            buttonPanel.add(projectButton);
            
            buttonPanel.add(Box.createVerticalGlue());
        }

        JButton addButton = new JButton("+");
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addButton.addActionListener(e -> switchToAddProjectPanel());
        buttonPanel.add(addButton);
        buttonPanel.add(Box.createVerticalGlue());
        
        return buttonPanel;
    }

    private void switchToMyProjectPanel() {
    	cardLayout.show(cardPanel, "myProjectPanel");
    }
    
    private void switchToAddProjectPanel() {
    	cardLayout.show(cardPanel, "addProjectPanel");
    }
    
    private void logout() {
    	cardLayout.show(cardPanel, "loginPanel");
    	// LogoutHandler.logout();
    }
}

