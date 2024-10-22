package com.jobportal.JobPortal.Controller;

import java.util.List;

public record DatesGetFormExample(
        List<String> dates,
        List<PeriodGetFromExample> periods
) {
}
