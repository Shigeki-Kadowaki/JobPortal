package com.jobportal.JobPortal.Controller;

import com.jobportal.JobPortal.Service.MainService;
import constants.VG.a;
import constants.VG.b;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Set;

@Controller
@RequiredArgsConstructor
public class MainController {

    @Autowired
    private MainService service;
    @Autowired
    public SmartValidator validator;

    @GetMapping("/")
    public String showFormAgain(@ModelAttribute("validateTest") validateTest validateTest){
        return "index";
    }
    @GetMapping("/test")
    public String returnIndex(@ModelAttribute("validateTest") validateTest validateTest){
        return "index";
    }

    @PostMapping(value="/test", params="button1")
    public String test(@ModelAttribute("validateTest") validateTest validateTest, BindingResult bindingResult) {
        BindingResult bd;
        System.out.println("success??");
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<validateTest>> violations = validator.validate(validateTest, a.class);
        for (ConstraintViolation<validateTest> violation : violations) {
            bindingResult.rejectValue(violation.getPropertyPath().toString(), "", violation.getMessage());
        }
        if (bindingResult.hasErrors()) {
            showErrorDetails(violations);
            return "index";
        } else {
            System.out.println("bindingResult1 is success");
            return "index";
        }
    }

    @PostMapping(value="/test", params="button2")
    public String test2(@ModelAttribute("validateTest") validateTest validatetest, BindingResult bindingResult) {
        BindingResult bd;
        System.out.println("success??");
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<validateTest>> violations = validator.validate(validatetest, b.class);
        for (ConstraintViolation<validateTest> violation : violations) {
            bindingResult.rejectValue(violation.getPropertyPath().toString(), "", violation.getMessage());
        }
        if (bindingResult.hasErrors()) {
            showErrorDetails(violations);
            return "index";
        } else {
            System.out.println("bindingResult2 is success");
            return "index";
        }
    }

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

//        System.out.println("bind");
//        System.out.println(form.getTest());
//        validator.validate(form, bindingResult);
//        if(bindingResult.hasErrors()){
//            System.out.println("error");
//            return "index";
//        }
//        System.out.println("success");
//    }


//    public String vtest(@Validated validateTest test){
//        validator.validate(test, vtest);
//        if (result.hasErrors()) {
//            model.addAttribute("validationSampleModel", vm);
//            return "validationSample";
//        }
//        return "index";
//    }

    //学生マイページ
    @GetMapping("/student")
    public String student(){
        return "/student";
    }





    //公欠届提出
//    @PostMapping("/student/OACreationForm")
//    public String postOAForm(@Validated() @ModelAttribute("oAMainForm") OAMainForm form, BindingResult bindingResult){
//        if(bindingResult.hasErrors()){
//            System.out.println("error");
//            return "/OACreationForm";
//        }
//        Date date = new Date();
//        System.out.println("success" + date.getTime());
//
////        return switch (form.getReasonForAbsence()) {
////            case "jobSearchForm" -> postJobSearchOA(form.getJobForm());
////            case "seminarForm" -> postSeminarOA(form.getSeminarForm());
////            case "bereavementForm" -> postBereavementOA(form.getBereavementForm());
////            case "attendanceBanForm" -> postAttendanceBanOA(form.getBanForm());
////            case "otherForm" -> postOtherOA(form.getOtherForm());
////            default -> null;
////        };
//        return "/OACreationForm";
//    }

//    @GetMapping("/student/OACreationForm")
//    private String showOACreationForm(@ModelAttribute("oAMainForm") OAMainForm form) {
//        if (form.getJobForm() == null) {
//            form.setJobForm(new JobSearchOAForm());
//        }
//        return "OACreationForm";
//    }
//
//    @PostMapping("/student/OACreationForm/jobSearch")
//    public String postJobSearchOA(@Validated JobSearchOAForm form){
//        System.out.println(form.getName());
//        return "redirect:/student/OACreationForm";
//    }
//
//    @PostMapping("/student/OACreationForm/seminar")
//    public String postSeminarOA(@Validated SeminarOAForm form){
//        System.out.println(form.getName());
//        return "redirect:/student/OACreationForm";
//    }
//
//    @PostMapping("/student/OACreationForm/bereavement")
//    public String postBereavementOA(@Validated BereavementOAForm form){
//        System.out.println(form.getName());
//        return "redirect:/student/OACreationForm";
//    }
//
//    @PostMapping("/student/OACreationForm")
//    public String postAttendanceBanOA(@Validated AttendanceBanOAForm form){
//        System.out.println(form.getName());
//        return "redirect:/student/OACreationForm";
//    }
//
//    @PostMapping("/student/OACreationForm")
//    public String postOtherOA(@Validated OtherOAForm form){
//        System.out.println(form.getName());
//        return "redirect:/student/OACreationForm";
//    }
//
//
//
//    //提出済み公欠届BOX
//    @GetMapping("/student/OABox")
//    public String getAllOA(){
//        return "OABox";
//    }

}
