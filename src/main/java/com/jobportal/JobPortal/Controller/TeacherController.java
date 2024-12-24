package com.jobportal.JobPortal.Controller;

import com.jobportal.JobPortal.Controller.Form.ClassificationForm;
import com.jobportal.JobPortal.Controller.Form.ExceptionDateForm;
import com.jobportal.JobPortal.Controller.Form.TeacherOASearchForm;
import com.jobportal.JobPortal.Controller.Form.TimeTableInfoForm;
import com.jobportal.JobPortal.Service.DTO.ExceptionDateDTO;
import com.jobportal.JobPortal.Service.DTO.OALessonsDTO;
import com.jobportal.JobPortal.Service.DTO.OAListDTO;
import com.jobportal.JobPortal.Service.DTO.OAMainInfoDTO;
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

    public final String sendAddress = "40104kk@saisen.ac.jp";
    final Integer pageSize = 25;
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
        if(session.getAttribute("searchForm") != null) {
            form = (TeacherOASearchForm) session.getAttribute("searchForm");
        }
        if(page == 0){
            page = 1;
            if(session.getAttribute("searchForm") == null) {
                session.setAttribute("page", page);
            }else{
                page = (Integer) session.getAttribute("page");
            }
        }else{
            session.setAttribute("page", page);
        }
        List<OAListEntity> listEntity = service.teacherFindAllOAs(form, page, pageSize);
        if(!listEntity.isEmpty()) {
            List<OAListDTO> listDTO = service.toListEntity(listEntity);
            model.addAttribute("mainList", listDTO);
        }
        Integer size = service.countOA();
        model.addAttribute("size", size);
        model.addAttribute("maxSize", (int)Math.ceil((double) size / pageSize));
        model.addAttribute("searchForm", form);
        model.addAttribute("colors", colors);
        model.addAttribute("page", page);
        System.out.println((int)Math.ceil((double) size / pageSize));
        return "teacher_OAList";
    }
    //OAList検索
    @GetMapping(value = "/teacher/OAList", params = "search")
    public String showTeacherOAListSearch(TeacherOASearchForm form, Model model,@ModelAttribute("page") @RequestParam(defaultValue = "0", value = "page") Integer page){
        session.setAttribute("searchForm", form);
        if(page == 0){
            page = 1;
            if(session.getAttribute("searchForm") == null) {
                session.setAttribute("page", page);
            }else{
                page = (Integer) session.getAttribute("page");
            }
        }else{
            session.setAttribute("page", page);
        }
        List<OAListEntity> listEntity = service.teacherFindAllOAs(form, page, pageSize);
        if(!listEntity.isEmpty()) {
            List<OAListDTO> listDTO = service.toListEntity(listEntity);
            model.addAttribute("mainList", listDTO);
        }
        Integer size = service.countSearchOA(form);
        model.addAttribute("size", size);
        model.addAttribute("maxSize", (int)Math.ceil((double) size / pageSize));
        model.addAttribute("searchForm", form);
        model.addAttribute("colors", colors);
        model.addAttribute("page", page);
        System.out.println((int)Math.ceil((double) size / pageSize));
        return "teacher_OAList";
    }
    //OA詳細
    @GetMapping("/teacher/OAList/{OAId}")
    public String showTeacherOAInfo(@PathVariable("OAId") Integer OAId, Model model) {
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
        return "teacher_OAInfo";
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
        System.out.println(exceptionDateDTO);
        model.addAttribute("exceptionDateList", exceptionDateDTO);
        return "exceptionDate";
    }
    //例外時間割ポスト
    @PostMapping("/teacher/exceptionDate/post")
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
    @GetMapping("/teacher/exceptionDate/delete/id")
    public String deleteExceptionDate(@RequestParam("id") Integer id) {
        System.out.println(id);
        service.deleteExceptionDate(id);
        return "redirect:/jobportal/teacher/exceptionDate";
    }
}
