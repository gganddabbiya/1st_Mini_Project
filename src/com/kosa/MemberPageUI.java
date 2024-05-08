package com.kosa;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class MemberPageUI extends JFrame {
	private JPanel mainPanel;
    private JButton backButton;
    private JLabel titleLabel;
    private JLabel teamMemberLabel;
    private JLabel teamMemberImageLabel;
    private JSeparator separator;
    private JLabel totalTimeLabel;
    private JLabel progressLabel;
    private JProgressBar progressBar;
    private JPanel taskPanel;
    private JScrollPane taskScrollPane;
    private JPanel priorityPanel;
    private JLabel priorityLabel;
    private JComboBox<String> priorityComboBox;
    private JButton addTaskButton;
    private JLabel userIDLabel; // 사용자 아이디를 표시할 레이블 추가


    private int memberID;
    private MemberPageDAO memberPageDAO;

    public MemberPageUI(int memberID) {
        this.memberID = memberID;
        this.memberPageDAO = new MemberPageDAO(); // 생성자를 수정하여 새로운 Connection 객체를 사용하도록 변경
        initializeUI();
        fetchMemberTasks(); // 페이지가 열릴 때 멤버의 태스크를 가져옴
        updateUserIDLabel(); // 사용자 아이디를 업데이트하여 표시
    }

    private void initializeUI() {
        setTitle("Member Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        backButton = new JButton("Back");
        titleLabel = new JLabel("To Do List", SwingConstants.CENTER);
        teamMemberLabel = new JLabel("Team Member: ");
        teamMemberImageLabel = new JLabel(new ImageIcon("image/profile.png"));
        separator = new JSeparator();
        totalTimeLabel = new JLabel("Total Time: ");
        progressLabel = new JLabel("Progress: ", SwingConstants.LEFT);
        progressBar = new JProgressBar();
        taskPanel = new JPanel();
        taskPanel.setLayout(new BoxLayout(taskPanel, BoxLayout.Y_AXIS));
        taskScrollPane = new JScrollPane(taskPanel);
        priorityPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        priorityLabel = new JLabel("Priority:");
        priorityComboBox = new JComboBox<>(new String[]{"High", "Medium", "Low"});
        addTaskButton = new JButton("Add Task");
     // 사용자 아이디를 표시할 레이블 생성
        userIDLabel = new JLabel("", SwingConstants.RIGHT);
        userIDLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10)); // 오른쪽 여백 추가

        // 타이틀 레이블과 사용자 아이디 레이블을 담을 패널 생성
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.add(userIDLabel, BorderLayout.EAST);

        // 전체 프레임에 타이틀 패널 추가
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        // Add action listener to addTaskButton
        addTaskButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Show modal dialog to add task
                showAddTaskDialog();
            }
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.CENTER);

        JPanel teamMemberPanel = new JPanel(new BorderLayout());
        teamMemberPanel.add(teamMemberLabel, BorderLayout.SOUTH);
        teamMemberPanel.add(teamMemberImageLabel, BorderLayout.CENTER);

        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.add(totalTimeLabel);
        sidePanel.add(progressLabel);
        sidePanel.add(progressBar);
        sidePanel.add(teamMemberPanel);

        priorityPanel.add(priorityLabel);
        priorityPanel.add(priorityComboBox);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(addTaskButton);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(separator, BorderLayout.CENTER);
        mainPanel.add(sidePanel, BorderLayout.WEST);
        mainPanel.add(taskScrollPane, BorderLayout.CENTER);
        mainPanel.add(priorityPanel, BorderLayout.SOUTH);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }
    private void updateUserIDLabel() {
        String userID = Session.getUserID();
        if (userID != null) {
            userIDLabel.setText("User ID: " + userID);
        } else {
            userIDLabel.setText("User ID: Unknown");
        }
    }
    private void fetchMemberTasks() {
        taskPanel.removeAll(); // 이전 태스크를 모두 제거

        // 멤버의 태스크를 DAO를 통해 가져옴
        memberPageDAO.fetchMemberTasks(memberID);

        // 가져온 태스크를 UI에 표시하는 코드를 추가할 수 있습니다.
        // 여기서는 DAO 클래스 내부에서 직접 UI를 조작하는 대신
        // 데이터를 반환하고 UI에서 표시하는 방법을 선택했습니다.
    }

    private void showAddTaskDialog() {
        // Create modal dialog
        JDialog dialog = new JDialog(this, "Add Task", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(null);

        // Panel for task details
        JPanel taskDetailsPanel = new JPanel(new GridLayout(3, 1));
        JTextField taskNameField = new JTextField();
        JPanel priorityPanel = new JPanel();
        JLabel priorityLabel = new JLabel("Priority:");
        JComboBox<String> priorityComboBox = new JComboBox<>(new String[]{"High", "Medium", "Low"});
        priorityPanel.add(priorityLabel);
        priorityPanel.add(priorityComboBox);
        taskDetailsPanel.add(new JLabel("Task Name:"));
        taskDetailsPanel.add(taskNameField);
        taskDetailsPanel.add(priorityPanel);

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get task details
                String taskName = taskNameField.getText();
                String priority = (String) priorityComboBox.getSelectedItem();

                // Close dialog
                dialog.dispose();
                
                // Add task to task list
                addTask(taskName, priority);

            }
        });
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close dialog
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
        // Create modal dialog
        JDialog dialog = new JDialog(this, "Add Task", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(null);

        // Panel for task details
        JPanel taskDetailsPanel = new JPanel(new GridLayout(3, 1));
        JTextField taskNameField = new JTextField();
        JPanel priorityPanel = new JPanel();
        JLabel priorityLabel = new JLabel("Priority:");
        JComboBox<String> priorityComboBox = new JComboBox<>(new String[]{"High", "Medium", "Low"});
        priorityPanel.add(priorityLabel);
        priorityPanel.add(priorityComboBox);
        taskDetailsPanel.add(new JLabel("Task Name:"));
        taskDetailsPanel.add(taskNameField);
        taskDetailsPanel.add(priorityPanel);

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get task details
                String taskName = taskNameField.getText();
                String priority = (String) priorityComboBox.getSelectedItem();

                // Close dialog
                dialog.dispose();
                
                // Add task to task list using MemberPageDAO
                memberPageDAO.addTask(taskName, priority, memberID, 1); // Assuming project ID is 1 for demo

                // Refresh UI (if needed)
            }
        });
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close dialog
                dialog.dispose();
            }
        });
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);

        dialog.add(taskDetailsPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    
    private void updateTaskCompletion(int taskId, int completion) {
        memberPageDAO.updateTaskCompletion(taskId, completion);
    }

    public static void main(String[] args) {
    	int memberID = 2; // 적절한 멤버 ID를 지정합니다.

        // MemberPageUI 인스턴스를 생성하여 UI를 표시합니다.
        SwingUtilities.invokeLater(() -> {
            MemberPageUI memberPageUI = new MemberPageUI(memberID);
            memberPageUI.setVisible(true);
        });
    }
}
