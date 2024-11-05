package com.jobportal.JobPortal.Service.Entity;

import com.jobportal.JobPortal.Service.OAReason;
import com.jobportal.JobPortal.Service.OAStatus;

import java.time.LocalDate;


public record OAListEntity (
        Integer officialAbsenceId,
        Integer studentId,
        OAStatus status,
        OAReason reason,
        OAStatus reportStatus,
        LocalDate startDate,
        LocalDate endDate,
        Integer period
){

//    public OAListDTO toListDTO() {
//        return new OAListDTO(
//                officialAbsenceId,
//                studentId,
//                status.getJapaneseName(),
//                reason.getJapaneseName(),
//                existsReport(reportStatus),
//                MainService.dateFormat(startDate),
//                MainService.dateFormat(endDate)
//        );
//    }
    public String existsReport(OAStatus status) {
        if(status == null){
            return "未提出";
        }else {
            return status.getJapaneseName();
        }
    }
//    public String getStatus(OAStatus status) {
//        Map<OAStatus,String> map = new HashMap<>();
//        map.put(acceptance,"受理");
//        map.put(unaccepted,"未受理");
//        map.put(rejection,"却下");
//        return map.get(status);
//    }
}