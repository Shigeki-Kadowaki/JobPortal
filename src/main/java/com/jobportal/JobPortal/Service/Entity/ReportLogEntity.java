package com.jobportal.JobPortal.Service.Entity;

import com.jobportal.JobPortal.Service.CompanyDecision;
import com.jobportal.JobPortal.Service.JobInterviewType;
import com.jobportal.JobPortal.Service.NextAction;
import com.jobportal.JobPortal.Service.ReportType;
import lombok.Data;

import java.util.List;

@Data
public class ReportLogEntity {
    Integer reportId;
    Integer studentId;
    ReportType reason;
    Integer activityTime;
    CompanyDecision isSelection;
    NextAction nextAction;
    String companyName;

    //説明会.
    String briefingContent;
    String briefingImpressions;

    //試験.
    Integer generalKnowledgeNumber;//一般常識問題数
    String generalKnowledgeType;//一般常識問題種類
    Integer jobQuestionNumber;//職業問題数
    String jobQuestionType;//職業問題種類
    Integer SPILanguageSystemNumber;//SPI言語系問題数
    Integer SPINonLanguageSystemNumber;//SPI非言語系問題数
    Integer SPIOthersNumber;//SPIその他問題数
    Integer personalityDiagnosisNumber;//性格問題問題数
    String personalityDiagnosisType;//性格問題種類
    Integer nationalLanguageNumber;//国語問題数
    String nationalLanguageType;//国語問題種類
    Integer mathNumber;//数学問題数
    String mathType;//数学問題種類
    Integer englishNumber;//英語問題数
    String englishType;//英語問題種類
    Integer currentAffairsNumber;//時事問題数
    String currentAffairsType;//時事問題種類
    Integer writingTimer;//作文時間
    Integer writingNumberOfCharacters;//作文文字数
    String writingTheme;//作文テーマ
    Integer expertiseNumber;//専門問題数
    String expertiseType;//専門問題種類
    String others;//その他
    String testImpressions;//感想

    //面接
    Integer interviewerNumber;
    JobInterviewType interviewType;
    String interviewContent;
    String interviewImpressions;

    //研修
    String trainingImpressions;

    //内定式
    String informalCeremonyImpressions;

    //セミナー
    List<ReportSeminarEntity> seminarForms;

    //その他.
    String activityContent;
    String othersImpressions;

    public Integer idToYear(Integer studentId) {
        return studentId / 100;
    }
    public ReportLogEntity(ReportInfoEntity reportInfoEntity, ReportBriefingEntity briefingEntity) {
        this.reportId = reportInfoEntity.reportId();
        this.studentId = reportInfoEntity.studentId();
        this.reason = reportInfoEntity.reason();
        this.activityTime = reportInfoEntity.activityTime();
        this.companyName = reportInfoEntity.companyName();
        this.isSelection = reportInfoEntity.companyDecision();
        this.nextAction = reportInfoEntity.nextAction();

        this.briefingContent = briefingEntity.briefingContent();
        this.briefingImpressions = briefingEntity.briefingImpressions();

    }
    public ReportLogEntity(ReportInfoEntity reportInfoEntity, ReportInterviewEntity interviewEntity) {
        this.reportId = reportInfoEntity.reportId();
        this.studentId = reportInfoEntity.studentId();
        this.reason = reportInfoEntity.reason();
        this.activityTime = reportInfoEntity.activityTime();
        this.companyName = reportInfoEntity.companyName();
        this.isSelection = reportInfoEntity.companyDecision();
        this.nextAction = reportInfoEntity.nextAction();

        this.interviewerNumber = interviewEntity.interviewerNumber();
        this.interviewType = interviewEntity.interviewType();
        this.interviewContent = interviewEntity.interviewContent();
        this.interviewImpressions = interviewEntity.interviewImpressions();
    }
    public ReportLogEntity(ReportInfoEntity reportInfoEntity, ReportTestEntity testEntity) {
        this.reportId = reportInfoEntity.reportId();
        this.studentId = reportInfoEntity.studentId();
        this.reason = reportInfoEntity.reason();
        this.activityTime = reportInfoEntity.activityTime();
        this.companyName = reportInfoEntity.companyName();
        this.isSelection = reportInfoEntity.companyDecision();
        this.nextAction = reportInfoEntity.nextAction();

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
    public ReportLogEntity(ReportInfoEntity reportInfoEntity, ReportInformalCeremonyEntity informalCeremonyEntity ) {
        this.reportId = reportInfoEntity.reportId();
        this.studentId = reportInfoEntity.studentId();
        this.reason = reportInfoEntity.reason();
        this.activityTime = reportInfoEntity.activityTime();
        this.companyName = reportInfoEntity.companyName();
        this.isSelection = reportInfoEntity.companyDecision();
        this.nextAction = reportInfoEntity.nextAction();

        this.informalCeremonyImpressions = informalCeremonyEntity.informalCeremonyImpressions();
    }
    public ReportLogEntity(ReportInfoEntity reportInfoEntity, ReportTrainingEntity trainingEntity) {
        this.reportId = reportInfoEntity.reportId();
        this.studentId = reportInfoEntity.studentId();
        this.reason = reportInfoEntity.reason();
        this.activityTime = reportInfoEntity.activityTime();
        this.companyName = reportInfoEntity.companyName();
        this.isSelection = reportInfoEntity.companyDecision();
        this.nextAction = reportInfoEntity.nextAction();

        this.trainingImpressions = trainingEntity.trainingImpressions();
    }
    public ReportLogEntity(ReportInfoEntity reportInfoEntity, ReportOtherEntity otherEntity) {
        this.reportId = reportInfoEntity.reportId();
        this.studentId = reportInfoEntity.studentId();
        this.reason = reportInfoEntity.reason();
        this.activityTime = reportInfoEntity.activityTime();
        this.companyName = reportInfoEntity.companyName();
        this.isSelection = reportInfoEntity.companyDecision();
        this.nextAction = reportInfoEntity.nextAction();

        this.activityContent = otherEntity.activityContent();
        this.othersImpressions = otherEntity.othersImpressions();
    }
    public ReportLogEntity(ReportInfoEntity reportInfoEntity, List<ReportSeminarEntity> seminarEntities) {
        this.reportId = reportInfoEntity.reportId();
        this.studentId = reportInfoEntity.studentId();
        this.reason = reportInfoEntity.reason();
        this.activityTime = reportInfoEntity.activityTime();
        this.seminarForms = seminarEntities;
    }

    public ReportLogEntity() {

    }
}
