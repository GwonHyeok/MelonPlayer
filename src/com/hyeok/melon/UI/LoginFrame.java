package com.hyeok.melon.UI;

import com.hyeok.melon.Exception.LoginFailException;
import com.hyeok.melon.Melon;
import com.hyeok.melon.MelonUtil.DatabaseUtil;
import com.hyeok.melon.MelonUtil.MemberInfo;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by GwonHyeok on 14. 12. 11..
 */
public class LoginFrame extends JFrame {
    private JPanel rootPanel;
    private JPasswordField passwordField;
    private JCheckBox CheckBox;
    private JButton loginButton;
    private JTextField loginField;
    private LoginStatus loginStatus;

    public LoginFrame() {
        super();
        setContentPane(rootPanel);
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    @Override
                    public void run() {
                        doLoginWork();
                    }
                }.start();
            }
        });
    }

    private void doLoginWork() {
        String userid = loginField.getText();
        String userpw = new String(passwordField.getPassword());
        if (userid.isEmpty() || userpw.isEmpty()) {
            showDialog("아이디나 비밀번호를 입력해주세요.");
            return;
        }

        Melon melon = new Melon(userid, userpw);
        try {
            melon.Login();
            MemberInfo.getInstance().setKeyCookie(melon.getKeyCookie());
            loginStatus.isLoginSuccess(true);
            if (CheckBox.isSelected()) {
                DatabaseUtil.getInstance().updateLoginData();
            }
            System.out.println("Success To Login");
            System.out.println("melon KeyCookie : " + melon.getKeyCookie());
        } catch (LoginFailException e) {
            showDialog("로그인 실패");
            e.printStackTrace();
        }
    }

    private void showDialog(String title) {
        BasicDialog dialog = new BasicDialog();
        dialog.setDialogTitle(title);
        dialog.setVisible(true);
    }

    public void setLoginStatusListener(LoginStatus loginStatus) {
        this.loginStatus = loginStatus;
    }

    public interface LoginStatus {
        public void isLoginSuccess(boolean isSuccess);
    }
}
