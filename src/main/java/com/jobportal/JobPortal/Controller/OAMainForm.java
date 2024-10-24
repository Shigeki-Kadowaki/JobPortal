package com.jobportal.JobPortal.Controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class OAMainForm {
        @NotBlank
        @Pattern(regexp = "jobSearchForm|seminarForm|bereavementForm|attendanceBanForm|otherForm")
        private String reasonForAbsence;
//        private List<String> OADates;

        @NotEmpty
        @Valid
        private Map<@NotBlank String,@NotEmpty List<@NotBlank String>> OAPeriods;

        @Valid
        private JobSearchOAForm jobForm;
        @Valid
        private SeminarOAForm seminarForm;
        @Valid
        private BereavementOAForm bereavementForm;
        @Valid
        private AttendanceBanOAForm banForm;
        @Valid
        private OtherOAForm otherForm;
}
