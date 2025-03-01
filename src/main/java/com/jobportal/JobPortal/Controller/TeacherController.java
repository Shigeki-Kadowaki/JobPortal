package com.jobportal.JobPortal.Controller;

import com.jobportal.JobPortal.Controller.Form.*;
import com.jobportal.JobPortal.Service.DTO.ExceptionDateDTO;
import com.jobportal.JobPortal.Service.DTO.OAListDTO;
import com.jobportal.JobPortal.Service.Entity.*;
import com.jobportal.JobPortal.Service.MainService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/jobportal")
public class TeacherController {
    private final MailController mailController;
    @Autowired
    public MainService service;
    @Autowired
    public HttpSession session;

    public final String sendAddress = "SaisenJobPortal@gmail.com";
    final Map<String, String> colors = new HashMap<>(){
        {
            put("受理", "list-group-item-success");
            put("未受理", "list-group-item-warning");
            put("却下", "list-group-item-danger");
            put("未提出", "list-group-item-dark");
            put("不要", "list-group-item-light");
        }
    };

    @GetMapping("/teacher")
    public String showTeacherPage() {
        return "teacher";
    }

    //OAList
    @GetMapping("/teacher/OAList")
    public String showTeacherOAList(TeacherOASearchForm form, Model model,@ModelAttribute("page") @RequestParam(defaultValue = "0", value = "currentPage") Integer currentPage) {
        if(session.getAttribute("teacherSearchForm") != null) {
            form = (TeacherOASearchForm) session.getAttribute("teacherSearchForm");
        }
        return service.getTeacherOAList(currentPage, form, model, session);
    }
    //OAList検索
    @GetMapping(value = "/teacher/OAList", params = "search")
    public String showTeacherOAListSearch(TeacherOASearchForm form, Model model,@ModelAttribute("currentPage") @RequestParam(defaultValue = "0", value = "currentPage") Integer currentPage){
        session.setAttribute("teacherSearchForm", form);
        return service.getTeacherOAList(currentPage, form, model, session);
    }
    //OA詳細
    @GetMapping("/teacher/OAList/{OAId}")
    public String showTeacherOAInfo(@PathVariable("OAId") Integer OAId, Model model) {
        OAMainInfoEntity mainInfoEntity = service.findMainInfo(OAId);
        List<OADateInfoEntity> dateInfoEntities = service.findDateInfo(OAId);
        return service.getOAInfo(mainInfoEntity, dateInfoEntities, OAId, model, "teacher_", "info");
    }
    //OA承認
    @PutMapping(value="/teacher/{OAId}", params = "acceptance")
    public String OAAccepted(@PathVariable("OAId") Integer OAId, @RequestParam(value = "reportRequired", required = false, defaultValue = "unnecessary")String reportRequired, @RequestParam("teacherType") String teacherType, @RequestParam("careerCheckRequired") boolean careerCheckRequired) {
        service.updateCheck(OAId, teacherType, true);
        Integer reportId = service.getReportId(OAId);
        /*
        * キャリアチェックで報告書不要が選択された場合、報告書のステータスを「不要」に、公欠届の報告書必要フラグを「false」にする
        *
        * それぞれ、公欠届と報告書のステータスをチェックする。
        * */
        if(teacherType.equals("career") && reportRequired.equals("unnecessary")) {
            service.updateReportStatus(reportId, "unnecessary");
            service.updateReportRequired(OAId, false);
        } else if (reportRequired.equals("necessary")) {
            service.updateReportRequired(OAId, true);
            service.checkOAAndReportCondition(OAId, reportId);
        } else {
            service.checkOAAndReportCondition(OAId, reportId);
        }
        return "redirect:/jobportal/teacher/OAList";
    }
    //OA却下
    @PutMapping(value = "/teacher/{OAId}", params = "rejection")
    public String OAUnaccepted(HttpServletRequest request, @PathVariable("OAId") Integer OAId, @RequestParam(value = "reasonForRejection", required = false)String reasonForRejection, @RequestParam("studentEmail") String studentEmail) {
        service.updateOAStatus(OAId,"rejection");
        //mailController.sendMail(sendAddress, studentEmail, reasonForRejection);
        return "redirect:/jobportal/teacher/OAList";
    }

    //授業区分入力フォーム表示
    @GetMapping("/teacher/classificationForm")
    public String showScheduleClassification(@ModelAttribute("classification") ClassificationForm classification, Model model) {
        List<String> courses = service.getCourses();
        model.addAttribute("courses", courses);
        return "classificationForm";
    }
    //授業区分ポスト、授業区分別時間割入力フォーム表示
    @PostMapping("/teacher/classification")
    public String postScheduleClassification(@Validated @ModelAttribute("classification") ClassificationForm classification, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()){
            List<String> courses = service.getCourses();
            model.addAttribute("courses", courses);
            System.out.println("error");
            return "classificationForm";
        }
        //学校で取得する用
        List<String> lessons = service.getSubjects(classification);
        //テスト用
//        List<String> lessons = new ArrayList<>(List.of("[0] HR (鈴木)","[1] システム開発(SE) (田中)","[2] システム開発Ⅱ(SE) (佐藤)","[4] キャリア (後藤)"));
        Map<Integer, String> lessonInfos = service.toSubjectInfos(lessons);
        model.addAttribute("lessonInfos", lessonInfos);
        model.addAttribute("classification", classification);
        return "scheduleForm";
    }
    //時間割ポスト
    @PostMapping("/teacher/timeTable")
    public String postSchedule(@ModelAttribute TimeTableInfoForm timeTableInfo){

        List<TimeTableEntity> timeTable = timeTableInfo.toTimeTableEntity();
        service.createTimeTable(timeTableInfo, timeTable);
        //json化
//        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
//        String json;
//        try {
//            json = mapper.writeValueAsString(timeTable);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }

        return "redirect:/jobportal/teacher";
    }

    //例外時間割ページ表示
    @GetMapping("/teacher/exceptionDate")
    public String exceptionDate(@ModelAttribute("exceptionDate") ExceptionDateForm exceptionDate, Model model) {
        List<ExceptionDateEntity> exceptionDateList = service.getExceptionDates();
        List<ExceptionDateDTO> exceptionDateDTO = exceptionDateList.stream()
                        .map(e->new ExceptionDateDTO(MainService.dateFormat(e.exceptionDate()), MainService.weekDayFormat(e.weekdayNumber())))
                        .toList();
        model.addAttribute("exceptionDateList", exceptionDateDTO);
        return "exceptionDate";
    }

    //例外時間割ポスト
    @PostMapping("/teacher/exceptionDate")
    public String postExceptionDate(@Validated @ModelAttribute("exceptionDate") ExceptionDateForm exceptionDate, BindingResult bindingResult, Model model) {
        model.addAttribute("exceptionDate", exceptionDate);
        if(bindingResult.hasErrors()){
            return "exceptionDate";
        }
        service.createExceptionDate(exceptionDate.toLocalDate());
        return "redirect:/jobportal/teacher/exceptionDate";
    }

    //例外日削除
    @DeleteMapping ("/teacher/exceptionDate")
    public String deleteExceptionDate(@RequestParam("id") Integer id) {
        service.deleteExceptionDate(id);
        return "redirect:/jobportal/teacher/exceptionDate";
    }

    //学生検索ページ
    @GetMapping("/teacher/studentSearch")
    public String showStudentSearch(@ModelAttribute("studentId") @RequestParam(value = "studentId", required = false) Integer studentId, Model model) {
        model.addAttribute("studentId", studentId);
        System.out.println(studentId);
        service.getStudentOAList(studentId, new StudentOASearchForm(null, null), model);
        DesiredOccupation desiredOccupation = service.getOccupation(studentId);
        model.addAttribute("desiredOccupation", desiredOccupation);
        return "studentSearch";
    }

    //学生情報からOA詳細
    @GetMapping("/teacher/studentSearch/{OAId}")
    public String showSearchStudentOAInfo(@PathVariable("OAId") Integer OAId, Model model) {
        OAMainInfoEntity mainInfoEntity = service.findMainInfo(OAId);
        List<OADateInfoEntity> dateInfoEntities = service.findDateInfo(OAId);
        return service.getOAInfo(mainInfoEntity, dateInfoEntities, OAId, model, "teacher_", "search");
    }
    //公欠届一括承認モード
    @GetMapping("/teacher/approvalMode")
    public String showTeacherApprovalMode(@RequestParam("teacherType") String teacherType, Model model) {
        TeacherOASearchForm form;
        //送られてきた先生タイプによって別々の検索Formを作る。
        if(teacherType.equals("teacher")){
            //公欠届ステータスが「未受理」で、担任チェックが「未承認」のもの
            form = new TeacherOASearchForm(0, "all", List.of("unaccepted"), "false", null, true, List.of("unaccepted"), null);
        }else{
            //公欠届ステータスが「未受理」で、キャリアチェックが「未承認」のもの
            form = new TeacherOASearchForm(0, "all", List.of("unaccepted"), null, "false", true, List.of("unaccepted"), null);
        }
        List<OAListEntity> listEntity = service.teacherFindAllOAs(form, 1, 100);
        if(!listEntity.isEmpty()) {
            List<OAListDTO> listDTO = service.toListEntity(listEntity);
            model.addAttribute("mainList", listDTO);
        }
        model.addAttribute("teacherType", teacherType);

        model.addAttribute("mode", "approval");
        return "teacher_OAList";
    }

    //一括承認ポスト
    @PostMapping("/teacher/approvalMode")
    public String postTeacherApprovalMode(ApprovalForm approval, Model model){
        approval.getApproval().forEach((k,v)->{
            service.updateCheck(Integer.parseInt(k), approval.getTeacherType(), true);
        });
        return showTeacherApprovalMode(approval.getTeacherType(), model);
    }
    //報告書詳細ページ
    @GetMapping("/teacher/report/{reportId}")
    public String showStudentReportInfo(@PathVariable("reportId") Integer reportId, Model model){
        ReportInfoEntity mainInfo = service.getReportInfo(reportId);
        model.addAttribute("mainInfo", mainInfo);
        System.out.println(mainInfo.getReportId());
        System.out.println(mainInfo.getMaxVersion());
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
        model.addAttribute("mode", "teacher");
        return "reportInfo";
    }

    //report承認
    @PutMapping(value="/teacher/report/{reportId}", params = "acceptance")
    public String reportAcceptance(@PathVariable("reportId") Integer reportId) {
        service.updateReportStatus(reportId, "acceptance");
        return "redirect:/jobportal/teacher/OAList";
    }
    //report却下
    @PutMapping(value = "/teacher/report/{reportId}", params = "rejection")
    public String reportUnaccepted(HttpServletRequest request, @PathVariable("reportId") Integer reportId, @RequestParam(value = "reasonForRejection", required = false)String reasonForRejection, @RequestParam("studentEmail") String studentEmail) {
        service.updateReportStatus(reportId, "rejection");
        //mailController.sendMail(sendAddress, studentEmail, reasonForRejection);
        return "redirect:/jobportal/teacher/OAList";
    }

    @GetMapping("/teacher/reportApproval")
    public String reportApprovalMode(Model model){
        model.addAttribute("mode", "reportApproval");
        List<ReportLogEntity> logEntities = service.searchReportLogs("", 1,"unaccepted");
        model.addAttribute("logEntities", logEntities);
        model.addAttribute("approvalForm", new ReportApprovalForm());
        return "reportLogs";
    }

    @PostMapping(value = "/teacher/reportApproval")
    public String postReportApproval(@ModelAttribute("approvalForm") ReportApprovalForm approvalForm, Model model){
        approvalForm.getReportApproval().forEach((k)->{
            service.updateReportStatus(k,"acceptance");
        });
        model.addAttribute("mode", "reportApproval");
        List<ReportLogEntity> logEntities = service.searchReportLogs("", 1,"unaccepted");
        model.addAttribute("logEntities", logEntities);
        return "reportLogs";
    }

}
