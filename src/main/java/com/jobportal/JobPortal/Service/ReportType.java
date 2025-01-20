package com.jobportal.JobPortal.Service;

public enum ReportType {
    briefing("説明会"),
    test("試験"),
    jobInterview("面接"),
    informalCeremony("内定式"),
    training("研修"),
    jobOther("その他"),
    seminar("セミナー・合説");

    private final String japanese;

    ReportType(String japanese) {
        this.japanese = japanese;
    }

    public String getJapaneseName() {
        return japanese;
    }
}
