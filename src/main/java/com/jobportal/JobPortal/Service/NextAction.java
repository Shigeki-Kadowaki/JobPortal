package com.jobportal.JobPortal.Service;

public enum NextAction {
    mailingDocuments("書類郵送"),
    attendingBriefing("説明会参加"),
    jobInterview("面接"),
    exam("学科試験"),
    aptitudeTest("適性検査");

    private final String japanese;

    NextAction(String japanese) {
        this.japanese = japanese;
    }

    public String getJapaneseName() {
        return japanese;
    }
}
