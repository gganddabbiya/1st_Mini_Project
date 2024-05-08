package swing;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dao.AccountDAO;
import dao.ProjectDAO;
import dao.ProjectMemberDAO;
import session.Session;

// 프로젝트 추가하는 화면
public class AddProjectPanel extends JPanel{
	private CardLayout cardLayout;
    private JPanel cardPanel;
    private Map <String, String> projectMemberMap = new HashMap<>();
    private ProjectDAO projectDAO = new ProjectDAO();
    private ProjectMemberDAO projectMemberDAO = new ProjectMemberDAO();
    private AccountDAO accountDAO = new AccountDAO();
    
	public AddProjectPanel(CardLayout cardLayout, JPanel cardPanel) {
		this.cardLayout = cardLayout;
		this.cardPanel = cardPanel;
		
		// 뒤로가기 버튼
		JButton backButton = new JButton("Back");
		backButton.addActionListener(e -> cardLayout.show(cardPanel, "mainPanel"));
		add(backButton);
		
		// 포로젝트 이름 입력
		JLabel projectNameLabel = new JLabel("프로젝트 명");
		JTextField projectNameField = new JTextField(50);
		
		add(projectNameLabel);
		add(projectNameField);
		
		// 프로젝트 멤버 입력
		add(createProjectMemberPanel(projectMemberMap));
		
		// 프로젝트 추가 버튼
		JButton addProjectButton = new JButton("프로젝트 생성");
		addProjectButton.addActionListener(e -> addProject(projectNameField.getText()));
		add(addProjectButton);
		
	}
	
	public JPanel createProjectMemberPanel(Map <String, String> projectMemberMap) {
		JPanel projectMemberPanel = new JPanel(new FlowLayout());
		
		JLabel projectMemberLabel = new JLabel("프로젝트 멤버");
		JTextField memberIdField = new JTextField("Id 입력", 10);		
		JButton addMemberButton = new JButton("추가");
		
		JPanel projectMemberButtonPanel = new JPanel(new FlowLayout());
		
		addMemberButton.addActionListener(e -> {
			String userId = memberIdField.getText();
		    String userName = accountDAO.getUserNameByUserId(userId);
		    
		    projectMemberMap.put(Session.getUserId(), accountDAO.getUserNameByUserId(Session.getUserId())); // 로그인한 사용자 먼저 추가
		    
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
		            JOptionPane.showMessageDialog(this, "이미 추가된 사용자입니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
		        }
		        
		        memberIdField.setText("");
		    } else {
		        JOptionPane.showMessageDialog(this, "존재하지 않는 사용자입니다.", "오류", JOptionPane.ERROR_MESSAGE);
		    }
		    System.out.println(projectMemberMap);
		});		
		
		projectMemberPanel.add(projectMemberLabel);
		projectMemberPanel.add(memberIdField);
		projectMemberPanel.add(addMemberButton);
		projectMemberPanel.add(projectMemberButtonPanel);
				
		return projectMemberPanel;
	}
	
	public void addProject(String projectName) {
		if(projectName.isEmpty()) {
			JOptionPane.showInputDialog(this, "프로젝트명을 입력하세요.", "알림", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		else {
			int projectId = projectDAO.insertProject(projectName);
			projectMemberDAO.insertProjectMember(projectId, projectMemberMap);
			cardLayout.show(cardPanel, "projectPanel");
		}
			
	}
	
	public void deleteProjectMember(JButton memberButton, String userId) {
		projectMemberMap.remove(userId);
		memberButton.setVisible(false);
	}
	
}
