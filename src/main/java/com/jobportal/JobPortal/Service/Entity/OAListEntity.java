package com.jobportal.JobPortal.Service.Entity;

import com.jobportal.JobPortal.Service.DTO.OAListDTO;
import com.jobportal.JobPortal.Service.OAReason;
import com.jobportal.JobPortal.Service.OAStatus;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.jobportal.JobPortal.Service.OAStatus.*;
import static java.lang.Integer.parseInt;

public record OAListEntity (
        Integer officialAbsenceId,
        Integer studentId,
        OAStatus status,
        OAReason reason,
        boolean submittedFlag,
        OAStatus reportStatus,
        LocalDate startDate,
        LocalDate endDate
){
    public OAListDTO toListDTO() {
        return new OAListDTO(
                officialAbsenceId,
                studentId,
                status.getJapaneseName(),
                reason.getJapaneseName(),
                submittedFlag,
                reportStatus.getJapaneseName(),
                dateFormat(startDate),
                dateFormat(endDate)
        );
    }
    public String dateFormat(LocalDate date) {
        String yyyy = date.toString().substring(0,4);
        String mm = date.toString().substring(5,7);
        String dd = date.toString().substring(8,10);
        String dow = "";
        Calendar cal = Calendar.getInstance();
        cal.set(parseInt(yyyy), parseInt(mm) - 1, parseInt(dd));
        dow = switch (cal.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY -> "(日)";
            case Calendar.MONDAY -> "(月)";
            case Calendar.TUESDAY -> "(火)";
            case Calendar.WEDNESDAY -> "(水)";
            case Calendar.THURSDAY -> "(木)";
            case Calendar.FRIDAY -> "(金)";
            case Calendar.SATURDAY -> "(土)";
            default -> dow;
        };
        return yyyy + "年" + mm + "月" + dd + "日" + dow;
    }
//    public String getStatus(OAStatus status) {
//        Map<OAStatus,String> map = new HashMap<>();
//        map.put(acceptance,"受理");
//        map.put(unaccepted,"未受理");
//        map.put(rejection,"却下");
//        return map.get(status);
//    }
}
