package com.tyy.win.utils;

import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;

import org.springframework.core.io.ClassPathResource;

public class Res {

    private static Res res;

    public Res() {
        res = this;
    }

    public static Res get() {
        if (res == null) {
            res = new Res();
        }
        return Res.res;
    }

    @SuppressWarnings("deprecation")
    public ImageIcon getImage(File file) {
        try {
            return new ImageIcon(file.toURL());
        } catch (IOException e) {
            return null;
        }
    }

    public ImageIcon getImage(String key) {
        return loadLocal(key);
    }

    private ImageIcon loadLocal(String key) {
        ClassPathResource resource = new ClassPathResource("static/" + key);
        try {
            return new ImageIcon(resource.getURL());
        } catch (IOException e) {
            return null;
        }
    }

}
