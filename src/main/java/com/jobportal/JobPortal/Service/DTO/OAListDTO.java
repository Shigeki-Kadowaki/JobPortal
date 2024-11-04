package com.jobportal.JobPortal.Service.DTO;

import java.util.List;

public record OAListDTO(
        Integer officialAbsenceId,
        Integer studentId,
        String status,
        String reason,
        String reportStatus,
        String startDate,
        String endDate,
        List<Integer> lessons
){
//    public String checkColor (String status) {
//        switch (status) {
//            case "accepted" -> {
//                return "a";}
//            case "rejected" -> {return "r";}
//            case "pending" -> {return "p";}
//            default -> {return "list-group-item-warning";}
//        }
//    }

}
