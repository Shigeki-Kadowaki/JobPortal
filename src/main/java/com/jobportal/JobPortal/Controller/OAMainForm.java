package com.jobportal.JobPortal.Controller;

import com.jobportal.JobPortal.Service.OAMainEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public record OAMainForm (
        @NotBlank
        @Pattern(regexp = "JobSearch | seminear | breavement | attendanceBan | other")
        String reason,
        List<OADates> date,

        JobSearchOAForm jobForm,
        BereavementOAForm bereaveForm,
        AttendanceBanOAForm banForm,
        OtherOAForm otherForm
){
    //TODO:toEntityメソッド作成
//    public OAMainEntity toEntity(OAMainForm form){
//
//    }
}
