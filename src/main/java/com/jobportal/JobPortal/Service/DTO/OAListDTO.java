package com.jobportal.JobPortal.Service.DTO;

import java.util.List;

public record OAListDTO(
        Integer officialAbsenceId,
        Integer studentId,
        Integer grade,
        String classroom,
        String course,
        String name,
        String status,
        String reason,
        String reportStatus,
        boolean reportRequired,
        String startDate,
        String endDate,
        boolean reportSubmittedFlag,
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
