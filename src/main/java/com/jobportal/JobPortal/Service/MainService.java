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

import static java.lang.Integer.parseInt;

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
    public List<OAListEntity> teacherFindAllOAs(){
        return repository.teacherFindAllOAs();
    }
    //DateInfo取得
    public List<OADateInfoEntity> findDateInfo(Integer oaId) {return repository.selectInfo(oaId);}
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


    //重複データを排除するために、ListをMapにするメソッド
    public Map<String, List<OALessonsDTO>> toLessonInfoDTO(List<OADateInfoEntity> allInfoDTO) {
        Map<String, List<OALessonsDTO>> map = new TreeMap<>();
        List<OALessonsDTO> allLessonsList = new ArrayList<>();
        LocalDate prevDate = allInfoDTO.getFirst().officialAbsenceDate();
        for (OADateInfoEntity oaAllInfoDTO : allInfoDTO) {
            if (prevDate.toString().equals(oaAllInfoDTO.officialAbsenceDate().toString())) {
                allLessonsList.add(new OALessonsDTO(oaAllInfoDTO.period(), oaAllInfoDTO.lessonName()));
            } else {
                map.put(dateFormat(prevDate), allLessonsList);
                prevDate = oaAllInfoDTO.officialAbsenceDate();
                allLessonsList = new ArrayList<>(List.of(new OALessonsDTO(oaAllInfoDTO.period(), oaAllInfoDTO.lessonName())));
            }
        }
        map.put(dateFormat(prevDate), allLessonsList);
//        map.forEach((k,v)->{
//            System.out.println(k);
//            v.forEach(System.out::println);
//        });
        return map;
    }

    public static String dateFormat(LocalDate date) {
        String yyyy = date.toString().substring(0,4);
        String mm = date.toString().substring(5,7);
        String dd = date.toString().substring(8,10);
        String dow = "";
        Calendar cal = Calendar.getInstance();
        cal.set(parseInt(yyyy), parseInt(mm) - 1, parseInt(dd));
        dow = switch (cal.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY -> "(日)";
            case Calendar.MONDAY -> "(月)";
            case Calendar.TUESDAY -> "(火)";
            case Calendar.WEDNESDAY -> "(水)";
            case Calendar.THURSDAY -> "(木)";
            case Calendar.FRIDAY -> "(金)";
            case Calendar.SATURDAY -> "(土)";
            default -> dow;
        };
        return yyyy + "年" + mm + "月" + dd + "日" + dow;
    }

    public OAMainInfoEntity findMainInfo(Integer oaId) {
        return  repository.selectMainInfo(oaId);
    }

    public JobSearchEntity findJobSearchInfo(Integer oaId) {
        return repository.selectJobSearchInfo(oaId);
    }
    public SeminarEntity findSeminarInfo(Integer oaId) {
        return repository.selectSeminarInfo(oaId);
    }
    public BereavementEntity findBereavementInfo(Integer oaId) {
        return repository.selectBereavementInfo(oaId);
    }
    public AttendanceBanEntity findAttendanceBanInfo(Integer oaId) {
        return repository.selectAttendanceBanInfo(oaId);
    }
    public OtherEntity findOtherInfo(Integer oaId) {
        return repository.selectOtherInfo(oaId);
    }

    public List<OAListDTO> toListEntity(List<OAListEntity> listEntity) {
        List<OAListDTO> listDTO = new ArrayList<>();
        List<Integer> lessons = new ArrayList<>();
        Integer prevId = listEntity.getFirst().officialAbsenceId();
        final LocalDate currentDate = LocalDate.now();
        int index = 0;
        int i = 0;
        for(var list : listEntity){
            if(listEntity.get(index).officialAbsenceId().equals(list.officialAbsenceId())){
                lessons.add(list.period());
            }else {
                listDTO.add(new OAListDTO(
                        listEntity.get(index).officialAbsenceId(),
                        listEntity.get(index).studentId(),
                        existsReport(listEntity.get(index).status()),
                        listEntity.get(index).reason().getJapaneseName(),
                        existsReport(listEntity.get(index).reportStatus()),
                        dateFormat(listEntity.get(index).startDate()),
                        dateFormat(listEntity.get(index).endDate()),
                        listEntity.get(index).endDate().isBefore(currentDate) || listEntity.get(index).endDate().isEqual(currentDate),
                        lessons
                        ));
                lessons = new ArrayList<>(List.of(list.period()));
                index = i;
            }
            i++;
        }
        listDTO.add(new OAListDTO(
                listEntity.get(index).officialAbsenceId(),
                listEntity.get(index).studentId(),
                existsReport(listEntity.get(index).status()),
                listEntity.get(index).reason().getJapaneseName(),
                existsReport(listEntity.get(index).reportStatus()),
                dateFormat(listEntity.get(index).startDate()),
                dateFormat(listEntity.get(index).endDate()),
                listEntity.get(index).endDate().isBefore(currentDate) || listEntity.get(index).endDate().isEqual(currentDate),
                lessons
        ));
        return listDTO;
    }

    public String existsReport(OAStatus status) {
        if(status == null){
            return "未提出";
        }else {
            return status.getJapaneseName();
        }
    }

    public void updateOAStatus(Integer oaId) {
        repository.updateOAStatus(oaId);
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
