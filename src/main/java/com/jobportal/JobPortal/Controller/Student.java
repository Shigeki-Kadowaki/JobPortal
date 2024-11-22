package com.jobportal.JobPortal.Controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Student {
    private Integer id;
    private String surname;
    private String givenname;
    private String group;
    private Integer grade;
    private String classroom;
    private Integer cno;
    private String department;
    private String course;
}
