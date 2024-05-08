package com.kosa;

import javax.swing.SwingUtilities;

public class main {

	public static void main(String[] args) {
		
		Connection connection = null;
		//LoginUI loginUI = new LoginUI();
		//SignUpUI signupUI = new SignUpUI();
		SwingUtilities.invokeLater(() -> {
            MemberPageUI memberPageUI = new MemberPageUI(2, connection);
            memberPageUI.setVisible(true);
        });

		
	}

}
