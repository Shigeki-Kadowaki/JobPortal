package com.jobportal.JobPortal.Repository;

import com.jobportal.JobPortal.Service.Entity.*;
import org.apache.ibatis.annotations.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
            submitted_date,
            career_check_required,
            teacher_check,
            career_check,
            submitted_date_histories.version
        FROM official_absences
        LEFT OUTER JOIN reports
        USING (official_absence_id)
        INNER JOIN submitted_date_histories
        USING (official_absence_id)
        WHERE (official_absence_id, submitted_date_histories.version) IN (
            SELECT 
                official_absence_id,
                MAX(version)
            FROM official_absence_date_histories
            GROUP BY official_absence_id
        ) AND official_absence_id = #{OAId};
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
        WHERE (official_absence_id, version) IN (
            SELECT 
                official_absence_id,
                MAX(version)
            FROM official_absence_date_histories
            GROUP BY official_absence_id
        ) AND official_absence_id = #{OAId}
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
            career_check_required,
            teacher_check,
            career_check
        ) VALUES (
            #{entity.studentId},
            #{entity.reportRequired},
            #{entity.status},
            #{entity.reason},
            #{entity.careerCheckRequired},
            #{entity.teacherCheck},
            #{entity.careerCheck}
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
    //その他インサート
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
        ) AND official_absence_id = #{OAId};
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
        FROM seminar_histories WHERE (official_absence_id, version) IN (
            SELECT 
                official_absence_id,
                MAX(version)
            FROM seminar_histories
            GROUP BY official_absence_id
        ) AND official_absence_id = #{OAId};
    """)
    SeminarEntity selectSeminarInfo(@Param("OAId") Integer OAId);
    @Select("""
        SELECT
            official_absence_id,
            deceased_name,
            relationship,
            remarks
        FROM bereavement_histories WHERE (official_absence_id, version) IN (
            SELECT 
                official_absence_id,
                MAX(version)
            FROM bereavement_histories
            GROUP BY official_absence_id
        ) AND official_absence_id = #{OAId};
    """)
    BereavementEntity selectBereavementInfo(@Param("OAId") Integer OAId);
    @Select("""
        SELECT
            official_absence_id,
            ban_reason,
            remarks
        FROM attendance_ban_histories WHERE (official_absence_id, version) IN (
            SELECT 
                official_absence_id,
                MAX(version)
            FROM attendance_ban_histories
            GROUP BY official_absence_id
        ) AND official_absence_id = #{OAId};
    """)
    AttendanceBanEntity selectAttendanceBanInfo(@Param("OAId") Integer OAId);
    @Select("""
        SELECT
            official_absence_id,
            other_reason,
            remarks
        FROM other_histories WHERE (official_absence_id, version) IN (
            SELECT 
                official_absence_id,
                MAX(version)
            FROM other_histories
            GROUP BY official_absence_id
        ) AND official_absence_id = #{OAId};
    """)
    OtherEntity selectOtherInfo(@Param("OAId") Integer OAId);
    //ステータス変更
    @Transactional
    @Update("""
        UPDATE official_absences
        SET status = #{status}
        WHERE official_absence_id = #{OAId};
    """)
    void updateOAStatus(@Param("OAId") Integer OAId, @Param("status") String status);
    //報告書有無変更
    @Transactional
    @Update("""
        UPDATE official_absences
        SET report_required = #{flag}
        WHERE official_absence_id = #{OAId};
    """)
    void updateReportRequired(@Param("OAId") Integer OAId, @Param("flag") boolean flag);
    //初回提出日インサート
    @Transactional
    @Insert("""
        INSERT INTO submitted_date_histories VALUES(
            #{OAId},
            1,
            #{now}
        );
    """)
    void createSubmittedDate(@Param("OAId") Integer OAId,@Param("now") LocalDate now);
    //再提出日インサート
    @Transactional
    @Insert("""
        INSERT INTO submitted_date_histories VALUES(
            #{OAId},
            (SELECT MAX(version) FROM submitted_date_histories WHERE official_absence_id = #{OAId}) + 1,
            #{now}
        );
    """)
    void updateSubmittedDate(@Param("OAId") Integer OAId,@Param("now") LocalDate now);
    //公欠日再提出インサート
    @Transactional
    @Insert("""
    <script>
            INSERT INTO official_absence_date_histories VALUES
            <foreach item='date' collection='dateList' separator=','>(
                    #{OAId},
                    2,
                    #{date.OAPeriod},
                    #{date.OADate},
                    (SELECT MAX(version) FROM submitted_date_histories WHERE official_absence_id = #{OAId})
            )</foreach>
            ;
            </script>
    """)
    void updateOADates(@Param("dateList") List<OADatesEntity> dateList, @Param("OAId") Integer OAId);
    //再提出インサート
    @Transactional
    @Insert("""
        INSERT INTO job_search_histories VALUES (
            #{entity.officialAbsenceId},
            #{entity.work},
            #{entity.companyName},
            #{entity.address},
            (SELECT MAX(version) FROM submitted_date_histories WHERE official_absence_id = #{entity.officialAbsenceId}),
            #{entity.remarks},
            #{entity.visitStartHour},
            #{entity.visitStartMinute}
        );
    """)
    void updateJobSearch(@Param("entity") JobSearchEntity jobEntity);
    @Transactional
    @Insert("""
        INSERT INTO seminar_histories VALUES (
            #{entity.officialAbsenceId},
            #{entity.seminarName},
            #{entity.location},
            #{entity.venueName},
            (SELECT MAX(version) FROM submitted_date_histories WHERE official_absence_id = #{entity.officialAbsenceId}), 
            #{entity.remarks},
            #{entity.visitStartHour},
            #{entity.visitStartMinute}
        );
    """)
    void updateSeminar(@Param("entity") SeminarEntity seminar);
    @Transactional
    @Insert("""
        INSERT INTO bereavement_histories VALUES(
            #{entity.officialAbsenceId},
            #{entity.deceasedName},
            #{entity.relationship},
            (SELECT MAX(version) FROM submitted_date_histories WHERE official_absence_id = #{entity.officialAbsenceId}), 
            #{entity.remarks}
        );
    """)
    void updateBereavement(@Param("entity") BereavementEntity bereavement);
    @Transactional
    @Insert("""
        INSERT INTO attendance_ban_histories VALUES(
            #{entity.officialAbsenceId},
            #{entity.banReason},
            (SELECT MAX(version) FROM submitted_date_histories WHERE official_absence_id = #{entity.officialAbsenceId}), 
            #{entity.remarks}
        );
    """)
    void updateAttendanceBan(@Param("entity") AttendanceBanEntity attendanceBan);
    @Transactional
    @Insert("""
        INSERT INTO other_histories VALUES(
            #{entity.officialAbsenceId},
            #{entity.otherReason},           
            (SELECT MAX(version) FROM submitted_date_histories WHERE official_absence_id = #{entity.officialAbsenceId}), 
            #{entity.remakrs}
        );
    """)
    void updateOther(@Param("entity") OtherEntity other);
    //削除系
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
    @Transactional
    @Delete("""
        DELETE FROM job_search_histories WHERE official_absence_id = #{OAId};
    """)
    void deleteJobSearch(@Param("OAId") Integer OAId);
    @Transactional
    @Delete("""
        DELETE FROM seminar_histories WHERE official_absence_id = #{OAId};
    """)
    void deleteSeminar(@Param("OAId") Integer OAId);
    @Transactional
    @Delete("""
        DELETE FROM bereavement_histories WHERE official_absence_id = #{OAId};
    """)
    void deleteBereavement(Integer oaId);
    @Transactional
    @Delete("""
        DELETE FROM attendance_ban_histories WHERE official_absence_id = #{OAId};
    """)
    void deleteAttendanceBan(Integer oaId);
    @Transactional
    @Delete("""
        DELETE FROM other_histories WHERE official_absence_id = #{OAId};
    """)
    void deleteOther(Integer oaId);
    @Transactional
    @Delete("""
        DELETE FROM submitted_date_histories WHERE official_absence_id = #{OAId};
    """)
    void deleteSubmittedDate(@Param("OAId") Integer OAId);

    @Transactional
    @Update("""
        <script>
            UPDATE official_absences
            <if test='type == "teacher"'>
            SET teacher_check = #{check}
            </if>
            <if test='type == "career"'>
            SET career_check = #{check}
            </if>
            WHERE official_absence_id = #{OAId};
        </script>
    """)
    void updateCheck(@Param("OAId") Integer OAId,@Param("type") String type, @Param("check") boolean check);

    @Select("""
        SELECT teacher_check FROM official_absences WHERE official_absence_id = #{OAId};
    """)
    boolean teacherCheckCondition(Integer OAId);
    @Select("""
        SELECT career_check FROM official_absences WHERE official_absence_id = #{OAId};
    """)
    boolean careerCheckCondition(Integer OAId);
}