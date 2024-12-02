package com.jobportal.JobPortal.Controller.Form;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@RequiredArgsConstructor
public class TimeTable {
    Integer grade;
    String classroom;
    String course;
    String semester;
    Map<Integer, List<Integer>> timeTable;
}
