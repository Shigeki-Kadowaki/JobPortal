package com.jobportal.JobPortal.Controller.Form;

import com.jobportal.JobPortal.Service.Entity.OtherEntity;
import com.jobportal.JobPortal.Controller.ValidationGroup.*;
import com.jobportal.JobPortal.Service.*;
import com.jobportal.JobPortal.Service.Entity.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.jobportal.JobPortal.Service.OAStatus.unaccepted;

@Data
public class OAMainForm {
        //共通部分
//        private Integer id;
        @NotBlank
        @Pattern(regexp = "jobSearch|seminar|bereavement|attendanceBan|other")
        private String reasonForAbsence;
        @NotEmpty(message = "日付が未選択です")
        private Map<String,List<String>> OAPeriods;
        @Size(max = 256, message = "256文字以内で入力してください")
        private String remarks;
//        private List<OADatesForm> OADates;
        private boolean reportRequired;
//就活部分
        @NotBlank(message = "必須項目です",groups = jobSearchFormGroup.class)
        @Pattern(regexp = "briefing|test|visit|other",groups = jobSearchFormGroup.class)
        private String work;
        @NotBlank(message = "必須項目です",groups = jobSearchFormGroup.class)
        private String companyName;
        @NotBlank(message = "必須項目です",groups = jobSearchFormGroup.class)
        @Size(max = 256, message = "256文字以内で入力してください",groups = jobSearchFormGroup.class)
        private String address;
        @NotBlank(message = "必須項目です", groups = jobSearchFormGroup.class)
        private String jobSearchVisitStartHour;
        @NotBlank(message = "必須項目です", groups = jobSearchFormGroup.class)
        private String jobSearchVisitStartMinute;

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
        @NotBlank(message = "必須項目です", groups = seminarGroup.class)
        private String seminarVisitStartHour;
        @NotBlank(message = "必須項目です", groups = seminarGroup.class)
        private String seminarVisitStartMinute;
//忌引部分
        @NotBlank(message = "必須項目です",groups = bereavementGroup.class)
        @Size(max = 64, message = "64文字以内で入力してください",groups = bereavementGroup.class)
        private String deceasedName;
        @NotBlank(message = "必須項目です",groups = bereavementGroup.class)
        @Size(max = 64, message = "64文字以内で入力してください",groups = bereavementGroup.class)
        private String relationship;
//出席停止部分
        @NotBlank(message = "必須項目です",groups = attendanceBanGroup.class)
        @Size(max = 256, message = "256文字以内で入力してください",groups = attendanceBanGroup.class)
        private String banReason;
//その他部分
        @NotBlank(message = "必須項目です",groups = otherGroup.class)
        @Size(max = 128, message = "128文字以内で入力してください",groups = otherGroup.class)
        private String otherReason;


        public OAMainEntity toMainEntity(Integer studentId){
                return new OAMainEntity(
                        null,
                        studentId,
                        reportRequired,
                        OAStatus.valueOf("unaccepted"),
                        OAReason.valueOf(reasonForAbsence),
                        false
                );
        }



        public JobSearchEntity toJobSearchEntity(Integer officialAbsenceId){
                return new JobSearchEntity(
                        officialAbsenceId,
                        JobSearchWork.valueOf(work),
                        companyName,
                        address
                );
        }

        public SeminarEntity toSeminarEntity(Integer officialAbsenceId){
                return new SeminarEntity(
                        officialAbsenceId,
                        seminarName,
                        location,
                        venueName
                );
        }

        public BereavementEntity toBereavementEntity(Integer officialAbsenceId){
                return new BereavementEntity(
                        officialAbsenceId,
                        deceasedName,
                        relationship
                );
        }

        public AttendanceBanEntity toAttendanceBanEntity(Integer officialAbsenceId){
                return new AttendanceBanEntity(
                        officialAbsenceId,
                        banReason
                );
        }

        public OtherEntity toOtherEntity(Integer officialAbsenceId){
                return new OtherEntity(
                        officialAbsenceId,
                        otherReason
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
                                                LocalDate.parse(formatDate(date,"-")),
                                                LocalDate.now()
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

//        public static  toDatesEntity(OAMainForm form) {
//
}