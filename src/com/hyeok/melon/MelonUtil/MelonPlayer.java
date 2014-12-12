package com.hyeok.melon.MelonUtil;

import com.hyeok.melon.MelonSearch;
import com.hyeok.melon.MelonSong;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by GwonHyeok on 14. 12. 13..
 */
public class MelonPlayer {
    public static MelonPlayer instance;
    private MelonPlayerSeekListener playerSeekListener;
    private Thread playSongThread;
    private Player player;

    private MelonPlayer() {

    }

    public synchronized static MelonPlayer getInstance() {
        if (instance == null) {
            instance = new MelonPlayer();
        }
        return instance;
    }

    public void playSong(MelonSearch.SearchData searchData) {
        stopSong();
        playSongThread = new Thread(new playRunnable(searchData));
        playSongThread.start();
    }

    public void stopSong() {
        System.out.println("interruptSong");
        if (playSongThread != null) {
            player.close();
            playSongThread = null;
        }
    }

    private class playRunnable implements Runnable {
        private MelonSearch.SearchData searchData;
        private String bitrate;

        public playRunnable(MelonSearch.SearchData searchData) {
            this.searchData = searchData;
        }

        private void broadcastPosition() {
            new Thread() {
                @Override
                public void run() {
                    while (!player.isComplete()) {
                        playerSeekListener.getPosition((player.getPosition() / 1000) * Integer.parseInt(bitrate) * 1000 / 8);
                    }
                }
            }.start();
        }

        @Override
        public void run() {
            try {
                MelonSong melonSong = new MelonSong(MemberInfo.getInstance().getKeyCookie());
                melonSong.getSongData(this.searchData.getSID());
                Image image = ImageIO.read(new URL(melonSong.getAlbumArtURL()));
                BufferedImage buffered = (BufferedImage) image;
                if (playerSeekListener != null) {
                    playerSeekListener.getAlbumartImage(buffered);
                    playerSeekListener.initSong(searchData.getSongName(), searchData.getSinger());
                }
                bitrate = melonSong.getBitrate();
                URL url = new URL(melonSong.getMusicURL());
                final HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setConnectTimeout(3000);
                urlConn.setReadTimeout(3000);
                urlConn.setUseCaches(false);
                urlConn.setRequestMethod("GET");
                urlConn.setRequestProperty("Range", "bytes=0");
                urlConn.setInstanceFollowRedirects(false);
                System.out.println("Content Length : " + urlConn.getContentLengthLong());
                if (playerSeekListener != null) {
                    playerSeekListener.startSong(0, urlConn.getContentLength());
                }
                final BufferedInputStream in = new BufferedInputStream(urlConn.getInputStream());
                player = new Player(in);
                broadcastPosition();
                player.play();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JavaLayerException e) {
                e.printStackTrace();
            }

        }
    }

    public void setPlayerSeekListener(MelonPlayerSeekListener playerSeekListener) {
        this.playerSeekListener = playerSeekListener;
    }

    public interface MelonPlayerSeekListener {
        public void getPosition(int position);

        public void startSong(int start, int end);

        public void initSong(String songName, String artistName);

        public void getAlbumartImage(BufferedImage albumartImage);
    }
}
