package com.jobportal.JobPortal.Service;

import com.jobportal.JobPortal.Controller.*;

import java.util.Date;
import java.util.List;

public record OAMainEntity(
        int id,
        Date OAdate,
        Date submissionDate,
        OAStatus status,
        OAReason reason,
        List<OADates> date,

        JobSearchOAForm jobForm,
        BereavementOAForm bereaveForm,
        AttendanceBanOAForm banForm,
        OtherOAForm otherForm
) {
}
