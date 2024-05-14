package com.kosa.swing;

import javax.swing.*;

import com.kosa.dao.ProjectMemberDAO;
import com.kosa.dao.TaskDAO;
import com.kosa.dto.ProjectMemberDTO;
import com.kosa.dto.TaskDTO;
import com.kosa.session.Session;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TaskPageUI extends JPanel {
	private JPanel mainPanel;
	private JPanel topPanel;
	private JPanel bodyPanel;
	private JButton backButton;
	private JLabel priorityLabel;
	private JLabel taskNameLabel;
	private JLabel completionLabel;
	private JToggleButton amazingButton;
	private JToggleButton goodButton;
	private JToggleButton soSoButton;
	private JToggleButton unsatisfactoryButton;
	private JToggleButton retryButton;
	private JToggleButton errorButton;
	private ButtonGroup buttonGroup;
	private TaskDAO taskDAO = new TaskDAO();
	private TaskDTO task;
	private int inputMemberId; // feedback을 입력한 멤버 
	private ProjectMemberDAO projectMemberDAO = new ProjectMemberDAO();
	private ArrayList<ProjectMemberDTO> projectMembers;

	public TaskPageUI(TaskDTO task) {
		this.task = task;
		
		projectMembers = projectMemberDAO.getProjectMembersByProjectId(task.getProjectId());
		
		for(ProjectMemberDTO projectMember : projectMembers) {
			if(projectMember.getUserId().equals(Session.getUserID())){
				this.inputMemberId = projectMember.getMemberId();
			}
		}

		initializeUI();
	}

	private void initializeUI() {
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		JPanel topPanel = new JPanel(new BorderLayout());
		System.out.println("Task Name: " + task.getTaskName());
		backButton = new JButton("<");
		backButton.addActionListener(e -> {
			for(ProjectMemberDTO projectMember : projectMembers) {
				if(projectMember.getMemberId() == task.getMemberId()){
					int taskCount = taskDAO.getTaskCountByMemberId(projectMember.getMemberId());
					int completeTaskCount = taskDAO.getCompleteTaskCountByMemberId(projectMember.getMemberId());
					
					projectMember.setProgressBar(ProjectProgressBar.createProjectProgressBar(taskCount, completeTaskCount));
					MemberPageUI memberPageUI = new MemberPageUI(projectMember);
					if (!MainFrame.isCardPanel(memberPageUI))
						MainFrame.addCardPanel(memberPageUI, "memberPageUI");
					MainFrame.showCardPanel("memberPageUI");
				}
			}
		});
		
		taskNameLabel = new JLabel(task.getTaskName(), SwingConstants.CENTER);
		//taskNameLabel.setFont(new Font("Arial", Font.BOLD, 20));
		completionLabel = new JLabel(task.isCompletion() ? "Completion!!" : "Not Yet");

		topPanel.add(backButton, BorderLayout.WEST);
		topPanel.add(taskNameLabel, BorderLayout.CENTER);
		topPanel.add(completionLabel, BorderLayout.EAST);
		mainPanel.add(topPanel, BorderLayout.NORTH);

		bodyPanel = new JPanel();
		mainPanel.add(bodyPanel);

		JPanel buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.setLayout(new GridLayout(0, 3)); // 0은 행이 자동으로 설정되고, 2는 열의 개수입니다.

		JPanel leftPanel = new JPanel(new BorderLayout());

		amazingButton = new JToggleButton("Amazing");
		goodButton = new JToggleButton("Good");
		soSoButton = new JToggleButton("So So");
		unsatisfactoryButton = new JToggleButton("Unsatisfactory");
		retryButton = new JToggleButton("Please Retry");
		errorButton = new JToggleButton("Error Detection!!");

		buttonGroup = new ButtonGroup();
		buttonGroup.add(amazingButton);
		buttonGroup.add(goodButton);
		buttonGroup.add(soSoButton);
		buttonGroup.add(unsatisfactoryButton);
		buttonGroup.add(retryButton);
		buttonGroup.add(errorButton);

		buttonPanel.add(amazingButton);
		buttonPanel.add(goodButton);
		buttonPanel.add(soSoButton);
		buttonPanel.add(unsatisfactoryButton);
		buttonPanel.add(retryButton);
		buttonPanel.add(errorButton);
		leftPanel.add(buttonPanel, BorderLayout.WEST);
		bodyPanel.add(leftPanel, BorderLayout.WEST);

		add(mainPanel);
		selectPreviousButton();

		amazingButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updatePreviousButton("Amazing", 100);
				addFeedback("Amazing", 100); // 프로시저를 호출하여 feedback 테이블에 값을 추가합니다.
				taskDAO.updateFeedbackTotalScore(task.getTaskId(), 100);
				
				int newFeedbackTotalScore = 0;
				
				for(TaskDTO updateTask : taskDAO.getTasksByProjectId(task.getProjectId())) {
					if (updateTask.getTaskId() == task.getTaskId()) {
						newFeedbackTotalScore = updateTask.getFeedbackTotalScore();
					}
				}
				
				taskDAO.updateRetryTask(task.getTaskId(), newFeedbackTotalScore);
			}
		});

		// Good 버튼에 대한 ActionListener 추가
		goodButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updatePreviousButton("Good", 80);
				addFeedback("Good", 80); // 프로시저를 호출하여 feedback 테이블에 값을 추가합니다.
				taskDAO.updateFeedbackTotalScore(task.getTaskId(), 80);
				System.out.println(task.getFeedbackTotalScore());
				
				int newFeedbackTotalScore = 0;
				
				for(TaskDTO updateTask : taskDAO.getTasksByProjectId(task.getProjectId())) {
					if (updateTask.getTaskId() == task.getTaskId()) {
						newFeedbackTotalScore = updateTask.getFeedbackTotalScore();
					}
				}
				
				taskDAO.updateRetryTask(task.getTaskId(), newFeedbackTotalScore);
			}
		});

		// So So 버튼에 대한 ActionListener 추가
		soSoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updatePreviousButton("So So", 50);
				addFeedback("So So", 50); // 프로시저를 호출하여 feedback 테이블에 값을 추가합니다.
				taskDAO.updateFeedbackTotalScore(task.getTaskId(), 50);
				System.out.println(task.getFeedbackTotalScore());
				
				int newFeedbackTotalScore = 0;
				
				for(TaskDTO updateTask : taskDAO.getTasksByProjectId(task.getProjectId())) {
					if (updateTask.getTaskId() == task.getTaskId()) {
						newFeedbackTotalScore = updateTask.getFeedbackTotalScore();
					}
				}
				
				taskDAO.updateRetryTask(task.getTaskId(), newFeedbackTotalScore);
			}
		});

		// Unsatisfactory 버튼에 대한 ActionListener 추가
		unsatisfactoryButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updatePreviousButton("Unsatisfactory", 30);
				addFeedback("Unsatisfactory", 30); // 프로시저를 호출하여 feedback 테이블에 값을 추가합니다.
				taskDAO.updateFeedbackTotalScore(task.getTaskId(), 30);
				System.out.println(task.getFeedbackTotalScore());
				
				int newFeedbackTotalScore = 0;
				
				for(TaskDTO updateTask : taskDAO.getTasksByProjectId(task.getProjectId())) {
					if (updateTask.getTaskId() == task.getTaskId()) {
						newFeedbackTotalScore = updateTask.getFeedbackTotalScore();
					}
				}
				
				taskDAO.updateRetryTask(task.getTaskId(), newFeedbackTotalScore);
			}
		});

		// Please Retry 버튼에 대한 ActionListener 추가
		retryButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updatePreviousButton("Please Retry", 0);
				addFeedback("Please Retry", 0); // 프로시저를 호출하여 feedback 테이블에 값을 추가합니다.
				taskDAO.updateFeedbackTotalScore(task.getTaskId(), 0);
				System.out.println(task.getFeedbackTotalScore());
				
				int newFeedbackTotalScore = 0;
				
				for(TaskDTO updateTask : taskDAO.getTasksByProjectId(task.getProjectId())) {
					if (updateTask.getTaskId() == task.getTaskId()) {
						newFeedbackTotalScore = updateTask.getFeedbackTotalScore();
					}
				}
				
				taskDAO.updateRetryTask(task.getTaskId(), newFeedbackTotalScore);
			}
		});

		// Error Detection 버튼에 대한 ActionListener 추가 (예시로 작성했습니다.)
		errorButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updatePreviousButton("Error Detection", -1);
				addFeedback("Error Detection", -1); // 프로시저를 호출하여 feedback 테이블에 값을 추가합니다.
				taskDAO.updateFeedbackTotalScore(task.getTaskId(), -1);
			}
		});
	}

	private void addFeedback(String adviceName, int adviceScore) {
		try {
			// 프로시저 호출
			taskDAO.addFeedback(adviceName, adviceScore, task.getTaskId(), task.getMemberId(), inputMemberId, task.getProjectId());
			System.out.println(adviceName + adviceScore + task.getTaskId() + task.getMemberId() + inputMemberId + task.getProjectId());

		} catch (SQLException ex) {
			ex.printStackTrace();
			// 에러 처리
		}
	}

	private void deleteFeedback() {
		try {
			// 프로시저 호출
			buttonGroup.clearSelection();
			taskDAO.deleteFeedback(task.getTaskId(), task.getMemberId(), task.getProjectId());
			System.out.println("Deleted feedback for task ID: " + task.getTaskId() + " and member ID: " + inputMemberId);
			return;
		} catch (SQLException ex) {
			ex.printStackTrace();
			// 에러 처리
		}
	}

	private void selectPreviousButton() {
		try {
			String selectedFeedback = taskDAO.getSelectedFeedback(task.getTaskId(), inputMemberId, task.getProjectId());
			if (selectedFeedback != null) {
				JToggleButton selectedButton = null;
				switch (selectedFeedback) {
				case "Amazing":
					selectedButton = amazingButton;
					break;
				case "Good":
					selectedButton = goodButton;
					break;
				case "So So":
					selectedButton = soSoButton;
					break;
				case "Unsatisfactory":
					selectedButton = unsatisfactoryButton;
					break;
				case "Please Retry":
					selectedButton = retryButton;
					break;
				case "Error Detection":
					selectedButton = errorButton;
					break;
				}

				if (selectedButton != null) {
					selectedButton.setSelected(true);
					System.out.println(selectedFeedback);
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();

			// 에러 처리
		}
	}

	private void updatePreviousButton(String adviceName, int adviceScore) {
		try {
			String selectedFeedback = taskDAO.getSelectedFeedback(task.getTaskId(), inputMemberId, task.getProjectId());
			if (selectedFeedback != null) {
				taskDAO.updateFeedback(task.getTaskId(), inputMemberId, task.getProjectId(), adviceName, adviceScore);
				System.out.println("update Feedback for task ID: " + task.getTaskId() + " and member ID: " + inputMemberId);
			} else {
				System.out.println("머 없는디요 원래?");
			}
		} catch (SQLException ex) {
			ex.printStackTrace();

			// 에러 처리
		}
	}
}