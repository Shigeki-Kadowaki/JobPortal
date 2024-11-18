package com.jobportal.JobPortal.Controller;

import com.jobportal.JobPortal.Controller.Form.OAMainForm;
import com.jobportal.JobPortal.Controller.Form.StudentOASearchForm;
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
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/jobportal")
public class StudentController {

    @Autowired
    private final MainService service;

    @GetMapping(value = "/")
    public String showFormAgain(@ModelAttribute("student") Student student) throws IOException {
            student.setId(40104);
            student.setSurname("木谷");
            return "redirect:/jobportal/student/" + student.getId();
    }
    @GetMapping(value= "/test", produces = "text/html; charset=UTF-8")
    public Map<String, String> test(HttpServletResponse response, HttpServletRequest request) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        Enumeration<String> headerNames = request.getHeaderNames();
        PrintWriter out = response.getWriter();
        Map<String, String > map = new HashMap<>();
        StringBuffer sb = new StringBuffer();

        sb.append("<html>");
        sb.append("<head>");
        sb.append("<title>テスト</title>");
        sb.append("</head>");
        sb.append("<body>");
        sb.append("<p>");
        List<String> values = new ArrayList<>();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String value = new String(request.getHeader(headerName).getBytes(StandardCharsets.ISO_8859_1),
                    StandardCharsets.UTF_8);
            sb.append(headerName);
            sb.append(":");
            sb.append(value);
            sb.append("<br>");
            values.add(value);
            map.put(headerName, value);
        }
        String group;
        if(values.contains("bdab862e-69fc-4932-ab21-96a46e05881f")){
            group = "教職員";
        }else {
            group = "学生";
        }
        sb.append("group");
        sb.append(":");
        sb.append(group);
        sb.append("<br>");
        sb.append("</p>");

        sb.append("</body>");
        sb.append("</html>");

        out.println(new String(sb));

        out.close();
        return map;
    }


    @GetMapping(value="/student/{studentId}")
    public String student(@ModelAttribute("student") Student student, @PathVariable("studentId") Integer studentId) {
        student.setId(studentId);
        student.setSurname("木谷");
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
    public String showForm(@PathVariable("studentId") Integer studentId, @ModelAttribute("oAMainForm") OAMainForm form, Model model){
        model.addAttribute("studentId",studentId);
        model.addAttribute("mode", "create");
        return "OAForm";
    }

    //就活公欠届提出
    @PostMapping(value = "/student/{studentId}", params = "jobSearch")
    public String postJobForm(@ModelAttribute("studentId") @PathVariable("studentId") Integer studentId, @Validated(jobSearchFormGroup.class) @ModelAttribute("oAMainForm") OAMainForm form, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            System.out.println("error");
            return showForm(studentId, form, model);
        }
        OAMainEntity mainEntity = form.toMainEntity(studentId);
        service.createOA(mainEntity);
        Integer officialAbsenceId = mainEntity.getOfficialAbsenceId();
        List<OADatesEntity> dateList = form.toDatesEntity();
        service.createSubmitted(officialAbsenceId);
        service.createOADates(dateList, officialAbsenceId);
        JobSearchEntity jobEntity = form.toJobSearchEntity(officialAbsenceId);
        service.createJobSearch(jobEntity);
        return "redirect:/jobportal/student/{studentId}/OAList";
    }
    //セミナー公欠届提出
    @PostMapping(value = "/student/{studentId}", params = "seminar")
    public String postSeminarForm(@ModelAttribute("studentId") @PathVariable("studentId") Integer studentId, @Validated(seminarGroup.class) @ModelAttribute("oAMainForm") OAMainForm form, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            System.out.println("error");
            return showForm(studentId, form, model);
        }
        OAMainEntity mainEntity = form.toMainEntity(studentId);
        service.createOA(mainEntity);
        Integer officialAbsenceId = mainEntity.getOfficialAbsenceId();
        List<OADatesEntity> dateList = form.toDatesEntity();
        service.createSubmitted(officialAbsenceId);
        service.createOADates(dateList, officialAbsenceId);
        SeminarEntity seminarEntity = form.toSeminarEntity(officialAbsenceId);
        service.createSeminar(seminarEntity);
        return "redirect:/jobportal/student/{studentId}/OAList";
    }
    //忌引公欠届提出
    @PostMapping(value = "/student/{studentId}", params = "bereavement")
    public String postBereavementForm(@ModelAttribute("studentId") @PathVariable("studentId") Integer studentId, @Validated(bereavementGroup.class) @ModelAttribute("oAMainForm") OAMainForm form, BindingResult bindingResult, Model model){
        model.addAttribute("studentId", studentId);
        if(bindingResult.hasErrors()){
            System.out.println("error");
            return showForm(studentId, form, model);
        }
        OAMainEntity mainEntity = form.toMainEntity(studentId);
        service.createOA(mainEntity);
        Integer officialAbsenceId = mainEntity.getOfficialAbsenceId();
        List<OADatesEntity> dateList = form.toDatesEntity();
        service.createSubmitted(officialAbsenceId);
        service.createOADates(dateList, officialAbsenceId);
        BereavementEntity bereavementEntity = form.toBereavementEntity(officialAbsenceId);
        service.createBereavement(bereavementEntity);
        return "redirect:/jobportal/student/{studentId}/OAList";
    }
    //出席停止公欠届提出
    @PostMapping(value = "/student/{studentId}", params = "attendanceBan")
    public String postBanForm(@ModelAttribute("studentId") @PathVariable("studentId") Integer studentId, @Validated(attendanceBanGroup.class) @ModelAttribute("oAMainForm") OAMainForm form, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            System.out.println("error");
            return showForm(studentId, form, model);
        }
        OAMainEntity mainEntity = form.toMainEntity(studentId);
        service.createOA(mainEntity);
        Integer officialAbsenceId = mainEntity.getOfficialAbsenceId();
        List<OADatesEntity> dateList = form.toDatesEntity();
        service.createSubmitted(officialAbsenceId);
        service.createOADates(dateList, officialAbsenceId);
        AttendanceBanEntity attendanceBanEntity = form.toAttendanceBanEntity(officialAbsenceId);
        service.createAttendanceBan(attendanceBanEntity);
        return "redirect:/jobportal/student/{studentId}/OAList";
    }
    //その他公欠届提出
    @PostMapping(value = "/student/{studentId}", params = "other")
    public String postOtherForm(@ModelAttribute("studentId") @PathVariable("studentId") Integer studentId, @Validated(otherGroup.class) @ModelAttribute("oAMainForm") OAMainForm form, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            System.out.println("error");
            return showForm(studentId, form, model);
        }
        OAMainEntity mainEntity = form.toMainEntity(studentId);
        service.createOA(mainEntity);
        Integer officialAbsenceId = mainEntity.getOfficialAbsenceId();
        List<OADatesEntity> dateList = form.toDatesEntity();
        service.createSubmitted(officialAbsenceId);
        service.createOADates(dateList, officialAbsenceId);
        OtherEntity otherEntity = form.toOtherEntity(officialAbsenceId);
        service.createOther(otherEntity);
        return "redirect:/jobportal/student/{studentId}/OAList";
    }

    //提出済み公欠届List
    @GetMapping("/student/{studentId}/OAList")
    public String showStudentOAList(@PathVariable("studentId") Integer studentId, StudentOASearchForm form, Model model){
        Map<String, String> colors = new HashMap<>();
        colors.put("受理","list-group-item-success");
        colors.put("未受理","list-group-item-warning");
        colors.put("却下","list-group-item-danger");
        colors.put("未提出","list-group-item-dark");
        //OAList取得
        List<OAListEntity> listEntity = service.findAllOAs(studentId, form);
        //公欠日時をMapにする
        if(!listEntity.isEmpty()) {
            List<OAListDTO> listDTO = service.toListEntity(listEntity);
            model.addAttribute("mainList", listDTO);
//            listDTO.forEach(e->{
//                System.out.println(e.officialAbsenceId());
//                e.lessons().forEach(System.out::println);
//            });
        }
        model.addAttribute("searchForm", form);
        model.addAttribute("colors", colors);
        return "OAList";
    }

    //公欠届詳細
    @GetMapping("/student/{studentId}/OAList/{OAId}")
    public String showStudentOAInfo(@ModelAttribute @PathVariable("studentId") Integer studentId,@ModelAttribute  @PathVariable("OAId") Integer OAId, Model model){
        //OAInfo取得
        OAMainInfoEntity mainInfoEntity = service.findMainInfo(OAId);
        List<OADateInfoEntity> dateInfoEntities = service.findDateInfo(OAId);
        //公欠日時をMapにする
        if(!dateInfoEntities.isEmpty()){
            Map<String, List<OALessonsDTO>> lessonInfoEntities = service.toLessonInfoDTO(dateInfoEntities);
            OAMainInfoDTO mainInfoDTO = mainInfoEntity.toInfoDTO();
//        //共通部分抽出
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
            model.addAttribute("mode", "info");
        }
        return "OAInfo";
    }
    //公欠届別バージョン詳細
    @GetMapping("/student/{studentId}/{OAId}")
    public String showStudentOAInfoByVersion(@ModelAttribute @PathVariable("studentId") Integer studentId,@ModelAttribute  @PathVariable("OAId") Integer OAId,@RequestParam("version") Integer version, Model model){
        //OAInfo取得
        OAMainInfoEntity mainInfoEntity = service.findMainInfoByVersion(OAId, version);
        List<OADateInfoEntity> dateInfoEntities = service.findDateInfoByVersion(OAId, version);
        //公欠日時をMapにする
        if(!dateInfoEntities.isEmpty()){
            Map<String, List<OALessonsDTO>> lessonInfoEntities = service.toLessonInfoDTO(dateInfoEntities);
            OAMainInfoDTO mainInfoDTO = mainInfoEntity.toInfoDTO();
//        //共通部分抽出
            switch (mainInfoDTO.reason()){
                case "就活" -> {
                    JobSearchEntity  jobSearch = service.findJobSearchInfoByVersion(OAId, version);
                    model.addAttribute("jobSearchInfo", jobSearch);
                }
                case "セミナー・合説" -> {
                    SeminarEntity seminar = service.findSeminarInfoByVersion(OAId, version);
                    model.addAttribute("seminarInfo", seminar);
                }
                case "忌引" -> {
                    BereavementEntity bereavement = service.findBereavementInfoByVersion(OAId, version);
                    model.addAttribute("bereavementInfo", bereavement);
                }
                case "出席停止" -> {
                    AttendanceBanEntity attendanceBan = service.findAttendanceBanInfoByVersion(OAId, version);
                    model.addAttribute("attendanceBanInfo", attendanceBan);
                }
                case "その他" -> {
                    OtherEntity other = service.findOtherInfoByVersion(OAId, version);
                    model.addAttribute("otherInfo", other);
                }
            }
            model.addAttribute("lessonInfo", lessonInfoEntities);
            model.addAttribute("mainInfo", mainInfoDTO);
            model.addAttribute("mode", "reading");
        }
        return "OAInfo";
    }

    @GetMapping("/student/{studentId}/reportInfo/{OAId}")
    public String showStudentReportInfo(@PathVariable("studentId") Integer studentId, @PathVariable("OAId") Integer OAId, Model model){
        return "student_reportInfo";
    }
    //編集画面
    @GetMapping("/student/{studentId}/OAList/{OAId}/OAEditForm")
    public String showEditForm(@PathVariable("studentId") Integer studentId, @PathVariable("OAId") Integer OAId, Model model){
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
                    JobSearchEntity jobSearch = service.findJobSearchInfo(OAId);
                    OAMainForm form = service.toJobSearchForm(mainInfoDTO,lessonInfoEntities,jobSearch);
                    model.addAttribute("oAMainForm", form);
                }
                case "セミナー・合説" -> {
                    SeminarEntity seminar = service.findSeminarInfo(OAId);
                    OAMainForm form = service.toSeminarForm(mainInfoDTO, lessonInfoEntities, seminar);
                    model.addAttribute("oAMainForm", form);
                }
                case "忌引" -> {
                    BereavementEntity bereavement = service.findBereavementInfo(OAId);
                    OAMainForm form = service.toBereavementForm(mainInfoDTO, lessonInfoEntities, bereavement);
                    model.addAttribute("oAMainForm", form);
                }
                case "出席停止" -> {
                    AttendanceBanEntity attendanceBan = service.findAttendanceBanInfo(OAId);
                    OAMainForm form = service.toAttendanceBanForm(mainInfoDTO, lessonInfoEntities, attendanceBan);
                    model.addAttribute("oAMainForm", form);
                }
                case "その他" -> {
                    OtherEntity other = service.findOtherInfo(OAId);
                    OAMainForm form = service.toOtherForm(mainInfoDTO, lessonInfoEntities, other);
                    model.addAttribute("oAMainForm", form);
                }
            }
            model.addAttribute("OAId", mainInfoDTO.officialAbsenceId());
        }
        model.addAttribute("mode", "edit");
        return "OAForm";
    }
    //就活再提出
    @PostMapping(value = "/student/{studentId}/OAList/{OAId}", params = "jobSearch")
    public String postJobForm(@ModelAttribute("studentId") @PathVariable("studentId") Integer studentId,@PathVariable("OAId") Integer OAId, @Validated(jobSearchFormGroup.class) @ModelAttribute("oAMainForm") OAMainForm form, BindingResult bindingResult){
        System.out.println("test");
        if(bindingResult.hasErrors()){
            System.out.println("error");
            return "OAForm";
        }
        service.updateSubmittedDate(OAId);
        List<OADatesEntity> dateList = form.toDatesEntity();
        service.updateOADates(dateList, OAId);
        JobSearchEntity jobSearch = form.toJobSearchEntity(OAId);
        service.updateJobSearch(jobSearch);
        service.updateOAStatus(OAId, "unaccepted");
        service.updateCheck(OAId, "teacher", false);
        service.updateCheck(OAId, "career", false);
        return "redirect:/jobportal/student/{studentId}/OAList";
    }
    //セミナー再提出
    @PostMapping(value = "/student/{studentId}/OAList/{OAId}", params = "seminar")
    public String postSeminarForm(@ModelAttribute("studentId") @PathVariable("studentId") Integer studentId,@PathVariable("OAId") Integer OAId,  @Validated(seminarGroup.class) @ModelAttribute("oAMainForm") OAMainForm form, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            System.out.println("error");
            return "OAForm";
        }
        service.updateSubmittedDate(OAId);
        List<OADatesEntity> dateList = form.toDatesEntity();
        service.updateOADates(dateList, OAId);
        SeminarEntity seminar = form.toSeminarEntity(OAId);
        service.updateSeminar(seminar);
        service.updateOAStatus(OAId, "unaccepted");
        service.updateCheck(OAId, "teacher", false);
        service.updateCheck(OAId, "career", false);
        return "redirect:/jobportal/student/{studentId}/OAList";
    }
    //忌引再提出
    @PostMapping(value = "/student/{studentId}/OAList/{OAId}", params = "bereavement")
    public String postBereavementForm(@ModelAttribute("studentId") @PathVariable("studentId") Integer studentId,@PathVariable("OAId") Integer OAId,  @Validated(bereavementGroup.class) @ModelAttribute("oAMainForm") OAMainForm form, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            System.out.println("error");
            return "OAForm";
        }
        service.updateSubmittedDate(OAId);
        List<OADatesEntity> dateList = form.toDatesEntity();
        service.updateOADates(dateList, OAId);
        BereavementEntity bereavement = form.toBereavementEntity(OAId);
        service.updateBereavement(bereavement);
        service.updateOAStatus(OAId, "unaccepted");
        service.updateCheck(OAId, "teacher", false);
        return "redirect:/jobportal/student/{studentId}/OAList";
    }
    //出席停止再提出
    @PostMapping(value = "/student/{studentId}/OAList/{OAId}", params = "attendanceBan")
    public String postBanForm(@ModelAttribute("studentId") @PathVariable("studentId") Integer studentId,@PathVariable("OAId") Integer OAId,  @Validated(attendanceBanGroup.class) @ModelAttribute("oAMainForm") OAMainForm form, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            System.out.println("error");
            return "OAForm";
        }
        service.updateSubmittedDate(OAId);
        List<OADatesEntity> dateList = form.toDatesEntity();
        service.updateOADates(dateList, OAId);
        AttendanceBanEntity attendanceBan = form.toAttendanceBanEntity(OAId);
        service.updateAttendanceBan(attendanceBan);
        service.updateOAStatus(OAId, "unaccepted");
        service.updateCheck(OAId, "teacher", false);
        return "redirect:/jobportal/student/{studentId}/OAList";
    }
    //その他再提出
    @PostMapping(value = "/student/{studentId}/OAList/{OAId}", params = "other")
    public String postOtherForm(@ModelAttribute("studentId") @PathVariable("studentId") Integer studentId,@PathVariable("OAId") Integer OAId,  @Validated(otherGroup.class) @ModelAttribute("oAMainForm") OAMainForm form, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            System.out.println("error");
            return "OAForm";
        }
        service.updateSubmittedDate(OAId);
        List<OADatesEntity> dateList = form.toDatesEntity();
        service.updateOADates(dateList, OAId);
        OtherEntity other = form.toOtherEntity(OAId);
        service.updateOther(other);
        service.updateOAStatus(OAId, "unaccepted");
        service.updateCheck(OAId, "teacher", false);
        return "redirect:/jobportal/student/{studentId}/OAList";
    }

    //破棄
    @DeleteMapping("/student/OAList/{OAId}/cansel")
    public String deleteOA(@PathVariable("OAId")Integer OAId){
        OAMainInfoEntity mainInfoEntity = service.findMainInfo(OAId);
        switch (mainInfoEntity.reason()){
            case jobSearch -> {
                service.deleteJobSearch(OAId);
            }
            case seminar -> {
                service.deleteSeminar(OAId);
            }
            case bereavement -> {
                service.deleteBereavement(OAId);
            }
            case attendanceBan -> {
                service.deleteAttendanceBan(OAId);
            }
            case other -> {
                service.deleteOther(OAId);
            }
        }
        service.deleteDate(OAId);
        service.deleteSubmittedDate(OAId);
        service.deleteMain(OAId);
        String studentId = mainInfoEntity.studentId().toString();
        return "redirect:/jobportal/student/" + studentId + "/OAList";
    }
}
