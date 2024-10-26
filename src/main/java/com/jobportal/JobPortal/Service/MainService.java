package com.jobportal.JobPortal.Service;

import com.jobportal.JobPortal.Repository.MainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MainService {
    private final MainRepository repository;

    public void createOA(OAMainEntity entity){
        repository.insert(entity);
    }
}
