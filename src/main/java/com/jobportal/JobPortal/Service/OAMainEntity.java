package com.jobportal.JobPortal.Service;

import java.time.LocalDate;

public record OAMainEntity(
        Long id,
        Long studentId,
        LocalDate submissionDate,
        boolean jobSearchFlag,
        boolean teacherCheck,
        Boolean careerCheck,
        OAStatus status,
        OAReason reason

//        JobSearchEntity jobSearchEntity
//        BereavementOAForm bereaveForm,
//        AttendanceBanOAForm banForm,
//        OtherOAForm otherForm
) {
}
