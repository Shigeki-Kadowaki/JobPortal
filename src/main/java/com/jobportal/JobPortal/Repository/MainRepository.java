package com.jobportal.JobPortal.Repository;

import com.jobportal.JobPortal.Controller.OtherEntity;
import com.jobportal.JobPortal.Service.*;
import org.apache.ibatis.annotations.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Mapper
public interface MainRepository {


//テスト
//    @Insert("INSERT INTO test (title, body) VALUES (#{form.title},#{form.body} ); ")
//    void insert(@Param("form") exampleForm form);


    //該当生徒OAList
    @Select("""
            SELECT * FROM official_absences
            WHERE student_id = #{id};
    """)
    List<OAMainEntity> selectAll(@Param("id") Integer studentId);
    //該当生徒OAInfo
    @Select("""
            SELECT official_absence_id, student_id, submission_date, status, reason, official_absence_dates.period, lesson_name, day_of_week FROM official_absences
            INNER JOIN official_absence_dates
            USING (official_absence_id)
            INNER JOIN lessons
            USING (lesson_id)
            WHERE student_id = #{studentId}
            AND official_absence_id = #{oaId};
    """)
    List<OAInfoDTO> selectInfo(@Param("studentId") Integer studentId, @Param("oaId") Integer oaId);
    //メインOAフォームインサート
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


    @Insert("""
            INSERT INTO job_searches VALUES (
                #{entity.officialAbsenceId},
                #{entity.work},
                #{entity.companyName},
                #{entity.address}
            );

    """)
    void insertJobSearch(@Param("entity") JobSearchEntity jobSearchEntity);

    @Insert("""
            INSERT INTO seminars VALUES (
                #{entity.officialAbsenceId},
                #{entity.seminarName},
                #{entity.location},
                #{entity.venueName}
            );
    """)
    void insertSeminar(@Param("entity") SeminarEntity seminarEntity);


    @Insert("""
            INSERT INTO bereavements VALUES (
                #{entity.officialAbsenceId},
                #{entity.deceasedName},
                #{entity.relationship}
            );
    """)
    void insertBereavement(@Param("entity") BereavementEntity bereavementEntity);

    @Insert("""
            INSERT INTO attendance_bans VALUES (
                #{entity.officialAbsenceId},
                #{entity.banReason}
            );
    """)
    void insertAttendanceBan(@Param("entity") AttendanceBanEntity attendanceBanEntity);

    @Insert("""
            INSERT INTO others VALUES (
                #{entity.officialAbsenceId},
                #{entity.otherReason}
            );
    """)
    void insertOther(@Param("entity") OtherEntity otherEntity);

}