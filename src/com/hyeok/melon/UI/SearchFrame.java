package com.hyeok.melon.UI;

import com.hyeok.melon.CustomSwingContent.MelonSearchCellRenderer;
import com.hyeok.melon.CustomSwingContent.MelonSearchListModel;
import com.hyeok.melon.MelonSearch;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by GwonHyeok on 14. 12. 12..
 */
public class SearchFrame extends JFrame {
    private JRadioButton AccuracyradioButton;
    private JRadioButton RecentradioButton;
    private JRadioButton PopularradioButton;
    private JList<MelonSearch.SearchData> list1;
    private JTextField songTextField;
    private JButton SearchButton;
    private JPanel rootPanel;
    private static SearchFrame instance;
    private listClickListener clickListener;

    private SearchFrame() {
        super();
        setSize(340, 600);
        setContentPane(rootPanel);
        setButtonListener();
        setRadioGroup();
        setResizable(false);
    }

    private void setRadioGroup() {
        ButtonGroup radioButtonGroup = new ButtonGroup();
        radioButtonGroup.add(AccuracyradioButton);
        radioButtonGroup.add(RecentradioButton);
        radioButtonGroup.add(PopularradioButton);
        radioButtonGroup.setSelected(PopularradioButton.getModel(), true);
    }

    private void setButtonListener() {
        SearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchMelonSong(songTextField.getText());
            }
        });

        list1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() == 2) {
                    clickListener.selectNewPlaySong(list1.getSelectedValue());
                }
            }
        });
    }

    private void searchMelonSong(final String songName) {
        new Thread() {
            @Override
            public void run() {
                MelonSearch melonSearch = MelonSearch.getinstance();
                if (AccuracyradioButton.isSelected()) {
                    melonSearch.setOrder(MelonSearch.ACCURACY);
                } else if (PopularradioButton.isSelected()) {
                    melonSearch.setOrder(MelonSearch.POPULAR);
                } else if (RecentradioButton.isSelected()) {
                    melonSearch.setOrder(MelonSearch.RECENT);
                }
                melonSearch.setSongName(songName);
                melonSearch.Search();
                MelonSearchListModel listModel = new MelonSearchListModel();
                for (MelonSearch.SearchData searchData : melonSearch.getAllData()) {
                    listModel.addElement(searchData);
                }
                list1.setCellRenderer(new MelonSearchCellRenderer());
                list1.setModel(listModel);
            }
        }.start();
    }

    public synchronized static SearchFrame getInstance() {
        if (instance == null) {
            instance = new SearchFrame();
        }
        return instance;
    }

    public void setlistClickListener(listClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface listClickListener {
        public void selectNewPlaySong(MelonSearch.SearchData songData);
    }
}
