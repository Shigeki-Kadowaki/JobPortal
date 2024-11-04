package com.jobportal.JobPortal.Service.Entity;

import com.jobportal.JobPortal.Service.JobSearchWork;

//@Component
public record JobSearchEntity (
        Integer officialAbsenceId,
        JobSearchWork work,
        String companyName,
        String address
){
}
