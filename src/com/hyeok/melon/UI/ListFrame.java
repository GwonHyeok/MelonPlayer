package com.hyeok.melon.UI;

import com.hyeok.melon.CustomSwingContent.MelonPlayListTableModel;
import com.hyeok.melon.MelonUtil.DatabaseUtil;
import com.hyeok.melon.MelonUtil.MelonPlayer;
import com.hyeok.melon.MelonUtil.indexSearchData;
import com.hyeok.melon.SearchData;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Created by GwonHyeok on 14. 12. 12..
 */
public class ListFrame extends JFrame {
    private JPanel panel1;
    private JButton searchButton;
    private JButton button2;
    private JTable table1;
    private static ListFrame instance;

    private ListFrame() {
        setContentPane(panel1);
        setSize(340, 600);
        setComponentSpace();
        setButtonListener();
        setTableData();
        setTableListener();
        setResizable(false);
    }

    private void setTableData() {
        ArrayList<indexSearchData> datas = DatabaseUtil.getInstance().getPlaylistData();
        table1.setModel(new MelonPlayListTableModel(datas));
    }

    private void setTableListener() {
        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() == 2) {
                    SearchData searchData = ((MelonPlayListTableModel) table1.getModel()).getSelectedData(table1.getSelectedRow());
                    MelonPlayer.getInstance().playSong(searchData);
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    ArrayList<indexSearchData> datas = ((MelonPlayListTableModel) table1.getModel()).getSelectDataIndex(table1.getSelectedRows());
                    DatabaseUtil.getInstance().deletePlayList(datas);
                    refreshTableData();
                }
            }
        });
    }

    public void refreshTableData() {
        setTableData();
        repaint();
    }

    private void setComponentSpace() {
        searchButton.setBounds(254, 0, 40, 30);
    }

    private void setButtonListener() {
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SearchFrame searchFrame = SearchFrame.getInstance();
                searchFrame.setVisible(!searchFrame.isVisible());
            }
        });
    }

    public static synchronized ListFrame getInstance() {
        if (instance == null) {
            instance = new ListFrame();
        }
        return instance;
    }
}
