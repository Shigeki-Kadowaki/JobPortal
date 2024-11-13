package com.jobportal.JobPortal.Service;

import com.jobportal.JobPortal.Controller.Form.OAMainForm;
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
    public void createOA(OAMainEntity entity){repository.insertMainOA(entity);
    }
    @Transactional
    public void createOADates(List<OADatesEntity> entity, Integer officialAbsenceId){
        repository.insertOADates(entity, officialAbsenceId);
    }
    @Transactional
    public void createJobSearch(JobSearchEntity jobSearchEntity) {repository.insertJobSearch(jobSearchEntity);
    }
    @Transactional
    public void createSeminar(SeminarEntity seminarEntity) {repository.insertSeminar(seminarEntity);
    }
    @Transactional
    public void createBereavement(BereavementEntity bereavementEntity) {
        repository.insertBereavement(bereavementEntity);
    }
    @Transactional
    public void createAttendanceBan(AttendanceBanEntity attendanceBanEntity) {
        repository.insertAttendanceBan(attendanceBanEntity);
    }
    @Transactional
    public void createOther(OtherEntity otherEntity) {repository.insertOther(otherEntity);
    }
    @Transactional
    public void createSubmitted(Integer officialAbsenceId) {
        repository.createSubmittedDate(officialAbsenceId,LocalDate.now());
    }
    //List取得
    public List<OAListEntity> findAllOAs(Integer studentId){return repository.selectAll(studentId);
    }
    public List<OAListEntity> teacherFindAllOAs(){return repository.teacherFindAllOAs();
    }
    //info取得
    public OAMainInfoEntity findMainInfo(Integer oaId) {return  repository.selectMainInfo(oaId);
    }
    public List<OADateInfoEntity> findDateInfo(Integer oaId) {return repository.selectDateInfo(oaId);
    }
    public JobSearchEntity findJobSearchInfo(Integer oaId) {return repository.selectJobSearchInfo(oaId);
    }
    public SeminarEntity findSeminarInfo(Integer oaId) {return repository.selectSeminarInfo(oaId);
    }
    public BereavementEntity findBereavementInfo(Integer oaId) {return repository.selectBereavementInfo(oaId);
    }
    public AttendanceBanEntity findAttendanceBanInfo(Integer oaId) {return repository.selectAttendanceBanInfo(oaId);
    }
    public OtherEntity findOtherInfo(Integer oaId) {return repository.selectOtherInfo(oaId);
    }
    //過去versionInfo取得
    public OAMainInfoEntity findMainInfoByVersion(Integer oaId, Integer version) {return  repository.selectMainInfoByVersion(oaId, version);
    }
    public List<OADateInfoEntity> findDateInfoByVersion(Integer oaId, Integer version) {return repository.selectDateInfoByVersion(oaId, version);
    }
    public JobSearchEntity findJobSearchInfoByVersion(Integer oaId, Integer version) {return repository.selectJobSearchInfoByVersion(oaId, version);
    }
    public SeminarEntity findSeminarInfoByVersion(Integer oaId, Integer version) {return repository.selectSeminarInfoByVersion(oaId, version);
    }
    public BereavementEntity findBereavementInfoByVersion(Integer oaId, Integer version) {return repository.selectBereavementInfoByVersion(oaId, version);
    }
    public AttendanceBanEntity findAttendanceBanInfoByVersion(Integer oaId, Integer version) {return repository.selectAttendanceBanInfoByVersion(oaId, version);
    }
    public OtherEntity findOtherInfoByVersion(Integer oaId, Integer version) {return repository.selectOtherInfoByVersion(oaId, version);
    }
    //削除
    @Transactional
    public void deleteDate(Integer OAId) {repository.deleteDate(OAId);
    }
    @Transactional
    public void deleteMain(Integer OAId) {repository.deleteMain(OAId);
    }
    @Transactional
    public void deleteJobSearch(Integer OAId) {repository.deleteJobSearch(OAId);
    }
    @Transactional
    public void deleteSeminar(Integer OAId) {repository.deleteSeminar(OAId);
    }
    @Transactional
    public void deleteBereavement(Integer OAId) {repository.deleteBereavement(OAId);
    }
    @Transactional
    public void deleteAttendanceBan(Integer OAId) {repository.deleteAttendanceBan(OAId);
    }
    @Transactional
    public void deleteOther(Integer OAId) {repository.deleteOther(OAId);
    }
    @Transactional
    public void deleteSubmittedDate(Integer OAId) {repository.deleteSubmittedDate(OAId);
    }
    //再提出
    @Transactional
    public void updateSubmittedDate(Integer OAId) {repository.updateSubmittedDate(OAId, LocalDate.now());
    }
    @Transactional
    public void updateOADates(List<OADatesEntity> dateList, Integer OAId) {repository.updateOADates(dateList, OAId);
    }
    @Transactional
    public void updateJobSearch(JobSearchEntity jobEntity) {repository.updateJobSearch(jobEntity);
    }
    @Transactional
    public void updateSeminar(SeminarEntity seminar) {repository.updateSeminar(seminar);
    }
    @Transactional
    public void updateBereavement(BereavementEntity bereavement) {repository.updateBereavement(bereavement);
    }
    @Transactional
    public void updateAttendanceBan(AttendanceBanEntity attendanceBan) {
        repository.updateAttendanceBan(attendanceBan);
    }
    @Transactional
    public void updateOther(OtherEntity other) {repository.updateOther(other);
    }
    //ステータス更新
    @Transactional
    public void updateOAStatus(Integer OAId, String status) {repository.updateOAStatus(OAId, status);
    }
    @Transactional
    public void updateReportRequired(Integer OAId, boolean flag) {repository.updateReportRequired(OAId, flag);
    }
    @Transactional
    public void updateCheck(Integer OAId, String type, boolean check) {repository.updateCheck(OAId, type, check);
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
    //公欠授業をリスト化
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
                        listEntity.get(index).reportRequired(),
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
                listEntity.get(index).reportRequired(),
                dateFormat(listEntity.get(index).startDate()),
                dateFormat(listEntity.get(index).endDate()),
                listEntity.get(index).endDate().isBefore(currentDate) || listEntity.get(index).endDate().isEqual(currentDate),
                lessons
        ));
        return listDTO;
    }
    //yyyy-mm-ddをyyyy年mm月dd日(曜日)にする
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
    //statusを日本語化
    public static String existsReport(OAStatus status) {
        if(status == null){
            return "未提出";
        }else {
            return status.getJapaneseName();
        }
    }
    public OAMainForm toJobSearchForm(OAMainInfoDTO mainInfoDTO, Map<String, List<OALessonsDTO>> lessonInfoEntities, JobSearchEntity jobSearch) {
        Map<String, List<String>> map = new HashMap<>();
        lessonInfoEntities.forEach((k,v)->{
            List<String> l = new ArrayList<>();
            v.forEach(e->{
                l.add(e.toString());
            });
            map.put(k, l);
        });
        return new OAMainForm(
            jobSearch,
            mainInfoDTO.reason(),
            map,
            mainInfoDTO.reportRequired(),
            jobSearch.work().toString(),
            jobSearch.companyName(),
            jobSearch.address(),
            jobSearch.remarks(),
            jobSearch.visitStartHour(),
            jobSearch.visitStartMinute()
        );
    }
    public OAMainForm toSeminarForm(OAMainInfoDTO mainInfoDTO, Map<String, List<OALessonsDTO>> lessonInfoEntities, SeminarEntity seminar) {
        Map<String, List<String>> map = new HashMap<>();
        lessonInfoEntities.forEach((k,v)->{
            List<String> l = new ArrayList<>();
            v.forEach(e->{
                l.add(e.toString());
            });
            map.put(k, l);
        });
        return new OAMainForm(
                seminar,
                mainInfoDTO.reason(),
                map,
                mainInfoDTO.reportRequired(),
                seminar.seminarName(),
                seminar.location(),
                seminar.venueName(),
                seminar.remarks(),
                seminar.visitStartHour(),
                seminar.visitStartMinute()
        );
    }
    public OAMainForm toBereavementForm(OAMainInfoDTO mainInfoDTO, Map<String, List<OALessonsDTO>> lessonInfoEntities, BereavementEntity bereavement) {
        Map<String, List<String>> map = new HashMap<>();
        lessonInfoEntities.forEach((k,v)->{
            List<String> l = new ArrayList<>();
            v.forEach(e->{
                l.add(e.toString());
            });
            map.put(k, l);
        });
        return new OAMainForm(
                bereavement,
                mainInfoDTO.reason(),
                map,
                mainInfoDTO.reportRequired(),
                bereavement.remarks(),
                bereavement.deceasedName(),
                bereavement.relationship()
        );
    }
    public OAMainForm toAttendanceBanForm(OAMainInfoDTO mainInfoDTO, Map<String, List<OALessonsDTO>> lessonInfoEntities, AttendanceBanEntity ban) {
        Map<String, List<String>> map = new HashMap<>();
        lessonInfoEntities.forEach((k,v)->{
            List<String> l = new ArrayList<>();
            v.forEach(e->{
                l.add(e.toString());
            });
            map.put(k, l);
        });
        return new OAMainForm(
                ban,
                mainInfoDTO.reason(),
                map,
                mainInfoDTO.reportRequired(),
                ban.banReason(),
                ban.remarks()
        );
    }
    public OAMainForm toOtherForm(OAMainInfoDTO mainInfoDTO, Map<String, List<OALessonsDTO>> lessonInfoEntities, OtherEntity other) {
        Map<String, List<String>> map = new HashMap<>();
        lessonInfoEntities.forEach((k,v)->{
            List<String> l = new ArrayList<>();
            v.forEach(e->{
                l.add(e.toString());
            });
            map.put(k, l);
        });
        return new OAMainForm(
                other,
                mainInfoDTO.reason(),
                map,
                mainInfoDTO.reportRequired(),
                other.otherReason(),
                other.remarks()
        );
    }
    public boolean checkConditionJudge(Integer OAId, boolean type) {
        if(type){
            return repository.teacherCheckCondition(OAId) && repository.careerCheckCondition(OAId);
        }else{
            return repository.teacherCheckCondition(OAId);
        }
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
