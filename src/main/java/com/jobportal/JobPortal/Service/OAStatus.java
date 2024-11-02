package com.jobportal.JobPortal.Service;

public enum OAStatus {
    acceptance("受理"),
    unaccepted("未受理"),
    rejection("却下");

    private String japanese;

    OAStatus(String japanese) {
        this.japanese = japanese;
    }

    public String getJapaneseName() {
        return japanese;
    }
}
