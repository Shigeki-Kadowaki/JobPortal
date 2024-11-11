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
            LEFT OUTER JOIN official_absence_date_histories
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
            	report_required,
                MIN(official_absence_date) AS startDate,
                MAX(official_absence_date) AS endDate,
                official_absence_date_histories.period
            FROM official_absences
            LEFT OUTER JOIN official_absence_date_histories
            USING (official_absence_id)
            LEFT OUTER JOIN reports
            USING (official_absence_id)
            WHERE (official_absence_id, version) IN (
                SELECT 
                    official_absence_id,
                    MAX(version)
                FROM official_absence_date_histories
                GROUP BY official_absence_id
            )
            GROUP BY official_absence_id,student_id,official_absences.status,reason,reportStatus,report_required,official_absence_date_histories.period
            ORDER BY official_absence_id DESC, period;
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
            WHERE official_absence_id = #{OAId};
    """)
    OAMainInfoEntity selectMainInfo(@Param("OAId") Integer OAId);
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
            WHERE official_absence_id = #{OAId}
            ORDER BY official_absence_date, period;
    """)
    List<OADateInfoEntity> selectInfo(@Param("OAId") Integer OAId);

    //メインOAフォームインサート
    @Transactional
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
                #{entity.remarks},
                #{entity.visitStartHour},
                #{entity.visitStartMinute}
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
                #{entity.remarks},
                #{entity.visitStartHour},
                #{entity.visitStartMinute}
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
                remarks,
                visit_start_hour,
                visit_start_minute
            FROM job_search_histories 
            WHERE (official_absence_id, version) IN (
                SELECT 
                    official_absence_id,
                    MAX(version)
                FROM job_search_histories
                GROUP BY official_absence_id
            )
            AND official_absence_id = #{OAId};
    """)
    JobSearchEntity selectJobSearchInfo(@Param("OAId") Integer OAId);
    @Select("""
            SELECT
                official_absence_id,
                seminar_name,
                location,
                venue_name,
                remarks,
                visit_start_hour,
                visit_start_minute
            FROM seminar_histories WHERE official_absence_id = #{OAId};
    """)
    SeminarEntity selectSeminarInfo(@Param("OAId") Integer OAId);
    @Select("""
            SELECT
                official_absence_id,
                deceased_name,
                relationship,
                remarks
            FROM bereavement_histories WHERE official_absence_id = #{OAId};
    """)
    BereavementEntity selectBereavementInfo(@Param("OAId") Integer OAId);
    @Select("""
            SELECT
                official_absence_id,
                ban_reason,
                remarks
            FROM attendance_ban_histories WHERE official_absence_id = #{OAId};
    """)
    AttendanceBanEntity selectAttendanceBanInfo(@Param("OAId") Integer OAId);
    @Select("""
            SELECT
                official_absence_id,
                other_reason,
                remarks
            FROM other_histories WHERE official_absence_id = #{OAId};
    """)
    OtherEntity selectOtherInfo(@Param("OAId") Integer OAId);

    @Transactional
    @Update("""
            UPDATE official_absences
            SET status = #{status}
            WHERE official_absence_id = #{OAId};
    """)
    void updateOAStatus(@Param("OAId") Integer OAId, @Param("status") String status);

    @Transactional
    @Update("""
        UPDATE official_absences
        SET report_required = #{flag}
        WHERE official_absence_id = #{OAId};
    """)
    void updateReportRequired(@Param("OAId") Integer OAId, @Param("flag") boolean flag);

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
    void updateOADates(List<OADatesEntity> dateList, Integer oaId);
    @Transactional
    @Insert("""
        INSERT INTO job_search_histories VALUES (
            #{entity.officialAbsenceId},
            #{entity.work},
            #{entity.companyName},
            #{entity.address},
            (SELECT version FROM job_search_histories WHERE official_absence_id = #{entity.officialAbsenceId}) + 1,
            #{entity.remarks},
            #{entity.visit_start_hour},
            #{entity.visit_start_minute}
    )
    """)
    void updateJobSearch(@Param("entity") JobSearchEntity jobEntity);

    @Transactional
    @Delete("""
        DELETE FROM job_search_histories WHERE official_absence_id = #{OAId};
    """)
    void deleteJobSearch(Integer oaId);

    @Transactional
    @Delete("""
        DELETE FROM official_absence_date_histories WHERE official_absence_id = #{OAId};
    """)
    void deleteDate(@Param("OAId") Integer OAId);

    @Transactional
    @Delete("""
        DELETE FROM official_absences WHERE official_absence_id = #{OAId};
    """)
    void deleteMain(@Param("OAId") Integer OAId);
}