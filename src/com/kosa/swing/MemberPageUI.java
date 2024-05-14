package com.kosa.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.kosa.dao.AccountDAO;
import com.kosa.dao.MemberPageDAO;
import com.kosa.dao.ProjectDAO;
import com.kosa.dao.ProjectMemberDAO;
import com.kosa.dao.TaskDAO;
import com.kosa.dto.ProjectDTO;
import com.kosa.dto.ProjectMemberDTO;
import com.kosa.dto.TaskDTO;
import com.kosa.session.Session;

public class MemberPageUI extends JPanel {
	private JLabel titleLabel;
	private JLabel teamMemberImageLabel;
	private JProgressBar progressBar;
	private JPanel taskPanel  = new JPanel();
	private JButton addTaskButton;
	private JLabel userNameLabel = new JLabel();
	private MemberPageDAO memberPageDAO = new MemberPageDAO();

	private ProjectMemberDTO projectMember;
	private AccountDAO accountDAO = new AccountDAO();
	private TaskDAO taskDAO = new TaskDAO();
	private ArrayList<TaskDTO> tasks = new ArrayList<>();

	public MemberPageUI(ProjectMemberDTO projectMember) {
		this.projectMember = projectMember;
		
		setLayout(new BorderLayout(30, 20));
		
		fetchMemberTasks();
		updateUserNameLabel();
		
		add(createNorthPanel(), BorderLayout.NORTH);
		
		JPanel centerPanel = new JPanel(new GridLayout(1, 2, 20, 20));
		
		centerPanel.add(createWestPanel());
		centerPanel.add(createEastPanel());
		add(centerPanel, BorderLayout.CENTER);
		
		if (projectMember.getUserId().equals(Session.getUserID()))
			add(createSouthPanel(), BorderLayout.SOUTH);

	}
	
	public JPanel createNorthPanel() {
		JPanel northPanel = new JPanel(new BorderLayout());
		
		// 뒤로 가기 버튼
		JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton backButton = new JButton("<");
		backButton.addActionListener(e -> {
			ProjectDAO projectDAO = new ProjectDAO();
			ProjectDTO project = new ProjectDTO();

			for (int projectId : projectDAO.getProjectsByUserId(projectMember.getUserId()).keySet()) {
				if (projectId == projectMember.getProjectId()) {
					project.setProjectId(projectId);
					project.setProjectName(projectDAO.getProjectsByUserId(projectMember.getUserId()).get(projectId));
				}
			}
			ProjectPanel projectPanel = new ProjectPanel(project);
			
			if (!MainFrame.isCardPanel(projectPanel))
				MainFrame.addCardPanel(projectPanel, "projectPanel");
			
			MainFrame.showCardPanel("projectPanel");
			updateProjectProgressBar(projectMember.getProgressBar());
			updateTotalProjectProgressBar(ProjectPanel.getTotalProgressBar());
		});
		backButtonPanel.add(backButton);
		northPanel.add(backButtonPanel, BorderLayout.WEST);
		
		// TO DO List 라벨
		JPanel titleLabelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		titleLabel = new JLabel("To Do List", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
		titleLabelPanel.add(titleLabel);
		northPanel.add(titleLabelPanel, BorderLayout.CENTER);
		
		return northPanel;
	};
	
	public JPanel createWestPanel() {
		JPanel westPanel = new JPanel();
		westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.Y_AXIS));
		
		teamMemberImageLabel = new JLabel(new ImageIcon("image/profile.png"));
		teamMemberImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		int taskCount = taskDAO.getTaskCountByMemberId(projectMember.getMemberId());
		int completeTaskCount = taskDAO.getCompleteTaskCountByMemberId(projectMember.getMemberId());
		progressBar = ProjectProgressBar.createProjectProgressBar(taskCount, completeTaskCount);
		progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		userNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		westPanel.add(teamMemberImageLabel);
		westPanel.add(userNameLabel);
		westPanel.add(progressBar);
		
		return westPanel;
	}
	
	public JScrollPane createEastPanel() {
		taskPanel.setMinimumSize(new Dimension(300, 300));
		taskPanel.setLayout(new BoxLayout(taskPanel, BoxLayout.Y_AXIS));
		taskPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JScrollPane scrollPane = new JScrollPane(taskPanel);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		return scrollPane;
	}
	
	public JPanel createSouthPanel() {
		JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		addTaskButton = new JButton("Add Task");

		addTaskButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showAddTaskDialog();
			}
		});
		
		southPanel.add(addTaskButton);
		return southPanel;
	}

	private void updateUserNameLabel() {
		String userID = projectMember.getUserId();

		if (userID != null) {
			String userName = accountDAO.getUserNameByUserId(userID);
			if (userName != null) {
				userNameLabel.setText(userName);
			} else {
				userNameLabel.setText("Unknown");
			}

		} else {
			userNameLabel.setText("Unknown");
		}
	}

	private void fetchMemberTasks() {
		taskPanel.removeAll();

		tasks = memberPageDAO.fetchMemberTasks(projectMember.getMemberId());

		for (TaskDTO task : tasks) {
			JButton taskButton = new JButton("[ " + task.getPriority() + " ] " + task.getTaskName());
			
			JCheckBox checkBox = new JCheckBox();

			taskButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					goToTaskPage(task);
				}
			});

			// Completion task는 체크 표시
			if (task.isCompletion()) {
				checkBox.setSelected(true);
			}
			
			// 체크박스 클릭 이벤트 처리
			checkBox.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// 체크박스 클릭 시 completion 값 업데이트
					int newCompletion = checkBox.isSelected() ? 1 : 0;
					memberPageDAO.updateTaskCompletion(task.getTaskId(), newCompletion);
					updateProjectProgressBar(progressBar);
				}
			});

			JPanel oneTaskPanel = new JPanel();
			oneTaskPanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // 왼쪽 정렬
			
			if (!projectMember.getUserId().equals(Session.getUserID())) {
				checkBox.setEnabled(false);
			}
			oneTaskPanel.add(checkBox);
			oneTaskPanel.add(taskButton);

			taskPanel.add(oneTaskPanel);
		}
	}

	private void showAddTaskDialog() {
		JDialog dialog = new JDialog(MainFrame.getInstance(), "Add Task", true);
		dialog.setLayout(new BorderLayout());
		dialog.setSize(300, 200);
		dialog.setLocationRelativeTo(null);

		JPanel taskDetailsPanel = new JPanel(new GridLayout(3, 1));
		JTextField taskNameField = new JTextField();
		JPanel priorityPanel = new JPanel();
		JLabel priorityLabel = new JLabel("Priority:");
		JComboBox<String> priorityComboBox = new JComboBox<>(new String[] { "High", "Medium", "Low" });
		priorityPanel.add(priorityLabel);
		priorityPanel.add(priorityComboBox);
		taskDetailsPanel.add(new JLabel("Task Name:"));
		taskDetailsPanel.add(taskNameField);
		taskDetailsPanel.add(priorityPanel);

		JPanel buttonPanel = new JPanel();
		JButton addButton = new JButton("Add");
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String taskName = taskNameField.getText();
				String priority = (String) priorityComboBox.getSelectedItem();
				dialog.dispose();
				addTask(taskName, priority);
			}
		});
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}
		});
		buttonPanel.add(addButton);
		buttonPanel.add(cancelButton);

		dialog.add(taskDetailsPanel, BorderLayout.CENTER);
		dialog.add(buttonPanel, BorderLayout.SOUTH);
		dialog.setVisible(true);
	}

	private void addTask(String taskName, String priority) {
		int memberId = projectMember.getMemberId();
		int projectId = projectMember.getProjectId();

		this.memberPageDAO.addTask(taskName, priority, memberId, projectId);

		fetchMemberTasks();
		updateProjectProgressBar(progressBar);
		revalidate();
		repaint();
	}
	
	private void updateProjectProgressBar(JProgressBar progressBar) {
		System.out.println(projectMember.getMemberId());
	    int taskCount = taskDAO.getTaskCountByMemberId(projectMember.getMemberId());
	    int completeTaskCount = taskDAO.getCompleteTaskCountByMemberId(projectMember.getMemberId());
	    int progressPercentage = (int)(((double)completeTaskCount / taskCount) * 100);
	    
	    progressBar.setString(completeTaskCount+"/"+taskCount);
	    progressBar.setValue(progressPercentage);
	}
	
	private void updateTotalProjectProgressBar(JProgressBar progressBar) {
	    int taskCount = taskDAO.getTotalTaskCountByProjectId(projectMember.getProjectId());
	    int completeTaskCount = taskDAO.getTotalCompleteTaskCountByProjectId(projectMember.getProjectId());	    
	    int progress = (int)(((double)completeTaskCount / taskCount) * 100);
	    
	    progressBar.setString(completeTaskCount+"/"+taskCount);
	    progressBar.setValue(progress);
	}

	private void goToTaskPage(TaskDTO task) {
		// 작업 페이지로 이동하는 코드 구현
		TaskPageUI taskPageUI = new TaskPageUI(task);
		if (!MainFrame.isCardPanel(taskPageUI))
			MainFrame.addCardPanel(taskPageUI, "taskPageUI");
		MainFrame.showCardPanel("taskPageUI");
	}

}