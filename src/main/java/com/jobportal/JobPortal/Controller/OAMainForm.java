package com.jobportal.JobPortal.Controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;
import java.util.Map;

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
        @NotBlank(message = "必須項目です")
        @Pattern(regexp = "briefing|test|visit|other")
        private String detail;
        @NotBlank(message = "必須項目です")
        private String companyName;
        @NotBlank(message = "必須項目です")
        @Size(max = 256, message = "256文字以内で入力してください")
        private String address;
//セミナー部分
        @NotBlank(message = "必須項目です")
        @Size(max = 64, message = "64文字以内で入力してください")
        private String seminarName;
        @NotBlank(message = "必須項目です")
        @Size(max = 64, message = "64文字以内で入力してください")
        private String location;
        @NotBlank(message = "必須項目です")
        @Size(max = 64, message = "64文字以内で入力してください")
        private String venueName;
//忌引部分
        @NotBlank(message = "必須項目です")
        @Size(max = 64, message = "64文字以内で入力してください")
        private String deceasedName;
        @NotBlank(message = "必須項目です")
        @Size(max = 64, message = "64文字以内で入力してください")
        private String relationship;
//出席停止部分
        @NotBlank(message = "必須項目です")
        @Size(max = 256, message = "256文字以内で入力してください")
        private String BanReason;
//その他部分
        @NotBlank(message = "必須項目です")
        @Size(max = 64, message = "64文字以内で入力してください")
        private String otherReason;

}
