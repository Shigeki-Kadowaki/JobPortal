package com.jobportal.JobPortal.Controller;

import com.jobportal.JobPortal.Controller.ValidationGroup.*;
import com.jobportal.JobPortal.Service.*;
import jakarta.validation.ConstraintViolation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class MainController {

    @Autowired
    private final MainService service;

    @GetMapping("/")
    public String showFormAgain(@ModelAttribute("student") student student) {
        student.setId(40104);
        student.setName("木谷");
        System.out.println(student.getId().toString());
        return "redirect:/student/" + student.getId();
    }

    @GetMapping("/student/{studentId}")
    public String student(@PathVariable("studentId") Integer studentId, @ModelAttribute("student") student student) {
        student.setId(40104);
        student.setName("木谷");
        return "student";
    }


//
//    @PostMapping(value="/test", params="button1")
//    public String test(@ModelAttribute("validateTest") @Validated({atext.class})validateTest validatetest, BindingResult bindingResult, Model model) {
//        System.out.println("success??");
//        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
//        Set<ConstraintViolation<List<validateTestChild>>> violations = validator.validate(validatetest.getList1(), atext.class);
//        if(!violations.isEmpty()){
//            System.out.println("error");
//            showErrorDetails(violations);
//            return "index";
//        }else {
//            System.out.println("successa");
//            return "redirect:/index";
//        }
//    }
//
//    @PostMapping(value="/test", params="button2")
//    public String test2(@ModelAttribute("validateTest") @Validated({btext.class}) validateTest validatetest, BindingResult bindingResult, Model model) {
//        System.out.println("success??");
//        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
//        Set<ConstraintViolation<List<validateTestChild>>> violations = validator.validate(validatetest.getList1(), btext.class);
//        if(!violations.isEmpty()){
//            System.out.println("error");
//            showErrorDetails(violations);
//            return "index";
//        }else {
//            System.out.println("successb");
//            return "redirect:/index";
//        }
//    }

    private static <T> void showErrorDetails(
            Set<ConstraintViolation<T>> constraintViolations) {
        for (ConstraintViolation<T> violation : constraintViolations) {
            System.out.println("----------");
            System.out.println(
                    "MessageTemplate : " + violation.getMessageTemplate());
            System.out.println("Message : " + violation.getMessage());
            System.out.println("InvalidValue : " + violation.getInvalidValue());
            System.out.println("PropertyPath : " + violation.getPropertyPath());
            System.out.println("RootBeanClass : " + violation.getRootBeanClass());
            System.out.println("RootBean : " + violation.getRootBean());
        }
    }


    //Form画面
    @GetMapping("/student/{studentId}/OACreationForm")
    public String getForm(@PathVariable("studentId") Integer studentId, @ModelAttribute("oAMainForm") OAMainForm form, Model model){
        model.addAttribute("studentId",studentId);
        return "OACreationForm";
    }

    //就活公欠届提出
    @PostMapping(value = "/student/{studentId}/OACreationForm", params = "jobSearchForm")
    public String postJobForm(@ModelAttribute("studentId") @PathVariable("studentId") Integer studentId, @Validated(jobSearchFormGroup.class) @ModelAttribute("oAMainForm") OAMainForm form, BindingResult bindingResult){
//        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
//        Set<ConstraintViolation<OAMainForm>> violations = validator.validate(form, jobSearchFormGroup.class);
//        if(!violations.isEmpty()){
//            showErrorDetails(violations);
//            return "OACreationForm";
//        }
        if(bindingResult.hasErrors()){
            System.out.println("error");
            return "OACreationForm";
        }
        OAMainEntity mainEntity = form.toMainEntity(studentId);
        service.createOA(mainEntity);
        Integer officialAbsenceId = mainEntity.getOfficialAbsenceId();
        List<OADatesEntity> dateList = form.toDatesEntity();
        service.createOADates(dateList, officialAbsenceId);
        JobSearchEntity jobEntity = form.toJobSearchEntity(officialAbsenceId);
        service.createJobSearch(jobEntity);
        return "redirect:/student/{studentId}/OACreationForm";
    }
    //セミナー公欠届提出
    @PostMapping(value = "/student/{studentId}/OACreationForm", params = "seminarForm")
    public String postSeminarForm(@ModelAttribute("studentId") @PathVariable("studentId") Integer studentId, @Validated(seminarGroup.class) @ModelAttribute("oAMainForm") OAMainForm form, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            System.out.println("error");
            return "OACreationForm";
        }
        OAMainEntity mainEntity = form.toMainEntity(studentId);
        service.createOA(mainEntity);
        Integer officialAbsenceId = mainEntity.getOfficialAbsenceId();
        List<OADatesEntity> dateList = form.toDatesEntity();
        service.createOADates(dateList, officialAbsenceId);
        SeminarEntity seminarEntity = form.toSeminarEntity(officialAbsenceId);
        service.createSeminar(seminarEntity);
        return "redirect:/student/{studentId}/OACreationForm";
    }
    //忌引公欠届提出
    @PostMapping(value = "/student/{studentId}/OACreationForm", params = "bereavementForm")
    public String postBereavementForm(@ModelAttribute("studentId") @PathVariable("studentId") Integer studentId, @Validated(bereavementGroup.class) @ModelAttribute("oAMainForm") OAMainForm form, BindingResult bindingResult, Model model){
        model.addAttribute("studentId", studentId);
        if(bindingResult.hasErrors()){
            System.out.println("error");
            return "/OACreationForm";
        }
        OAMainEntity mainEntity = form.toMainEntity(studentId);
        service.createOA(mainEntity);
        Integer officialAbsenceId = mainEntity.getOfficialAbsenceId();
        List<OADatesEntity> dateList = form.toDatesEntity();
        service.createOADates(dateList, officialAbsenceId);
        BereavementEntity bereavementEntity = form.toBereavementEntity(officialAbsenceId);
        service.createBereavement(bereavementEntity);
        return "redirect:/student/{studentId}/OACreationForm";
    }
    //出席停止公欠届提出
    @PostMapping(value = "/student/{studentId}/OACreationForm", params = "attendanceBanForm")
    public String postBanForm(@ModelAttribute("studentId") @PathVariable("studentId") Integer studentId, @Validated(attendanceBanGroup.class) @ModelAttribute("oAMainForm") OAMainForm form, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            System.out.println("error");
            return "OACreationForm";
        }
        OAMainEntity mainEntity = form.toMainEntity(studentId);
        service.createOA(mainEntity);
        Integer officialAbsenceId = mainEntity.getOfficialAbsenceId();
        List<OADatesEntity> dateList = form.toDatesEntity();
        service.createOADates(dateList, officialAbsenceId);
        AttendanceBanEntity attendanceBanEntity = form.toAttendanceBanEntity(officialAbsenceId);
        service.createAttendanceBan(attendanceBanEntity);
        return "redirect:/student/{studentId}/OACreationForm";
    }
    //その他公欠届提出
    @PostMapping(value = "/student/{studentId}/OACreationForm", params = "otherForm")
    public String postOtherForm(@ModelAttribute("studentId") @PathVariable("studentId") Integer studentId, @Validated(otherGroup.class) @ModelAttribute("oAMainForm") OAMainForm form, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            System.out.println("error");
            return "OACreationForm";
        }
        OAMainEntity mainEntity = form.toMainEntity(studentId);
        service.createOA(mainEntity);
        Integer officialAbsenceId = mainEntity.getOfficialAbsenceId();
        List<OADatesEntity> dateList = form.toDatesEntity();
        service.createOADates(dateList, officialAbsenceId);
        OtherEntity otherEntity = form.toOtherEntity(officialAbsenceId);
        service.createOther(otherEntity);
        return "redirect:/student/{studentId}/OACreationForm";
    }




    //提出済み公欠届BOX
    @GetMapping("/student/{studentId}/OABox")
    public String showAllOAs(@PathVariable("studentId") Integer studentId, Model model){
        var OAList = service.findAllOAs(studentId);
        model.addAttribute("OAList", OAList);
        model.addAttribute("studentId", studentId);
        return "OABox";
    }




}
