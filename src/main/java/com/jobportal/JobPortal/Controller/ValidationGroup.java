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

    public interface briefingSessionGroup extends Default{}
    public interface examGroup extends Default{}
    public interface InterviewGroup extends Default{}
    public interface informalDecisionCeremonyGroup extends Default{}
    public interface trainingGroup extends Default{}
    public interface reportSeminarGroup extends Default{}
    public interface reportOtherGroup extends Default{}
}
