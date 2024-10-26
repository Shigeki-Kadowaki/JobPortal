package com.jobportal.JobPortal.Repository;

import com.jobportal.JobPortal.Controller.exampleForm;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface MainRepository {

//    @Insert("""
//
//
//
//
//            """)
//    void insert(@Param("entity") OAMainEntity entity) ;

    @Insert("INSERT INTO test (title, body) VALUES (#{form.title},#{form.body} ); ")
    void insert(@Param("form") exampleForm form);
}


