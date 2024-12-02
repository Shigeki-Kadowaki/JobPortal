package com.jobportal.JobPortal.Service;

public enum Course {
    SE("SE・プログラマ");



    private final String japanese;

    Course(String japanese) {
        this.japanese = japanese;
    }
    public String getJapaneseName() {
        return japanese;
    }
}
