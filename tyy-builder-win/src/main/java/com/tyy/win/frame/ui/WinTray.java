package com.tyy.win.frame.ui;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;

import javax.swing.JFrame;

import org.springframework.beans.factory.annotation.Autowired;

import com.tyy.framework.exception.ServiceException;
import com.tyy.win.comm.Constant;
import com.tyy.win.frame.NinjiaBooter;
import com.tyy.win.utils.Res;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WinTray {

    private Image image;

    private String tooltip;

    private TrayIcon taryIcon;

    @Autowired
    private NinjiaBooter booter;

    public WinTray() {
        if (!SystemTray.isSupported()) { throw new ServiceException(); }
        image = Res.get().getImage(Constant.icon).getImage();

        SystemTray systemTray = SystemTray.getSystemTray();
        PopupMenu popup = new PopupMenu();
        MenuItem menuItem = new MenuItem("exit");
        menuItem.addActionListener(e -> System.exit(1));
        popup.add(menuItem);

        taryIcon = new TrayIcon(image, tooltip, popup);
        taryIcon.setImageAutoSize(true);
        taryIcon.addActionListener(e -> {
            booter.setVisible(true);
        });
        try {
            systemTray.add(taryIcon);
        } catch (AWTException e) {
            throw new ServiceException("添加系统托盘失败!", e);
        }
    }

}
