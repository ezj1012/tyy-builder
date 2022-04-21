package com.tyy.win.frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import com.tyy.win.comm.Constant;
import com.tyy.win.frame.ninjia.NinjiaItem;
import com.tyy.win.frame.ui.BtnUI;
import com.tyy.win.utils.Res;

public class NinjiaBooter extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    private int tileSize = 40;

    private volatile boolean stealth = false;

    private Timer timer = new Timer(10, this);

    private long lastHoverTime = 0;

    private int targetY;

    private ImageIcon back;

    private JPanel backPanel;

    public NinjiaBooter() {
        super();
        setMinimumSize(new Dimension(tileSize, tileSize));
        setMaximumSize(new Dimension(tileSize, tileSize * 5));
        // JFrame.setDefaultLookAndFeelDecorated(true);
        // AWTUtilities.setWindowShape(this, new RoundRectangle2D.Double(0.0D, 0.0D, this.getWidth(), this.getHeight(), 26.0D, 26.0D));
        timer.start();
        setIconImage(Comps.icon.getImage());
        setUndecorated(true);
        setBackground(new Color(232, 1, 1, 111));
        addMouseListener(new MouseAdapter() {
        });
        init();
        getContentPane().setLayout(null);
        back = Res.get().getImage(Constant.ninjia);
        backPanel = new JPanel() {

            private static final long serialVersionUID = 1L;

            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                g.drawImage(back.getImage(), 0, 0, this);
            };

        };
        backPanel.setBounds(0, 0, tileSize, tileSize * 5);
        backPanel.setLayout(null);
        getContentPane().setBackground(Color.BLACK);
        getContentPane().add(backPanel);
        setMinSize();
        setVisible(true);
    }

    List<NinjiaItem> frames = new ArrayList<>();

    public void addJFrame(NinjiaItem item) {
        JButton b = new JButton();
        b.setMaximumSize(new Dimension(tileSize - 10, tileSize - 10));
        b.setBounds(5, frames.size() * tileSize + 5, tileSize - 10, tileSize - 10);
        // b.setLocation(5, frames.size() * tileSize + 5);
        b.setIcon(new ImageIcon(item.getIcon()));
        b.setPressedIcon(new ImageIcon(item.getIconPr()));
        b.setRolloverIcon(new ImageIcon(item.getIconHl()));
        b.addActionListener(e -> item.toggle());
        b.setBorder(new EmptyBorder(0, 0, 0, 0));
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setUI(new BtnUI());
        backPanel.add(b);
        b.repaint();
        frames.add(item);
    }

    private void setMaxSize() {
        if (getY() != tileSize * 5) {
            setSize(tileSize, tileSize * 5);
        }
    }

    private void setMinSize() {
        if (getY() != tileSize * 5) {
            setSize(tileSize, tileSize * 5);
        }
    }

    public boolean isStealth() {
        return stealth;
    }

    public void stealth() {
        setLocation(-35, this.getY());
        stealth = true;
        setAlwaysOnTop(true);
    }

    public void actionPerformed(ActionEvent arg0) {
        Point mPoint = getMousePosition();
        long ct = System.currentTimeMillis();
        // 接触了
        if (mPoint != null) {
            lastHoverTime = ct;
            if (stealth) {
                setMaxSize();
                setLocation(1, this.getY());
                // repaint();
                stealth = false;
            }
        }

        boolean moveTo = mPoint == null && ct - lastHoverTime >= 1000 && !stealth;
        if (moveTo || stealth) {
            setMinSize();
        } else if (!stealth) {
            setMaxSize();
        }

        if (moveTo) {
            moveTo();
        }

    }

    private void init() {
        Rectangle bounds = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] screenDevices = ge.getScreenDevices();
        for (GraphicsDevice graphicsDevice : screenDevices) {
            bounds = graphicsDevice.getDefaultConfiguration().getBounds();
            if (bounds.x == 0) {
                break;
            }
        }
        targetY = (bounds.height - 300) / 2;
        setLocation(0, targetY);
    }

    private void moveTo() {
        if (stealth) { return; }
        if (getX() < 10 && Math.abs(getY() - targetY) < 10) {
            setLocation(0, targetY);
            stealth();
            return;
        }
        int step = 3;
        int x = getX() - step > 0 ? getX() - step : 0;
        int y = Math.abs(getY() - targetY) < 3 ? targetY : getY() > targetY ? getY() - step : getY() + step;
        setLocation(x, y);
    }

}
