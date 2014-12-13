package com.hyeok.melon.MelonUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;

/**
 * Created by GwonHyeok on 14. 12. 14..
 */
public class DatabaseUtil {
    private static DatabaseUtil instance;
    private Connection connection;

    private DatabaseUtil() {

    }

    public synchronized static DatabaseUtil getInstance() {
        if (instance == null) {
            instance = new DatabaseUtil();
        }
        return instance;
    }

    public boolean loginStatusCheck() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultset = statement.executeQuery("SELECT `keyCookie` FROM `userinfo`");
            String keyCookie = null;
            int rowCount = 0;

            while (resultset.next()) {
                keyCookie = resultset.getString(1);
                rowCount++;
            }
            Log("Login row Count : " + rowCount);
            if (rowCount > 0) {
                MemberInfo.getInstance().setKeyCookie(keyCookie);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public void updateLoginData() {
        try {
            Statement statement = connection.createStatement();
            statement.execute("DELETE FROM userinfo");
            statement.execute("INSERT INTO userinfo VALUES(" + MemberInfo.getInstance().getKeyCookie() + ");");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public boolean connectDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:MelonPlayer.db");
            Log("Connection Success");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            Log("Connection Failed Database");
            return false;
        }
    }

    public boolean checkDatabaseFile() {
        try {
            File databaseFile = new File("./MelonPlayer.db");
            if (databaseFile.isFile()) {
                Log("DatabaseFile Exist");
            } else {
                FileInputStream fileinput = new FileInputStream(DatabaseUtil.class.getResource("/res/database/MelonPlayer.db").getFile());
                FileOutputStream fileOutput = new FileOutputStream(databaseFile);
                byte[] buffer = new byte[1024];
                int read_bytes;
                while ((read_bytes = fileinput.read(buffer)) != -1) {
                    fileOutput.write(buffer, 0, read_bytes);
                }
                Log("Copy Finished");
                if (databaseFile.setWritable(true) && databaseFile.setReadable(true)) {
                    Log("DatabaseFile set Write & Read able");
                }
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    private void Log(String message) {
        String TAG = "DatabaseUtil";
        System.out.println(TAG + " : " + message);
    }
}
