package com.jobportal.JobPortal.Controller.Form;

import com.jobportal.JobPortal.Controller.ValidationGroup;
import com.jobportal.JobPortal.Service.Entity.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class ReportForm {

    private Integer reportId;
    private String reason;
    @NotNull(message = "必須項目です")
    private Integer activityTime;
    private String isSelection;
    private String nextAction;

    //説明会.
    @NotEmpty(message = "必須項目です", groups = ValidationGroup.briefingGroup.class)
    private String briefingContent;
    @NotEmpty(message = "必須項目です", groups = ValidationGroup.briefingGroup.class)
    private String briefingImpressions;

    //試験.
    @Size(max = 64, message = "64文字以内で入力してください",groups = ValidationGroup.testGroup.class)
    private String generalKnowledgeNumber;//一般常識問題数
    @Size(max = 64, message = "64文字以内で入力してください",groups = ValidationGroup.testGroup.class)
    private String generalKnowledgeType;//一般常識問題種類
    @Max(value = 100, groups = ValidationGroup.testGroup.class)
    @Min(value = 0, groups = ValidationGroup.testGroup.class)
    private Integer jobQuestionNumber;//職業問題数
    @Size(max = 64, message = "64文字以内で入力してください",groups = ValidationGroup.testGroup.class)
    private String jobQuestionType;//職業問題種類
    @Max(value = 100, groups = ValidationGroup.testGroup.class)
    @Min(value = 0, groups = ValidationGroup.testGroup.class)
    private Integer SPILanguageSystemNumber;//SPI言語系問題数
    @Max(value = 100, groups = ValidationGroup.testGroup.class)
    @Min(value = 0, groups = ValidationGroup.testGroup.class)
    private Integer SPINonLanguageSystemNumber;//SPI非言語系問題数
    @Max(value = 100, groups = ValidationGroup.testGroup.class)
    @Min(value = 0, groups = ValidationGroup.testGroup.class)
    private Integer SPIOthersNumber;//SPIその他問題数
    @Max(value = 100, groups = ValidationGroup.testGroup.class)
    @Min(value = 0, groups = ValidationGroup.testGroup.class)
    private Integer personalityDiagnosisNumber;//性格問題問題数
    @Size(max = 64, message = "64文字以内で入力してください",groups = ValidationGroup.testGroup.class)
    private String personalityDiagnosisType;//性格問題種類
    @Max(value = 100, groups = ValidationGroup.testGroup.class)
    @Min(value = 0, groups = ValidationGroup.testGroup.class)
    private Integer nationalLanguageNumber;//国語問題数
    @Size(max = 64, message = "64文字以内で入力してください",groups = ValidationGroup.testGroup.class)
    private String nationalLanguageType;//国語問題種類
    @Max(value = 100, groups = ValidationGroup.testGroup.class)
    @Min(value = 0, groups = ValidationGroup.testGroup.class)
    private Integer mathNumber;//数学問題数
    @Size(max = 64, message = "64文字以内で入力してください",groups = ValidationGroup.testGroup.class)
    private String mathType;//数学問題種類
    @Max(value = 100, groups = ValidationGroup.testGroup.class)
    @Min(value = 0, groups = ValidationGroup.testGroup.class)
    private Integer englishNumber;//英語問題数
    @Size(max = 64, message = "64文字以内で入力してください",groups = ValidationGroup.testGroup.class)
    private String englishType;//英語問題種類
    @Max(value = 100, groups = ValidationGroup.testGroup.class)
    @Min(value = 0, groups = ValidationGroup.testGroup.class)
    private Integer currentAffairsNumber;//時事問題数
    @Size(max = 64, message = "64文字以内で入力してください",groups = ValidationGroup.testGroup.class)
    private String currentAffairsType;//時事問題種類
    @Max(value = 100, groups = ValidationGroup.testGroup.class)
    @Min(value = 0, groups = ValidationGroup.testGroup.class)
    private Integer writingTimer;//作文時間
    @Max(value = 100, groups = ValidationGroup.testGroup.class)
    @Min(value = 0, groups = ValidationGroup.testGroup.class)
    private Integer writingNumberOfCharacters;//作文文字数
    @Size(max = 64, message = "64文字以内で入力してください",groups = ValidationGroup.testGroup.class)
    private String writingTheme;//作文テーマ
    @Max(value = 100, groups = ValidationGroup.testGroup.class)
    @Min(value = 0, groups = ValidationGroup.testGroup.class)
    private Integer expertiseNumber;//専門問題数
    @Size(max = 64, message = "64文字以内で入力してください",groups = ValidationGroup.testGroup.class)
    private String expertiseType;//専門問題種類
    @Size(max = 64, message = "64文字以内で入力してください",groups = ValidationGroup.testGroup.class)
    private String others;//その他
    @NotEmpty(message = "必須項目です", groups = ValidationGroup.testGroup.class)
    private String testImpressions;//感想

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
    @NotBlank(message = "必須項目です", groups = ValidationGroup.informalCeremonyGroup.class)
    private String informalCeremonyImpressions;
    //セミナー.
    @NotEmpty(message = "最低一つは入力してください", groups = ValidationGroup.reportSeminarGroup.class)
    @Valid
    private List<ReportSeminarForm> seminarForms;

    //その他.
    @NotBlank(message = "必須項目です", groups = ValidationGroup.reportOtherGroup.class)
    private String activityContent;
    @NotBlank(message = "必須項目です", groups = ValidationGroup.reportOtherGroup.class)
    private String othersImpressions;

    public ReportForm(ReportInfoEntity mainInfo, ReportBriefingEntity briefingEntity){
        this.reportId = mainInfo.reportId();
        this.reason = String.valueOf(mainInfo.reason());
        this.activityTime = mainInfo.activityTime();
        this.isSelection = String.valueOf(mainInfo.companyDecision());
        this.nextAction = String.valueOf(mainInfo.nextAction());

        this.briefingContent = briefingEntity.briefingContent();
        this.briefingImpressions = briefingEntity.briefingImpressions();
    }
    public ReportForm(ReportInfoEntity mainInfo, ReportTestEntity testEntity){
        this.reportId = mainInfo.reportId();
        this.reason = String.valueOf(mainInfo.reason());
        this.activityTime = mainInfo.activityTime();
        this.isSelection = String.valueOf(mainInfo.companyDecision());
        this.nextAction = String.valueOf(mainInfo.nextAction());

        this.generalKnowledgeNumber = testEntity.generalKnowledgeNumber();
        this.generalKnowledgeType = testEntity.generalKnowledgeType();
        this.jobQuestionNumber = testEntity.jobQuestionNumber();
        this.jobQuestionType = testEntity.jobQuestionType();
        this.SPILanguageSystemNumber = testEntity.SPILanguageSystemNumber();
        this.SPINonLanguageSystemNumber = testEntity.SPINonLanguageSystemNumber();
        this.SPIOthersNumber = testEntity.SPIOthersNumber();
        this.personalityDiagnosisNumber = testEntity.personalityDiagnosisNumber();
        this.personalityDiagnosisType = testEntity.personalityDiagnosisType();
        this.nationalLanguageNumber = testEntity.nationalLanguageNumber();
        this.nationalLanguageType = testEntity.nationalLanguageType();
        this.mathNumber = testEntity.mathNumber();
        this.mathType = testEntity.mathType();
        this.englishNumber = testEntity.englishNumber();
        this.englishType = testEntity.englishType();
        this.currentAffairsNumber = testEntity.currentAffairsNumber();
        this.currentAffairsType = testEntity.currentAffairsType();
        this.writingTimer = testEntity.writingTimer();
        this.writingNumberOfCharacters = testEntity.writingNumberOfCharacters();
        this.writingTheme = testEntity.writingTheme();
        this.expertiseNumber = testEntity.expertiseNumber();
        this.expertiseType = testEntity.expertiseType();
        this.others = testEntity.others();
        this.testImpressions = testEntity.testImpressions();
    }
    public ReportForm(ReportInfoEntity mainInfo, ReportInterviewEntity interviewEntity){
        this.reportId = mainInfo.reportId();
        this.reason = String.valueOf(mainInfo.reason());
        this.activityTime = mainInfo.activityTime();
        this.isSelection = String.valueOf(mainInfo.companyDecision());
        this.nextAction = String.valueOf(mainInfo.nextAction());

        this.interviewerNumber = interviewEntity.interviewerNumber();
        this.interviewType = interviewEntity.interviewType();
        this.interviewContent = interviewEntity.interviewContent();
        this.interviewImpressions = interviewEntity.interviewImpressions();
    }
    public ReportForm(ReportInfoEntity mainInfo, ReportInformalCeremonyEntity informalCeremonyEntity){
        this.reportId = mainInfo.reportId();
        this.reason = String.valueOf(mainInfo.reason());
        this.activityTime = mainInfo.activityTime();
        this.isSelection = String.valueOf(mainInfo.companyDecision());
        this.nextAction = String.valueOf(mainInfo.nextAction());

        this.informalCeremonyImpressions = informalCeremonyEntity.informalCeremonyImpressions();
    }
    public ReportForm(ReportInfoEntity mainInfo, ReportTrainingEntity trainingEntity){
        this.reportId = mainInfo.reportId();
        this.reason = String.valueOf(mainInfo.reason());
        this.activityTime = mainInfo.activityTime();
        this.isSelection = String.valueOf(mainInfo.companyDecision());
        this.nextAction = String.valueOf(mainInfo.nextAction());

        this.trainingImpressions = trainingEntity.trainingImpressions();
    }
    public ReportForm(ReportInfoEntity mainInfo, ReportOtherEntity otherEntity){
        this.reportId = mainInfo.reportId();
        this.reason = String.valueOf(mainInfo.reason());
        this.activityTime = mainInfo.activityTime();
        this.isSelection = String.valueOf(mainInfo.companyDecision());
        this.nextAction = String.valueOf(mainInfo.nextAction());

        this.activityContent = otherEntity.activityContent();
        this.othersImpressions = otherEntity.othersImpressions();
    }
    public ReportForm(ReportInfoEntity mainInfo, List<ReportSeminarEntity> seminarEntities){
        this.reportId = mainInfo.reportId();
        this.reason = String.valueOf(mainInfo.reason());
        this.activityTime = mainInfo.activityTime();
        this.seminarForms = seminarEntities.stream().map(ReportSeminarEntity::toSeminarForm).toList();
    }
}
