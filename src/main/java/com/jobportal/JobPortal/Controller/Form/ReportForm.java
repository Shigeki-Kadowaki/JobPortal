package com.jobportal.JobPortal.Controller.Form;

import com.jobportal.JobPortal.Controller.ValidationGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ReportForm {

    private Integer reportId;
    private String reportReason;
    private Integer activityTime;
    private Integer isSelection;
    private Integer nextAction;

    //説明会.
    @NotEmpty(message = "必須項目です", groups = ValidationGroup.briefingSessionGroup.class)
    private String briefingContent;
    @NotEmpty(message = "必須項目です", groups = ValidationGroup.briefingSessionGroup.class)
    private String impressions;

    //試験.
    private String generalKnowledge;
    private String nationalLanguageType;
    private String mathType;
    private String englishType;
    private String currentAffairsType;
    private String writingTheme;
    private String expertiseType;
    private String jobQuestionType;
    private String personalityDiagnosisType;
    private String others;
    private Integer nationalLanguageNumber;
    private Integer mathNumber;
    private Integer englishNumber;
    private Integer currentAffairsNumber;
    private Integer writingTimer;
    private Integer writingNumberOfCharacters;
    private Integer expertiseNumber;
    private Integer jobQuestionNumber;
    private Integer personalityDiagnosisNumber;

    //面接.
    @NotNull(message = "必須項目です", groups = ValidationGroup.InterviewGroup.class)
    private Integer interviewerNumber;
    @NotBlank(message = "必須項目です", groups = ValidationGroup.InterviewGroup.class)
    private String interviewType;
    @NotBlank(message = "必須項目です", groups = ValidationGroup.InterviewGroup.class)
    private String interviewContent;
    @NotBlank(message = "必須項目です", groups = ValidationGroup.InterviewGroup.class)
    private String interviewImpressions;

    //研修.
    private String trainingImpressions;
    //内定式.
    private String informalDecisionCeremonyImpressions;
    //セミナー.
    private String companyName;
    private String manager;
    private String industry;
    private String seminarImpressions;
    //その他.
    private String activityContent;
    private String othersImpressions;
}
