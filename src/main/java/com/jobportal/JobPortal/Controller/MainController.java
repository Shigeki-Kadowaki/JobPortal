package com.jobportal.JobPortal.Controller;

import com.jobportal.JobPortal.Service.MainService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class MainController {

    @Autowired
    private MainService service;

    //testMethod
    @PostMapping("/test")
    public String test(@Validated @ModelAttribute("form") Form form, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            System.out.println("error");
            return "index";
        }
//        Date date = new Date();


//        System.out.println("success" + date.getTime());
        return "index";
    }

    @PostMapping("/test2")
    public String test2(@ModelAttribute Test form){
//        if(bindingResult.hasErrors()){
//            System.out.println("error");
//            return showFormAgain();
//        }
        System.out.println(form.text());
        return "/index";
    }
    @GetMapping("/")
    public String showFormAgain(@ModelAttribute("form") Form form){
        return "index";
    }
    @GetMapping("/index")
    public String returnIndex(@ModelAttribute("form") Form form){
        return "index";
    }
    //学生マイページ
    @GetMapping("/student")
    public String student(){
        return "/student";
    }



    //公欠届提出
    @PostMapping("/student/OACreationForm")
    public String postOAForm(@Valid @ModelAttribute("oAMainForm") OAMainForm form, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            bindingResult.getAllErrors().forEach(error -> {
                System.out.println(error.getDefaultMessage());
            });
            System.out.println("error");
            return showOACreationForm(form);
        }
        Date date = new Date();
        if(Objects.nonNull(form.getOADates())){

            form.getOADates().forEach(System.out::println);
        }
        else {
            System.out.println("dates is null");
        }
        if(Objects.nonNull(form.getOAPeriods())){
            form.getOAPeriods().forEach((key, values) -> {
                System.out.println(key + ": " + values);
            });
        }
        else{
            System.out.println("checkbox is null");
        }
        System.out.println("success" + date.getTime());
//        return switch (mainForm.reason()) {
//            case "jobSearch" -> postJobSearchOA(mainForm);
//            case "seminar" -> postSeminarOA(mainForm);
//            case "bereavement" -> postBereavementOA(mainForm);
//            case "other" -> postOtherOA(mainForm);
//            default -> null;
//        };
        System.out.println(form.getReasonForAbsence());
        System.out.println("a");
        System.out.println(form.getJobForm().getCompanyName());
        System.out.println("a");
        return "redirect:/student/OACreationForm";
    }

    @GetMapping("/student/OACreationForm")
    private String showOACreationForm(@ModelAttribute("oAMainForm") OAMainForm form) {
        if (form.getJobForm() == null) {
            form.setJobForm(new JobSearchOAForm());
        }
        return "OACreationForm";
    }
//    public String postJobSearchOA(OAMainForm mainForm){
//        var jobForm = mainForm.getJobForm();
//        System.out.println(jobForm.detail() + "です");
////        service.insertJobSearchForm;
//        return "redirect:/student/OABox";
//    }
//
//    public String postSeminarOA(OAMainForm mainForm){
//        var jobForm = mainForm.getJobForm();
//        System.out.println(jobForm.detail() + "です");
////        service.insertJobSearchForm;
//        return "redirect:/student/OABox";
//    }
//
//    public String postBereavementOA(OAMainForm mainForm){
//        var jobForm = mainForm.getJobForm();
//        System.out.println(jobForm.detail() + "です");
////        service.insertJobSearchForm;
//        return "redirect:/student/OABox";
//    }
//
//    public String postOtherOA(OAMainForm mainForm){
//        var jobForm = mainForm.getJobForm();
//        System.out.println(jobForm.detail() + "です");
////        service.insertJobSearchForm;
//        return "redirect:/student/OABox";
//    }



    //提出済み公欠届BOX
    @GetMapping("/student/OABox")
    public String getAllOA(){
        return "OABox";
    }

}
