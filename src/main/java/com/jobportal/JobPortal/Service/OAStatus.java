package com.jobportal.JobPortal.Service;

public enum OAStatus {
    acceptance("受理"),
    unaccepted("未受理"),
    rejection("却下"),
    unsubmitted("未提出"),
    unnecessary("不要");

    private final String japanese;

    OAStatus(String japanese) {
        this.japanese = japanese;
    }

    public String getJapaneseName() {
        return japanese;
    }

}
