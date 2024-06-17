package com.kosa.swing;

import javax.swing.*;

import com.kosa.dao.LoginDAO;
import com.kosa.session.Session;

import java.awt.*;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginUI extends JPanel implements ActionListener {
    private JTextField useridField;
    private JPasswordField passwordField;
    private RoundedButton loginButton;
    private RoundedButton signUpButton;

    public LoginUI() {
    	setLayout(new BorderLayout());

        // 배경색이 있는 패널 추가
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.WHITE); // 배경색 설정
                g.fillRect(0, 0, getWidth(), getHeight()); // 패널 크기에 맞게 색 채우기
            }
        };
        
        add(backgroundPanel, BorderLayout.CENTER);        
        

        // 로그인 패널 생성
        JPanel loginPanel = createLoginPanel();
        // 로그인 패널에 레이블 추가
        JLabel loginLabel = new JLabel("Login", SwingConstants.CENTER);
        loginLabel.setFont(new Font("Arial", Font.BOLD, 20));
        loginPanel.add(loginLabel, BorderLayout.NORTH);

        // 가운데 정렬을 위한 패널 생성
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        //gbc.insets = new Insets(5, 5, 5, 5); // 여백 설정

        // 패널에 로그인 패널 추가
        centerPanel.add(loginPanel, gbc);

        // 프레임에 전체 패널 추가
        add(centerPanel, BorderLayout.CENTER);

        setVisible(true);

    } 

    private JPanel createLoginPanel(){
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 패딩 설정

        JPanel contentPanel = new JPanel(new GridLayout(3, 1));
        contentPanel.setBackground(Color.WHITE);

        JLabel useridLabel = new JLabel("아이디:");
        useridField = new JTextField();
        JLabel passwordLabel = new JLabel("비밀번호:");
        passwordField = new JPasswordField();
        loginButton = new RoundedButton("로그인");
        signUpButton = new RoundedButton("회원가입");

        loginButton.addActionListener(this);
        signUpButton.addActionListener(this);
        loginButton.setBackground(new Color(200, 200, 255));
        signUpButton.setBackground(new Color(200, 255, 200));

        contentPanel.add(useridLabel);
        contentPanel.add(useridField);
        contentPanel.add(passwordLabel);
        contentPanel.add(passwordField);
        contentPanel.add(loginButton);
        contentPanel.add(signUpButton);

        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String userid = useridField.getText();
            String password = new String(passwordField.getPassword());

            // 로그인 실행
            int loginResult = LoginDAO.login(userid, password);

            // 로그인 결과 확인
            if (loginResult == 1) {
                JOptionPane.showMessageDialog(this, "로그인 성공!");
                Session.setUserID(userid);

                System.out.println("loginUI : " + Session.getUserID());
                
                MainPanel mainPanel = new MainPanel();
                if(!MainFrame.isCardPanel(mainPanel))
                	MainFrame.addCardPanel(mainPanel, "mainPanel");
                MainFrame.showCardPanel("mainPanel");
            } else {
                JOptionPane.showMessageDialog(this, "로그인 실패. 아이디 또는 비밀번호를 확인하세요.");
            }
            useridField.setText("");
            passwordField.setText("");
        }
        
        else if (e.getSource()==signUpButton) {
        	SignUpUI signUpUI = new SignUpUI();
            if(!MainFrame.isCardPanel(signUpUI))
            	MainFrame.addCardPanel(signUpUI, "signUpUI");
        	MainFrame.showCardPanel("signUpUI");
        }
    }
    
}
