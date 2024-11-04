package com.jobportal.JobPortal.Service;

public enum OAReason {
    jobSearchForm("就活"),
    seminarForm("セミナー・合説"),
    bereavementForm("忌引"),
    attendanceBanForm("出席停止"),
    otherForm("その他");

    private final String japanese;

    OAReason(String japanese) {
        this.japanese = japanese;
    }

    public String getJapaneseName() {
        return japanese;
    }
}
