package com.example.pjmk.model;

import java.io.Serializable;

public class Toggle implements Serializable {
    private boolean activate;
    private String name;
    private int vNumber;

    public Toggle(boolean activate, String name, int vNumber) {
        this.activate = activate;
        this.name = name;
        this.vNumber = vNumber;
    }

    public boolean isActivate() {
        return activate;
    }

    public void setActivate(boolean activate) {
        this.activate = activate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getvNumber() {
        return vNumber;
    }

    public void setvNumber(int vNumber) {
        this.vNumber = vNumber;
    }
}
