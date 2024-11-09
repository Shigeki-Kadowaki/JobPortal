package com.jobportal.JobPortal.Repository;

import com.jobportal.JobPortal.Service.Entity.OADateInfoEntity;
import com.jobportal.JobPortal.Service.Entity.OAMainInfoEntity;
import com.jobportal.JobPortal.Service.Entity.OtherEntity;
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
                reports.status AS reportStatus,
            	report_required,
                MIN(official_absence_date) AS startDate,
                MAX(official_absence_date) AS endDate,
                official_absence_date_histories.period
            FROM official_absences
            RIGHT OUTER JOIN official_absence_date_histories
            USING (official_absence_id)
            LEFT OUTER JOIN reports
            USING (official_absence_id)
            WHERE student_id = #{studentId}
            AND (official_absence_id, version) IN (
                SELECT 
                    official_absence_id,
                    MAX(version)
                FROM official_absence_date_histories
                GROUP BY official_absence_id
            )
            GROUP BY official_absence_id,student_id,official_absences.status,reason,reportStatus,official_absence_date_histories.period,report_required
            ORDER BY official_absence_id DESC, period;
    """)
    List<OAListEntity> selectAll(@Param("studentId") Integer studentId);
    //先生側OAList
    @Select("""
            SELECT
                official_absence_id,
                student_id,
                official_absences.status,
                reason,
                reports.status AS reportStatus,
                MIN(official_absence_date) AS startDate,
                MAX(official_absence_date) AS endDate,
                official_absence_date_histories.period
            FROM official_absences
            RIGHT OUTER JOIN official_absence_date_histories
            USING (official_absence_id)
            LEFT OUTER JOIN reports
            USING (official_absence_id)
            GROUP BY official_absence_id,student_id,official_absences.status,reason,reportStatus,official_absence_date_histories.period
            ORDER BY official_absence_id, period;
    """)
    List<OAListEntity> teacherFindAllOAs();
    //共通部分Info
    @Select("""
            SELECT DISTINCT 
                official_absence_id,
                student_id,
                report_required,
                official_absences.status,
                reason,
                reports.status,
                submitted_date
            FROM official_absences
            LEFT OUTER JOIN reports
            USING (official_absence_id)
            WHERE official_absence_id = #{oaId};
    """)
    OAMainInfoEntity selectMainInfo(@Param("oaId") Integer oaId);
    //日時Info
    @Select("""
            SELECT  
                official_absence_date_histories.official_absence_date,
                official_absence_date_histories.period,
                lesson_name,
                version
            FROM official_absence_date_histories
            INNER JOIN lessons
            USING (lesson_id)
            WHERE official_absence_id = #{oaId}
            ORDER BY official_absence_date, period;
    """)
    List<OADateInfoEntity> selectInfo(@Param("oaId") Integer oaId);

    //メインOAフォームインサート
    @Insert("""
            INSERT INTO official_absences (
                student_id,
                report_required,
                status,
                reason,
                submitted_date
            ) VALUES (
                #{entity.studentId},
                #{entity.reportRequired},
                #{entity.status},
                #{entity.reason},
                #{entity.submittedDate}
            );
    """)
    @Options(useGeneratedKeys = true, keyProperty = "entity.officialAbsenceId")
    void insertMainOA(@Param("entity") OAMainEntity entity);

    //日時インサート
    @Transactional
    @Insert("""
            <script>
            INSERT INTO official_absence_date_histories
            VALUES
            <foreach item='date' collection='dateList' separator=','>
                    (#{officialAbsenceId},
                    2,
                    #{date.OAPeriod},
                    #{date.OADate},
                    1)
            </foreach>
            ;
            </script>
    """)
    void insertOADates(@Param("dateList") List<OADatesEntity> dateList, @Param("officialAbsenceId") Integer officialAbsenceId);

    //就活インサート
    @Transactional
    @Insert("""
            INSERT INTO job_search_histories VALUES (
                #{entity.officialAbsenceId},
                #{entity.work},
                #{entity.companyName},
                #{entity.address},
                1,
                #{entity.remarks}
            );
    """)
    void insertJobSearch(@Param("entity") JobSearchEntity jobSearchEntity);
    //セミナーインサート
    @Transactional
    @Insert("""
            INSERT INTO seminar_histories VALUES (
                #{entity.officialAbsenceId},
                #{entity.seminarName},
                #{entity.location},
                #{entity.venueName},
                1,
                #{entity.remarks}
            );
    """)
    void insertSeminar(@Param("entity") SeminarEntity seminarEntity);

    //忌引インサート
    @Transactional
    @Insert("""
            INSERT INTO bereavement_histories VALUES (
                #{entity.officialAbsenceId},
                #{entity.deceasedName},
                #{entity.relationship},
                1,
                #{entity.remarks}
            );
    """)
    void insertBereavement(@Param("entity") BereavementEntity bereavementEntity);
    //出席停止インサート
    @Transactional
    @Insert("""
            INSERT INTO attendance_ban_histories VALUES (
                #{entity.officialAbsenceId},
                #{entity.banReason},
                1,
                #{entity.remarks}
            );
    """)
    void insertAttendanceBan(@Param("entity") AttendanceBanEntity attendanceBanEntity);

    @Transactional
    @Insert("""
            INSERT INTO other_histories VALUES (
                #{entity.officialAbsenceId},
                #{entity.otherReason},
                1,
                #{entity.remarks}
            );
    """)
    void insertOther(@Param("entity") OtherEntity otherEntity);

    //公欠内容Info
    @Select("""
            SELECT
                official_absence_id,
                work,
                company_name,
                address,
                remarks
            FROM job_search_histories WHERE official_absence_id = #{oaId};
    """)
    JobSearchEntity selectJobSearchInfo(@Param("oaId") Integer oaId);
    @Select("""
            SELECT
                official_absence_id,
                seminar_name,
                location,
                venue_name,
                remarks
            FROM seminar_histories WHERE official_absence_id = #{oaId};
    """)
    SeminarEntity selectSeminarInfo(@Param("oaId") Integer oaId);
    @Select("""
            SELECT
                official_absence_id,
                deceased_name,
                relationship,
                remarks
            FROM bereavement_histories WHERE official_absence_id = #{oaId};
    """)
    BereavementEntity selectBereavementInfo(@Param("oaId") Integer oaId);
    @Select("""
            SELECT
                official_absence_id,
                ban_reason,
                remarks
            FROM attendance_ban_histories WHERE official_absence_id = #{oaId};
    """)
    AttendanceBanEntity selectAttendanceBanInfo(@Param("oaId") Integer oaId);
    @Select("""
            SELECT
                official_absence_id,
                other_reason,
                remarks
            FROM other_histories WHERE official_absence_id = #{oaId};
    """)
    OtherEntity selectOtherInfo(@Param("oaId") Integer oaId);

    @Transactional
    @Update("""
            UPDATE official_absences
            SET status = 'acceptance'
            WHERE official_absence_id = #{oaId};
    """)
    void updateOAStatus(Integer oaId);
}