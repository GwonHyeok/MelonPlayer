package com.hyeok.melon.CustomSwingContent;

import com.hyeok.melon.MelonSearch;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by GwonHyeok on 14. 12. 12..
 */
public class MelonSearchCellRenderer extends JLabel implements ListCellRenderer<MelonSearch.SearchData> {

    public MelonSearchCellRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends MelonSearch.SearchData> list, MelonSearch.SearchData value, int index, boolean isSelected, boolean cellHasFocus) {
        try {
            setIcon(new ImageIcon(ImageIO.read(new URL(value.getAlbumart()))));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setText(value.getSongName());
        return this;
    }
}
