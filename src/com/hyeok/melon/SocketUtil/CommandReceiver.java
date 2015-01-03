package com.hyeok.melon.SocketUtil;

import com.hyeok.melon.MelonUtil.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class CommandReceiver extends Thread {
    private BufferedReader mReceivedBufferedReader;
    private RunCommand mRunCommand;
    private String key;

    public CommandReceiver(String key, Socket socket) throws IOException {
        this.mReceivedBufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.key = key;
        mRunCommand = RunCommand.getInstance();
    }

    @Override
    public void run() {
        while (true) {
            try {
                String command = mReceivedBufferedReader.readLine();
                if (!command.isEmpty()) {
                    mRunCommand.execute(command);
                }
            } catch (Exception e) {
                killsocket();
//                MelonPlayer.NsUserNotificationsBridge.instance.sendNotification("클라이언트와 연결이 끊겼습니다.", "", "", 0);
                Log("클라이언트와 연결이 끊겼습니다.");
//                e.printStackTrace();
                break;
            }
        }
    }

    private void killsocket() {
        try {
            mReceivedBufferedReader.close();
            SocketClientUtil.getInstance().removeClient(this.key);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Log(String message) {
        String TAG = "Socket-CommandReceiver";
        Log.v(TAG, message);
    }

}
