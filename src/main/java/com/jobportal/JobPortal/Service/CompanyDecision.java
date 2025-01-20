package com.jobportal.JobPortal.Service;

public enum CompanyDecision {
    doNot("しない"),
    underConsideration("検討中"),
    takingExam("受験する"),
    hasOffer("内定済み");

    private final String japanese;

    CompanyDecision(String japanese) {
        this.japanese = japanese;
    }

    public String getJapaneseName() {
        return japanese;
    }
}
