package com.tyy.win.frame.ninjia;

import java.awt.Image;
import java.util.function.Supplier;

import javax.swing.JFrame;

import com.tyy.win.utils.Res;

public class NinjiaItem {

    private Image icon;

    private Image iconHl;

    private Image iconPr;

    private JFrame frame;

    private Supplier<JFrame> frameSupplier;

    private boolean show = false;

    public NinjiaItem(Image icon, JFrame frame) {
        super();
        this.icon = icon;
        this.iconHl = icon;
        this.iconPr = icon;
        this.frame = frame;
    }

    public NinjiaItem(Image icon, Supplier<JFrame> frameSupplier) {
        super();
        this.icon = icon;
        this.iconHl = icon;
        this.iconPr = icon;
        this.frameSupplier = frameSupplier;
    }

    public NinjiaItem(String icon, String iconHl, String iconPr, Supplier<JFrame> frameSupplier) {
        super();
        this.icon = Res.get().getImage(icon).getImage();
        this.iconHl = Res.get().getImage(iconHl).getImage();
        this.iconPr = Res.get().getImage(iconPr).getImage();
        this.frameSupplier = frameSupplier;
    }

    public Image getIcon() {
        return icon;
    }

    public Image getIconHl() {
        return iconHl;
    }

    public Image getIconPr() {
        return iconPr;
    }

    public synchronized void toggle() {
        if (frame == null) {
            frame = this.frameSupplier.get();
            frame.setVisible(false);
        }
        frame.setVisible(show = !show);
    }

}
