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
import java.util.List;
import java.util.Map;

import static com.jobportal.JobPortal.Service.ReportType.seminar;

@Controller
@RequiredArgsConstructor
public class StudentController {

    private final MailController mailController;
    @Autowired
    private final MainService service;
    @Autowired
    public final HttpSession session;
//    public final Subject[][] subjects = {
//        {new Subject(1,"情報システム演習"),new Subject(2,"情報システム演習"),new Subject(3,"資格対策"),new Subject(4,"資格対策"),new Subject(5,"プレゼンテーション")},
//        {new Subject(6,"システム開発Ⅰ"),new Subject(7,"システム開発Ⅰ"),new Subject(8,"IT応用")},
//        {new Subject(9,"システム開発Ⅱ"),new Subject(10,"システム開発Ⅱ")},
//        {new Subject(13,"システム開発Ⅱ実習"),new Subject(14,"システム開発Ⅱ実習")},
//        {new Subject(17,"システム開発Ⅰ実習"),new Subject(18,"システム開発Ⅰ実習")}
//    };

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
            //student.setGno(Integer.parseInt(person.get("mellon-email").substring(0, 5)));
            if(person.get("group").equals("学生")) {
                return "redirect:/student/" + student.getGno();
            }
            else return "redirect:/teacher/";
    }

    /*
    * 生徒のトップページ表示
    *
    * SSOから取得した学生情報を元に作成されたStudentクラスがrequestで渡される。
    * 生徒の希望職業と希望業種を取得し、トップページに表示する。
    *
    * */
    @GetMapping(value="/student/{studentId}")
    public String student(HttpServletRequest request,Student student, @PathVariable("studentId") Integer studentId, Model model) {
        student.setGno(studentId);
        student = (Student) request.getAttribute("student");
        DesiredOccupation desiredOccupation = service.getOccupation(studentId);
        model.addAttribute("student", student);
        model.addAttribute("desiredOccupation", desiredOccupation);
        return "student";
    }

    /*
    * 希望業種・希望職業選択ページ表示
    * */
    @GetMapping("/student/{studentId}/desiredOccupation")
    public String getDesiredOccupation(@PathVariable("studentId") @ModelAttribute Integer studentId, Model model) {
//        DesiredOccupation desiredOccupation = service.getOccupation(studentId);
//        model.addAttribute("desiredOccupation", desiredOccupation);
        return "desiredOccupation";
    }

    /*
    * 希望業種・希望職業ポストメソッド
    *
    * もし既に希望業種・希望職業が存在すればUpdate、存在しなければInsertする。
    * */
    @PostMapping("/student/{studentId}/desiredOccupation")
    public String postDesiredOccupation(@PathVariable("studentId") Integer studentId, @RequestParam("business") String business,@RequestParam("occupation") String occupation, Model model){
        if(service.existsDesired(studentId)){
            service.updateDesiredBusiness(studentId, business);
            service.updateDesiredOccupation(studentId, occupation);
        }else {
            service.insertDesired(studentId, business, occupation);
        }
        return "redirect:/student/{studentId}";
    }

    /*
    * 公欠届Formページ表示
    *
    * Studentクラスの区分から、時間割(Subject[][] subjects)を求めて返す。
    * Subjectはidとnameがある。
    * 例外授業日(List<ExceptionDateEntity> exceptionDates)も返す。
    * ExceptionDateEntityは日付(exceptionDate)と変更後の曜日(weekdayNumber)がある。
    * */
    @GetMapping("/student/{studentId}/OACreationForm")
    public String showForm(HttpServletRequest request, @PathVariable("studentId") Integer studentId, @ModelAttribute("oAMainForm") OAMainForm form, Model model){
        model.addAttribute("studentId",studentId);
        model.addAttribute("mode", "create");
        Student student = (Student) request.getAttribute("student");
        //学校で使う用
        Subject[][] subjects = service.getSubjectArr(service.setClassification(student));
        List<ExceptionDateEntity> exceptionDates = service.getExceptionDates();
        model.addAttribute("subjects", subjects);
        model.addAttribute("exceptionDates", exceptionDates);
        return "OAForm";
    }

    /*
     * 公欠届編集ページ表示
     *
     * 公欠IDから公欠情報と公欠日情報を取得。
     * 公欠情報の公欠理由から固有の公欠情報を取得
     * それぞれの情報からForm情報を構築し、htmlに渡す。
     * 時間割と例外授業に関する処理はshowForm↑と同じ。
     * */
    @GetMapping("/student/{studentId}/OAList/{OAId}/OAEditForm")
    public String showEditForm(HttpServletRequest request, @PathVariable("studentId") Integer studentId, @PathVariable("OAId") Integer OAId, Model model){
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
        Student student = (Student) request.getAttribute("student");
        Subject[][] subjects = service.getSubjectArr(service.setClassification(student));
        model.addAttribute("subjects", subjects);
        model.addAttribute("exceptionDates", exceptionDates);
        model.addAttribute("OAPeriods", OAPeriods);
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

    /*
    * 公欠届List
    *
    * ページ遷移が行われたときにこのハンドラメソッドが呼び出される。
    * */
    @GetMapping("/student/{studentId}/OAList")
    public String showStudentOAList(@PathVariable("studentId") Integer studentId, StudentOASearchForm form, Model model){
        //session取得
        if(session.getAttribute("StudentSearchForm") != null){
            form = (StudentOASearchForm) session.getAttribute("StudentSearchForm");
        }
        return service.getStudentOAList(studentId, form, model);
    }
    /*
    * 公欠届List(検索)
    *
    * 検索が行われたときにこのハンドラメソッドが呼び出される。
    * */
    @GetMapping(value = "/student/{studentId}/OAList", params = "search")
    public String showStudentOAListSearch(@PathVariable("studentId") Integer studentId, StudentOASearchForm form, Model model){
        session.setAttribute("StudentSearchForm", form);
        return service.getStudentOAList(studentId, form, model);
    }

    //公欠届詳細表示
    @GetMapping("/student/{studentId}/OAList/{OAId}")
    public String showStudentOAInfo(@ModelAttribute @PathVariable("studentId") Integer studentId,@ModelAttribute  @PathVariable("OAId") Integer OAId, Model model){
        OAMainInfoEntity mainInfoEntity = service.findMainInfo(OAId);
        List<OADateInfoEntity> dateInfoEntities = service.findDateInfo(OAId);
        //(公欠情報, 公欠日情報、公欠ID、model、teacherFlag(生徒なら""、先生なら"teacher"),mode(普通はinfo。学生検索から呼び出されたときのみsearch))
        return service.getOAInfo(mainInfoEntity, dateInfoEntities, OAId, model, "", "info");
    }

    //公欠届別バージョン詳細表示
    @GetMapping("/student/{studentId}/{OAId}")
    public String showStudentOAInfoByVersion(@ModelAttribute @PathVariable("studentId") Integer studentId,@ModelAttribute  @PathVariable("OAId") Integer OAId,@RequestParam("version") Integer version, Model model){
        //公欠情報
        OAMainInfoEntity mainInfoEntity = service.findMainInfoByVersion(OAId, version);
        //公欠日情報
        List<OADateInfoEntity> dateInfoEntities = service.findDateInfoByVersion(OAId, version);
        return service.getOAInfoByVersion(mainInfoEntity, dateInfoEntities, OAId, model, version);
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

    //公欠破棄
    @DeleteMapping("/student/{studentId}/OAList/{OAId}")
    public String deleteOA(@PathVariable("OAId")Integer OAId, @PathVariable("studentId") String studentId){
        service.deleteOA(OAId);
        return "redirect:/student/{studentId}/OAList";
    }

    /*
    * 報告書Form表示
    *
    * 報告書は「就活」と「セミナー」に分かれる。
    *
    * */
    @GetMapping("/student/{studentId}/reportCreationForm/{oaId}")
    public String editReportForm(@PathVariable("studentId") Integer studentId,
                                 @PathVariable("oaId") Integer OAId,
                                 Model model) {
        model.addAttribute("studentId", studentId);
        model.addAttribute("oaId", OAId);
        OAMainInfoEntity mainInfoEntity = service.findMainInfo(OAId);
        List<OADateInfoEntity> dateInfoEntities = service.findDateInfo(OAId);
        model.addAttribute("OADate",dateInfoEntities);
        switch (mainInfoEntity.reason()) {
            case jobSearch -> {
                JobSearchEntity jobSearch = service.findJobSearchInfo(OAId);
                model.addAttribute("reason",mainInfoEntity.reason().toString());
                model.addAttribute("jobSearch",jobSearch);
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

    //面接報告書提出
    @PostMapping(value="/student/{studentId}/reportCreationForm/{oaId}", params="jobInterview")
    public String interviewReport(@PathVariable("studentId") Integer studentId,
                                  @PathVariable("oaId") Integer OAId,
                                  @Validated(interviewGroup.class) @ModelAttribute("ReportForm") ReportForm form,
                                  BindingResult bindingResult,
                                  Model model)
    {
        return service.postReport(bindingResult, OAId, form, model);
    }
    //説明会報告書提出
    @PostMapping(value="/student/{studentId}/reportCreationForm/{oaId}", params="briefing")
    public String briefingReport(@PathVariable("studentId") Integer studentId,
                                        @PathVariable("oaId") Integer OAId,
                                        @Validated(briefingGroup.class) @ModelAttribute("ReportForm") ReportForm form,
                                        BindingResult bindingResult,
                                        Model model)
    {
        return service.postReport(bindingResult, OAId, form, model);
    }
    //試験報告書提出
    @PostMapping(value="/student/{studentId}/reportCreationForm/{oaId}", params="test")
    public String testReport(@PathVariable("studentId") Integer studentId,
                                        @PathVariable("oaId") Integer OAId,
                                        @Validated(testGroup.class) @ModelAttribute("ReportForm") ReportForm form,
                                        BindingResult bindingResult,
                                        Model model)
    {
        return service.postReport(bindingResult, OAId, form, model);
    }
    //内定式報告書提出
    @PostMapping(value="/student/{studentId}/reportCreationForm/{oaId}", params="informalCeremony")
    public String informalCeremonyReport(@PathVariable("studentId") Integer studentId,
                                        @PathVariable("oaId") Integer OAId,
                                        @Validated(informalCeremonyGroup.class) @ModelAttribute("ReportForm") ReportForm form,
                                        BindingResult bindingResult,
                                        Model model)
    {
        return service.postReport(bindingResult, OAId, form, model);
    }
    //研修報告書提出
    @PostMapping(value="/student/{studentId}/reportCreationForm/{oaId}", params="training")
    public String trainingReport(@PathVariable("studentId") Integer studentId,
                                        @PathVariable("oaId") Integer OAId,
                                        @Validated(trainingGroup.class) @ModelAttribute("ReportForm") ReportForm form,
                                        BindingResult bindingResult,
                                        Model model)
    {
        return service.postReport(bindingResult, OAId, form, model);
    }
    //その他報告書提出
    @PostMapping(value="/student/{studentId}/reportCreationForm/{oaId}", params="jobOther")
    public String otherReport(@PathVariable("studentId") Integer studentId,
                              @PathVariable("oaId") Integer OAId,
                              @Validated(reportOtherGroup.class) @ModelAttribute("ReportForm") ReportForm form,
                              BindingResult bindingResult,
                              Model model)
    {
        return service.postReport(bindingResult, OAId, form, model);
    }
    //セミナー報告書提出
    @PostMapping(value="/student/{studentId}/reportCreationForm/{oaId}", params="reportSeminar")
    public String seminarReport(@PathVariable("studentId") Integer studentId,
                                        @PathVariable("oaId") Integer OAId,
                                        @Validated(reportSeminarGroup.class) @ModelAttribute("ReportForm") ReportForm form,
                                        BindingResult bindingResult,
                                        Model model)
    {
        return service.postReport(bindingResult, OAId, form, model);
    }
    /*
    * 報告書再提出Form表示
    *
    * 報告書IDから取得した公欠情報によって再提出用のFormを作る。
    * セミナー以外は会社情報(会社名など)が別テーブルに用意されているのでそれも取得する。
    * セミナーはセミナーテーブル内に会社情報が含まれている。
    * */
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
        if(!mainInfo.getReason().equals(seminar)){
            JobSearchEntity jobSearch = service.findJobSearchInfo(mainInfo.getOfficialAbsenceId());
            model.addAttribute("jobSearch",jobSearch);
        }
        model.addAttribute("reason", mainInfo.getReason().toString());
        model.addAttribute("ReportForm", form);
        model.addAttribute("mode", "edit");
        return "reportForm";
    }

    /*
    * 報告書詳細ページ
    *
    * 報告書IDから取得した公欠情報によって固有情報を取得する。
    * 提出日はLocalDateで返ってくるので日本語のデータ(yyyy年MM月dd日(曜日))にする。
    * */
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
    //説明会報告書再提出
    @PostMapping(value = "/sutdent/{studentId}/report/{reportId}", params = "briefing")
    public String repostReportBriefing(@PathVariable("studentId") Integer studentId, @PathVariable("reportId") Integer reportId,@Validated(briefingGroup.class) @ModelAttribute("ReportForm") ReportForm form,
                                       BindingResult bindingResult,
                                       Model model){
        return service.repostReport(reportId, form, bindingResult);
    }
    //試験報告書再提出
    @PostMapping(value = "/sutdent/{studentId}/report/{reportId}", params = "test")
    public String repostReportTest(@PathVariable("studentId") Integer studentId, @PathVariable("reportId") Integer reportId, @Validated(testGroup.class) @ModelAttribute("ReportForm") ReportForm form,
                                   BindingResult bindingResult,
                                   Model model){
        return service.repostReport(reportId, form, bindingResult);
    }
    //面接報告書再提出
    @PostMapping(value = "/sutdent/{studentId}/report/{reportId}", params = "jobInterview")
    public String repostReportJobInterview(@PathVariable("studentId") Integer studentId, @PathVariable("reportId") Integer reportId, @Validated(interviewGroup.class) @ModelAttribute("ReportForm") ReportForm form,
                                           BindingResult bindingResult,
                                           Model model){
        return service.repostReport(reportId, form, bindingResult);
    }
    //内定式報告書再提出
    @PostMapping(value = "/sutdent/{studentId}/report/{reportId}", params = "informalCeremony")
    public String repostReportInformalCeremony(@PathVariable("studentId") Integer studentId, @PathVariable("reportId") Integer reportId, @Validated(informalCeremonyGroup.class) @ModelAttribute("ReportForm") ReportForm form,
                                               BindingResult bindingResult,
                                               Model model){
        return service.repostReport(reportId, form, bindingResult);
    }
    //研修報告書再提出
    @PostMapping(value = "/sutdent/{studentId}/report/{reportId}", params = "training")
    public String repostReportTraining(@PathVariable("studentId") Integer studentId, @PathVariable("reportId") Integer reportId, @Validated(trainingGroup.class) @ModelAttribute("ReportForm") ReportForm form,
                                       BindingResult bindingResult,
                                       Model model){
        return service.repostReport(reportId, form, bindingResult);
    }
    //その他報告書再提出
    @PostMapping(value = "/sutdent/{studentId}/report/{reportId}", params = "other")
    public String repostReportJobOther(@PathVariable("studentId") Integer studentId, @PathVariable("reportId") Integer reportId, @Validated(reportOtherGroup.class) @ModelAttribute("ReportForm") ReportForm form,
                                       BindingResult bindingResult,
                                       Model model){
        return service.repostReport(reportId, form, bindingResult);
    }
    //セミナー報告書再提出
    @PostMapping(value = "/sutdent/{studentId}/report/{reportId}", params = "seminar")
    public String repostReportSeminar(@PathVariable("studentId") Integer studentId, @PathVariable("reportId") Integer reportId, @Validated(reportSeminarGroup.class) @ModelAttribute("ReportForm") ReportForm form,
                                      BindingResult bindingResult,
                                      Model model){
        if(bindingResult.hasErrors()){
            return "reportForm";
        }
        return service.repostReport(reportId, form, bindingResult);
    }

    /*
    * 公欠届別バージョン詳細ページ表示
    *
    * 報告書IDと指定したバージョンから取得した公欠情報によって指定したバージョンの固有情報を取得する。
    * */
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
    //報告書破棄
    @DeleteMapping("/student/{studentId}/report/{reportId}")
    public String deleteReport(@PathVariable("studentId") Integer studentId, @PathVariable("reportId") Integer reportId){
        service.deleteReports(reportId);
        return "redirect:/student/{studentId}/OAList";
    }
    /*
    * 報告書一覧ページ(会社検索)
    *
    * 先輩達の報告書を閲覧できる。
    * 会社名で検索する。
    *
    * */
    @GetMapping("/student/{studentId}/reportLogs")
    public String companyLogs(@PathVariable("studentId") Integer studentId, Model model, @RequestParam(value = "companyName",defaultValue = "") String companyName, @RequestParam(value = "page", defaultValue = "1") Integer page){
        model.addAttribute("companyName", companyName);
        model.addAttribute("studentId", studentId);
        List<ReportLogEntity> logEntities = service.searchReportLogs(companyName, page);
        logEntities = logEntities.stream()
                        .peek(e->e.setStudentId(e.getStudentId() / 1000))
                        .toList();

        //以下ページング処理
        //1ページに表示する件数
        int pageSize = 25;
        Integer count = service.countSearchReportLogs(companyName);
        //最大ページ数
        int maxSize = (int)Math.ceil((double) count / pageSize);
        //画面に表示するページ数。1~5。
        int displayPageCount = Math.min(maxSize, 5);
        //ページの起点となるページ番号。
        int start = Math.max(1, Math.min(page - (displayPageCount - 1) / 2, maxSize - displayPageCount + 1));
        //ページの終点となるページ番号
        int end = Math.min(maxSize, start + displayPageCount - 1);
        model.addAttribute("maxSize", maxSize);
        model.addAttribute("currentPage", page);
        model.addAttribute("start", start);
        model.addAttribute("end", end);
        model.addAttribute("logEntities", logEntities);
        return "reportLogs";
    }
}
