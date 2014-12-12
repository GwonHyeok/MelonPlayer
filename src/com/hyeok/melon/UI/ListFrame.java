package com.hyeok.melon.UI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by GwonHyeok on 14. 12. 12..
 */
public class ListFrame extends JFrame {
    private JList list1;
    private JPanel panel1;
    private JButton searchButton;
    private JButton button2;
    private static ListFrame instance;

    private ListFrame() {
        setContentPane(panel1);
        setSize(340, 600);
        setComponentSpace();
        setButtonListener();
        setResizable(false);
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
