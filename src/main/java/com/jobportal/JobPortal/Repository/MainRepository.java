package com.jobportal.JobPortal.Repository;

import com.jobportal.JobPortal.Service.DTO.OADateInfoDTO;
import com.jobportal.JobPortal.Service.DTO.OAMainInfoDTO;
import com.jobportal.JobPortal.Service.Entity.OtherEntity;
import com.jobportal.JobPortal.Service.DTO.OADateInfoDTO;
import com.jobportal.JobPortal.Service.DTO.OAResonInfoDTO;
import com.jobportal.JobPortal.Service.Entity.*;
import org.apache.ibatis.annotations.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Mapper
public interface MainRepository {


//テスト
//    @Insert("INSERT INTO test (title, body) VALUES (#{form.title},#{form.body} ); ")
//    void insert(@Param("form") exampleForm form);


    //該当生徒OAList
    @Select("""
            SELECT
            	official_absence_id,
            	student_id,
            	official_absences.status,
            	reason,
            	submitted_flag,
            	reports.status,
                MIN(official_absence_date) AS startDate,
                MAX(official_absence_date) AS endDate
            FROM official_absences
            RIGHT OUTER JOIN official_absence_dates
            USING (official_absence_id)
            LEFT OUTER JOIN reports
            USING (official_absence_id)
            WHERE student_id = #{studentId}
            GROUP BY official_absence_id,student_id,official_absences.status,reason,submitted_flag,reports.status
            ORDER BY official_absence_id;
    """)
    List<OAListEntity> selectAll(@Param("studentId") Integer studentId);
    //共通部分Info
    @Select("""
            SELECT DISTINCT official_absence_id,
                            student_id,
                            submission_date,
                            status,
                            reason,
                            submitted_flag
            FROM official_absences
            INNER JOIN official_absence_dates
            USING (official_absence_id)
            INNER JOIN lessons
            USING (lesson_id)
            WHERE official_absence_id = #{oaId};
    """)
    OAMainInfoDTO selectMainInfo(@Param("oaId") Integer oaId);
    //日時Info
    @Select("""
            SELECT  official_absence_date,
                    official_absence_dates.period,
                    lesson_name
            FROM official_absence_dates
            INNER JOIN lessons
            USING (lesson_id)
            WHERE official_absence_id = #{oaId}
            ORDER BY official_absence_date, period;
    """)
    List<OADateInfoDTO> selectInfo(@Param("oaId") Integer oaId);
    //OA内容Info
    OAResonInfoDTO selectReasonInfo(Integer oaId);
    //メインOAフォームインサート
    @Insert("""
            INSERT INTO official_absences (
                student_id,
                submission_date,
                job_search_flag,
                teacher_check,
                career_check,
                status,
                reason,
                submitted_flag
            ) VALUES (
                #{entity.studentId},
                #{entity.submissionDate},
                #{entity.jobSearchFlag},
                #{entity.teacherCheck},
                #{entity.careerCheck},
                #{entity.status},
                #{entity.reason},
                #{entity.submittedFlag}
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