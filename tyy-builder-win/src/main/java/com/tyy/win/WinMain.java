package com.tyy.win;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class WinMain {

    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(WinMain.class);
        builder.headless(false).run(args);
    }

}
