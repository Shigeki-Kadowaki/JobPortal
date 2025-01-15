package com.jobportal.JobPortal.Controller.Form;

import com.jobportal.JobPortal.Controller.ValidationGroup;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ReportForm {

    private Integer reportId;
    private String reportReason;
    @NotNull(message = "必須項目です")
    private Integer activityTime;
    private String isSelection;
    private String nextAction;

    //説明会.
    @NotEmpty(message = "必須項目です", groups = ValidationGroup.briefingSessionGroup.class)
    private String briefingContent;
    @NotEmpty(message = "必須項目です", groups = ValidationGroup.briefingSessionGroup.class)
    private String briefingSessionImpressions;

    //試験.
    @Size(max = 64, message = "64文字以内で入力してください",groups = ValidationGroup.aptitudeTestGroup.class)
    private String generalKnowledge;
    @Size(max = 64, message = "64文字以内で入力してください",groups = ValidationGroup.aptitudeTestGroup.class)
    private String nationalLanguageType;
    @Size(max = 64, message = "64文字以内で入力してください",groups = ValidationGroup.aptitudeTestGroup.class)
    private String mathType;
    @Size(max = 64, message = "64文字以内で入力してください",groups = ValidationGroup.aptitudeTestGroup.class)
    private String englishType;
    @Size(max = 64, message = "64文字以内で入力してください",groups = ValidationGroup.aptitudeTestGroup.class)
    private String currentAffairsType;
    @Size(max = 64, message = "64文字以内で入力してください",groups = ValidationGroup.aptitudeTestGroup.class)
    private String writingTheme;
    @Size(max = 64, message = "64文字以内で入力してください",groups = ValidationGroup.aptitudeTestGroup.class)
    private String expertiseType;
    @Size(max = 64, message = "64文字以内で入力してください",groups = ValidationGroup.aptitudeTestGroup.class)
    private String jobQuestionType;
    @Size(max = 64, message = "64文字以内で入力してください",groups = ValidationGroup.aptitudeTestGroup.class)
    private String personalityDiagnosisType;
    @Size(max = 64, message = "64文字以内で入力してください",groups = ValidationGroup.aptitudeTestGroup.class)
    private String others;
    @Max(value = 100, groups = ValidationGroup.aptitudeTestGroup.class)
    @Min(value = 0, groups = ValidationGroup.aptitudeTestGroup.class)
    private Integer nationalLanguageNumber;
    @Max(value = 100, groups = ValidationGroup.aptitudeTestGroup.class)
    @Min(value = 0, groups = ValidationGroup.aptitudeTestGroup.class)
    private Integer mathNumber;
    @Max(value = 100, groups = ValidationGroup.aptitudeTestGroup.class)
    @Min(value = 0, groups = ValidationGroup.aptitudeTestGroup.class)
    private Integer englishNumber;
    @Max(value = 100, groups = ValidationGroup.aptitudeTestGroup.class)
    @Min(value = 0, groups = ValidationGroup.aptitudeTestGroup.class)
    private Integer currentAffairsNumber;
    @Max(value = 100, groups = ValidationGroup.aptitudeTestGroup.class)
    @Min(value = 0, groups = ValidationGroup.aptitudeTestGroup.class)
    private Integer writingTimer;
    @Max(value = 100, groups = ValidationGroup.aptitudeTestGroup.class)
    @Min(value = 0, groups = ValidationGroup.aptitudeTestGroup.class)
    private Integer writingNumberOfCharacters;
    @Max(value = 100, groups = ValidationGroup.aptitudeTestGroup.class)
    @Min(value = 0, groups = ValidationGroup.aptitudeTestGroup.class)
    private Integer expertiseNumber;
    @Max(value = 100, groups = ValidationGroup.aptitudeTestGroup.class)
    @Min(value = 0, groups = ValidationGroup.aptitudeTestGroup.class)
    private Integer jobQuestionNumber;
    @Max(value = 100, groups = ValidationGroup.aptitudeTestGroup.class)
    @Min(value = 0, groups = ValidationGroup.aptitudeTestGroup.class)
    private Integer personalityDiagnosisNumber;
    @Max(value = 100, groups = ValidationGroup.aptitudeTestGroup.class)
    @Min(value = 0, groups = ValidationGroup.aptitudeTestGroup.class)
    private Integer SPILanguageSystemNumber;
    @Max(value = 100, groups = ValidationGroup.aptitudeTestGroup.class)
    @Min(value = 0, groups = ValidationGroup.aptitudeTestGroup.class)
    private Integer SPINonLanguageSystemNumber;
    @Max(value = 100, groups = ValidationGroup.aptitudeTestGroup.class)
    @Min(value = 0, groups = ValidationGroup.aptitudeTestGroup.class)
    private Integer SPIOthersNumber;
    @NotEmpty(message = "必須項目です", groups = ValidationGroup.aptitudeTestGroup.class)
    private  String aptitudeTestImpressions;

    //面接.
    @NotNull(message = "必須項目です", groups = ValidationGroup.interviewGroup.class)
    private Integer interviewerNumber;
    @NotBlank(message = "必須項目です", groups = ValidationGroup.interviewGroup.class)
    private String interviewType;
    @NotBlank(message = "必須項目です", groups = ValidationGroup.interviewGroup.class)
    private String interviewContent;
    @NotBlank(message = "必須項目です", groups = ValidationGroup.interviewGroup.class)
    private String interviewImpressions;

    //研修.
    @NotBlank(message = "必須項目です", groups = ValidationGroup.trainingGroup.class)
    private String trainingImpressions;
    //内定式.
    @NotBlank(message = "必須項目です", groups = ValidationGroup.informalDecisionCeremonyGroup.class)
    private String informalDecisionCeremonyImpressions;
    //セミナー.
    @NotBlank(message = "必須項目です", groups = ValidationGroup.reportSeminarGroup.class)
    private String companyName;
    @NotBlank(message = "必須項目です", groups = ValidationGroup.reportSeminarGroup.class)
    private String manager;
    @NotBlank(message = "必須項目です", groups = ValidationGroup.reportSeminarGroup.class)
    private String industry;
    @NotBlank(message = "必須項目です", groups = ValidationGroup.reportSeminarGroup.class)
    private String seminarImpressions;
    //その他.
    @NotBlank(message = "必須項目です", groups = ValidationGroup.reportOtherGroup.class)
    private String activityContent;
    @NotBlank(message = "必須項目です", groups = ValidationGroup.reportOtherGroup.class)
    private String othersImpressions;
}
