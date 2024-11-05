package com.jobportal.JobPortal.Controller;

import com.jobportal.JobPortal.Controller.Form.OAMainForm;
import com.jobportal.JobPortal.Controller.ValidationGroup.*;
import com.jobportal.JobPortal.Service.DTO.OALessonsDTO;
import com.jobportal.JobPortal.Service.DTO.OAListDTO;
import com.jobportal.JobPortal.Service.DTO.OAMainInfoDTO;
import com.jobportal.JobPortal.Service.Entity.*;
import com.jobportal.JobPortal.Service.MainService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class StudentController {

    @Autowired
    private final MainService service;

    @GetMapping("/jobportal")
    public String showFormAgain(@ModelAttribute("student") student student) {
        student.setId(40104);
        student.setName("木谷");
        System.out.println(student.getId().toString());
        return "redirect:/jobportal/student/" + student.getId();
    }
    @GetMapping(value= "/test", produces = "text/html; charset=UTF-8")
    public void test(@ModelAttribute("student") student student, HttpServletResponse response, HttpServletRequest request) throws IOException {
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        StringBuilder sb = new StringBuilder();

        sb.append("<html>");
        sb.append("<head>");
        sb.append("<title>テスト</title>");
        sb.append("</head>");
        sb.append("<body>");

        sb.append("<p>");

        Enumeration<String> headernames = request.getHeaderNames();
        while (headernames.hasMoreElements()){
            String name = headernames.nextElement();
            Enumeration<String> headervals = request.getHeaders(name);
            while (headervals.hasMoreElements()){
                String val = headervals.nextElement();
                sb.append(name);
                sb.append(":");
                sb.append(val);
                sb.append("<br>");
            }
        }

        sb.append("</p>");

        sb.append("</body>");
        sb.append("</html>");

        out.println(new String(sb));

        out.close();
    }


    @GetMapping("/jobportal/student/{studentId}")
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
    @GetMapping("/jobportal/student/{studentId}/OACreationForm")
    public String getForm(@PathVariable("studentId") Integer studentId, @ModelAttribute("oAMainForm") OAMainForm form, Model model){
        model.addAttribute("studentId",studentId);
        return "OACreationForm";
    }

    //就活公欠届提出
    @PostMapping(value = "/jobportal/student/{studentId}/OACreationForm", params = "jobSearchForm")
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
        return "redirect:/jobportal/student/{studentId}/OAList";
    }
    //セミナー公欠届提出
    @PostMapping(value = "/jobportal/student/{studentId}/OACreationForm", params = "seminarForm")
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
        return "redirect:/jobportal/student/{studentId}/OAList";
    }
    //忌引公欠届提出
    @PostMapping(value = "/jobportal/student/{studentId}/OACreationForm", params = "bereavementForm")
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
        return "redirect:/jobportal/student/{studentId}/OAList";
    }
    //出席停止公欠届提出
    @PostMapping(value = "/jobportal/student/{studentId}/OACreationForm", params = "attendanceBanForm")
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
        return "redirect:/jobportal/student/{studentId}/OAList";
    }
    //その他公欠届提出
    @PostMapping(value = "/jobportal/student/{studentId}/OACreationForm", params = "otherForm")
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
        return "redirect:/jobportal/student/{studentId}/OAList";
    }




    //提出済み公欠届List
    @GetMapping("/jobportal/student/{studentId}/OAList")
    public String showStudentOAList(@PathVariable("studentId") Integer studentId, Model model){
        Map<String, String> colors = new HashMap<>();
        colors.put("受理","list-group-item-success");
        colors.put("未受理","list-group-item-warning");
        colors.put("却下","list-group-item-danger");
        colors.put("未提出","list-group-item-dark");
        //OAList取得
        List<OAListEntity> listEntity = service.findAllOAs(studentId);
        //公欠日時をMapにする
        if(!listEntity.isEmpty()) {
//            List<OAListDTO> listDTO = listEntity.stream().map(OAListEntity::toListDTO).collect(Collectors.toList());
            List<OAListDTO> listDTO = service.toListEntity(listEntity);
            model.addAttribute("mainList", listDTO);
//            listDTO.forEach(e->{
//                System.out.println(e.officialAbsenceId());
//                e.lessons().forEach(System.out::println);
//            });
        }
        model.addAttribute("colors", colors);
        return "OAList";
    }

    //公欠届詳細
    @GetMapping("/jobportal/student/{studentId}/OAInfo/{OAId}")
    public String showStudentOAInfo(@ModelAttribute @PathVariable("studentId") Integer studentId,@ModelAttribute  @PathVariable("OAId") Integer OAId, Model model){
        //OAInfo取得
//        List<OADateInfoDTO> allInfoDTO = service.findOAAllInfo(OAId);
        OAMainInfoEntity mainInfoEntity = service.findMainInfo(OAId);
        List<OADateInfoEntity> dateInfoEntities = service.findDateInfo(OAId);
        //公欠日時をMapにする
        if(!dateInfoEntities.isEmpty()){
            Map<String, List<OALessonsDTO>> lessonInfoEntities = service.toLessonInfoDTO(dateInfoEntities);
            OAMainInfoDTO mainInfoDTO = mainInfoEntity.toInfoDTO();
//        //共通部分抽出
//        OAMainInfoDTO mainInfoDTO = allInfoDTO.getFirst().toOAMainInfoDTO();
            switch (mainInfoDTO.reason()){
                case "就活" -> {
                    JobSearchEntity  jobSearch = service.findJobSearchInfo(OAId);
                    model.addAttribute("jobSearchInfo", jobSearch);
                }
                case "セミナー・合説" -> {
                    SeminarEntity seminar = service.findSeminarInfo(OAId);
                    model.addAttribute("seminarInfo", seminar);
                }
                case "忌引" -> {
                    BereavementEntity bereavement = service.findBereavementInfo(OAId);
                    model.addAttribute("bereavementInfo", bereavement);
                }
                case "出席停止" -> {
                    AttendanceBanEntity attendanceBan = service.findAttendanceBanInfo(OAId);
                    model.addAttribute("attendanceBanInfo", attendanceBan);
                }
                case "その他" -> {
                    OtherEntity other = service.findOtherInfo(OAId);
                    model.addAttribute("otherInfo", other);
                }
            }
            model.addAttribute("lessonInfo", lessonInfoEntities);
            model.addAttribute("mainInfo", mainInfoDTO);
        }
        return "OAInfo";
    }

    @GetMapping("/jobportal/student/{studentId}/reportinfo/{OAId}")
    public String showStudentReportInfo(@PathVariable("studentId") Integer studentId, @PathVariable("OAId") Integer OAId, Model model){
        return "student_reportInfo";
    }



}
