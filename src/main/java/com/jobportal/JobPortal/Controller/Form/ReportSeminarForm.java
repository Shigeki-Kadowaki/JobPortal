package com.jobportal.JobPortal.Controller.Form;

import com.jobportal.JobPortal.Controller.ValidationGroup;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ReportSeminarForm {
    @NotBlank(message = "必須項目です", groups = ValidationGroup.reportSeminarGroup.class)
    private String companyName;
    @NotBlank(message = "必須項目です", groups = ValidationGroup.reportSeminarGroup.class)
    private String manager;
    @NotBlank(message = "必須項目です", groups = ValidationGroup.reportSeminarGroup.class)
    private String industry;
    @NotBlank(message = "必須項目です", groups = ValidationGroup.reportSeminarGroup.class)
    private String seminarImpressions;
    @NotBlank(message = "必須項目です", groups = ValidationGroup.reportSeminarGroup.class)
    private String seminarEmploymentIntention;
    @NotBlank(message = "必須項目です", groups = ValidationGroup.reportSeminarGroup.class)
    private String seminarNextAction;

    public ReportSeminarForm(String companyName, String manager, String industry, String seminarImpressions, String seminarIsSelection, String seminarNextAction) {
        this.companyName = companyName;
        this.manager = manager;
        this.industry = industry;
        this.seminarImpressions = seminarImpressions;
        this.seminarEmploymentIntention = seminarIsSelection;
        this.seminarNextAction = seminarNextAction;
    }
}
