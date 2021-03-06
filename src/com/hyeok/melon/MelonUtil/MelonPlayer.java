package com.hyeok.melon.MelonUtil;

import com.hyeok.melon.MelonSong;
import com.hyeok.melon.SocketUtil.CommandSender;
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
    private final Object playerLock = new Object();
    private indexSearchData songData;
    private SongStatus songStatus;
    private Player player;
    private CommandSender commandSender = new CommandSender();

    private enum SongStatus {RESUME, PAUSE, PLAY, FINISH}

    private MelonPlayer() {

    }

    public synchronized static MelonPlayer getInstance() {
        if (instance == null) {
            instance = new MelonPlayer();
        }
        return instance;
    }

    public void playSong(indexSearchData searchData) {
        stopSong();
        this.songData = searchData;
        this.songStatus = SongStatus.PLAY;
        playSongThread = new Thread(new playRunnable(searchData));
        playSongThread.start();
    }

    public indexSearchData getSongData() {
        return this.songData;
    }

    private class PlayNullException extends Exception {
        public PlayNullException() {
            super("Player is Null may be call StopSong Method");
        }
    }

    private class playRunnable implements Runnable {
        private indexSearchData searchData;
        private String bitrate;

        public playRunnable(indexSearchData searchData) {
            this.searchData = searchData;
        }

        private void broadcastPosition() throws PlayNullException {
            if (player == null) {
                throw new PlayNullException();
            }
            if (playerSeekListener != null) {
                playerSeekListener.getPosition((player.getPosition() / 1000) * Integer.parseInt(bitrate) * 1000 / 8);
            }
        }

        @Override
        public void run() {
            try {
                MelonSong melonSong = new MelonSong(MemberInfo.getInstance().getKeyCookie());
                melonSong.getSongData(this.searchData.getSID());
                Log(this.searchData.getId() + " : " + this.searchData.getSinger() + " - " + this.searchData.getSongName());
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
                Log("Content Length : " + urlConn.getContentLengthLong() + "bytes");
                if (playerSeekListener != null) {
                    playerSeekListener.startSong(0, urlConn.getContentLength());
                }
                commandSender.sendSID(this.searchData.getSID());
                final BufferedInputStream in = new BufferedInputStream(urlConn.getInputStream());
                player = new Player(in);
                while (songStatus != SongStatus.FINISH) {
                    if (!player.play(1)) {
                        playerSeekListener.finishSong();
                        player.close();
                        player = null;
                        break;
                    }
                    broadcastPosition();
                    if (songStatus == SongStatus.PAUSE) {
                        synchronized (playerLock) {
                            try {
                                playerLock.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (songStatus == SongStatus.RESUME) {
                        songStatus = SongStatus.PLAY;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JavaLayerException e) {
                e.printStackTrace();
            } catch (PlayNullException ignore) {
                /* May call stop song method */
            }
        }
    }

    public void pauseSong() {
        synchronized (playerLock) {
            Log("Pause Song");
            this.songStatus = SongStatus.PAUSE;
            playerLock.notify();
        }
    }

    public void resumeSong() {
        synchronized (playerLock) {
            Log("Resume Song");
            this.songStatus = SongStatus.RESUME;
            playerLock.notify();
        }
    }

    public void toggleSong() {
        if (playerSeekListener != null) {
            this.playerSeekListener.toggleSong();
        }
    }

    public void stopSong() {
        Log("Stop Song");
        if (playSongThread != null) {
            this.songStatus = SongStatus.FINISH;
            player = null;
            playSongThread = null;
        }
    }

    private void Log(String message) {
        String TAG = "MelonPlayer";
        Log.v(TAG, message);
    }

    public void setPlayerSeekListener(MelonPlayerSeekListener playerSeekListener) {
        this.playerSeekListener = playerSeekListener;
    }

    public void callfindNextSong() {
        if (this.playerSeekListener != null) {
            this.playerSeekListener.findNextSong();
        }
    }

    public void callfindPrevSong() {
        if (this.playerSeekListener != null) {
            this.playerSeekListener.findPrevSong();
        }
    }

    public interface MelonPlayerSeekListener {
        public void getPosition(int position);

        public void startSong(int start, int end);

        public void finishSong();

        public void initSong(String songName, String artistName);

        public void getAlbumartImage(BufferedImage albumartImage);

        public void toggleSong();

        public void findNextSong();

        public void findPrevSong();
    }
}
