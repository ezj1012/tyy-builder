package com.tyy.win.frame.special;

import java.awt.Component;

public abstract class AbsBase {

    Component component;

    public AbsBase(Component component) {
        super();
        this.component = component;
    }

    public Component getComponent() {
        return component;
    }

    public int getWidth() {
        return component.getWidth();
    }

    public int getHeight() {
        return component.getHeight();
    }

}
