package com.hyeok.melon.SocketUtil;

import com.hyeok.melon.MelonUtil.Log;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by GwonHyeok on 15. 1. 3..
 */
public class SocketClientUtil {
    private HashMap<String, Socket> socketHashMap = new HashMap<String, Socket>();

    private static SocketClientUtil instance;

    private SocketClientUtil() {
    }

    public synchronized static SocketClientUtil getInstance() {
        if (instance == null) {
            instance = new SocketClientUtil();
        }
        return instance;
    }

    public void addClient(String key, Socket clientSocket) {
        Log("add Client - key : " + key + " ip : " + clientSocket.getInetAddress());
        socketHashMap.put(key, clientSocket);

        try {
            CommandReceiver commandreceiver = new CommandReceiver(key, clientSocket);
            commandreceiver.start();
        } catch (IOException ignore) {
            removeClient(key);
        }
    }

    public void removeClient(String key) {
        Socket socket = socketHashMap.get(key);
        socketHashMap.remove(key);
        Log("remove Client - key : " + key + " ip : " + socket.getInetAddress());
    }

    public void broadCastMessage(String message) {
        String key = null;
        Socket socket = null;
        for (Map.Entry<String, Socket> entry : socketHashMap.entrySet()) {
            try {
                key = entry.getKey();
                socket = entry.getValue();
                Log("BroadCastMessage - key : " + key + " ip : " + socket.getInetAddress());
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
                bufferedOutputStream.write(message.getBytes());
                bufferedOutputStream.flush();
            } catch (IOException e) {
                removeClient(key);
            }
        }

    }

    public void Log(String message) {
        String TAG = "SocketClientUtil";
        Log.v(TAG, message);
    }
}
