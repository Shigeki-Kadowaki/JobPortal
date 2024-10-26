package com.jobportal.JobPortal.Repository;

import com.jobportal.JobPortal.Controller.exampleForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MainRepository extends JpaRepository<exampleForm, Integer> {

//    @Insert("""
//
//
//
//
//            """)
//    void insert(@Param("entity") OAMainEntity entity) ;

//    @Insert("insert into example(title, body) values (#{form.title}, #{form.body}) ")
//    void insert(@Param("form") exampleForm form);
}


