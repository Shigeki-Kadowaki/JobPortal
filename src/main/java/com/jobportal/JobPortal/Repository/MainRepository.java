package com.jobportal.JobPortal.Repository;

import com.jobportal.JobPortal.Controller.OtherEntity;
import com.jobportal.JobPortal.Service.*;
import org.apache.ibatis.annotations.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Mapper
public interface MainRepository {


//テスト
//    @Insert("INSERT INTO test (title, body) VALUES (#{form.title},#{form.body} ); ")
//    void insert(@Param("form") exampleForm form);


    //該当生徒のOA全取得
    @Select("""
            SELECT * FROM official_absences
            WHERE student_id = #{id};
    """)
    List<OAMainEntity> selectAll(@Param("id") Integer studentId);


//メインOAフォームインサート

    @Transactional
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
                #{entity.studentId},
                #{entity.submissionDate},
                #{entity.jobSearchFlag},
                #{entity.teacherCheck},
                #{entity.careerCheck},
                #{entity.status},
                #{entity.reason}
            );
    """)
    @Options(useGeneratedKeys = true, keyProperty = "entity.officialAbsenceId")
    void insertMainOA(@Param("entity") OAMainEntity entity);

    //日時インサート
    @Transactional
    @Insert("""
        <script>
            INSERT INTO official_absence_dates
            VALUES
            <foreach collection='dateList' item='date' separator=','>
                (#{officialAbsenceId}, null, #{date.OAPeriod}, #{date.OADate})
            </foreach>
        </script>
    """)
    void insertOADates(@Param("dateList") List<OADatesEntity> dateList, @Param("officialAbsenceId") Integer officialAbsenceId);


    @Transactional
    @Insert("""
            INSERT INTO job_searches VALUES (
                #{entity.officialAbsenceId},
                #{entity.work},
                #{entity.companyName},
                #{entity.address}
            );

    """)
    void insertJobSearch(@Param("entity") JobSearchEntity jobSearchEntity);

    @Transactional
    @Insert("""
            INSERT INTO seminars VALUES (
                #{entity.officialAbsenceId},
                #{entity.seminarName},
                #{entity.location},
                #{entity.venueName}
            );
    """)
    void insertSeminar(@Param("entity") SeminarEntity seminarEntity);


    @Transactional
    @Insert("""
            INSERT INTO bereavements VALUES (
                #{entity.officialAbsenceId},
                #{entity.deceasedName},
                #{entity.relationship}
            );
    """)
    void insertBereavement(@Param("entity") BereavementEntity bereavementEntity);

    @Transactional
    @Insert("""
            INSERT INTO attendance_bans VALUES (
                #{entity.officialAbsenceId},
                #{entity.banReason}
            );
    """)
    void insertAttendanceBan(@Param("entity") AttendanceBanEntity attendanceBanEntity);

    @Transactional
    @Insert("""
            INSERT INTO others VALUES (
                #{entity.officialAbsenceId},
                #{entity.otherReason}
            );
    """)
    void insertOther(@Param("entity") OtherEntity otherEntity);
}