package com.jobportal.JobPortal.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record OADatesEntity(
    Map<LocalDate,List<Integer>> OAPeriods
) {
}
