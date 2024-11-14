package com.jobportal.JobPortal.Controller.Form;

import java.util.List;
import java.util.Optional;

public record StudentOASearchForm(
        List<String> OAStatus,
        List<String> reportStatus
        //    String sort
) {
    public boolean isOAStatusChecked(String status){
        return Optional.ofNullable(OAStatus)
                .map(l -> l.contains(status))
                .orElse(false);
    }
    public boolean isReportStatusChecked(String status){
        return Optional.ofNullable(reportStatus)
                .map(l -> l.contains(status))
                .orElse(false);
    }
}
