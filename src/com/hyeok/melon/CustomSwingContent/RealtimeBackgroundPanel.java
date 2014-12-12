package com.hyeok.melon.CustomSwingContent;

import com.hyeok.melon.ImageFilterUtil.GaussianFilter;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by GwonHyeok on 14. 12. 12..
 */

public class RealtimeBackgroundPanel extends JPanel {
    private Image backgroundImage;

    public RealtimeBackgroundPanel() {
        super();
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(backgroundImage, -130, -100, 600, 700, null);

    }

    public Image getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(Image backgroundImage) {
        this.backgroundImage = backgroundImage;
    }
}
