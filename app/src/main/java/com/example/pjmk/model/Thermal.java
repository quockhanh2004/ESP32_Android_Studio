package com.example.pjmk.model;

import java.io.Serializable;

public class Thermal implements Serializable {
    private int vNumberNhiet, vNumberAm;

    public Thermal(int vNumberNhiet, int vNumberAm) {
        this.vNumberNhiet = vNumberNhiet;
        this.vNumberAm = vNumberAm;
    }

    public int getvNumberNhiet() {
        return vNumberNhiet;
    }

    public void setvNumberNhiet(int vNumberNhiet) {
        this.vNumberNhiet = vNumberNhiet;
    }

    public int getvNumberAm() {
        return vNumberAm;
    }

    public void setvNumberAm(int vNumberAm) {
        this.vNumberAm = vNumberAm;
    }
}
