package com.jobportal.JobPortal.Service;

public enum OAReason {
    jobSearch("就活"),
    seminar("セミナー・合説"),
    bereavement("忌引"),
    attendanceBan("出席停止"),
    other("その他");

    private final String japanese;

    OAReason(String japanese) {
        this.japanese = japanese;
    }

    public String getJapaneseName() {
        return japanese;
    }
}
