package com.hyeok.melon.SocketUtil;

public class CommandSender {
    private final String ALBUMART_TAG = "ALBUMART";
    private final String SINGER_TAG = "SINGER";
    private final String SONG_TAG = "SONG";
    private final String SID_TAG = "SID";
    private final String VOL_TAG = "VOL";
    private final String PLAYLIST_TAG = "PLAYLIST";
    private final String PLAYLIST_REFRESH_TAG = "REFRESH_PLAYLIST";
    private final String KEYCOOKIE_TAG = "KEYCOOKIE";

    public void sendPlayList(String data) {
        sendData(PLAYLIST_TAG, data);
    }

    public void sendVol() {
//        Volume volume = new Volume();
//        sendData(VOL_TAG, String.valueOf(volume.getvolume()));
//        volume = null;
    }

    public void sendKeyCookie(String data) {
        sendData(KEYCOOKIE_TAG, data);
    }

    public void sendSinger(String data) {
        sendData(SINGER_TAG, data);
    }

    public void sendSong(String data) {
        sendData(SONG_TAG, data);
    }

    public void sendAlbumartURL(String data) {
        sendData(ALBUMART_TAG, data);
    }

    public void sendSID(String data) {
        sendData(SID_TAG, data);
    }

    public void sendRefreshPlaylist() {
        sendData(PLAYLIST_REFRESH_TAG, "");
    }

    private synchronized void sendData(String Tag, String data) {
        SocketClientUtil.getInstance().broadCastMessage("[" + Tag + "]" + data + "\n");
    }
}
