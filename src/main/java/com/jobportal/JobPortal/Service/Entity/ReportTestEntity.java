package com.jobportal.JobPortal.Service.Entity;

public record ReportTestEntity(
        Integer reportId,
        String generalKnowledgeNumber,
        String generalKnowledgeType,
        Integer jobQuestionNumber,
        String jobQuestionType,
        Integer SPILanguageSystemNumber,
        Integer SPINonLanguageSystemNumber,
        Integer SPIOthersNumber,
        Integer personalityDiagnosisNumber,
        String personalityDiagnosisType,
        Integer nationalLanguageNumber,
        String nationalLanguageType,
        Integer mathNumber,
        String mathType,
        Integer englishNumber,
        String englishType,
        Integer currentAffairsNumber,
        String currentAffairsType,
        Integer writingTimer,
        Integer writingNumberOfCharacters,
        String writingTheme,
        Integer expertiseNumber,
        String expertiseType,
        String others,
        String testImpressions
) {
}
