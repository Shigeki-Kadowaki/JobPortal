package com.jobportal.JobPortal.Controller.Form;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@RequiredArgsConstructor
public class TimeTableForm {
    Integer grade;
    String classroom;
    String course;
    String semester;
    Map<Integer, List<Integer>> timeTable;

//    public void toTineTableEntity() {
//        A
//        return new TimeTableEntity(
//
//        );
//    }
}
