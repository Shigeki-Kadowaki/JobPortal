package com.jobportal.JobPortal.Controller;

import com.jobportal.JobPortal.Controller.ValidationGroup.*;
import com.jobportal.JobPortal.Service.MainService;
import com.jobportal.JobPortal.Service.OADatesEntity;
import com.jobportal.JobPortal.Service.OAMainEntity;
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

import java.util.Date;
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



    @GetMapping("/student/{id}/OACreationForm")
    public String getForm(@PathVariable("id") Integer studentId, @ModelAttribute("oAMainForm") OAMainForm form, Model model){
        model.addAttribute("studentId",studentId);
        return "OACreationForm";
    }



    //就活公欠届提出
    @PostMapping(value = "/student/{id}/OACreationForm", params = "jobSearchForm")
    public String postJobForm(@PathVariable("id") Integer studentId, @Validated(jobSearchFormGroup.class) @ModelAttribute("oAMainForm") OAMainForm form, BindingResult bindingResult, Model model){
        model.addAttribute("studentId", studentId);
        if(bindingResult.hasErrors()){
            System.out.println("error");
            return "OACreationForm";
        }
        Date date = new Date();
        System.out.println("success" + date.getTime());
        OAMainEntity entity = OAMainForm.toMainEntity(form);
        Integer officialAbsenceId = entity.getOfficialAbsenceId();
        service.createOA(entity,studentId);
        //System.out.println(entity.getOfficialAbsenceId());
//        service.createDate(OAMainForm.toDatesEntity(form), entity.getOfficialAbsenceId());
//        form.getOAPeriods().forEach((key, values) -> {
//            System.out.println(key);
//            values.forEach(System.out::println);
//        });

        List<OADatesEntity> dateList = OAMainForm.toDatesEntity(form);
        service.createOADates(dateList, officialAbsenceId);
//        System.out.println(form.getOAPeriods().keySet());

        return "redirect:/student/{id}/OACreationForm";
    }
    //セミナー公欠届提出
    @PostMapping(value = "/student/{id}/OACreationForm", params = "seminarForm")
    public String postSeminarForm(@Validated(seminarGroup.class) @ModelAttribute("oAMainForm") OAMainForm form, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            System.out.println("error");
            return "OACreationForm";
        }
        Date date = new Date();
        System.out.println("success" + date.getTime());
        return "redirect:/student/{id}/OACreationForm";
    }
    //忌引公欠届提出
    @PostMapping(value = "/student/{id}/OACreationForm", params = "bereavementForm")
    public String postBereavementForm(@PathVariable("id") Integer studentId, @Validated(bereavementGroup.class) @ModelAttribute("oAMainForm") OAMainForm form, BindingResult bindingResult, Model model){
        model.addAttribute("studentId", studentId);
        if(bindingResult.hasErrors()){
            System.out.println("error");
            return "/OACreationForm";
        }
        Date date = new Date();
        System.out.println("success" + date.getTime());
        return "redirect:/student/{id}/OACreationForm";
    }
    //出席停止公欠届提出
    @PostMapping(value = "/student/{id}/OACreationForm", params = "attendanceBanForm")
    public String postBanForm(@Validated(attendanceBanGroup.class) @ModelAttribute("oAMainForm") OAMainForm form, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            System.out.println("error");
            return "OACreationForm";
        }
        Date date = new Date();
        System.out.println("success" + date.getTime());
        return "redirect:/student/{id}/OACreationForm";
    }
    //その他公欠届提出
    @PostMapping(value = "/student/{id}/OACreationForm", params = "otherForm")
    public String postOtherForm(@Validated(otherGroup.class) @ModelAttribute("oAMainForm") OAMainForm form, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            System.out.println("error");
            return "OACreationForm";
        }
        Date date = new Date();
        System.out.println("success" + date.getTime());
        return "redirect:/student/{id}/OACreationForm";
    }




    //提出済み公欠届BOX
    @GetMapping("/student/{id}/OABox")
    public String showAllOAs(@PathVariable("id") Integer studentId, Model model){
        var OAList = service.findAllOAs(studentId);
        model.addAttribute("OAList", OAList);
        model.addAttribute("studentId", studentId);
        return "OABox";
    }




}
