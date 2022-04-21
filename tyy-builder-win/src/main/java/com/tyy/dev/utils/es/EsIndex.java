package com.tyy.dev.utils.es;

import lombok.Getter;

@Getter
public class EsIndex {

    private String index;

    private String type;

    private EsIndex(String index, String type) {
        super();
        this.index = index;
        this.type = type;
    }

    public static EsIndex of(String index, String type) {
        return new EsIndex(index, type);
    }

}
