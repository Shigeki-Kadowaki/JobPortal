package com.jobportal.JobPortal.Controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Lesson {
    final Integer id;
    final String name;
    final String teacher;
}
