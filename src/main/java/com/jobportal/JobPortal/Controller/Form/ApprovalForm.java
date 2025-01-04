package com.jobportal.JobPortal.Controller.Form;

import lombok.Data;

import java.util.Map;

@Data
public class ApprovalForm {
    private String teacherType;
    private Map<String, String> approval;
}
