package com.hyeok.melon;

import com.hyeok.melon.MelonUtil.DatabaseUtil;
import com.hyeok.melon.UI.LoginFrame;
import com.hyeok.melon.UI.PlayerFrame;

/**
 * Created by GwonHyeok on 14. 12. 11..
 */
public class MelonMain {

    public static void main(String args[]) {
        boolean isLogedIn = false;

        /* 데이터베이스 파일 확인 성공 */
        if (DatabaseUtil.getInstance().checkDatabaseFile()) {
            if (DatabaseUtil.getInstance().connectDatabase()) {
                isLogedIn = DatabaseUtil.getInstance().loginStatusCheck();
            }
        }

        final LoginFrame loginFrame = new LoginFrame();
        final PlayerFrame playerFrame = new PlayerFrame();
        if (!isLogedIn) {
            loginFrame.setLoginStatusListener(new LoginFrame.LoginStatus() {
                @Override
                public void isLoginSuccess(boolean isSuccess) {
                    loginFrame.dispose();
                    playerFrame.setVisible(true);
                }
            });
            loginFrame.setVisible(true);
        } else {
            playerFrame.setVisible(true);
        }
    }
}
