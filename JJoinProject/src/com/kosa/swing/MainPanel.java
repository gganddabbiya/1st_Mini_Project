package com.kosa.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.kosa.dao.ProjectDAO;
import com.kosa.dto.ProjectDTO;
import com.kosa.session.Session;

public class MainPanel extends JPanel {
	private ProjectDAO projectDAO = new ProjectDAO();

	public MainPanel() {
		System.out.println("MainPanel id : " + Session.getUserID());

		setLayout(new BorderLayout());
		
		JPanel northPanel = new JPanel(new BorderLayout());

		// 로고
		JLabel logoLabel = new JLabel("JJOIN");
		logoLabel.setFont(new Font("Arial", Font.BOLD, 20));
		logoLabel.setHorizontalAlignment(JLabel.CENTER);
		northPanel.add(logoLabel, BorderLayout.CENTER);

		// 로그아웃
		JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton logoutButton = new JButton("로그아웃");
		logoutButton.addActionListener(e -> logout());
		logoutPanel.add(logoutButton);
		northPanel.add(logoutPanel, BorderLayout.EAST);

		// 프로젝트 목록 버튼
		add(northPanel, BorderLayout.NORTH);
		add(createProjectButtonPanel(), BorderLayout.CENTER);

	}

	private JScrollPane createProjectButtonPanel() {
		JPanel buttonPanel = new JPanel();		
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

		buttonPanel.add(Box.createVerticalGlue());
		
		Map<Integer, String> projects = projectDAO.getProjectsByUserId(Session.getUserID());

		for (int projectId : projects.keySet()) {
			String projectName = projects.get(projectId);
			JButton projectButton = new JButton(projectName);
			projectButton.setAlignmentX(Component.CENTER_ALIGNMENT);
			projectButton.addActionListener(e -> switchToMyProjectPanel(projectId, projectName));

			buttonPanel.add(projectButton);

			buttonPanel.add(Box.createVerticalGlue());
		}

		JButton addButton = new JButton("+");
		addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		addButton.addActionListener(e -> switchToAddProjectPanel());
		buttonPanel.add(addButton);
		buttonPanel.add(Box.createVerticalGlue());
		
		JScrollPane scrollPane = new JScrollPane(buttonPanel);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);;
		return scrollPane;
	}

	private void switchToMyProjectPanel(int projectId, String projectName) {
		ProjectDTO selectProject = new ProjectDTO();
		selectProject.setProjectId(projectId);
		selectProject.setProjectName(projectName);
		
		ProjectPanel ProjectPanel = new ProjectPanel(selectProject);
		
		if (!MainFrame.isCardPanel(ProjectPanel)) 
			MainFrame.addCardPanel(ProjectPanel, "projectPanel");
		MainFrame.showCardPanel("projectPanel");
	}

	private void switchToAddProjectPanel() {
		AddProjectPanel addProjectPanel = new AddProjectPanel();
		
		if (!MainFrame.isCardPanel(addProjectPanel))
			MainFrame.addCardPanel(addProjectPanel, "addProjectPanel");
		MainFrame.showCardPanel("addProjectPanel");
	}

	private void logout() {
		MainFrame.showCardPanel("loginUI");
		Session.setUserID(null);
	}
}
