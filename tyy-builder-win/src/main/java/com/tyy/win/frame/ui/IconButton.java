package com.tyy.win.frame.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

import sun.awt.image.ToolkitImage;

@SuppressWarnings("restriction")
public class IconButton extends JButton {

    private static final long serialVersionUID = 1L;

    private int w;

    private int h;

    private BufferedImage icon;

    public IconButton(ImageIcon base, ImageIcon hover, ImageIcon pre) {
        this(base, hover, pre, null);
    }

    public IconButton(ImageIcon base, ImageIcon hover, ImageIcon pre, String text) {
        this.setIcon(base);
        this.setPressedIcon(pre);
        this.setRolloverIcon(hover);
        if (base.getImage() instanceof ToolkitImage) {
            icon = ((ToolkitImage) base.getImage()).getBufferedImage();
        } else {
            icon = ((BufferedImage) base.getImage());
        }

        w = base.getIconWidth();
        h = base.getIconHeight();

        this.setSize(w, h);
        this.setMaximumSize(new Dimension(w, h));
        this.setMinimumSize(new Dimension(w, h));
        this.setPreferredSize(new Dimension(w, h));
        this.setBorder(new EmptyBorder(0, 0, 0, 0));
        this.setBorderPainted(false);

        this.setFocusPainted(false);
        this.setOpaque(false);
        this.setContentAreaFilled(false);
        this.setUI(new BtnUI());

        this.setText(text);
        this.setForeground(Color.WHITE);
        this.setHorizontalTextPosition(CENTER);

        if (text != null && !"".equals(text.trim())) {
            this.setFont(new Font("宋体", Font.BOLD, (int) (h / 7 * 3 - 2)));
            // System.out.println(this.getFont().getSize() + " fonts");
            this.setForeground(Color.WHITE);
        }
    }

    public IconButton setBo(int x, int y) {
        setLocation(x, y);
        return this;
    }

    @Override
    public boolean contains(int x, int y) {
        x--;
        y--;
        if (x < 0 || y < 0 || x >= this.w || y >= this.h) { return false; }
        return (icon.getRGB(x, y) >> 24) != 0x00;
    }

}
