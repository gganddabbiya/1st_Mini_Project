package com.kosa.swing;

import javax.swing.*;

import com.kosa.dao.MemberDAO;

import java.awt.CardLayout;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignUpUI extends JPanel implements ActionListener {
    private JTextField usernameField, emailField, nameField;
    private JPasswordField passwordField;
    private JButton signUpButton;
    private JButton backButton;
    
    public SignUpUI() {

        JPanel contentPanel = new JPanel(new BorderLayout());

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        contentPanel.add(backgroundPanel, BorderLayout.CENTER);

        JPanel signUpPanel = createSignUpPanel();
        JLabel signUpLabel = new JLabel("Sign Up", SwingConstants.CENTER); // 회원가입 글자 표시
        signUpLabel.setFont(new Font("Arial", Font.BOLD, 20)); // 글자 크기 및 폰트 설정
        signUpPanel.add(signUpLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        centerPanel.add(signUpPanel, gbc);

        contentPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // 왼쪽 정렬된 패널
        backButton = new JButton("<"); // 뒤로가기 버튼
        backButton.addActionListener(this); // 뒤로가기 버튼에 ActionListener 추가
        topPanel.add(backButton); // 뒤로가기 버튼 추가

        contentPanel.add(topPanel, BorderLayout.NORTH); // 전체 패널에 상단 패널 추가

        add(contentPanel);
        setVisible(true);
    }

    private JPanel createSignUpPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel contentPanel = new JPanel(new GridLayout(5, 2));
        contentPanel.setBackground(Color.WHITE);

        JLabel usernameLabel = new JLabel("아이디:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("비밀번호:");
        passwordField = new JPasswordField();
        JLabel emailLabel = new JLabel("이메일:");
        emailField = new JTextField();
        JLabel nameLabel = new JLabel("이름:");
        nameField = new JTextField();
        signUpButton = new JButton("가입하기");

        signUpButton.addActionListener(this);
        signUpButton.setBackground(new Color(200, 200, 255));

        contentPanel.add(usernameLabel);
        contentPanel.add(usernameField);
        contentPanel.add(passwordLabel);
        contentPanel.add(passwordField);
        contentPanel.add(emailLabel);
        contentPanel.add(emailField);
        contentPanel.add(nameLabel);
        contentPanel.add(nameField);
        contentPanel.add(new JLabel());
        contentPanel.add(signUpButton);

        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == signUpButton) {
            // 회원가입 처리
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String email = emailField.getText();
            String name = nameField.getText();

            // 회원가입 처리
            int result = MemberDAO.registerMember(username, password, email, name);

            if (result == 1) {
                JOptionPane.showMessageDialog(this, "회원가입이 완료되었습니다.");
                MainFrame.showCardPanel("loginUI");
            } else {
                JOptionPane.showMessageDialog(this, "이미 존재하는 아이디입니다.");
            }
        } else if (e.getSource() == backButton) {
        	MainFrame.showCardPanel("loginUI");
        }
    }

}
