package com.jobportal.JobPortal.Controller.Form;

import com.jobportal.JobPortal.Controller.Student;
import com.jobportal.JobPortal.Controller.ValidationGroup.*;
import com.jobportal.JobPortal.Service.Entity.*;
import com.jobportal.JobPortal.Service.JobSearchWork;
import com.jobportal.JobPortal.Service.OAReason;
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
        @NotBlank(message = "必須項目です")
        //@Pattern(regexp = "jobSearch|seminar|bereavement|attendanceBan|other")
        private String reasonForAbsence;
        @NotEmpty(message = "日付が未選択です")
        private Map<String, List<String>> OAPeriods;
        private Map<String, List<String>> OASubjects;
//        private List<OADatesForm> OADates;
        private boolean reportRequired;
//就活部分
        @NotBlank(message = "必須項目です", groups = jobSearchFormGroup.class)
        @Pattern(regexp = "briefing|test|visit|jobOther", groups = jobSearchFormGroup.class)
        private String work;
        @NotBlank(message = "必須項目です", groups = jobSearchFormGroup.class)
        private String companyName;
        @NotBlank(message = "必須項目です", groups = jobSearchFormGroup.class)
        @Size(max = 256, message = "256文字以内で入力してください", groups = jobSearchFormGroup.class)
        private String address;
        @Size(max = 256, message = "256文字以内で入力してください", groups = jobSearchFormGroup.class)
        private String jobSearchRemarks;
        @NotNull(message = "必須項目です", groups = jobSearchFormGroup.class)
        @Max(value = 23, groups = jobSearchFormGroup.class)
        @Min(value = 0, groups = jobSearchFormGroup.class)
        private Integer jobSearchVisitStartHour;
        @NotNull(message = "必須項目です", groups = jobSearchFormGroup.class)
        @Max(value = 59, groups = jobSearchFormGroup.class)
        @Min(value = 0, groups = jobSearchFormGroup.class)
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
        @Size(max = 256, message = "256文字以内で入力してください",groups = seminarGroup.class)
        private String seminarRemarks;
        @NotNull(message = "必須項目です", groups = seminarGroup.class)
        @Max(value = 23, groups = seminarGroup.class)
        @Min(value = 0, groups = seminarGroup.class)
        private Integer seminarVisitStartHour;
        @NotNull(message = "必須項目です", groups = seminarGroup.class)
        @Max(value = 59, groups = seminarGroup.class)
        @Min(value = 0, groups = seminarGroup.class)
        private Integer seminarVisitStartMinute;
//忌引部分
        @NotBlank(message = "必須項目です",groups = bereavementGroup.class)
        @Size(max = 64, message = "64文字以内で入力してください",groups = bereavementGroup.class)
        private String deceasedName;
        @NotBlank(message = "必須項目です",groups = bereavementGroup.class)
        @Size(max = 64, message = "64文字以内で入力してください",groups = bereavementGroup.class)
        private String relationship;
        @Size(max = 256, message = "256文字以内で入力してください",groups = bereavementGroup.class)
        private String bereavementRemarks;
//出席停止部分
        @NotBlank(message = "必須項目です",groups = attendanceBanGroup.class)
        @Size(max = 256, message = "256文字以内で入力してください",groups = attendanceBanGroup.class)
        private String banReason;
        @Size(max = 256, message = "256文字以内で入力してください",groups = attendanceBanGroup.class)
        private String banRemarks;
//その他部分
        @NotBlank(message = "必須項目です",groups = otherGroup.class)
        @Size(max = 128, message = "128文字以内で入力してください",groups = otherGroup.class)
        private String otherReason;
        @Size(max = 256, message = "256文字以内で入力してください",groups = otherGroup.class)
        private String otherRemarks;


        public OAMainEntity toMainEntity(Integer studentId, Student student){
                return new OAMainEntity(
                        null,
                        studentId,
                        checkReportRequired(reasonForAbsence),
                        "unaccepted",
                        OAReason.valueOf(reasonForAbsence),
                        false,
                        checkReportRequired(reasonForAbsence),
                        false,
                        checkReportRequired(reasonForAbsence)?false:null,
                        student.getGrade(),
                        student.getClassroom(),
                        student.getCourse(),
                        student.getSurname()+" "+student.getGivenname(),
                        student.getMail()
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
                var periodMap = getOAPeriods();
                var subjectMap = getOASubjects();
                ArrayList<OADatesEntity> dates = new ArrayList<>();
                periodMap.forEach((date,periods)->{
                        final int[] index = {0};
                                periods.forEach(period->{
                                        dates.add(new OADatesEntity(
                                                Integer.parseInt(period),
                                                LocalDate.parse(formatDate(date,"-")),
                                                subjectMap.get(date).get(index[0]++))
                                                );
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

        public OAMainForm(JobSearchEntity entity,String reasonForAbsence, Map<String, List<String>> lessonInfoEntities, boolean reportRequired, String work, String companyName, String address,String jobSearchRemarks, Integer jobSearchVisitStartHour, Integer jobSearchVisitStartMinute){
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
        public OAMainForm(SeminarEntity entity, String reasonForAbsence, Map<String, List<String>> lessonInfoEntities, boolean reportRequired, String seminarName, String location, String venueName, String remarks, Integer seminarVisitStartHour, Integer seminarVisitStartMinute){
                this.reasonForAbsence = reasonForAbsence;
                this.OAPeriods = lessonInfoEntities;
                this.reportRequired = reportRequired;
                this.seminarName = seminarName;
                this.location = location;
                this.venueName = venueName;
                this.seminarRemarks = remarks;
                this.seminarVisitStartHour = seminarVisitStartHour;
                this.seminarVisitStartMinute = seminarVisitStartMinute;
        }
        public OAMainForm(BereavementEntity entity, String reasonForAbsence, Map<String, List<String>> lessonInfoEntities, boolean reportRequired, String remarks, String deceasedName, String relationship){
                this.reasonForAbsence = reasonForAbsence;
                this.OAPeriods = lessonInfoEntities;
                this.reportRequired = reportRequired;
                this.bereavementRemarks = remarks;
                this.deceasedName = deceasedName;
                this.relationship = relationship;
        }
        public OAMainForm(AttendanceBanEntity entity, String reasonForAbsence, Map<String, List<String>> lessonInfoEntities, boolean reportRequired, String banRemarks, String banReason){
                this.reasonForAbsence = reasonForAbsence;
                this.OAPeriods = lessonInfoEntities;
                this.reportRequired = reportRequired;
                this.banReason = banReason;
                this.banRemarks = banRemarks;
        }
        public OAMainForm(OtherEntity entity, String reasonForAbsence, Map<String, List<String>> lessonInfoEntities, boolean reportRequired, String otherRemarks, String otherReason){
                this.reasonForAbsence = reasonForAbsence;
                this.OAPeriods = lessonInfoEntities;
                this.reportRequired = reportRequired;
                this.otherReason = otherReason;
                this.otherRemarks = otherRemarks;
        }
//        public static  toDatesEntity(OAMainForm form) {
//
}