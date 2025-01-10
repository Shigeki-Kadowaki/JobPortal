package com.jobportal.JobPortal.Controller;

import jakarta.validation.groups.Default;
import lombok.Builder;

public interface ValidationGroup {
    public interface jobSearchFormGroup extends Default {}
    public interface seminarGroup extends Default {}
    public interface bereavementGroup extends Default {}
    public interface attendanceBanGroup extends Default {}
    public interface otherGroup extends Default {}
    public static interface atext extends Default {}
    public static interface btext extends Default {}

    public interface briefingSessionHistoryGroup extends Default{}
}
