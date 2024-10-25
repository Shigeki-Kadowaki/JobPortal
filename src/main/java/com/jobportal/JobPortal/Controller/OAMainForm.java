package com.jobportal.JobPortal.Controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;
import java.util.Map;
import com.jobportal.JobPortal.Controller.ValidationGroup.*;

@Data
public class OAMainForm {
//共通部分
        @NotBlank
        @Pattern(regexp = "jobSearchForm|seminarForm|bereavementForm|attendanceBanForm|otherForm")
        private String reasonForAbsence;
        @NotEmpty
        @Valid
        private Map<@NotBlank String,@NotEmpty List<@NotBlank String>> OAPeriods;
//就活部分
        @NotBlank(message = "必須項目です",groups = jobSearchFormGroup.class)
        @Pattern(regexp = "briefing|test|visit|other",groups = jobSearchFormGroup.class)
        private String detail;
        @NotBlank(message = "必須項目です",groups = jobSearchFormGroup.class)
        private String companyName;
        @NotBlank(message = "必須項目です",groups = jobSearchFormGroup.class)
        @Size(max = 256, message = "256文字以内で入力してください",groups = jobSearchFormGroup.class)
        private String address;
//セミナー部分
        @NotBlank(message = "必須項目です",groups = seminarGroup.class)
        @Size(max = 64, message = "64文字以内で入力してください",groups = seminarGroup.class)
        private String seminarName;
        @NotBlank(message = "必須項目です",groups = seminarGroup.class)
        @Size(max = 64, message = "64文字以内で入力してください",groups = seminarGroup.class)
        private String location;
        @NotBlank(message = "必須項目です",groups = seminarGroup.class)
        @Size(max = 64, message = "64文字以内で入力してください",groups = seminarGroup.class)
        private String venueName;
//忌引部分
        @NotBlank(message = "必須項目です",groups = bereavementGroup.class)
        @Size(max = 64, message = "64文字以内で入力してください",groups = bereavementGroup.class)
        private String deceasedName;
        @NotBlank(message = "必須項目です",groups = bereavementGroup.class)
        @Size(max = 64, message = "64文字以内で入力してください",groups = bereavementGroup.class)
        private String relationship;
//出席停止部分
        @NotBlank(message = "必須項目です",groups = banGroup.class)
        @Size(max = 256, message = "256文字以内で入力してください",groups = banGroup.class)
        private String BanReason;
//その他部分
        @NotBlank(message = "必須項目です",groups = otherGroup.class)
        @Size(max = 64, message = "64文字以内で入力してください",groups = otherGroup.class)
        private String otherReason;

}
