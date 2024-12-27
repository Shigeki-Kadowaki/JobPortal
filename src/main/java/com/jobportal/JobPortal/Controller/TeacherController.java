package com.jobportal.JobPortal.Controller;

import com.jobportal.JobPortal.Controller.Form.*;
import com.jobportal.JobPortal.Service.DTO.ExceptionDateDTO;
import com.jobportal.JobPortal.Service.Entity.ExceptionDateEntity;
import com.jobportal.JobPortal.Service.Entity.OADateInfoEntity;
import com.jobportal.JobPortal.Service.Entity.OAMainInfoEntity;
import com.jobportal.JobPortal.Service.Entity.TimeTableEntity;
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

    public final String sendAddress = "40104kk@saisen.ac.jp";
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
    public String showTeacherOAList(TeacherOASearchForm form, Model model,@ModelAttribute("page") @RequestParam(defaultValue = "0", value = "page") Integer page) {
        if(session.getAttribute("teacherSearchForm") != null) {
            form = (TeacherOASearchForm) session.getAttribute("teacherSearchForm");
        }
        return service.getTeacherOAList(page, form, model, session);
    }
    //OAList検索
    @GetMapping(value = "/teacher/OAList", params = "search")
    public String showTeacherOAListSearch(TeacherOASearchForm form, Model model,@ModelAttribute("page") @RequestParam(defaultValue = "0", value = "page") Integer page){
        session.setAttribute("teacherSearchForm", form);
        return service.getTeacherOAList(page, form, model, session);
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
    public String OAAccepted(@PathVariable("OAId") Integer OAId, @RequestParam(value = "reportRequired", required = false)String reportRequired, @RequestParam("teacherType") String teacherType, @RequestParam("careerCheckRequired") boolean careerCheckRequired) {
        service.updateCheck(OAId, teacherType, true);
        if(careerCheckRequired) {
            if(service.checkConditionJudge(OAId, true)){service.updateOAStatus(OAId, "acceptance");}
        }else{
            if(service.checkConditionJudge(OAId, false)){service.updateOAStatus(OAId, "acceptance");}
        }
        service.updateReportRequired(OAId, reportRequired != null);
        return "redirect:/jobportal/teacher/OAList";
    }
    //OA却下
    @PutMapping(value = "/teacher/{OAId}", params = "rejection")
    public String OAUnaccepted(HttpServletRequest request, @PathVariable("OAId") Integer OAId, @RequestParam(value = "reasonForRejection", required = false)String reasonForRejection, @RequestParam("studentEmail") String studentEmail) {
        service.updateOAStatus(OAId,"rejection");
        System.out.println(reasonForRejection);
        //mailController.sendMail(sendAddress, studentEmail, reasonForRejection);
        return "redirect:/jobportal/teacher/OAList";
    }

    //授業区分入力フォーム
    @GetMapping("/teacher/classificationForm")
    public String showScheduleClassification(@ModelAttribute("classification") ClassificationForm classification, Model model) {
        List<String> courses = service.getCourses();
        model.addAttribute("courses", courses);
        return "classificationForm";
    }
    //授業区分ポスト、授業区分別時間割入力フォーム
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
    //例外時間割
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
            System.out.println("error");
            return "exceptionDate";
        }
        service.createExceptionDate(exceptionDate.toLocalDate());
        System.out.println("success");
        return "redirect:/jobportal/teacher/exceptionDate";
    }

    @DeleteMapping ("/teacher/exceptionDate")
    public String deleteExceptionDate(@RequestParam("id") Integer id) {
        System.out.println(id);
        System.out.println("delete");
        service.deleteExceptionDate(id);
        return "redirect:/jobportal/teacher/exceptionDate";
    }

    @GetMapping("/teacher/studentSearch")
    public String showStudentSearch(@ModelAttribute("studentId") @RequestParam(value = "studentId", required = false) Integer studentId, Model model) {
        model.addAttribute("studentId", studentId);
        System.out.println(studentId);
        service.getStudentOAList(studentId, new StudentOASearchForm(null, null), model);
        DesiredOccupation desiredOccupation = service.getOccupation(studentId);
        model.addAttribute("desiredOccupation", desiredOccupation);
        return "studentSearch";
    }

    //OA詳細
    @GetMapping("/teacher/studentSearch/{OAId}")
    public String showSearchStudentOAInfo(@PathVariable("OAId") Integer OAId, Model model) {
        OAMainInfoEntity mainInfoEntity = service.findMainInfo(OAId);
        List<OADateInfoEntity> dateInfoEntities = service.findDateInfo(OAId);
        return service.getOAInfo(mainInfoEntity, dateInfoEntities, OAId, model, "teacher_", "search");
    }
}
