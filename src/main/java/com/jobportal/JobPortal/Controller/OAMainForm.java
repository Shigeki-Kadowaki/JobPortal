package com.jobportal.JobPortal.Controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class OAMainForm {
        @NotBlank
        @Pattern(regexp = "jobSearchForm|seminarForm|bereavementForm|attendanceBanForm|otherForm")
        String reasonForAbsence;
        List<String> OADates;
        Map<String, List<String>> OAPeriods;

        @Valid
        JobSearchOAForm jobForm;
//        BereavementOAForm bereaveForm;
//        AttendanceBanOAForm banForm;
//        OtherOAForm otherForm;
}
