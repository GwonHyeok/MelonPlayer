package com.hyeok.melon.SocketUtil;

import com.hyeok.melon.MelonUtil.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class RunCommand {
    private static RunCommand instance;
    private CommandSender sender = new CommandSender();
    public static final String PLAYLIST = "[PLAYLIST]";
    public static final String REFRESH_PLAYLIST = "[REFRESH_PLAYLIST]";


    private RunCommand() {
    }

    public synchronized static RunCommand getInstance() {
        if (instance == null) {
            instance = new RunCommand();
        }
        return instance;
    }

    public void execute(String command) {
        Log("Execute : " + command);
        if (command.equals("next")) {
            MelonPlayer.getInstance().callfindNextSong();
        } else if (command.equals("prev")) {
            MelonPlayer.getInstance().callfindPrevSong();
        } else if (command.equals("toggle")) {
            MelonPlayer.getInstance().toggleSong();
        } else if (command.contains("[VOL]")) {
//            Volume volume = new Volume();
//            volume.setvolume(Integer.parseInt(command.split("]")[1]));
//            MelonPlayer.ChangetVolumeSlider();
        } else if (command.equals(PLAYLIST)) {
            ArrayList<indexSearchData> data = DatabaseUtil.getInstance().getPlaylistData();
            JSONArray jsonArray = new JSONArray();
            for (indexSearchData searchData : data) {
                JSONObject jsonObject = new JSONObject();
                HashMap<String, String> mapData = new HashMap<String, String>();
                mapData.put("id", String.valueOf(searchData.getId()));
                mapData.put("sid", searchData.getSID());
                mapData.put("singer", searchData.getSinger());
                mapData.put("song", searchData.getSongName());
                mapData.put("albumart", searchData.getAlbumart());
                jsonObject.putAll(mapData);
                jsonArray.add(jsonObject);
            }
            sender.sendPlayList(jsonArray.toJSONString());
        } else if (command.equals(REFRESH_PLAYLIST)) {
            sender.sendRefreshPlaylist();
        } else if (command.contains("[PLSINDEX]")) {
            indexSearchData searchData = DatabaseUtil.getInstance().getSearchDataWithID(Integer.parseInt(command.split("]")[1]));
            MelonPlayer.getInstance().playSong(searchData);
        } else if (command.contains("[INIT]")) {
            sender.sendKeyCookie(MemberInfo.getInstance().getKeyCookie());
            indexSearchData data = MelonPlayer.getInstance().getSongData();
            if (data != null) {
                sender.sendSID(data.getSID());
            }
        }
    }

    public void Log(String message) {
        String TAG = "RunCommand";
        Log.v(TAG, message);
    }
}
