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

    @Transactional
    public void createOA(OAMainEntity entity, Integer id){
        repository.insertMainOA(entity, id);
        System.out.println(entity.officialAbsenceId);
    }

    @Transactional
    public void createDate(OADatesEntity entity, Integer id){
        repository.insertOADates(entity, id);
    }

    public List<OAMainEntity> findAllOAs(Integer studentId){
        return repository.selectAll(studentId);
    }



    @Transactional
    public void insert(exampleForm form){
        repository.insert(form);
    }

//    @Transactional
//    public void saveDate(OADatesEntity datesEntity) {
//        datesEntity.OAPeriods().forEach((key, values) -> {
//            System.out.println(key + ": " + values);
//            values.forEach(value->repository.insertOADates(key, value));
//        });
//    }
}
