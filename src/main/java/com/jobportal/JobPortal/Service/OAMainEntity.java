package com.jobportal.JobPortal.Service;

import com.jobportal.JobPortal.Controller.*;

import java.util.Date;

public record OAMainEntity(
        int id,
        Date OAdate,
        Date submissionDate,
        OAStatus status,
        OAReason reason,

        JobSearchOAForm jobForm,
        BereavementOAForm bereaveForm,
        AttendanceBanOAForm banForm,
        OtherOAForm otherForm
) {
}
