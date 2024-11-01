package com.jobportal.JobPortal.Service;

import com.jobportal.JobPortal.Controller.OtherEntity;
import com.jobportal.JobPortal.Repository.MainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public List<OAMainEntity> findAllOAs(Integer studentId){
        return repository.selectAll(studentId);
    }
    //該当生徒該当OA取得
    public List<OAInfoDTO> findOAInfo(Integer studentId, Integer oaId) {return repository.selectInfo(studentId, oaId);}
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
