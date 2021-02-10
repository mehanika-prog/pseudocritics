package com.pseudocritics.domain;

public enum SessionType {


    WEB (0),
    MOBILE (1);

    private final int index;


    SessionType(int index) {

        this.index = index;

    }

    public static SessionType getFromIndex(int index) {

        switch (index) {

            case 0: return WEB;
            case 1: return MOBILE;
            default: return null;

        }

    }

}
