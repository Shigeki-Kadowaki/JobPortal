package com.jobportal.JobPortal.Controller.Form;

import com.jobportal.JobPortal.Service.Entity.TimeTableEntity;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@RequiredArgsConstructor
public class TimeTableInfoForm {
    Integer grade;
    String classroom;
    String course;
    String semester;
    Map<Integer, List<Integer>> timeTable;

    public List<TimeTableEntity> toTimeTableEntity() {
        List<TimeTableEntity> list = new ArrayList<>();
        timeTable.forEach((weekdayNumber,subjectIds)->{
            int period = 1;
            for(var subjectId : subjectIds) {
                if(subjectId != null) {
                    list.add(new TimeTableEntity(weekdayNumber, period, subjectId));
                }
                period++;
            }
        });
        return list;
    }
}
