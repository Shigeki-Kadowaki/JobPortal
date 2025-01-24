package com.jobportal.JobPortal.Service;

public enum JobInterviewType {
    individual("個人"),
    group("集団"),
    groupDiscussion("グループディスカッション");
    private final String japanese;

    JobInterviewType(String japanese) {
        this.japanese = japanese;
    }

    public String getJapaneseName() {
        return japanese;
    }
}
