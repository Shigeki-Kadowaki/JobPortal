package com.jobportal.JobPortal.Service.Entity;

import com.jobportal.JobPortal.Service.OAReason;
import com.jobportal.JobPortal.Service.OAStatus;

import java.time.LocalDate;


public record OAListEntity (
        Integer officialAbsenceId,
        Integer reportId,
        Integer studentId,
        Integer grade,
        String classroom,
        String course,
        String name,
        OAStatus OAStatus,
        OAReason reason,
        OAStatus reportStatus,
        boolean reportRequired,
        LocalDate startDate,
        LocalDate endDate,
        Integer period,
        Integer jobSearchVisitStartHour,
        Integer jobSearchVisitStartMinute,
        Integer seminarVisitStartHour,
        Integer seminarVisitStartMinute
){
    public String existsReport(OAStatus status) {
        if(status == null){
            return "未提出";
        }else {
            return status.getJapaneseName();
        }
    }
}
