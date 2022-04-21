package com.tyy.win.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tyy.dev.frames.DevFrames;
import com.tyy.win.comm.Constant;
import com.tyy.win.frame.NinjiaBooter;
import com.tyy.win.frame.ninjia.NinjiaItem;
import com.tyy.win.frame.ui.WinTray;
import com.tyy.win.utils.Res;

@Configuration
public class WinConfig {

    @Bean
    public Res initRes() {
        return new Res();
    }

    @Bean
    public WinTray initWinTray() {
        return new WinTray();
    }

    @Bean
    public NinjiaBooter initNinjiaBooter() {
        NinjiaBooter ninjiaBooter = new NinjiaBooter();
        ninjiaBooter.addJFrame(new NinjiaItem(Res.get().getImage(Constant.dix).getImage(), () -> new DevFrames()));
        return ninjiaBooter;
    }

}
