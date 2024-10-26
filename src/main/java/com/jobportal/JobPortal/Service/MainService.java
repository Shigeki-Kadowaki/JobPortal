package com.jobportal.JobPortal.Service;

import com.jobportal.JobPortal.Controller.exampleForm;
import com.jobportal.JobPortal.Repository.MainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MainService {
    @Autowired
    private final MainRepository repository;

    public void createOA(OAMainEntity entity){
//        repository.insert(entity);
    }


    public void insert(exampleForm form){
        repository.insert(form);
    }
}
