package com.jobportal.JobPortal.Controller.Form;

import com.jobportal.JobPortal.Controller.ValidationGroup;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ReportForm {
    private Integer reportId;
    private Integer version;
    private Integer activityTime;

    @NotEmpty(message = "必須項目です", groups = ValidationGroup.briefingSessionHistoryGroup.class)
    private String briefingContent;
    @NotEmpty(message = "必須項目です", groups = ValidationGroup.briefingSessionHistoryGroup.class)
    private String impressions;

    private Integer isSelection;
    private Integer nextAction;
}
