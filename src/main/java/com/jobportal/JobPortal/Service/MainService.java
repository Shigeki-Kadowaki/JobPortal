package com.jobportal.JobPortal.Service;

import com.jobportal.JobPortal.Service.DTO.*;
import com.jobportal.JobPortal.Service.Entity.OtherEntity;
import com.jobportal.JobPortal.Repository.MainRepository;
import com.jobportal.JobPortal.Service.Entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MainService {

    private final MainRepository repository;


    //OA作成
    @Transactional
    public void createOA(OAMainEntity entity){
        repository.insertMainOA(entity);
    }
    //日付登録
    @Transactional
    public void createOADates(List<OADatesEntity> entity, Integer officialAbsenceId){repository.insertOADates(entity, officialAbsenceId);}
    //該当生徒OAList取得
    public List<OAListEntity> findAllOAs(Integer studentId){
        return repository.selectAll(studentId);
    }
    //DateInfo取得
    public List<OADateInfoDTO> findDateInfo(Integer oaId) {return repository.selectInfo(oaId);}
    //就活情報登録
    @Transactional
    public void createJobSearch(JobSearchEntity jobSearchEntity) {
        repository.insertJobSearch(jobSearchEntity);
    }
    //セミナー情報登録
    @Transactional
    public void createSeminar(SeminarEntity seminarEntity) {
        repository.insertSeminar(seminarEntity);
    }
    //忌引情報登録
    @Transactional
    public void createBereavement(BereavementEntity bereavementEntity) {repository.insertBereavement(bereavementEntity);}
    //出席停止情報登録
    @Transactional
    public void createAttendanceBan(AttendanceBanEntity attendanceBanEntity) {repository.insertAttendanceBan(attendanceBanEntity);}
    //その他情報登録
    @Transactional
    public void createOther(OtherEntity otherEntity) {
        repository.insertOther(otherEntity);
    }

    public OAResonInfoDTO findOAReasonInfo(Integer oaId) {
        return repository.selectReasonInfo(oaId);
    }

    //重複データを排除するために、ListをMapにするメソッド
    public Map<LocalDate, List<OALessonsDTO>> toLessonInfoDTO(List<OADateInfoDTO> allInfoDTO) {
        Map<LocalDate, List<OALessonsDTO>> map = new TreeMap<>();
        List<OALessonsDTO> allLessonsList = new ArrayList<>();
        LocalDate prevDate = allInfoDTO.getFirst().officialAbsenceDate();
        for (OADateInfoDTO oaAllInfoDTO : allInfoDTO) {
            if (prevDate.toString().equals(oaAllInfoDTO.officialAbsenceDate().toString())) {
                allLessonsList.add(new OALessonsDTO(oaAllInfoDTO.period(), oaAllInfoDTO.lessonName()));
            } else {
                map.put(prevDate, allLessonsList);
                prevDate = oaAllInfoDTO.officialAbsenceDate();
                allLessonsList = new ArrayList<>(List.of(new OALessonsDTO(oaAllInfoDTO.period(), oaAllInfoDTO.lessonName())));
            }
        }
        map.put(prevDate, allLessonsList);
        map.forEach((k,v)->{
            System.out.println(k);
            v.forEach(System.out::println);
        });
        return map;
    }

    public OAMainInfoDTO findMainInfo(Integer oaId) {
        return  repository.selectMainInfo(oaId);
    }


//    public Map<LocalDate, List<Integer>> toLessonList(List<OAListDTO> list) {
//        Map<LocalDate, List<Integer>> map = new TreeMap<>();
//        List<Integer> lessonList = new ArrayList<>();
//        LocalDate prevDate = list.getFirst().officialAbsenceDate();
//        for (OAListDTO oaListDTO : list) {
//            if(prevDate.toString().equals(oaListDTO.officialAbsenceDate().toString())){
//                lessonList.add(oaListDTO.period());
//            }else {
//                map.put(prevDate, lessonList);
//                prevDate = oaListDTO.officialAbsenceDate();
//                lessonList = new ArrayList<>(List.of(oaListDTO.period()));
//            }
//        }
//        map.put(prevDate, lessonList);
//        return map;
//    }

//    public List<OAMainListDTO> toMainList(List<OAListEntity> list) {
//        return null;
//    }


//    @Transactional
//    public void insertOADates(exampleForm form){
//        repository.insert(form);
//    }

//    @Transactional
//    public void saveDate(OADatesEntity datesEntity) {
//        datesEntity.OAPeriods().forEach((key, values) -> {
//            System.out.println(key + ": " + values);
//            values.forEach(value->repository.insertOADates(key, value));
//        });
//    }
}
