package com.tyy.win.frame.special;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.tyy.win.comm.Constant;
import com.tyy.win.utils.Res;

public class SnowyDay extends JPanel {

    // public SnowyDay(Component component) {
    // super(component);
    // }

    ImageIcon image;

    public SnowyDay() {
        image = Res.get().getImage(Constant.snowy);
        Thread s = new Thread(() -> {
            while (true) {
                this.repaint();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
        });
        s.start();
    }

    int i = 0;

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 400, 400);
        g.drawImage(image.getImage(), 100 + ++i, 100, this);
        // BufferedImage img = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);

    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setBounds(300, 200, 400, 400);
        f.getContentPane().setLayout(null);
        SnowyDay snowyDay = new SnowyDay();
        snowyDay.setSize(400, 400);
        f.getContentPane().add(snowyDay);
        f.setVisible(true);

    }

}
