package com.jobportal.JobPortal.Controller;

import com.jobportal.JobPortal.Controller.Form.TeacherOASearchForm;
import com.jobportal.JobPortal.Service.DTO.OALessonsDTO;
import com.jobportal.JobPortal.Service.DTO.OAListDTO;
import com.jobportal.JobPortal.Service.DTO.OAMainInfoDTO;
import com.jobportal.JobPortal.Service.Entity.*;
import com.jobportal.JobPortal.Service.MainService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/jobportal")
public class TeacherController {

    @Autowired
    public MainService service;
    @Autowired
    public HttpSession session;

    @GetMapping("/teacher")
    public String showTeacherPage() {
        return "teacher";
    }

    //OAList
    @GetMapping("/teacher/OAList")
    public String showTeacherOAList(TeacherOASearchForm form, Model model) {
        if(session.getAttribute("searchForm") != null) {
            form = (TeacherOASearchForm) session.getAttribute("searchForm");
        }
        Map<String, String> colors = new HashMap<>();
        colors.put("受理","list-group-item-success");
        colors.put("未受理","list-group-item-warning");
        colors.put("却下","list-group-item-danger");
        colors.put("未提出","list-group-item-dark");
        colors.put("不要","list-group-item-light");
        List<OAListEntity> listEntity = service.teacherFindAllOAs(form);
        if(!listEntity.isEmpty()) {
            List<OAListDTO> listDTO = service.toListEntity(listEntity);
            model.addAttribute("mainList", listDTO);
        }
        model.addAttribute("searchForm", form);
        model.addAttribute("colors", colors);
        return "teacher_OAList";
    }
    //OAList検索
    @GetMapping(value = "/teacher/OAList", params = "search")
    public String showTeacherOAListSearch(TeacherOASearchForm form, Model model){
        session.setAttribute("searchForm", form);
        Map<String, String> colors = new HashMap<>();
        colors.put("受理","list-group-item-success");
        colors.put("未受理","list-group-item-warning");
        colors.put("却下","list-group-item-danger");
        colors.put("未提出","list-group-item-dark");
        colors.put("不要","list-group-item-light");
        List<OAListEntity> listEntity = service.teacherFindAllOAs(form);
        if(!listEntity.isEmpty()) {
            List<OAListDTO> listDTO = service.toListEntity(listEntity);
            model.addAttribute("mainList", listDTO);
        }
        model.addAttribute("searchForm", form);
        model.addAttribute("colors", colors);
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
    public String OAUnaccepted(@PathVariable("OAId") Integer OAId,@RequestParam(value = "reportRequired", required = false)String reasonForRejection) {
        service.updateOAStatus(OAId,"rejection");
        System.out.println(reasonForRejection);
        return "redirect:/jobportal/teacher/OAList";
    }

    @GetMapping("/teacher/classification")
    public String showScheduleClassification() {
        return "classification";
    }
    @PostMapping("/teacher/classification")
    public String postScheduleClassification(@Validated Classification classification){
        List<String> lessons = service.getLessons(classification);
        return "scheduleForm";
    }
}
