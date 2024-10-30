package com.jobportal.JobPortal.Service;

import com.jobportal.JobPortal.Controller.exampleForm;
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

    //日付作成
    @Transactional
    public void createOADates(List<OADatesEntity> entity, Integer officialAbsenceId){
        repository.insertOADates(entity, officialAbsenceId);
    }


    //該当生徒OA取得
    public List<OAMainEntity> findAllOAs(Integer studentId){
        return repository.selectAll(studentId);
    }


    //就活情報登録
    public void createJobSearch(JobSearchEntity jobSearchEntity) {
        repository.insertJobSearch(jobSearchEntity);
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
