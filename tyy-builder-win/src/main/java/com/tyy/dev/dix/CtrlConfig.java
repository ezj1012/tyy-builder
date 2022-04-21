package com.tyy.dev.dix;

import java.util.ArrayList;
import java.util.List;

public class CtrlConfig {

    private String name;

    private List<CtrlItem> items = new ArrayList<CtrlConfig.CtrlItem>();

    public CtrlConfig(String name) {
        super();
        this.name = name;
    }

    public CtrlConfig addItem(String name, String start, String stop) {
        items.add(new CtrlItem(name, start, stop));
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CtrlItem> getItems() {
        return items;
    }

    public void setItems(List<CtrlItem> items) {
        this.items = items;
    }

    public static class CtrlItem {

        private String name;

        private String start;

        private String stop;

        public CtrlItem(String name, String start, String stop) {
            super();
            this.name = name;
            this.start = start;
            this.stop = stop;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStart() {
            return start;
        }

        public void setStart(String start) {
            this.start = start;
        }

        public String getStop() {
            return stop;
        }

        public void setStop(String stop) {
            this.stop = stop;
        }

    }

}
