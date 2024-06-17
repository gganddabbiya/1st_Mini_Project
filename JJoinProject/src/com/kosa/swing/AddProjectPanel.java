package com.kosa.swing;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.kosa.dao.AccountDAO;
import com.kosa.dao.ProjectDAO;
import com.kosa.dao.ProjectMemberDAO;
import com.kosa.dto.ProjectDTO;
import com.kosa.session.Session;

// 프로젝트 추가하는 화면
public class AddProjectPanel extends JPanel{
    private Map <String, String> projectMemberMap = new HashMap<>();
    private ProjectDAO projectDAO = new ProjectDAO();
    private ProjectMemberDAO projectMemberDAO = new ProjectMemberDAO();
    private AccountDAO accountDAO = new AccountDAO();
    
	public AddProjectPanel() {
		setLayout(new BorderLayout());
		
		JPanel northPanel = new JPanel(new BorderLayout());

		// 뒤로가기 버튼
		JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton backButton = new JButton("<");
		backButton.addActionListener(e -> MainFrame.showCardPanel("mainPanel"));
		backButtonPanel.add(backButton);
		northPanel.add(backButtonPanel, BorderLayout.WEST);

		// 프로젝트 이름 입력
		JPanel projectNamePanel = new JPanel();
		JLabel projectNameLabel = new JLabel("프로젝트 명");
		JTextField projectNameField = new JTextField(50);
		projectNamePanel.add(projectNameLabel);
		projectNamePanel.add(projectNameField);

		northPanel.add(projectNamePanel, BorderLayout.CENTER);
		
		add(northPanel, BorderLayout.NORTH);
		
		// 프로젝트 멤버 입력
		add(createProjectMemberPanel(projectMemberMap), BorderLayout.CENTER);
		
		// 프로젝트 추가 버튼
		JPanel addProjectButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JButton addProjectButton = new JButton("프로젝트 생성");
		addProjectButton.addActionListener(e -> addProject(projectNameField.getText()));
		addProjectButtonPanel.add(addProjectButton);
		add(addProjectButtonPanel, BorderLayout.SOUTH);
		
	}
	
	public JPanel createProjectMemberPanel(Map <String, String> projectMemberMap) {
		JPanel projectMemberPanel = new JPanel(new BorderLayout());
		
		// 프로젝트 멤버 입력 패널
		JPanel projectMemberIdPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JLabel projectMemberLabel = new JLabel("프로젝트 멤버");
		JTextField memberIdField = new JTextField("Id 입력", 15);		
		JButton addMemberButton = new JButton("추가");
		projectMemberIdPanel.add(projectMemberLabel);
		projectMemberIdPanel.add(memberIdField);
		projectMemberIdPanel.add(addMemberButton);
		
		projectMemberPanel.add(projectMemberIdPanel, BorderLayout.NORTH);
		
		// 추가된 멤버 패널
		JPanel projectMemberButtonPanel = new JPanel(new FlowLayout());
		
		projectMemberMap.put(Session.getUserID(), accountDAO.getUserNameByUserId(Session.getUserID())); // 로그인한 사용자 먼저 추가
		
		addMemberButton.addActionListener(e -> {
			String userId = memberIdField.getText();
		    String userName = accountDAO.getUserNameByUserId(userId);
		    
		    if (userName != null) {
		        // 중복 체크
		        if (!projectMemberMap.containsKey(userId)) {
		            // 새로운 멤버 추가된 경우에만 추가
		        	projectMemberMap.put(userId, userName);
		            
		            JButton memberButton = new JButton(userName + " X");
		            memberButton.addActionListener(k -> deleteProjectMember(memberButton, userId));
		            projectMemberButtonPanel.add(memberButton);
		            
		            projectMemberButtonPanel.revalidate();
		    		projectMemberButtonPanel.repaint();
		            
		        } else {
		        	JOptionPane.showMessageDialog(this, "이미 추가된 사용자입니다.", "오류", JOptionPane.ERROR_MESSAGE);
		        }
		        
		        memberIdField.setText("");
		    } else {
		        JOptionPane.showMessageDialog(this, "존재하지 않는 사용자입니다.", "오류", JOptionPane.ERROR_MESSAGE);
		    }
		    System.out.println(projectMemberMap);
		});		
		
		projectMemberPanel.add(projectMemberButtonPanel, BorderLayout.CENTER);
				
		return projectMemberPanel;
	}
	
	public void addProject(String projectName) {
		if(projectName.isEmpty()) {
			JOptionPane.showMessageDialog(this, "프로젝트명을 입력하세요.", "알림", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		else {
			int projectId = projectDAO.insertProject(projectName);
			projectMemberDAO.insertProjectMember(projectId, projectMemberMap);
			
			ProjectDTO newProject = new ProjectDTO();
			newProject.setProjectId(projectId);
			newProject.setProjectName(projectName);
			
			ProjectPanel projectPanel = new ProjectPanel(newProject);
			
			if (!MainFrame.isCardPanel(projectPanel)) {
				MainFrame.addCardPanel(projectPanel, "projectPanel");
			}
			
			MainFrame.showCardPanel("projectPanel");
		}
			
	}
	
	public void deleteProjectMember(JButton memberButton, String userId) {
		projectMemberMap.remove(userId);
		memberButton.setVisible(false);
	}
	
}
