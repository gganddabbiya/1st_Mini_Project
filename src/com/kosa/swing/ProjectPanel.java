package com.kosa.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.kosa.dao.AccountDAO;
import com.kosa.dao.ProjectMemberDAO;
import com.kosa.dao.TaskDAO;
import com.kosa.dto.ProjectDTO;
import com.kosa.dto.ProjectMemberDTO;
import com.kosa.dto.TaskDTO;
import com.kosa.session.Session;

public class ProjectPanel extends JPanel{
	private ProjectDTO project;
    private ProjectMemberDTO newProjectMember = new ProjectMemberDTO();
	private ArrayList<ProjectMemberDTO> projectMembers = new ArrayList<>();
	
	private ProjectMemberDAO projectMemberDAO = new ProjectMemberDAO();
	private TaskDAO taskDAO = new TaskDAO();
    private AccountDAO accountDAO = new AccountDAO();

    private JPanel projectMembersPanel = new JPanel();
    private JScrollPane projectMembersScrollPane = new JScrollPane();
	private static JProgressBar totalProgressBar;

	public static JProgressBar getTotalProgressBar() {
		return totalProgressBar;
	}
	
	public ProjectPanel(ProjectDTO project) {
		this.project = project;
		this.projectMembers = projectMemberDAO.getProjectMembersByProjectId(project.getProjectId());
		
		setLayout(new BorderLayout());
		
		// 뒤로가기 버튼 , 프로젝트 명, 프로젝트 멤버 추가 패널, 프로젝트 진행도
		add(createNorthPanel(), BorderLayout.NORTH); 
					
		// 프로젝트 멤버 목록
		projectMembersPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		createProjectMembersPanel();
		projectMembersScrollPane.setViewportView(projectMembersPanel);
		add(projectMembersScrollPane, BorderLayout.CENTER);
		
		// retry 목록
		add(createRetryPanel(), BorderLayout.SOUTH);
		
	}
	
	public JPanel createNorthPanel() {
		JPanel northPanel = new JPanel(new BorderLayout());

		// 뒤로 가기 버튼
		JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton backButton = new JButton("<");
		backButton.addActionListener(e -> {
			MainPanel mainPanel = new MainPanel();
			mainPanel.revalidate();
			mainPanel.repaint();
			
			if(!MainFrame.isCardPanel(mainPanel))
            	MainFrame.addCardPanel(mainPanel, "mainPanel");
			MainFrame.showCardPanel("mainPanel");
		});
		backButtonPanel.add(backButton);
		northPanel.add(backButtonPanel, BorderLayout.WEST);

		// 프로젝트명
		JLabel projectNameLabel = new JLabel(project.getProjectName());
		projectNameLabel.setFont(new Font("Arial", Font.BOLD, 20));
		projectNameLabel.setHorizontalAlignment(JLabel.CENTER);
		northPanel.add(projectNameLabel, BorderLayout.CENTER);

		// 프로젝트 멤버 추가 패널 - 리더만 가능
		for (ProjectMemberDTO projectMember : projectMembers) {
			if (projectMember.getUserId().equals(Session.getUserID()) && projectMember.isLeader()) {
				JPanel inviteProjectMemberPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
				inviteProjectMemberPanel.add(createInviteProjectMemberPanel());
				northPanel.add(inviteProjectMemberPanel, BorderLayout.EAST);
			}
		}
		
		// 프로젝트 진행도
		int totalTaskCount = taskDAO.getTotalTaskCountByProjectId(project.getProjectId());
		int totalCompleteTaskCount = taskDAO.getTotalCompleteTaskCountByProjectId(project.getProjectId());
		
		JPanel totalProgressBarPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		totalProgressBar = ProjectProgressBar.createProjectProgressBar(totalTaskCount, totalCompleteTaskCount); 
		totalProgressBarPanel.add(totalProgressBar);
		northPanel.add(totalProgressBarPanel, BorderLayout.SOUTH);	

		return northPanel;
	}

	public void createProjectMembersPanel() {
		projectMembersPanel.removeAll();
		
		for(ProjectMemberDTO projectMember : projectMembers) {
			// 멤버 한 명 당 패널
			JPanel projectMemberPanel = new JPanel();
			projectMemberPanel.setLayout(new BoxLayout(projectMemberPanel, BoxLayout.Y_AXIS));
			projectMemberPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			
			// 이미지
			ImageIcon profileIcon = new ImageIcon("image/profile.png");
	        JLabel profileIconLabel = new JLabel(profileIcon);
	        profileIconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	        projectMemberPanel.add(profileIconLabel);
			
			// 이름
	        JLabel projectMemberNameLabel = new JLabel(accountDAO.getUserNameByUserId(projectMember.getUserId()));
	        projectMemberNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			projectMemberPanel.add(projectMemberNameLabel);
			
			// 리더 표시
			if(projectMember.isLeader()) {
				JLabel leaderLabel = new JLabel("프로젝트 리더");
				leaderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
				projectMemberPanel.add(leaderLabel);
			}

			// 멤버별 프로젝트별 프로젝트(task) 진행도
			int taskCount = taskDAO.getTaskCountByMemberId(projectMember.getMemberId());
			int completeTaskCount = taskDAO.getCompleteTaskCountByMemberId(projectMember.getMemberId());
			
			projectMember.setProgressBar(ProjectProgressBar.createProjectProgressBar(taskCount, completeTaskCount));

			projectMemberPanel.add(projectMember.getProgressBar());
			
			// 멤버 패널 클릭했을 때 task 페이지로 이동
			JButton projectMemberPanelButton = new JButton();
			projectMemberPanel.add(projectMemberPanelButton);
			projectMemberPanelButton.setVisible(false);
			projectMemberPanel.addMouseListener(new MouseAdapter() {
			    @Override
			    public void mouseClicked(MouseEvent e) {
			        MemberPageUI memberPageUI = new MemberPageUI(projectMember);
			        if (!MainFrame.isCardPanel(memberPageUI)) 
			            MainFrame.addCardPanel(memberPageUI, "memberPageUI");            
			        MainFrame.showCardPanel("memberPageUI");
			    }
			});
			
			projectMembersPanel.add(projectMemberPanel);
		}
	}
	
	public JPanel createInviteProjectMemberPanel() {
		JPanel inviteProjectMemberPanel = new JPanel();
		JTextField memberIdField = new JTextField("id 입력", 15);
		JButton addButton = new JButton("추가");
		
		addButton.addActionListener(e -> {
			String userId = memberIdField.getText();
			String userName = accountDAO.getUserNameByUserId(userId);

			// 존재하는 사용자인지
			if(userName != null) {
				// 이미 초대된 사용자인지
				if (projectMemberDAO.checkProjectMemberInProject(project.getProjectId(), userId) == 0) {
					projectMemberDAO.insertProjectMember(project.getProjectId(), userId);
					
					JOptionPane.showMessageDialog(this, "추가가 완료되었습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
					
					JPanel newProjectMemberPanel = new JPanel();
					
					for(ProjectMemberDTO projectMember: projectMembers) {
						if(projectMember.getUserId().equals(userId))
							newProjectMember = projectMember;
					}

					newProjectMemberPanel.setLayout(new BoxLayout(newProjectMemberPanel, BoxLayout.Y_AXIS));
					newProjectMemberPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
					
					// 이미지
					ImageIcon profileIcon = new ImageIcon("image/profile.png");
			        JLabel profileIconLabel = new JLabel(profileIcon);
			        profileIconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			        newProjectMemberPanel.add(profileIconLabel);
					
					// 이름
			        JLabel projectMemberNameLabel = new JLabel(accountDAO.getUserNameByUserId(userId));
			        projectMemberNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			        newProjectMemberPanel.add(projectMemberNameLabel);

					// 멤버별 프로젝트별 프로젝트(task) 진행도
					int taskCount = taskDAO.getTaskCountByMemberId(newProjectMember.getMemberId());
					int completeTaskCount = taskDAO.getCompleteTaskCountByMemberId(newProjectMember.getMemberId());
					
					newProjectMember.setProgressBar(ProjectProgressBar.createProjectProgressBar(taskCount, completeTaskCount));

					newProjectMemberPanel.add(newProjectMember.getProgressBar());
					
					// 멤버 패널 클릭했을 때 task 페이지로 이동
					JButton projectMemberPanelButton = new JButton();
					newProjectMemberPanel.add(projectMemberPanelButton);
					projectMemberPanelButton.setVisible(false);
					newProjectMemberPanel.addMouseListener(new MouseAdapter() {
					    @Override
					    public void mouseClicked(MouseEvent e) {
					        MemberPageUI memberPageUI = new MemberPageUI(newProjectMember);
					        if (!MainFrame.isCardPanel(memberPageUI)) 
					            MainFrame.addCardPanel(memberPageUI, "memberPageUI");            
					        MainFrame.showCardPanel("memberPageUI");
					    }
					});
					
					projectMembersPanel.add(newProjectMemberPanel);
					projectMembersScrollPane.setViewportView(projectMembersPanel);
					
//					createProjectMembersPanel();
//					revalidate();
//					repaint();

				}
				else {
					JOptionPane.showMessageDialog(this, "이미 추가된 사용자입니다.", "오류", JOptionPane.ERROR_MESSAGE);
				}
			}
			else {
				JOptionPane.showMessageDialog(this, "존재하지 않는 사용자입니다.", "오류", JOptionPane.ERROR_MESSAGE);
			}
		});
		
		
		inviteProjectMemberPanel.add(memberIdField);
		inviteProjectMemberPanel.add(addButton);
		
		return inviteProjectMemberPanel;
	}
	
	public JScrollPane createRetryPanel() {
	    JPanel retryPanel = new JPanel(new BorderLayout());
	    retryPanel.setPreferredSize(new Dimension(100, 100));
	    
	    JPanel retryTaskPanel = new JPanel();
	    ArrayList<TaskDTO> tasks = taskDAO.getTasksByProjectId(project.getProjectId());
	    for(TaskDTO task : tasks) {
	        for(ProjectMemberDTO projectMember : projectMembers) {
	            if (task.isRetry() && task.getMemberId()==projectMember.getMemberId()) {
	                JButton retryTaskButton = new JButton("[" + accountDAO.getUserNameByUserId(projectMember.getUserId()) + "] " + task.getTaskName());
	                retryTaskButton.addActionListener(e -> {
	                	TaskPageUI taskPageUI = new TaskPageUI(task);
	                	if (!MainFrame.isCardPanel(taskPageUI))
	                		MainFrame.addCardPanel(taskPageUI, "taskPageUI");
	                	MainFrame.showCardPanel("taskPageUI");
	                });
	                retryTaskPanel.add(retryTaskButton);
	            }
	        }
	    }
	    
	    retryPanel.add(new JLabel("Retry List"), BorderLayout.NORTH);
	    retryPanel.add(retryTaskPanel, BorderLayout.CENTER);
	    
	    JScrollPane scrollPane = new JScrollPane(retryPanel);
	    return scrollPane;
	}

}
