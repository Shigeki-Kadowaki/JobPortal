package com.jobportal.JobPortal.Controller.Form;

import java.util.List;
import java.util.Optional;

public record TeacherOASearchForm(
        String grade,
        String classType,
        List<String> OAStatus,
        Boolean andFlag,
        List<String> reportStatus,
        Boolean todayOAFlag
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
