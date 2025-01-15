package com.jobportal.JobPortal.Service;

public enum JobSearchWork {
    jobInterview("面接"),
    briefing("説明会"),
    test("試験"),
    informalDecisionCeremony("内定式"),
    training("研修"),
    jobOther("その他");

    private final String japanese;

    JobSearchWork(String japanese) {
        this.japanese = japanese;
    }

    public String getJapaneseName() {
        return japanese;
    }
}
