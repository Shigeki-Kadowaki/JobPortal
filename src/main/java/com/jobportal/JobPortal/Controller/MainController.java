package com.jobportal.JobPortal.Controller;

import com.jobportal.JobPortal.Service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class MainController {

    @Autowired
    private MainService service;

    //testMethod
    @PostMapping("/test")
    public String test(@ModelAttribute("form") @Validated Form form, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            System.out.println("error");
            return showFormAgain(form,model);
        }
        Date date = new Date();
//        dates.getParentValue().forEach(System.out::println);
        System.out.println("parentValue:");
        for(String parentValue : form.getParentValue()){
            System.out.println(parentValue);
        }
        form.getParentValue().forEach(System.out::println);
        System.out.println("ChildValue:");
        if(Objects.nonNull(form.getChild())){
            form.getChild().forEach((key, values) -> {
                System.out.println(key + ": " + values);
            });
        }
        else{
            System.out.println("checkbox is null");
        }
        System.out.println("success" + date.getTime());
        return "index";
    }

    @GetMapping("/")
    public String showFormAgain(@ModelAttribute("form") Form form, Model model){
        return "index";
    }
//    @PostMapping("/validatedTest")
//    public String vT(@Validated Form form,BindingResult bindingResult,Model model){
//        if(bindingResult.hasErrors()){
//            return showIndex(form, model);
//        }
//        return "index";
//    }
//    //ホームアクセス
//
//    public String showIndex(@ModelAttribute("form") Form form, Model model) {
//        return "index";
//    }

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
