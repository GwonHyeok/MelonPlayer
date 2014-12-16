package com.hyeok.melon.MelonUtil;

import com.hyeok.melon.SearchData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by GwonHyeok on 14. 12. 14..
 */
public class DatabaseUtil {
    private static DatabaseUtil instance;
    private Connection connection;

    private DatabaseUtil() {

    }

    public synchronized static DatabaseUtil getInstance() {
        synchronized (DatabaseUtil.class) {
            if (instance == null) {
                instance = new DatabaseUtil();
            }
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

    public void insertSongData(SearchData songData) {
        try {
            int row = 1;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM playlist");
            while (resultSet.next()) {
                row++;
            }
            statement.execute("INSERT INTO playlist VALUES ('" + row + "', '" + songData.getSID() + "', '" + songData.getSongName()
                    + "', '" + songData.getSinger() + "', '" + songData.getAlbumart() + "');");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePlayList(ArrayList<indexSearchData> datas) {
        try {
            for (indexSearchData rowData : datas) {
                String sql = "DELETE FROM playlist WHERE id=" + rowData.getId() + ";";
                System.out.println(sql);
                Statement statement = connection.createStatement();
                statement.execute(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<indexSearchData> getPlaylistData() {
        ArrayList<indexSearchData> datas = new ArrayList<indexSearchData>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM playlist");
            while (resultSet.next()) {
                datas.add(new indexSearchData(resultSet.getInt(1), resultSet.getString(3), resultSet.getString(2), resultSet.getString(5), resultSet.getString(4)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return datas;
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

    public int getNextSongID(int currentSongID) {
        int nextSongID = -1;
        try {
            String sql = "SELECT * FROM playlist WHERE id > " + currentSongID;
            Statement statement = connection.createStatement();
            ResultSet resultset = statement.executeQuery(sql);
            if (resultset.next()) {
                nextSongID = resultset.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nextSongID;
    }

    public int getPrevSongID(int currentSongID) {
        int prevSongID = -1;
        try {
            String sql = "SELECT * FROM playlist WHERE id < " + currentSongID + " ORDER BY id DESC";
            Statement statement = connection.createStatement();
            ResultSet resultset = statement.executeQuery(sql);
            if (resultset.next()) {
                prevSongID = resultset.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prevSongID;
    }

    public indexSearchData getSearchDataWithID(int id) {
        indexSearchData songData = null;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM playlist WHERE id = " + id);
            if (resultSet.next()) {
                songData = new indexSearchData(resultSet.getInt(1), resultSet.getString(3), resultSet.getString(2), resultSet.getString(5), resultSet.getString(4));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return songData;
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
                InputStream fileinput = getClass().getResourceAsStream("/res/database/MelonPlayer.db");
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
        Log.v(TAG, message);
    }
}
