package com.tyy.dev.frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.tyy.dev.dix.CtrlConfig;
import com.tyy.dev.dix.CtrlConfig.CtrlItem;
import com.tyy.dev.dix.DixActions;
import com.tyy.dev.dix.DixDevConfig;
import com.tyy.dev.dix.EsDaoGetter;
import com.tyy.dev.utils.EsUtil;
import com.tyy.dev.utils.es.EsDao;
import com.tyy.dev.utils.es.EsDataSource;
import com.tyy.win.frame.Comps;

public class DevFrames extends JFrame implements EsDaoGetter {

    public final static DixDevConfig devConfig = new DixDevConfig();

    public final static List<CtrlConfig> configs = new ArrayList<>();

    public EsDao esDao;

    private DixActions actions;

    static {
        devConfig.setEsHosts("192.168.21.95:8200");

        configs.add(new CtrlConfig("Studio 在线")
                //
                .addItem("Console", "E:\\Develop\\dix\\studio\\diconsole-0.0.1-SNAPSHOT\\bin\\startDiconForES.bat", "E:\\Develop\\dix\\studio\\diconsole-0.0.1-SNAPSHOT\\bin\\stopDiconForES.bat")
                //
                .addItem("Dism", "E:\\Develop\\dix\\studio\\dism-0.0.1-SNAPSHOT\\bin\\startDismForES.bat", "E:\\Develop\\dix\\studio\\dism-0.0.1-SNAPSHOT\\bin\\stopDismForES.bat"));

        configs.add(new CtrlConfig("Studio 离线版")
                //
                .addItem("Console", "E:\\Develop\\dix\\studio-offline\\diconsole-0.0.1-SNAPSHOT\\bin\\start.bat", "E:\\Develop\\dix\\studio-offline\\diconsole-0.0.1-SNAPSHOT\\bin\\stop.bat")
        //
        // .addItem("Dism", "E:\\Develop\\dix\\studio\\dism-0.0.1-SNAPSHOT\\bin\\startDismForES.bat", "E:\\Develop\\dix\\studio\\dism-0.0.1-SNAPSHOT\\bin\\stopDismForES.bat")
        //
        );

    }

    private static final long serialVersionUID = 1L;

    private JTextField esHost;

    private JTextField username;

    private JTextField passwd;

    public DevFrames() {
        setIconImage(Comps.icon.getImage());
        getContentPane().setLayout(new BorderLayout(0, 0));
        actions = new DixActions(this);
        initEsConnect();
        initCtrls();
        initEsCtrls();
        setBounds(100, 300, 1000, 600);
    }

    @Override
    public EsDao getEsDao() {
        return esDao;
    }

    public void initEsCtrls() {
        JPanel cp = new JPanel();
        cp.setSize(700, 570);
        cp.setPreferredSize(new Dimension(700, 570));
        cp.setLayout(null);
        getContentPane().add(cp, BorderLayout.CENTER);

        JPanel ct = new JPanel();
        ct.setLayout(new FlowLayout(FlowLayout.LEADING));
        ct.setBounds(0, 0, cp.getWidth(), 30);

        JButton stopAllDip = new JButton("停止全部DIP");
        stopAllDip.addActionListener(e -> actions.stopAllDip());
        ct.add(stopAllDip);

        cp.add(ct);
    }

    public void initEsConnect() {
        JPanel esPanel = new JPanel();
        esPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        esPanel.setPreferredSize(new Dimension(1000, 30));
        FlowLayout fl_esPanel = (FlowLayout) esPanel.getLayout();
        fl_esPanel.setAlignment(FlowLayout.LEFT);
        getContentPane().add(esPanel, BorderLayout.NORTH);

        esPanel.add(new JLabel("Hosts : "));

        esHost = new JTextField();
        esHost.setText(devConfig.getEsHosts());
        esHost.setToolTipText("ES地址,多个,隔开");
        esHost.setColumns(30);
        esPanel.add(esHost);

        esPanel.add(new JLabel("用户名 : "));
        username = new JTextField();
        esPanel.add(username);
        username.setColumns(15);

        esPanel.add(new JLabel("密码 : "));
        passwd = new JTextField();
        esPanel.add(passwd);
        passwd.setColumns(20);

        JButton connect = new JButton("连接");
        esPanel.add(connect);

        JButton diConnect = new JButton("断开");
        diConnect.setVisible(false);
        esPanel.add(diConnect);

        connect.addActionListener(e -> {
            if (esDao != null) {
                try {
                    esDao.close();
                } catch (Exception e1) {
                }
            }
            esDao = EsUtil.get(EsDataSource.of(esHost.getText(), username.getText(), passwd.getText()));
            if (esDao.isRunning()) {
                connect.setVisible(false);
                diConnect.setVisible(true);
            }
        });
        diConnect.addActionListener(e -> {
            if (esDao != null) {
                EsUtil.close(esDao.getDs());
                esDao = null;
            }
            diConnect.setVisible(false);
            connect.setVisible(true);
        });
        connect.doClick();
    }

    public void initCtrls() {
        // JPanel ctrlPanel = new JPanel();
        // ctrlPanel.setBorder(new EmptyBorder(0, 10, 10, 10));
        // ctrlPanel.setPreferredSize(new Dimension(300, 560));
        // this.getContentPane().add(ctrlPanel, BorderLayout.WEST);
        JScrollPane sp = new JScrollPane();
        sp.setPreferredSize(new Dimension(300, 560));
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.getContentPane().add(sp, BorderLayout.WEST);

        JPanel con = new JPanel();
        con.setLayout(new FlowLayout(FlowLayout.LEFT));
        sp.setViewportView(con);
        con.setPreferredSize(new Dimension(300, 560));
        for (int i = 0; i < configs.size(); i++) {
            CtrlConfig config = configs.get(i);
            JPanel cPanel = new JPanel();
            cPanel.setBorder(new CompoundBorder(new EmptyBorder(5, 10, 10, 10), new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), config.getName(), TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0))));
            cPanel.setPreferredSize(new Dimension(280, 110));
            cPanel.setMinimumSize(new Dimension(280, 100));
            int w = config.getItems().size();
            cPanel.setLayout(new GridLayout(w, 3));
            for (CtrlItem item : config.getItems()) {
                JButton jLabel = new JButton(item.getName());
                JButton startBtn = new JButton("start");
                JButton stopBtn = new JButton("stop");
                jLabel.setToolTipText("打开所在目录");
                jLabel.addActionListener(e -> {
                    try {
                        Desktop.getDesktop().open(new File(item.getStart()).getParentFile().getParentFile());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                });
                startBtn.addActionListener(e -> new Thread(() -> {
                    exec(startBtn, item.getStart());
                }).start());
                stopBtn.addActionListener(e -> exec(stopBtn, item.getStop()));

                cPanel.add(jLabel);
                cPanel.add(startBtn);
                cPanel.add(stopBtn);
            }

            con.add(cPanel);
        }

    }

    private void exec(final JButton btn, String path) {
        String text = btn.getText();
        try {
            btn.setText(text + "ing");
            File f = new File(path);
            f = f.getCanonicalFile();
            Process p = Runtime.getRuntime().exec("cmd /c start " + f.getCanonicalPath(), null, f.getParentFile());
            BufferedReader ls_in1 = new BufferedReader(new InputStreamReader(p.getInputStream(), "gbk"));
            String ls_str1;
            try {
                while ((ls_str1 = ls_in1.readLine()) != null) {
                    System.out.println(ls_str1);
                }
            } catch (IOException e) {
                System.exit(0);
            }

            BufferedReader ls_in = new BufferedReader(new InputStreamReader(p.getErrorStream(), "gbk"));
            String ls_str;
            try {
                while ((ls_str = ls_in.readLine()) != null) {
                    System.err.println(ls_str);
                }
            } catch (IOException e) {
                System.exit(0);
            }
            p.destroyForcibly();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            btn.setText(text);
        }
    }

    public static void main(String[] args) {
        JFrame j = new DevFrames();

        j.setVisible(true);
    }

}
