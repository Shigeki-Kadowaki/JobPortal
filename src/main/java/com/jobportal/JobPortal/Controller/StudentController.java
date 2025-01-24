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
import java.time.LocalDate;
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

    @GetMapping("/dockerTest")
    public String dockerTest(Model model) {
        return "dockerTest";
    }

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
                System.out.println("otherReason : " + form.getOtherReason());
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
    public String postJobForm(@ModelAttribute("studentId") @PathVariable("studentId") Integer studentId,@PathVariable("OAId") Integer OAId, @Validated(jobSearchFormGroup.class) @ModelAttribute("oAMainForm") OAMainForm form, BindingResult bindingResult, Model model){
        return service.repostOA(bindingResult, OAId, form, model);
    }
    //セミナー再提出
    @PostMapping(value = "/student/{studentId}/OAList/{OAId}", params = "seminar")
    public String postSeminarForm(@ModelAttribute("studentId") @PathVariable("studentId") Integer studentId,@PathVariable("OAId") Integer OAId,  @Validated(seminarGroup.class) @ModelAttribute("oAMainForm") OAMainForm form, BindingResult bindingResult, Model model){
        return service.repostOA(bindingResult, OAId, form, model);
    }
    //忌引再提出
    @PostMapping(value = "/student/{studentId}/OAList/{OAId}", params = "bereavement")
    public String postBereavementForm(@ModelAttribute("studentId") @PathVariable("studentId") Integer studentId,@PathVariable("OAId") Integer OAId,  @Validated(bereavementGroup.class) @ModelAttribute("oAMainForm") OAMainForm form, BindingResult bindingResult, Model model){
        return service.repostOA(bindingResult, OAId, form, model);
    }
    //出席停止再提出
    @PostMapping(value = "/student/{studentId}/OAList/{OAId}", params = "attendanceBan")
    public String postBanForm(@ModelAttribute("studentId") @PathVariable("studentId") Integer studentId,@PathVariable("OAId") Integer OAId,  @Validated(attendanceBanGroup.class) @ModelAttribute("oAMainForm") OAMainForm form, BindingResult bindingResult, Model model){
        return service.repostOA(bindingResult, OAId, form, model);
    }
    //その他再提出
    @PostMapping(value = "/student/{studentId}/OAList/{OAId}", params = "other")
    public String postOtherForm(@ModelAttribute("studentId") @PathVariable("studentId") Integer studentId,@PathVariable("OAId") Integer OAId,  @Validated(otherGroup.class) @ModelAttribute("oAMainForm") OAMainForm form, BindingResult bindingResult, Model model){
        return service.repostOA(bindingResult, OAId, form, model);
    }

    //破棄
    @DeleteMapping("/student/{studentId}/OAList/{OAId}")
    public String deleteOA(@PathVariable("OAId")Integer OAId, @PathVariable("studentId") String studentId){
        service.deleteOA(OAId);
        return "redirect:/jobportal/student/{studentId}/OAList";
    }

    @GetMapping("/student/{studentId}/reportCreationForm/{oaId}")
    public String editReportForm(@PathVariable("studentId") Integer studentId,
                                 @PathVariable("oaId") Integer OAId,
                                 Model model) {
        model.addAttribute("studentId", studentId);
        model.addAttribute("oaId", OAId);
        OAMainInfoEntity mainInfoEntity = service.findMainInfo(OAId);
        List<OADateInfoEntity> dateInfoEntities = service.findDateInfo(OAId);
        model.addAttribute("OADate",dateInfoEntities);
        OAMainInfoDTO mainInfoDTO = mainInfoEntity.toInfoDTO();
        switch (mainInfoEntity.reason()) {
            case jobSearch -> {
                JobSearchEntity jobSearch = service.findJobSearchInfo(OAId);
                model.addAttribute("reason",mainInfoEntity.reason().toString());
                model.addAttribute("jobSearch",jobSearch);
                System.out.println(jobSearch.work().toString());

            }
            case seminar -> {
                SeminarEntity seminar = service.findSeminarInfo(OAId);
                model.addAttribute("reason",mainInfoEntity.reason().toString());
                model.addAttribute("seminar", seminar);
            }
        }
        model.addAttribute("ReportForm", new ReportForm());
        model.addAttribute("mode","create");
        return "reportForm";
    }

    @PostMapping(value="/student/{studentId}/reportCreationForm/{oaId}", params="jobInterview")
    public String interviewReport(@PathVariable("studentId") Integer studentId,
                                  @PathVariable("oaId") Integer OAId,
                                  @Validated(interviewGroup.class) @ModelAttribute("ReportForm") ReportForm form,
                                  BindingResult bindingResult,
                                  Model model)
    {
        return service.postReport(bindingResult, OAId, form, model);
    }

    @PostMapping(value="/student/{studentId}/reportCreationForm/{oaId}", params="briefing")
    public String briefingReport(@PathVariable("studentId") Integer studentId,
                                        @PathVariable("oaId") Integer OAId,
                                        @Validated(briefingGroup.class) @ModelAttribute("ReportForm") ReportForm form,
                                        BindingResult bindingResult,
                                        Model model)
    {
        return service.postReport(bindingResult, OAId, form, model);
    }

    @PostMapping(value="/student/{studentId}/reportCreationForm/{oaId}", params="test")
    public String testReport(@PathVariable("studentId") Integer studentId,
                                        @PathVariable("oaId") Integer OAId,
                                        @Validated(testGroup.class) @ModelAttribute("ReportForm") ReportForm form,
                                        BindingResult bindingResult,
                                        Model model)
    {
        return service.postReport(bindingResult, OAId, form, model);
    }


    @PostMapping(value="/student/{studentId}/reportCreationForm/{oaId}", params="informalCeremony")
    public String informalCeremonyReport(@PathVariable("studentId") Integer studentId,
                                        @PathVariable("oaId") Integer OAId,
                                        @Validated(informalCeremonyGroup.class) @ModelAttribute("ReportForm") ReportForm form,
                                        BindingResult bindingResult,
                                        Model model)
    {
        return service.postReport(bindingResult, OAId, form, model);
    }
    @PostMapping(value="/student/{studentId}/reportCreationForm/{oaId}", params="training")
    public String trainingReport(@PathVariable("studentId") Integer studentId,
                                        @PathVariable("oaId") Integer OAId,
                                        @Validated(trainingGroup.class) @ModelAttribute("ReportForm") ReportForm form,
                                        BindingResult bindingResult,
                                        Model model)
    {
        return service.postReport(bindingResult, OAId, form, model);
    }
    @PostMapping(value="/student/{studentId}/reportCreationForm/{oaId}", params="jobOther")
    public String otherReport(@PathVariable("studentId") Integer studentId,
                              @PathVariable("oaId") Integer OAId,
                              @Validated(reportOtherGroup.class) @ModelAttribute("ReportForm") ReportForm form,
                              BindingResult bindingResult,
                              Model model)
    {
        return service.postReport(bindingResult, OAId, form, model);
    }

    @PostMapping(value="/student/{studentId}/reportCreationForm/{oaId}", params="reportSeminar")
    public String seminarReport(@PathVariable("studentId") Integer studentId,
                                        @PathVariable("oaId") Integer OAId,
                                        @Validated(reportSeminarGroup.class) @ModelAttribute("ReportForm") ReportForm form,
                                        BindingResult bindingResult,
                                        Model model)
    {
        return service.postReport(bindingResult, OAId, form, model);
    }

    @PostMapping("/student/{studentId}/report/{reportId}/reportEditForm")
    public String showReportEditForm(@PathVariable("studentId") Integer studentId, @PathVariable("reportId") Integer reportId, Model model){
        ReportInfoEntity mainInfo = service.getReportInfo(reportId);
        model.addAttribute("mainInfo", mainInfo);
        List<OADateInfoEntity> dateInfoEntities = service.findDateInfo(mainInfo.getOfficialAbsenceId());
        model.addAttribute("OADate",dateInfoEntities);
        ReportForm form = new ReportForm();
        switch (mainInfo.getReason()){
            case jobInterview -> {
                ReportInterviewEntity interviewEntity = service.getInterviewEntity(reportId);
                form = service.toReportForm(mainInfo, interviewEntity);
            }
            case briefing -> {
                ReportBriefingEntity briefingEntity = service.getBriefingEntity(reportId);
                form = service.toReportForm(mainInfo, briefingEntity);
            }
            case test -> {
                ReportTestEntity testEntity = service.getTestEntity(reportId);
                form = service.toReportForm(mainInfo, testEntity);
            }
            case informalCeremony -> {
                ReportInformalCeremonyEntity informalCeremonyEntity = service.getInformalCeremonyEntity(reportId);
                form = service.toReportForm(mainInfo, informalCeremonyEntity);
            }
            case training -> {
                ReportTrainingEntity trainingEntity = service.getTrainingEntity(reportId);
                form = service.toReportForm(mainInfo, trainingEntity);
            }
            case jobOther -> {
                ReportOtherEntity otherEntity = service.getOtherEntity(reportId);
                form = service.toReportForm(mainInfo, otherEntity);
            }
            case seminar -> {
                List<ReportSeminarEntity> seminarEntities = service.getSeminarEntity(reportId);
                form = service.toReportForm(mainInfo, seminarEntities);
            }
        }
        if(!mainInfo.getReason().toString().equals("seminar")){
            JobSearchEntity jobSearch = service.findJobSearchInfo(mainInfo.getOfficialAbsenceId());
            model.addAttribute("jobSearch",jobSearch);
        }
        model.addAttribute("reason", mainInfo.getReason().toString());
        model.addAttribute("ReportForm", form);
        model.addAttribute("mode", "edit");
        return "reportForm";
    }

    @GetMapping("/student/{studentId}/report/{reportId}")
    public String showStudentReportInfo(@PathVariable("studentId") Integer studentId, @PathVariable("reportId") Integer reportId, Model model){
        ReportInfoEntity mainInfo = service.getReportInfo(reportId);
        mainInfo.setSubmittedDate(MainService.dateFormat(LocalDate.parse(mainInfo.getSubmittedDate())));
        model.addAttribute("mainInfo", mainInfo);
        switch (mainInfo.getReason()){
            case jobInterview -> {
                ReportInterviewEntity interviewEntity = service.getInterviewEntity(reportId);
                model.addAttribute("interviewEntity", interviewEntity);
            }
            case briefing -> {
                ReportBriefingEntity briefingEntity = service.getBriefingEntity(reportId);
                model.addAttribute("briefingEntity", briefingEntity);
            }
            case test -> {
                ReportTestEntity testEntity = service.getTestEntity(reportId);
                model.addAttribute("testEntity", testEntity);
            }
            case informalCeremony -> {
                ReportInformalCeremonyEntity informalCeremonyEntity = service.getInformalCeremonyEntity(reportId);
                model.addAttribute("informalCeremonyEntity", informalCeremonyEntity);
            }
            case training -> {
                ReportTrainingEntity trainingEntity = service.getTrainingEntity(reportId);
                model.addAttribute("trainingEntity", trainingEntity);
            }
            case jobOther -> {
                ReportOtherEntity otherEntity = service.getOtherEntity(reportId);
                model.addAttribute("otherEntity", otherEntity);
            }
            case seminar -> {
                List<ReportSeminarEntity> seminarEntities = service.getSeminarEntity(reportId);
                model.addAttribute("seminarEntities", seminarEntities);
            }
        }
        model.addAttribute("mode", "info");
        return "reportInfo";
    }

    @PostMapping(value = "/sutdent/{studentId}/report/{reportId}", params = "briefing")
    public String repostReportBriefing(@PathVariable("studentId") Integer studentId, @PathVariable("reportId") Integer reportId,@Validated(briefingGroup.class) @ModelAttribute("ReportForm") ReportForm form,
                                       BindingResult bindingResult,
                                       Model model){
        return service.repostReport(reportId, form, bindingResult);
    }
    @PostMapping(value = "/sutdent/{studentId}/report/{reportId}", params = "test")
    public String repostReportTest(@PathVariable("studentId") Integer studentId, @PathVariable("reportId") Integer reportId, @Validated(testGroup.class) @ModelAttribute("ReportForm") ReportForm form,
                                   BindingResult bindingResult,
                                   Model model){
        return service.repostReport(reportId, form, bindingResult);
    }
    @PostMapping(value = "/sutdent/{studentId}/report/{reportId}", params = "jobInterview")
    public String repostReportJobInterview(@PathVariable("studentId") Integer studentId, @PathVariable("reportId") Integer reportId, @Validated(interviewGroup.class) @ModelAttribute("ReportForm") ReportForm form,
                                           BindingResult bindingResult,
                                           Model model){
        return service.repostReport(reportId, form, bindingResult);
    }
    @PostMapping(value = "/sutdent/{studentId}/report/{reportId}", params = "informalCeremony")
    public String repostReportInformalCeremony(@PathVariable("studentId") Integer studentId, @PathVariable("reportId") Integer reportId, @Validated(informalCeremonyGroup.class) @ModelAttribute("ReportForm") ReportForm form,
                                               BindingResult bindingResult,
                                               Model model){
        return service.repostReport(reportId, form, bindingResult);
    }
    @PostMapping(value = "/sutdent/{studentId}/report/{reportId}", params = "training")
    public String repostReportTraining(@PathVariable("studentId") Integer studentId, @PathVariable("reportId") Integer reportId, @Validated(trainingGroup.class) @ModelAttribute("ReportForm") ReportForm form,
                                       BindingResult bindingResult,
                                       Model model){
        return service.repostReport(reportId, form, bindingResult);
    }
    @PostMapping(value = "/sutdent/{studentId}/report/{reportId}", params = "other")
    public String repostReportJobOther(@PathVariable("studentId") Integer studentId, @PathVariable("reportId") Integer reportId, @Validated(reportOtherGroup.class) @ModelAttribute("ReportForm") ReportForm form,
                                       BindingResult bindingResult,
                                       Model model){
        return service.repostReport(reportId, form, bindingResult);
    }
    @PostMapping(value = "/sutdent/{studentId}/report/{reportId}", params = "seminar")
    public String repostReportSeminar(@PathVariable("studentId") Integer studentId, @PathVariable("reportId") Integer reportId, @Validated(reportSeminarGroup.class) @ModelAttribute("ReportForm") ReportForm form,
                                      BindingResult bindingResult,
                                      Model model){
        if(bindingResult.hasErrors()){
            return "reportForm";
        }
        return service.repostReport(reportId, form, bindingResult);
    }

    //報告書別バージョン詳細
    @GetMapping("/student/{studentId}/reportByVersion/{reportId}")
    public String showStudentReportInfoByVersion(@ModelAttribute @PathVariable("studentId") Integer studentId,@ModelAttribute  @PathVariable("reportId") Integer reportId,@RequestParam("version") Integer version, Model model){
        ReportInfoEntity mainInfo = service.getReportInfoByVersion(reportId, version);
        model.addAttribute("mainInfo", mainInfo);
        switch (mainInfo.getReason()){
            case jobInterview -> {
                ReportInterviewEntity interviewEntity = service.getInterviewEntityByVersion(reportId, version);
                model.addAttribute("interviewEntity", interviewEntity);
            }
            case briefing -> {
                ReportBriefingEntity briefingEntity = service.getBriefingEntityByVersion(reportId, version);
                model.addAttribute("briefingEntity", briefingEntity);
            }
            case test -> {
                ReportTestEntity testEntity = service.getTestEntityByVersion(reportId, version);
                model.addAttribute("testEntity", testEntity);
            }
            case informalCeremony -> {
                ReportInformalCeremonyEntity informalCeremonyEntity = service.getInformalCeremonyEntityByVersion(reportId, version);
                model.addAttribute("informalCeremonyEntity", informalCeremonyEntity);
            }
            case training -> {
                ReportTrainingEntity trainingEntity = service.getTrainingEntityByVersion(reportId, version);
                model.addAttribute("trainingEntity", trainingEntity);
            }
            case jobOther -> {
                ReportOtherEntity otherEntity = service.getOtherEntityByVersion(reportId, version);
                model.addAttribute("otherEntity", otherEntity);
            }
            case seminar -> {
                List<ReportSeminarEntity> seminarEntities = service.getSeminarEntityByVersion(reportId, version);
                model.addAttribute("seminarEntities", seminarEntities);
            }
        }
        model.addAttribute("mode", "read");
        return "reportInfo";
    }

    @DeleteMapping("/student/{studentId}/report/{reportId}")
    public String deleteReport(@PathVariable("studentId") Integer studentId, @PathVariable("reportId") Integer reportId){
        service.deleteReports(reportId);
        return "redirect:/jobportal/student/{studentId}/OAList";
    }
    
    @GetMapping("/student/{studentId}/reportLogs")
    public String companyLogs(@PathVariable("studentId") Integer studentId, Model model, @RequestParam(value = "companyName",defaultValue = "") String companyName, @RequestParam(value = "page", defaultValue = "1") Integer page){
        model.addAttribute("companyName", companyName);
        model.addAttribute("studentId", studentId);
        List<ReportLogEntity> logEntities = service.searchReportLogs(companyName, page);
        logEntities = logEntities.stream()
                        .peek(e->e.setStudentId(e.getStudentId() / 1000))
                        .toList();
        int pageSize = 10;
        Integer count = service.countSearchReportLogs(companyName);
        int maxSize = (int)Math.ceil((double) count / pageSize);
        int displayPageCount = Math.min(maxSize, 5);
        int start = Math.max(1, Math.min(page - (displayPageCount - 1) / 2, maxSize - displayPageCount + 1));
        int end = Math.min(maxSize, start + displayPageCount - 1);
        model.addAttribute("maxSize", maxSize);
        model.addAttribute("currentPage", page);
        model.addAttribute("start", start);
        model.addAttribute("end", end);
        model.addAttribute("logEntities", logEntities);
        return "reportLogs";
    }
}
