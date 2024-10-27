package com.jobportal.JobPortal.Repository;

import com.jobportal.JobPortal.Controller.exampleForm;
import com.jobportal.JobPortal.Service.OAMainEntity;
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

    @Insert("""
            INSERT INTO official_absences (
                student_id,
                submission_date,
                job_search_flag,
                teacher_check,
                career_check,
                status,
                reason
            ) VALUES (
                #{id},
                #{entity.submissionDate},
                #{entity.jobSearchFlag},
                #{entity.teacherCheck},
                #{entity.careerCheck},
                #{entity.status},
                #{entity.reason}
            );
    """)
    void insertMainOA(@Param("entity")OAMainEntity entity,@Param("id") Integer id);
}


