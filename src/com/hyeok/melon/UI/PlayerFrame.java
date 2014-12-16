package com.hyeok.melon.UI;

import com.hyeok.melon.CustomSwingContent.RealtimeBackgroundPanel;
import com.hyeok.melon.ImageFilterUtil.BoxBlurFilter;
import com.hyeok.melon.MelonUtil.DatabaseUtil;
import com.hyeok.melon.MelonUtil.Log;
import com.hyeok.melon.MelonUtil.MelonPlayer;
import com.hyeok.melon.MelonUtil.indexSearchData;
import com.hyeok.melon.SearchData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

/**
 * Created by GwonHyeok on 14. 12. 11..
 */
public class PlayerFrame extends JFrame {
    private JButton play_pause_button;
    private JPanel rootPanel;
    private JLabel titleLabel;
    private JLabel artistLabel;
    private JLabel albumartLabel;
    private JButton nextButton;
    private JButton prevButton;
    private JButton playlistButton;
    private JSlider musicSeekbar;
    private RealtimeBackgroundPanel backgroundPanel;

    public PlayerFrame() {
        super();
        setContentPane(rootPanel);
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setContentSpace();
        setContentImage();
        setButtonListener();
        setSearchlistListener();
        setPlayerListener();
        setSize(340, 600);
    }

    private void setPlayerListener() {
        MelonPlayer.getInstance().setPlayerSeekListener(new MelonPlayer.MelonPlayerSeekListener() {
            @Override
            public void getPosition(int position) {
                musicSeekbar.setValue(position);
            }

            @Override
            public void startSong(int start, int end) {
                musicSeekbar.setMinimum(start);
                musicSeekbar.setMaximum(end);
            }

            @Override
            public void finishSong(indexSearchData songData) {
                int currentID = songData.getId();
                int nextSongID = DatabaseUtil.getInstance().getNextSongID(currentID);
                if (nextSongID == -1) {
                    musicSeekbar.setValue(0);
                    titleLabel.setText("");
                    artistLabel.setText("");
                    backgroundPanel.setBackgroundImage(null);
                    albumartLabel.setIcon(null);
                } else {
                    MelonPlayer.getInstance().playSong(DatabaseUtil.getInstance().getSearchDataWithID(nextSongID));
                }
                repaint();
            }

            @Override
            public void initSong(String songName, String artistName) {
                titleLabel.setText(songName);
                artistLabel.setText(artistName);
            }

            @Override
            public void getAlbumartImage(BufferedImage albumartImage) {
                Image albumartimage = albumartImage.getScaledInstance(310,
                        310, Image.SCALE_SMOOTH);
                albumartLabel.setIcon(new ImageIcon(albumartimage));
                long startTime = System.currentTimeMillis();
                Log("Blur start Time = " + startTime);
                BoxBlurFilter boxBlurFilter = new BoxBlurFilter();
                boxBlurFilter.setRadius(100);
                boxBlurFilter.filter(albumartImage, albumartImage);
                long endTime = System.currentTimeMillis();
                Log("Blur end Time = " + endTime);
                Log("Blur time is " + (endTime - startTime) + "ms");
                backgroundPanel.setBackgroundImage(albumartImage);
                invalidate();
                repaint();
            }
        });
    }

    private void setSearchlistListener() {
        SearchFrame.getInstance().setlistClickListener(new SearchFrame.listClickListener() {
            @Override
            public void selectNewPlaySong(final SearchData songData) {
                Log("clicked Song Name : " + songData.getSongName());
                DatabaseUtil.getInstance().insertSongData(songData);
                ListFrame.getInstance().refreshTableData();
            }
        });
    }

    /*
       각 컨텐츠들을 이전 멜론플레이어에서 사용했던 그 위치에 지정
     */
    private void setContentSpace() {
        rootPanel.setLayout(null);
        titleLabel.setBounds(0, 26, 340, 16);
        artistLabel.setBounds(0, 46, 340, 16);
        albumartLabel.setBounds(15, 65, 310, 330);
        play_pause_button.setBounds(150, 421, 40, 40);
        nextButton.setBounds(250, 432, 35, 20);
        prevButton.setBounds(50, 432, 35, 20);
        playlistButton.setBounds(300, 0, 40, 30);
        musicSeekbar.setBounds(49, 402, 240, 3);

        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        artistLabel.setHorizontalAlignment(SwingConstants.CENTER);

        albumartLabel.setBackground(new Color(123, 123, 123));

        /* 엘범아트의 실시간 배경 백그라운드 이미지 패널 */
        backgroundPanel = new RealtimeBackgroundPanel();
        backgroundPanel.setBounds(0, 0, 340, 600);
        rootPanel.add(backgroundPanel);
        rootPanel.setBackground(null);
    }

    private void Log(String message) {
        String TAG = "PlayerFrame";
        Log.v(TAG, message);
    }

    private void setButtonListener() {
        playlistButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ListFrame listFrame = ListFrame.getInstance();
                listFrame.setVisible(!listFrame.isVisible());
            }
        });
    }

    private void setContentImage() {
        Class<PlayerFrame> playerFrameClass = PlayerFrame.class;
    }


}
