package com.jobportal.JobPortal.Controller.Form;

import java.util.List;
import java.util.Optional;

public record TeacherOASearchForm(
        Integer grade,
        String classroom,
        List<String> OAStatus,
        String teacherCheckFlag,
        String careerCheckFlag,
        Boolean andFlag,
        List<String> reportStatus,
        Boolean todayOAFlag
) {
    public boolean isOAStatusSelected(String status){
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
