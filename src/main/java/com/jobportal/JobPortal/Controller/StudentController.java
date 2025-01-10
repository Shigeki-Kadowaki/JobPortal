package com.jobportal.JobPortal.Controller;

import com.jobportal.JobPortal.Controller.Form.OAMainForm;
import com.jobportal.JobPortal.Controller.Form.ReportForm;
import com.jobportal.JobPortal.Controller.Form.StudentOASearchForm;
import com.jobportal.JobPortal.Controller.ValidationGroup.*;
import com.jobportal.JobPortal.Service.DTO.OAMainInfoDTO;
import com.jobportal.JobPortal.Service.Entity.*;
import com.jobportal.JobPortal.Service.MainService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/jobportal")
public class StudentController {

    private final MailController mailController;
    @Autowired
    private final MainService service;
    @Autowired
    public final HttpSession session;
    public final Map<String, String> colors = new HashMap<>(){
        {
            put("受理", "list-group-item-success");
            put("未受理", "list-group-item-warning");
            put("却下", "list-group-item-danger");
            put("未提出", "list-group-item-dark");
            put("不要", "list-group-item-light");
        }
    };
    public final Subject[][] subjects = {
        {new Subject(1,"情報システム演習"),new Subject(2,"情報システム演習"),new Subject(3,"資格対策"),new Subject(4,"資格対策"),new Subject(5,"プレゼンテーション")},
        {new Subject(6,"システム開発Ⅰ"),new Subject(7,"システム開発Ⅰ"),new Subject(8,"IT応用")},
        {new Subject(9,"システム開発Ⅱ"),new Subject(10,"システム開発Ⅱ")},
        {new Subject(13,"システム開発Ⅱ実習"),new Subject(14,"システム開発Ⅱ実習")},
        {new Subject(17,"システム開発Ⅰ実習"),new Subject(18,"システム開発Ⅰ実習")}
    };

    @GetMapping(value = "/", produces = "text/html; charset=UTF-8")
    public String showFormAgain(RedirectAttributes r, HttpServletResponse response, HttpServletRequest request, @ModelAttribute("student") Student student, Model model) throws IOException {
            Map<String, String> person = service.getPersonInfo(response, request);
            //localでテスト用
            student.setGno(40104);
            //ssoから取得用
//            student.setGno(Integer.parseInt(person.get("mellon-email").substring(0, 5)));
            if(person.get("group").equals("学生")) {
                return "redirect:/jobportal/student/" + student.getGno();
            }
            else return "redirect:/jobportal/teacher/";
    }

    @GetMapping(value="/student/{studentId}")
    public String student(HttpServletRequest request,Student student, @PathVariable("studentId") Integer studentId, Model model) {
        student.setGno(studentId);
        student = (Student) request.getAttribute("student");
        DesiredOccupation desiredOccupation = service.getOccupation(studentId);
        model.addAttribute("student", student);
        model.addAttribute("desiredOccupation", desiredOccupation);

        //mailController.sendMail(student.getMail(), student.getMail(), "test");
        return "student";
    }

    @GetMapping("/student/{studentId}/desiredOccupation")
    public String getDesiredOccupation(@PathVariable("studentId") @ModelAttribute Integer studentId, Model model) {
        DesiredOccupation desiredOccupation = service.getOccupation(studentId);
        model.addAttribute("desiredOccupation", desiredOccupation);
        return "desiredOccupation";
    }

    @PostMapping("/student/{studentId}/desiredOccupation")
    public String postDesiredOccupation(@PathVariable("studentId") Integer studentId, @RequestParam("business") String business,@RequestParam("occupation") String occupation, Model model){
        if(service.existsDesired(studentId)){
            service.updateDesiredBusiness(studentId, business);
            service.updateDesiredOccupation(studentId, occupation);
        }else {
            service.insertDesired(studentId, business, occupation);
        }
        return "redirect:/jobportal/student/{studentId}";
    }

    //Form画面
    @GetMapping("/student/{studentId}/OACreationForm")
    public String showForm(HttpServletRequest request, @PathVariable("studentId") Integer studentId, @ModelAttribute("oAMainForm") OAMainForm form, Model model){
        model.addAttribute("studentId",studentId);
        model.addAttribute("mode", "create");
        Student student = (Student) request.getAttribute("student");
        //学校で使う用
        //Subject[][] subjects = service.getSubjectArr(service.setClassification(student));
        List<ExceptionDateEntity> exceptionDates = service.getExceptionDates();
        model.addAttribute("subjects", subjects);
        model.addAttribute("exceptionDates", exceptionDates);
        return "OAForm";
    }

    //就活公欠届提出
    @PostMapping(value = "/student/{studentId}", params = "jobSearch")
    public String postJobForm(HttpServletRequest request, @ModelAttribute("studentId") @PathVariable("studentId") Integer studentId, @Validated(jobSearchFormGroup.class) @ModelAttribute("oAMainForm") OAMainForm form, BindingResult bindingResult, Model model){
        return service.postOA(bindingResult, request, form, model);
    }
    //セミナー公欠届提出
    @PostMapping(value = "/student/{studentId}", params = "seminar")
    public String postSeminarForm(HttpServletRequest request, @ModelAttribute("studentId") @PathVariable("studentId") Integer studentId, @Validated(seminarGroup.class) @ModelAttribute("oAMainForm") OAMainForm form, BindingResult bindingResult, Model model){
        return service.postOA(bindingResult, request, form, model);
    }
    //忌引公欠届提出
    @PostMapping(value = "/student/{studentId}", params = "bereavement")
    public String postBereavementForm(HttpServletRequest request, @ModelAttribute("studentId") @PathVariable("studentId") Integer studentId, @Validated(bereavementGroup.class) @ModelAttribute("oAMainForm") OAMainForm form, BindingResult bindingResult, Model model){
        return service.postOA(bindingResult, request, form, model);
    }
    //出席停止公欠届提出
    @PostMapping(value = "/student/{studentId}", params = "attendanceBan")
    public String postBanForm(HttpServletRequest request, @ModelAttribute("studentId") @PathVariable("studentId") Integer studentId, @Validated(attendanceBanGroup.class) @ModelAttribute("oAMainForm") OAMainForm form, BindingResult bindingResult, Model model){
        return service.postOA(bindingResult, request, form, model);
    }
    //その他公欠届提出
    @PostMapping(value = "/student/{studentId}", params = "other")
    public String postOtherForm(HttpServletRequest request, @ModelAttribute("studentId") @PathVariable("studentId") Integer studentId, @Validated(otherGroup.class) @ModelAttribute("oAMainForm") OAMainForm form, BindingResult bindingResult, Model model){
        return service.postOA(bindingResult, request, form, model);
    }

    //提出済み公欠届List
    @GetMapping("/student/{studentId}/OAList")
    public String showStudentOAList(@PathVariable("studentId") Integer studentId, StudentOASearchForm form, Model model){
        //session取得
        if(session.getAttribute("StudentSearchForm") != null){
            form = (StudentOASearchForm) session.getAttribute("StudentSearchForm");
        }
        return service.getStudentOAList(studentId, form, model);
    }
    //OAList検索
    @GetMapping(value = "/student/{studentId}/OAList", params = "search")
    public String showStudentOAListSearch(@PathVariable("studentId") Integer studentId, StudentOASearchForm form, Model model){
        session.setAttribute("StudentSearchForm", form);
        return service.getStudentOAList(studentId, form, model);
    }
    //公欠届詳細
    @GetMapping("/student/{studentId}/OAList/{OAId}")
    public String showStudentOAInfo(@ModelAttribute @PathVariable("studentId") Integer studentId,@ModelAttribute  @PathVariable("OAId") Integer OAId, Model model){
        //OAInfo取得
        OAMainInfoEntity mainInfoEntity = service.findMainInfo(OAId);
        List<OADateInfoEntity> dateInfoEntities = service.findDateInfo(OAId);
        return service.getOAInfo(mainInfoEntity, dateInfoEntities, OAId, model, "", "info");
    }
    //公欠届別バージョン詳細
    @GetMapping("/student/{studentId}/{OAId}")
    public String showStudentOAInfoByVersion(@ModelAttribute @PathVariable("studentId") Integer studentId,@ModelAttribute  @PathVariable("OAId") Integer OAId,@RequestParam("version") Integer version, Model model){
        //OAInfo取得
        OAMainInfoEntity mainInfoEntity = service.findMainInfoByVersion(OAId, version);
        List<OADateInfoEntity> dateInfoEntities = service.findDateInfoByVersion(OAId, version);
        return service.getOAInfoByVersion(mainInfoEntity, dateInfoEntities, OAId, model, version);
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
        Map<String, List<String>> OAPeriods = service.toOAPeriods(dateInfoEntities);
        //公欠日時をMapにする
        OAMainInfoDTO mainInfoDTO = mainInfoEntity.toInfoDTO();
        switch (mainInfoEntity.reason()){
            case jobSearch -> {
                JobSearchEntity jobSearch = service.findJobSearchInfo(OAId);
                OAMainForm form = service.toJobSearchForm(mainInfoDTO,OAPeriods,jobSearch);
                model.addAttribute("oAMainForm", form);
            }
            case seminar -> {
                SeminarEntity seminar = service.findSeminarInfo(OAId);
                OAMainForm form = service.toSeminarForm(mainInfoDTO, OAPeriods, seminar);
                model.addAttribute("oAMainForm", form);
            }
            case bereavement -> {
                BereavementEntity bereavement = service.findBereavementInfo(OAId);
                OAMainForm form = service.toBereavementForm(mainInfoDTO, OAPeriods, bereavement);
                model.addAttribute("oAMainForm", form);
            }
            case attendanceBan -> {
                AttendanceBanEntity attendanceBan = service.findAttendanceBanInfo(OAId);
                OAMainForm form = service.toAttendanceBanForm(mainInfoDTO, OAPeriods, attendanceBan);
                model.addAttribute("oAMainForm", form);
            }
            case other -> {
                OtherEntity other = service.findOtherInfo(OAId);
                OAMainForm form = service.toOtherForm(mainInfoDTO, OAPeriods, other);
                model.addAttribute("oAMainForm", form);
            }
        }
        model.addAttribute("OAId", mainInfoDTO.officialAbsenceId());
        model.addAttribute("mode", "edit");
        List<ExceptionDateEntity> exceptionDates = service.getExceptionDates();
        model.addAttribute("subjects", subjects);
        model.addAttribute("exceptionDates", exceptionDates);
        model.addAttribute("OAPeriods", OAPeriods);
        return "OAForm";
    }
    //就活再提出
    @PostMapping(value = "/student/{studentId}/OAList/{OAId}", params = "jobSearch")
    public String postJobForm(@ModelAttribute("studentId") @PathVariable("studentId") Integer studentId,@PathVariable("OAId") Integer OAId, @Validated(jobSearchFormGroup.class) @ModelAttribute("oAMainForm") OAMainForm form, BindingResult bindingResult){
        return service.rePostOA(bindingResult, OAId, form);
    }
    //セミナー再提出
    @PostMapping(value = "/student/{studentId}/OAList/{OAId}", params = "seminar")
    public String postSeminarForm(@ModelAttribute("studentId") @PathVariable("studentId") Integer studentId,@PathVariable("OAId") Integer OAId,  @Validated(seminarGroup.class) @ModelAttribute("oAMainForm") OAMainForm form, BindingResult bindingResult){
        return service.rePostOA(bindingResult, OAId, form);
    }
    //忌引再提出
    @PostMapping(value = "/student/{studentId}/OAList/{OAId}", params = "bereavement")
    public String postBereavementForm(@ModelAttribute("studentId") @PathVariable("studentId") Integer studentId,@PathVariable("OAId") Integer OAId,  @Validated(bereavementGroup.class) @ModelAttribute("oAMainForm") OAMainForm form, BindingResult bindingResult){
        return service.rePostOA(bindingResult, OAId, form);
    }
    //出席停止再提出
    @PostMapping(value = "/student/{studentId}/OAList/{OAId}", params = "attendanceBan")
    public String postBanForm(@ModelAttribute("studentId") @PathVariable("studentId") Integer studentId,@PathVariable("OAId") Integer OAId,  @Validated(attendanceBanGroup.class) @ModelAttribute("oAMainForm") OAMainForm form, BindingResult bindingResult){
        return service.rePostOA(bindingResult, OAId, form);
    }
    //その他再提出
    @PostMapping(value = "/student/{studentId}/OAList/{OAId}", params = "other")
    public String postOtherForm(@ModelAttribute("studentId") @PathVariable("studentId") Integer studentId,@PathVariable("OAId") Integer OAId,  @Validated(otherGroup.class) @ModelAttribute("oAMainForm") OAMainForm form, BindingResult bindingResult){
        return service.rePostOA(bindingResult, OAId, form);
    }

    //破棄
    @DeleteMapping("/student/{studentId}/OAList/{OAId}")
    public String deleteOA(@PathVariable("OAId")Integer OAId, @PathVariable("studentId") String studentId){
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
        service.deleteReport(OAId);
        service.deleteMain(OAId);
        return "redirect:/jobportal/student/{studentId}/OAList";
    }

    @GetMapping("/student/{studentId}/reportform/{oaId}")
    public String editReportForm(@PathVariable("studentId") Integer studentId,
                                 @PathVariable("oaId") Integer OAId,
                                 Model model) {
        model.addAttribute("studentId", studentId);
        model.addAttribute("oaId", OAId);
        OAMainInfoEntity mainInfoEntity = service.findMainInfo(OAId);
        List<OADateInfoEntity> dateInfoEntities = service.findDateInfo(OAId);
        model.addAttribute("OADate",dateInfoEntities);
        OAMainInfoDTO mainInfoDTO = mainInfoEntity.toInfoDTO();
        switch (mainInfoDTO.reason()) {
            case "就活" -> {
                JobSearchEntity jobSearch = service.findJobSearchInfo(OAId);
                model.addAttribute("reason",mainInfoDTO.reason());
                model.addAttribute("work",jobSearch.work().getJapaneseName());
                model.addAttribute("jobSearch",jobSearch);

            }
            case "セミナー・合説" -> {
                SeminarEntity seminar = service.findSeminarInfo(OAId);
                model.addAttribute("reason",mainInfoDTO.reason());
                model.addAttribute("seminar", seminar);
            }
        }
        return "reportform";
    }
//    @PostMapping(value="/student/{studentId}/reportform/{oaId}", params="briefingSession")
//    public String briefingSessionReport(@PathVariable("studentId") Integer studentId,
//                                        @PathVariable("oaId") Integer OAId,
//                                        @Validated(briefingSessionGroup.class) @ModelAttribute("ReportForm") ReportForm form,
//                                        Model model)
//    {
//
//    }
//
//    @PostMapping(value="/student/{studentId}/reportform/{oaId}", params="briefingSession")
//    public String briefingSessionReport(@PathVariable("studentId") Integer studentId,
//                                        @PathVariable("oaId") Integer OAId,
//                                        @Validated(examGroup.class) @ModelAttribute("ReportForm") ReportForm form,
//                                        Model model)
//    {
//
//    }
    @PostMapping(value="/student/{studentId}/reportform/{oaId}", params="Interview")
    public String briefingSessionReport(@PathVariable("studentId") Integer studentId,
                                        @PathVariable("oaId") Integer OAId,
                                        @Validated(InterviewGroup.class) @ModelAttribute("ReportForm") ReportForm form,
                                        Model model)
    {
        System.out.println(form);
        return "redirect:/jobportal/student/{studentId}/OAList";
    }
//
//    @PostMapping(value="/student/{studentId}/reportform/{oaId}", params="informalDecisionCeremony")
//    public String briefingSessionReport(@PathVariable("studentId") Integer studentId,
//                                        @PathVariable("oaId") Integer OAId,
//                                        @Validated(informalDecisionCeremonyGroup.class) @ModelAttribute("ReportForm") ReportForm form,
//                                        Model model)
//    {
//
//    }
//    @PostMapping(value="/student/{studentId}/reportform/{oaId}", params="training")
//    public String briefingSessionReport(@PathVariable("studentId") Integer studentId,
//                                        @PathVariable("oaId") Integer OAId,
//                                        @Validated(trainingGroup.class) @ModelAttribute("ReportForm") ReportForm form,
//                                        Model model)
//    {
//
//    }
//    @PostMapping(value="/student/{studentId}/reportform/{oaId}", params="reportSeminar")
//    public String briefingSessionReport(@PathVariable("studentId") Integer studentId,
//                                        @PathVariable("oaId") Integer OAId,
//                                        @Validated(reportSeminarGroup.class) @ModelAttribute("ReportForm") ReportForm form,
//                                        Model model)
//    {
//
//    }
//    @PostMapping(value="/student/{studentId}/reportform/{oaId}", params="reportOther")
//    public String briefingSessionReport(@PathVariable("studentId") Integer studentId,
//                                        @PathVariable("oaId") Integer OAId,
//                                        @Validated(reportOtherGroup.class) @ModelAttribute("ReportForm") ReportForm form,
//                                        Model model)
//    {
//
//    }

}
