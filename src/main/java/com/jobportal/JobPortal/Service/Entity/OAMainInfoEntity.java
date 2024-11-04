package com.jobportal.JobPortal.Service.Entity;

import com.jobportal.JobPortal.Service.DTO.OAMainInfoDTO;
import com.jobportal.JobPortal.Service.MainService;
import com.jobportal.JobPortal.Service.OAReason;
import com.jobportal.JobPortal.Service.OAStatus;

import java.time.LocalDate;

public record OAMainInfoEntity(
        Integer officialAbsenceId,
        Integer studentId,
        LocalDate submissionDate,
        OAStatus status,
        OAReason reason
) {
    public OAMainInfoDTO toInfoDTO() {
        return new OAMainInfoDTO(
            officialAbsenceId,
            studentId,
            MainService.dateFormat(submissionDate),
            status.getJapaneseName(),
            reason.getJapaneseName()
        );
    }


}
