package com.jobportal.JobPortal.Service.Entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class OAMainEntity
{
    final Integer officialAbsenceId;
    final Integer studentId;
    final boolean reportRequired;//レポートが必要かどうか。trueなら必要。
    final String status;
    final String reason;
    final boolean reportSubmittedFlag;//該当OAの報告書が提出済みかどうか。trueなら提出済み。
    final boolean careerCheckRequired;//就活かセミナー・合説ならtrue
    final boolean teacherCheck;
    final Boolean careerCheck;
}
