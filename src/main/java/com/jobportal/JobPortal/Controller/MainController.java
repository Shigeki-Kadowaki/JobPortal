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
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;
import java.util.Set;
import com.jobportal.JobPortal.Controller.ValidationGroup.*;

@Controller
@RequiredArgsConstructor
public class MainController {

    @Autowired
    private MainService service;
    @Autowired
    public SmartValidator validator;

    @GetMapping("/")
    public String showFormAgain(@ModelAttribute validateTest validateTest){
        return "index";
    }
    @GetMapping("/index")
    public String returnIndex(@ModelAttribute validateTest validateTest){
        return "index";
    }

//    @PostMapping(value="/test", params="button1")
//    public String test(@ModelAttribute("validateTest") @Validated({atext.class})validateTest validateTest, BindingResult bindingResult, Model model) {
//        System.out.println("success??");
//        if(bindingResult.hasErrors()){
//            System.out.println("error");
//            return "index";
//        }else {
//            System.out.println(validateTest.getText1());
//            System.out.println("successa");
//            return "redirect:/index";
//        }
//    }
//
//    @PostMapping(value="/test", params="button2")
//    public String test2(@ModelAttribute("validateTest") @Validated({btext.class}) validateTest validatetest, BindingResult bindingResult, Model model) {
//        System.out.println("success??");
//        if(bindingResult.hasErrors()){
//            System.out.println("error");
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


    @GetMapping("/student/OACreationForm")
    public String getForm(@ModelAttribute("oAMainForm") OAMainForm form){
        return "OACreationForm";
    }



    //就活公欠届提出
    @PostMapping(value = "/student/OACreationForm", params = "jobSearchForm")
    public String postJobForm(@Validated(jobSearchFormGroup.class) @ModelAttribute("oAMainForm") OAMainForm form, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            System.out.println("error");
            return "OACreationForm";
        }
        Date date = new Date();
        System.out.println("success" + date.getTime());
        return "redirect:/OACreationForm";
    }
    //就活公欠届提出
    @PostMapping(value = "/student/OACreationForm", params = "seminarForm")
    public String postSeminarForm(@Validated(seminarGroup.class) @ModelAttribute("oAMainForm") OAMainForm form, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            System.out.println("error");
            return "OACreationForm";
        }
        Date date = new Date();
        System.out.println("success" + date.getTime());
        return "redirect:/OACreationForm";
    }
    //忌引公欠届提出
    @PostMapping(value = "/student/OACreationForm", params = "bereavementForm")
    public String postBereavementForm(@Validated(bereavementGroup.class) @ModelAttribute("oAMainForm") OAMainForm form, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            System.out.println("error");
            return "OACreationForm";
        }
        Date date = new Date();
        System.out.println("success" + date.getTime());
        return "redirect:/OACreationForm";
    }
    //出席停止公欠届提出
    @PostMapping(value = "/student/OACreationForm", params = "banForm")
    public String postBanForm(@Validated(banGroup.class) @ModelAttribute("oAMainForm") OAMainForm form, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            System.out.println("error");
            return "OACreationForm";
        }
        Date date = new Date();
        System.out.println("success" + date.getTime());
        return "redirect:/OACreationForm";
    }
    //その他公欠届提出
    @PostMapping(value = "/student/OACreationForm", params = "otherForm")
    public String postOtherForm(@Validated(otherGroup.class) @ModelAttribute("oAMainForm") OAMainForm form, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            System.out.println("error");
            return "OACreationForm";
        }
        Date date = new Date();
        System.out.println("success" + date.getTime());
        return "redirect:/OACreationForm";
    }




    //提出済み公欠届BOX
    @GetMapping("/student/OABox")
    public String getAllOA(){
        return "OABox";
    }




}
