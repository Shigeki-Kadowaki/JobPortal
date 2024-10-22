package com.jobportal.JobPortal.Controller;

import com.jobportal.JobPortal.Service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    @Autowired
    private MainService service;

    //testMethod
    @PostMapping("/test")
    public String test(@ModelAttribute DatesGetFormExample dates, Model model){
        model.addAttribute("object",dates);
        Date date = new Date();
        if(dates.dates() == null){
         System.out.println("Null");
        }
        System.out.println("success" + date.getTime());

        return "/index";
    }

    //ホームアクセス
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("message", "Welcome to Thymeleaf??");
        return "index";
    }

    //学生マイページ
    @GetMapping("/student")
    public String student(){
        return "student";
    }

    //新規公欠届提出ページ
    @GetMapping("/student/OACreationForm")
    public String OACreationForm(){

        return "OACreationForm";
    }

    //公欠届提出
    @PostMapping("/student/OACreationForm")
    public String postOAForm(@RequestParam("ReasonForAbsence") List<String> reason){
//        return switch (mainForm.reason()) {
//            case "jobSearch" -> postJobSearchOA(mainForm);
//            case "seminar" -> postSeminarOA(mainForm);
//            case "bereavement" -> postBereavementOA(mainForm);
//            case "other" -> postOtherOA(mainForm);
//            default -> null;
//        };
        System.out.println(reason);
        return "/student/OACreationForm";
    }
    public String postJobSearchOA(OAMainForm mainForm){
        var jobForm = mainForm.jobForm();
        System.out.println(jobForm.detail() + "です");
//        service.insertJobSearchForm;
        return "redirect:/student/OABox";
    }

    public String postSeminarOA(OAMainForm mainForm){
        var jobForm = mainForm.jobForm();
        System.out.println(jobForm.detail() + "です");
//        service.insertJobSearchForm;
        return "redirect:/student/OABox";
    }

    public String postBereavementOA(OAMainForm mainForm){
        var jobForm = mainForm.jobForm();
        System.out.println(jobForm.detail() + "です");
//        service.insertJobSearchForm;
        return "redirect:/student/OABox";
    }

    public String postOtherOA(OAMainForm mainForm){
        var jobForm = mainForm.jobForm();
        System.out.println(jobForm.detail() + "です");
//        service.insertJobSearchForm;
        return "redirect:/student/OABox";
    }

    @GetMapping("/index")
    public String returnIndex(Model model){
        model.addAttribute("message", "indexアクセス");
        return "index";
    }

    //提出済み公欠届BOX
    @GetMapping("/student/OABox")
    public String getAllOA(){
        return "OABox";
    }


}
