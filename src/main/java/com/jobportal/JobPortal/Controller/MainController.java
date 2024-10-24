package com.jobportal.JobPortal.Controller;

import com.jobportal.JobPortal.Service.MainService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;

@Controller
@RequiredArgsConstructor
public class MainController {

    @Autowired
    private MainService service;


    @GetMapping("/")
    public String showFormAgain(Model model){
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
            System.out.println("error");
            return "/OACreationForm";
        }
        Date date = new Date();
        System.out.println("success" + date.getTime());


        return "redirect/student/OACreationForm";
    }

    @PostMapping("/student/OACreationForm")
    public String postOAForm(@Valid @ModelAttribute("oAMainForm") a form, BindingResult bindingResult){
        return "redirect/student/OACreationForm";
    }

    @GetMapping("/student/OACreationForm")
    private String showOACreationForm(@ModelAttribute("oAMainForm") OAMainForm form) {
        return "OACreationForm";
    }

//    public String postJobSearchOA(@Validated JobSearchOAForm form){
//        System.out.println(form.getName());
//        return "redirect:/student/OACreationForm";
//    }
//
//    public String postSeminarOA(@Validated SeminarOAForm form){
//        System.out.println(form.getName());
//        return "redirect:/student/OACreationForm";
//    }
//
//    public String postBereavementOA(@Validated BereavementOAForm form){
//        System.out.println(form.getName());
//        return "redirect:/student/OACreationForm";
//    }
//
//    public String postAttendanceBanOA(@Validated AttendanceBanOAForm form){
//        System.out.println(form.getName());
//        return "redirect:/student/OACreationForm";
//    }
//
//    public String postOtherOA(@Validated OtherOAForm form){
//        System.out.println(form.getName());
//        return "redirect:/student/OACreationForm";
//    }



    //提出済み公欠届BOX
    @GetMapping("/student/OABox")
    public String getAllOA(){
        return "OABox";
    }




}
