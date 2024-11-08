package com.jobportal.JobPortal.Controller;

import com.jobportal.JobPortal.Service.DTO.OALessonsDTO;
import com.jobportal.JobPortal.Service.DTO.OAListDTO;
import com.jobportal.JobPortal.Service.DTO.OAMainInfoDTO;
import com.jobportal.JobPortal.Service.Entity.*;
import com.jobportal.JobPortal.Service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/jobportal")
public class TeacherController {

    public final MainService service;

    @GetMapping("/teacher")
    public String showTeacherPage() {
        return "teacher";
    }
    @GetMapping("/teacher/OAList")
    public String showTeacherOAList(Model model) {
        Map<String, String> colors = new HashMap<>();
        colors.put("受理","list-group-item-success");
        colors.put("未受理","list-group-item-warning");
        colors.put("却下","list-group-item-danger");
        colors.put("未提出","list-group-item-dark");
        List<OAListEntity> listEntity = service.teacherFindAllOAs();
        if(!listEntity.isEmpty()) {
            List<OAListDTO> listDTO = service.toListEntity(listEntity);
            model.addAttribute("mainList", listDTO);
        }
        model.addAttribute("colors", colors);
        return "teacher_OAList";
    }
    //
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
    @PostMapping("teacher/OAList/{OAId}/accepted")
    public String teacherTest(@PathVariable("OAId") Integer OAId, Model model) {
        service.updateOAStatus(OAId);
        return showTeacherOAList(model);
    }
}
