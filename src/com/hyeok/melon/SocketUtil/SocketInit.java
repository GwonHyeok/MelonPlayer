package com.hyeok.melon.SocketUtil;

import com.hyeok.melon.MelonUtil.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class SocketInit extends Thread {
    private final int PORT = 5719;


    @Override
    public void run() {
        SocketInitiallize();
    }

    public void SocketInitiallize() {
        try {
            Random random = new Random();
            ServerSocket mServerSocket = new ServerSocket(PORT);
            while (true) {
                Socket mClientSocket = mServerSocket.accept();
//                MelonPlayer.NsUserNotificationsBridge.instance.sendNotification("클라이언트와 연결되었습니다.", "", mClientSocket.getInetAddress().toString(), 0);
                Log("클라이언트와 연결되었습니다.");
                String key = String.valueOf(random.nextInt());
                SocketClientUtil.getInstance().addClient(key, mClientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Log(String message) {
        String TAG = "SocketInit";
        Log.v(TAG, message);
    }
}
