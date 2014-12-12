package com.hyeok.melon;

import com.hyeok.melon.UI.LoginFrame;
import com.hyeok.melon.UI.PlayerFrame;

/**
 * Created by GwonHyeok on 14. 12. 11..
 */
public class MelonMain {

    public static void main(String args[]) {

        final LoginFrame loginFrame = new LoginFrame();
        loginFrame.setLoginStatusListener(new LoginFrame.LoginStatus() {
            @Override
            public void isLoginSuccess(boolean isSuccess) {
                loginFrame.dispose();
                PlayerFrame playerFrame = new PlayerFrame();
                playerFrame.setVisible(true);
            }
        });
    }
}
