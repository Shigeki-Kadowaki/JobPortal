package com.jobportal.JobPortal.Controller.Form;

import com.jobportal.JobPortal.Controller.ValidationGroup.*;
import com.jobportal.JobPortal.Service.Entity.*;
import com.jobportal.JobPortal.Service.JobSearchWork;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@RequiredArgsConstructor
public class OAMainForm {
        //共通部分
//        private Integer id;
        @NotBlank
        @Pattern(regexp = "jobSearch|seminar|bereavement|attendanceBan|other")
        private String reasonForAbsence;
        @NotEmpty(message = "日付が未選択です")
        private Map<String,List<String>> OAPeriods;
//        private List<OADatesForm> OADates;
        private boolean reportRequired;
//就活部分
        @NotBlank(message = "必須項目です",groups = jobSearchFormGroup.class)
        @Pattern(regexp = "briefing|test|visit|jobOther",groups = jobSearchFormGroup.class)
        private String work;
        @NotBlank(message = "必須項目です",groups = jobSearchFormGroup.class)
        private String companyName;
        @NotBlank(message = "必須項目です",groups = jobSearchFormGroup.class)
        @Size(max = 256, message = "256文字以内で入力してください",groups = jobSearchFormGroup.class)
        private String address;
        @Size(max = 256, message = "256文字以内で入力してください")
        private String jobSearchRemarks;
        @NotNull(message = "必須項目です", groups = jobSearchFormGroup.class)
        @Max(value = 23,groups = jobSearchFormGroup.class)
        @Min(value = 0,groups = jobSearchFormGroup.class)
        private Integer jobSearchVisitStartHour;
        @NotNull(message = "必須項目です", groups = jobSearchFormGroup.class)
        @Max(value = 59, groups = jobSearchFormGroup.class)
        @Min(value = 0,groups = jobSearchFormGroup.class)
        private Integer jobSearchVisitStartMinute;

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
        @Size(max = 256, message = "256文字以内で入力してください")
        private String seminarRemarks;
        @NotBlank(message = "必須項目です", groups = seminarGroup.class)
        @Max(23)
        @Min(0)
        private Integer seminarVisitStartHour;
        @NotBlank(message = "必須項目です", groups = seminarGroup.class)
        @Max(59)
        @Min(0)
        private Integer seminarVisitStartMinute;
//忌引部分
        @NotBlank(message = "必須項目です",groups = bereavementGroup.class)
        @Size(max = 64, message = "64文字以内で入力してください",groups = bereavementGroup.class)
        private String deceasedName;
        @NotBlank(message = "必須項目です",groups = bereavementGroup.class)
        @Size(max = 64, message = "64文字以内で入力してください",groups = bereavementGroup.class)
        private String relationship;
        @Size(max = 256, message = "256文字以内で入力してください")
        private String bereavementRemarks;
//出席停止部分
        @NotBlank(message = "必須項目です",groups = attendanceBanGroup.class)
        @Size(max = 256, message = "256文字以内で入力してください",groups = attendanceBanGroup.class)
        private String banReason;
        @Size(max = 256, message = "256文字以内で入力してください")
        private String banRemarks;
//その他部分
        @NotBlank(message = "必須項目です",groups = otherGroup.class)
        @Size(max = 128, message = "128文字以内で入力してください",groups = otherGroup.class)
        private String otherReason;
        @Size(max = 256, message = "256文字以内で入力してください")
        private String otherRemarks;


        public OAMainEntity toMainEntity(Integer studentId){
                return new OAMainEntity(
                        null,
                        studentId,
                        checkReportRequired(reasonForAbsence),
                        "unaccepted",
                        reasonForAbsence,
                        false,
                        LocalDate.now()
                );
        }
        public boolean checkReportRequired(String reasonForAbsence){
                return reasonForAbsence.equals("jobSearch") || reasonForAbsence.equals("seminar");
        }



        public JobSearchEntity toJobSearchEntity(Integer officialAbsenceId){
                return new JobSearchEntity(
                        officialAbsenceId,
                        JobSearchWork.valueOf(work),
                        companyName,
                        address,
                        jobSearchRemarks,
                        jobSearchVisitStartHour,
                        jobSearchVisitStartMinute
                );
        }

        public SeminarEntity toSeminarEntity(Integer officialAbsenceId){
                return new SeminarEntity(
                        officialAbsenceId,
                        seminarName,
                        location,
                        venueName,
                        seminarRemarks,
                        seminarVisitStartHour,
                        seminarVisitStartHour
                );
        }

        public BereavementEntity toBereavementEntity(Integer officialAbsenceId){
                return new BereavementEntity(
                        officialAbsenceId,
                        deceasedName,
                        relationship,
                        bereavementRemarks
                );
        }

        public AttendanceBanEntity toAttendanceBanEntity(Integer officialAbsenceId){
                return new AttendanceBanEntity(
                        officialAbsenceId,
                        banReason,
                        banRemarks
                );
        }

        public OtherEntity toOtherEntity(Integer officialAbsenceId){
                return new OtherEntity(
                        officialAbsenceId,
                        otherReason,
                        otherRemarks
                );
        }

        //日付Formを日付Entityにする。
        public List<OADatesEntity> toDatesEntity(){
                var map = getOAPeriods();
                ArrayList<OADatesEntity> dates = new ArrayList<>();
                        map.forEach((date,periods)->{
                                periods.forEach(period->{
                                        //(String date(yyyymmdd), String period)を(LocalDate date(yyyy-mm-dd), Integer period)にする。
                                        dates.add(new OADatesEntity(
                                                1,
                                                Integer.parseInt(period),
                                                LocalDate.parse(formatDate(date,"-"))
                                                ));
                                });
                        });
                return dates;

        }

        public static  boolean checkJobSearchFlag(String reason){
                return (reason.equals("jobSearchForm") || reason.equals("seminarForm"));
        }
        public static Object checkCareer(boolean flag){
                if(flag) return false;
                else return null;
        }

        public static String formatDate(String date, String sep){
                String yyyy = date.substring(0,4);
                String mm = date.substring(4,6);
                String dd = date.substring(6,8);
                return yyyy + sep + mm + sep + dd;
        }

        public OAMainForm(String reasonForAbsence, Map<String, List<String>> lessonInfoEntities, boolean reportRequired, String work, String companyName, String address,String jobSearchRemarks, Integer jobSearchVisitStartHour, Integer jobSearchVisitStartMinute){
                this.reasonForAbsence = reasonForAbsence;
                this.OAPeriods = lessonInfoEntities;
                this.reportRequired = reportRequired;
                this.work = work;
                this.companyName = companyName;
                this.address = address;
                this.jobSearchRemarks = jobSearchRemarks;
                this.jobSearchVisitStartHour = jobSearchVisitStartHour;
                this.jobSearchVisitStartMinute = jobSearchVisitStartMinute;
        }
//        public static  toDatesEntity(OAMainForm form) {
//
}