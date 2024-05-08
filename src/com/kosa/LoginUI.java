package com.kosa;

import javax.swing.*;
import java.awt.*;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginUI extends JFrame implements ActionListener {
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton signUpButton;
    private String userID; // 사용자 ID를 저장할 변수 추가


    public LoginUI() {
	setTitle("로그인");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 전체 프레임의 패널
        JPanel contentPanel = new JPanel(new BorderLayout());

        // 배경색이 있는 패널 추가
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.WHITE); // 배경색 설정
                g.fillRect(0, 0, getWidth(), getHeight()); // 패널 크기에 맞게 색 채우기
            }
        };
        
        contentPanel.add(backgroundPanel, BorderLayout.CENTER);        // 로그인 버튼에 ActionListener 등록
        

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

        // 패널을 전환할 수 있는 CardLayout 추가
        cardPanel = new JPanel();
        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);

        cardPanel.add(centerPanel, "login");

        // 프레임에 전체 패널 추가
        contentPanel.add(cardPanel, BorderLayout.CENTER);

        add(contentPanel);
        setVisible(true);

    }

    

    private JPanel createLoginPanel(){
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 패딩 설정

        JPanel contentPanel = new JPanel(new GridLayout(3, 1));
        contentPanel.setBackground(Color.WHITE);

        JLabel usernameLabel = new JLabel("아이디:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("비밀번호:");
        passwordField = new JPasswordField();
        loginButton = new JButton("로그인");
        signUpButton = new JButton("회원가입");

        loginButton.addActionListener(this);
        signUpButton.addActionListener(this);
        loginButton.setBackground(new Color(200, 200, 255));
        signUpButton.setBackground(new Color(200, 255, 200));

        contentPanel.add(usernameLabel);
        contentPanel.add(usernameField);
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
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            // 로그인 실행
            int loginResult = LoginDAO.login(username, password);

            // 로그인 결과 확인
            if (loginResult == 1) {
                JOptionPane.showMessageDialog(this, "로그인 성공!");
                Session.setUserID(username);
                // 로그인 성공 시 다음 화면으로 이동하는 코드 작성
                // 예를 들어, MemberPageUI 클래스의 인스턴스를 생성하여 보여줄 수 있습니다.
                //MemberPageUI memberPageUI = new MemberPageUI();
                //memberPageUI.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "로그인 실패. 아이디 또는 비밀번호를 확인하세요.");
            }
        }
        
        else if (e.getSource()==signUpButton) {
        	
        	cardLayout.show(cardPanel, "signup");
        }
    }
    
    // 사용자 ID를 가져오는 메서드 추가
    public String getUserID() {
        return userID;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginUI::new);
    }

}
