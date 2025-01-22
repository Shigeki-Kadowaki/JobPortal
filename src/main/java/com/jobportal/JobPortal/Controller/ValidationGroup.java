package com.jobportal.JobPortal.Controller;

import jakarta.validation.groups.Default;

public interface ValidationGroup {
    public interface jobSearchFormGroup extends Default {}
    public interface seminarGroup extends Default {}
    public interface bereavementGroup extends Default {}
    public interface attendanceBanGroup extends Default {}
    public interface otherGroup extends Default {}
    public static interface atext extends Default {}
    public static interface btext extends Default {}

    public interface briefingGroup extends Default{}
    public interface testGroup extends Default{}
    public interface interviewGroup extends Default{}
    public interface informalCeremonyGroup extends Default{}
    public interface trainingGroup extends Default{}
    public interface reportSeminarGroup extends Default{}
    public interface reportOtherGroup extends Default{}
}
