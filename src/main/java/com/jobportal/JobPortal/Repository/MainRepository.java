package com.jobportal.JobPortal.Repository;

import com.jobportal.JobPortal.Controller.DesiredOccupation;
import com.jobportal.JobPortal.Controller.Form.ClassificationForm;
import com.jobportal.JobPortal.Controller.Form.StudentOASearchForm;
import com.jobportal.JobPortal.Controller.Form.TeacherOASearchForm;
import com.jobportal.JobPortal.Controller.Form.TimeTableInfoForm;
import com.jobportal.JobPortal.Service.Entity.*;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;


@Mapper
public interface MainRepository {
    //insert
    @Insert("""
        INSERT INTO official_absences (
            student_id,
            report_required,
            status,
            reason,
            career_check_required,
            teacher_check,
            career_check,
            grade,
            classroom,
            course,
            student_name,
            student_email
        ) VALUES (
            #{entity.studentId},
            #{entity.reportRequired},
            #{entity.status},
            #{entity.reason},
            #{entity.careerCheckRequired},
            #{entity.teacherCheck},
            #{entity.careerCheck},
            #{entity.grade},
            #{entity.classroom},
            #{entity.course},
            #{entity.studentName},
            #{entity.studentEmail}
        );
    """)
    @Options(useGeneratedKeys = true, keyProperty = "entity.officialAbsenceId")
    void insertMainOA(@Param("entity") OAMainEntity entity);
    @Insert("""
        INSERT INTO submitted_date_histories VALUES(
            #{OAId},
            1,
            #{now}
        );
    """)
    void createSubmittedDate(@Param("OAId") Integer OAId,@Param("now") LocalDate now);
    @Insert("""
        <script>
        INSERT INTO official_absence_date_histories
        VALUES
        <foreach item='date' collection='dateList' separator=','>
                (#{officialAbsenceId},
                #{date.OAPeriod},
                #{date.OADate},
                1,
                #{date.lessonName})
        </foreach>
        ;
        </script>
    """)
    void insertOADates(@Param("dateList") List<OADatesEntity> dateList, @Param("officialAbsenceId") Integer officialAbsenceId);
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
    @Insert("""
        INSERT INTO attendance_ban_histories VALUES (
            #{entity.officialAbsenceId},
            #{entity.banReason},
            1,
            #{entity.remarks}
        );
    """)
    void insertAttendanceBan(@Param("entity") AttendanceBanEntity attendanceBanEntity);
    @Insert("""
        INSERT INTO other_histories VALUES (
            #{entity.officialAbsenceId},
            #{entity.otherReason},
            1,
            #{entity.remarks}
        );
    """)
    void insertOther(@Param("entity") OtherEntity otherEntity);
    //List取得
    @Select("""
        <script>
        WITH DateList AS(
            SELECT official_absence_id,
                MIN(official_absence_date) as min,
                MAX(official_absence_date) as max
            FROM official_absence_date_histories d
            WHERE version = (SELECT MAX(version) FROM official_absence_date_histories WHERE official_absence_id = d.official_absence_id)
            GROUP BY official_absence_id
            ORDER BY official_absence_id DESC
        )
        SELECT DISTINCT
            o.official_absence_id,
            student_id,
            grade,
            classroom,
            course,
            student_name,
            o.status,
            reason,
            r.status AS reportStatus,
            report_required,
            min,
            max,
            d.period,
            NULL,
            NULL,
            NULL,
            NULL
        FROM official_absences o
        LEFT JOIN official_absence_date_histories d
            USING (official_absence_id)
        LEFT JOIN reports r
            USING (official_absence_id)
        LEFT JOIN DateList dl
            USING (official_absence_id)
        WHERE student_id = #{studentId}
        <if test='form.OAStatus != null and !form.OAStatus.isEmpty()'>
            AND o.status IN
                <foreach item='status' collection='form.OAStatus' open='(' separator=',' close=')'>
                    #{status}
                </foreach>
        </if>
        <if test='form.reportStatus != null and !form.reportStatus.isEmpty()'>
            AND r.status IN
                <foreach item='reportStatus' collection='form.reportStatus' open='(' separator=',' close=')'>
                    #{reportStatus}
                </foreach>
        </if>
        GROUP BY official_absence_id,student_id,grade,classroom,course,student_name,o.status,reason,reportStatus,d.period,report_required, dl.min, dl.max
        ORDER BY official_absence_id DESC, period;
        </script>
    """)
    List<OAListEntity> selectAll(@Param("studentId") Integer studentId, @Param("form")StudentOASearchForm form);
    @Select("""
    <script>
        WITH List AS(
            SELECT official_absence_id
            FROM official_absences o
            LEFT JOIN reports r
            USING (official_absence_id)
            WHERE true
             <if test='form.grade != 0 and form.grade != null'>
                AND grade = #{form.grade}
            </if>
            <if test='form.classroom != "all" and form.classroom != null'>
                AND classroom = #{form.classroom}
            </if>
            
            <if test='form.teacherCheckFlag != null'>
                AND o.teacher_check = false
            </if>
            <if test='form.careerCheckFlag != null'>
                AND o.career_check = false
            </if>
            <if test='form.OAStatus != null and !form.OAStatus.isEmpty()'>
                AND o.status IN
                    <foreach item='status' collection='form.OAStatus' open='(' separator=',' close=')'>
                        #{status}
                    </foreach>
            </if>
            <if test='form.reportStatus != null and !form.reportStatus.isEmpty()'>
                <if test='form.andFlag == null'>
                    AND
                </if>
                <if test='form.andFlag'>
                    OR
                </if>
                r.status IN
                <foreach item='reportStatus' collection='form.reportStatus' open='(' separator=',' close=')'>
                    #{reportStatus}
                </foreach>
            </if>
            <if test='form.todayOAFlag'>
                AND official_absence_date = CURRENT_DATE
            </if>
            ORDER BY official_absence_id DESC
            LIMIT #{pageSize} OFFSET (#{page} - 1) * #{pageSize}
        ),
        DateList AS(
            SELECT official_absence_id,
                MIN(official_absence_date) as min,
                MAX(official_absence_date) as max
            FROM official_absence_date_histories d
            WHERE version = (SELECT MAX(version) FROM official_absence_date_histories WHERE official_absence_id = d.official_absence_id)
            GROUP BY official_absence_id
            ORDER BY official_absence_id DESC
        ),
        JobList AS(
            SELECT official_absence_id,
             visit_start_minute,
             visit_start_hour
            FROM job_search_histories j
            WHERE version = (SELECT MAX(version) FROM job_search_histories WHERE official_absence_id = j.official_absence_id)
        ),
        SeminarList AS(
            SELECT official_absence_id,
                visit_start_hour,
            visit_start_minute
                FROM seminar_histories s
            WHERE version = (SELECT MAX(version) FROM seminar_histories WHERE official_absence_id = s.official_absence_id)
        )
        SELECT DISTINCT
            o.official_absence_id,
            student_id,
            grade,
            classroom,
            course,
            student_name,
            o.status,
            reason,
            r.status AS reportStatus,
            report_required,
            min,
            max,
            d.period,
            j.visit_start_hour AS job_visit_start_hour,
            j.visit_start_minute AS job_visit_start_minute,
            s.visit_start_hour AS seminar_visit_start_hour,
            s.visit_start_minute AS seminar_visit_start_minute
        FROM official_absences o
        LEFT JOIN official_absence_date_histories d
            USING (official_absence_id)
        LEFT JOIN reports r
            USING (official_absence_id)
        RIGHT JOIN List
            USING (official_absence_id)
        LEFT JOIN DateList dl
            USING (official_absence_id)
        LEFT JOIN JobList j
            USING (official_absence_id)
        LEFT JOIN SeminarList s
            USING (official_absence_id)
        GROUP BY o.official_absence_id,student_id,course,student_name,o.status,reason,reportStatus,report_required,d.period, d.official_absence_date, min, max, j.visit_start_hour, j.visit_start_minute,s.visit_start_hour,s.visit_start_minute
        ORDER BY official_absence_id DESC, period;
    </script>
    """)
    List<OAListEntity> teacherFindAllOAs(@Param("form")TeacherOASearchForm form, @Param("page") Integer page, @Param("pageSize") Integer pageSize);
    //Info取得
    @Select("""
        SELECT
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
        	submitted_date_histories.version,
        	(SELECT MAX(version) FROM submitted_date_histories WHERE official_absence_id = #{OAId}),
    	    student_email
        FROM official_absences
        LEFT OUTER JOIN reports
        USING (official_absence_id)
        INNER JOIN submitted_date_histories
        USING (official_absence_id)
        WHERE official_absence_id = #{OAId}
        ORDER BY submitted_date_histories.version DESC
        LIMIT 1;
    """)
    OAMainInfoEntity selectMainInfo(@Param("OAId") Integer OAId);
    @Select("""
        SELECT  
            official_absence_date_histories.official_absence_date,
            official_absence_date_histories.period,
            lesson_name
        FROM official_absence_date_histories
        WHERE (official_absence_id, version) IN (
            SELECT 
                official_absence_id,
                MAX(version)
            FROM official_absence_date_histories
            GROUP BY official_absence_id
        ) AND official_absence_id = #{OAId}
        ORDER BY official_absence_date, period;
    """)
    List<OADateInfoEntity> selectDateInfo(@Param("OAId") Integer OAId);
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
        WHERE official_absence_id = #{OAId}
        ORDER BY version DESC
        LIMIT 1;
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
        FROM seminar_histories
        WHERE official_absence_id = #{OAId}
        ORDER BY version DESC
        LIMIT 1;
    """)
    SeminarEntity selectSeminarInfo(@Param("OAId") Integer OAId);
    @Select("""
        SELECT
            official_absence_id,
            deceased_name,
            relationship,
            remarks
        FROM bereavement_histories 
        WHERE official_absence_id = #{OAId}
        ORDER BY version DESC
        LIMIT 1;
    """)
    BereavementEntity selectBereavementInfo(@Param("OAId") Integer OAId);
    @Select("""
        SELECT
            official_absence_id,
            ban_reason,
            remarks
        FROM attendance_ban_histories 
        WHERE official_absence_id = #{OAId}
        ORDER BY version DESC
        LIMIT 1;
    """)
    AttendanceBanEntity selectAttendanceBanInfo(@Param("OAId") Integer OAId);
    @Select("""
        SELECT
            official_absence_id,
            other_reason,
            remarks
        FROM other_histories
        WHERE official_absence_id = #{OAId}
        ORDER BY version DESC
        LIMIT 1;
    """)
    OtherEntity selectOtherInfo(@Param("OAId") Integer OAId);
    //過去versionInfo取得
    @Select("""
        SELECT
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
            submitted_date_histories.version,
            (SELECT MAX(version) FROM submitted_date_histories WHERE official_absence_id = #{OAId}),
            student_email
        FROM official_absences
        LEFT OUTER JOIN reports
        USING (official_absence_id)
        INNER JOIN submitted_date_histories
        USING (official_absence_id)
        WHERE official_absence_id = #{OAId}
        AND version = #{version};
    """)
    OAMainInfoEntity selectMainInfoByVersion(@Param("OAId") Integer OAId, @Param("version") Integer version);
    @Select("""
        SELECT
            official_absence_date_histories.official_absence_date,
            official_absence_date_histories.period,
            lesson_name
        FROM official_absence_date_histories
        WHERE official_absence_id = #{OAId}
        AND version = #{version}
        ORDER BY official_absence_date, period;
    """)
    List<OADateInfoEntity> selectDateInfoByVersion(@Param("OAId") Integer OAId, @Param("version") Integer version);
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
        WHERE official_absence_id = #{OAId}
        AND version = #{version};
    """)
    JobSearchEntity selectJobSearchInfoByVersion(@Param("OAId") Integer OAId, @Param("version") Integer version);
    @Select("""
        SELECT
            official_absence_id,
            seminar_name,
            location,
            venue_name,
            remarks,
            visit_start_hour,
            visit_start_minute
        FROM seminar_histories
        WHERE official_absence_id = #{OAId}
        AND version = #{version};
    """)
    SeminarEntity selectSeminarInfoByVersion(@Param("OAId") Integer OAId, @Param("version") Integer version);
    @Select("""
        SELECT
            official_absence_id,
            deceased_name,
            relationship,
            remarks
        FROM bereavement_histories 
        WHERE official_absence_id = #{OAId}
        AND version = #{version};
    """)
    BereavementEntity selectBereavementInfoByVersion(@Param("OAId") Integer OAId, @Param("version") Integer version);
    @Select("""
        SELECT
            official_absence_id,
            ban_reason,
            remarks
        FROM attendance_ban_histories 
        WHERE official_absence_id = #{OAId}
        AND version = #{version};
    """)
    AttendanceBanEntity selectAttendanceBanInfoByVersion(@Param("OAId") Integer OAId, @Param("version") Integer version);
    @Select("""
        SELECT
            official_absence_id,
            other_reason,
            remarks
        FROM other_histories 
        WHERE official_absence_id = #{OAId}
        AND version = #{version};
    """)
    OtherEntity selectOtherInfoByVersion(@Param("OAId") Integer OAId, @Param("version") Integer version);
    //削除
    @Delete("""
        DELETE FROM official_absence_date_histories WHERE official_absence_id = #{OAId};
    """)
    void deleteDate(@Param("OAId") Integer OAId);
    @Delete("""
        DELETE FROM official_absences WHERE official_absence_id = #{OAId};
    """)
    void deleteMain(@Param("OAId") Integer OAId);
    @Delete("""
        DELETE FROM job_search_histories WHERE official_absence_id = #{OAId};
    """)
    void deleteJobSearch(@Param("OAId") Integer OAId);
    @Delete("""
        DELETE FROM seminar_histories WHERE official_absence_id = #{OAId};
    """)
    void deleteSeminar(@Param("OAId") Integer OAId);
    @Delete("""
        DELETE FROM bereavement_histories WHERE official_absence_id = #{OAId};
    """)
    void deleteBereavement(Integer oaId);
    @Delete("""
        DELETE FROM attendance_ban_histories WHERE official_absence_id = #{OAId};
    """)
    void deleteAttendanceBan(Integer oaId);
    @Delete("""
        DELETE FROM other_histories WHERE official_absence_id = #{OAId};
    """)
    void deleteOther(Integer oaId);
    @Delete("""
        DELETE FROM submitted_date_histories WHERE official_absence_id = #{OAId};
    """)
    void deleteSubmittedDate(@Param("OAId") Integer OAId);
    //再提出
    @Insert("""
        INSERT INTO submitted_date_histories VALUES(
            #{OAId},
            (SELECT MAX(version) FROM submitted_date_histories WHERE official_absence_id = #{OAId}) + 1,
            #{now}
        );
    """)
    void updateSubmittedDate(@Param("OAId") Integer OAId,@Param("now") LocalDate now);
    @Insert("""
    <script>
            INSERT INTO official_absence_date_histories VALUES
            <foreach item='date' collection='dateList' separator=','>(
                    #{OAId},
                    #{date.OAPeriod},
                    #{date.OADate},
                    (SELECT MAX(version) FROM submitted_date_histories WHERE official_absence_id = #{OAId}),
                    #{date.lessonName}
            )</foreach>
            ;
            </script>
    """)
    void updateOADates(@Param("dateList") List<OADatesEntity> dateList, @Param("OAId") Integer OAId);
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
    @Insert("""
        INSERT INTO attendance_ban_histories VALUES(
            #{entity.officialAbsenceId},
            #{entity.banReason},
            (SELECT MAX(version) FROM submitted_date_histories WHERE official_absence_id = #{entity.officialAbsenceId}), 
            #{entity.remarks}
        );
    """)
    void updateAttendanceBan(@Param("entity") AttendanceBanEntity attendanceBan);
    @Insert("""
        INSERT INTO other_histories VALUES(
            #{entity.officialAbsenceId},
            #{entity.otherReason},           
            (SELECT MAX(version) FROM submitted_date_histories WHERE official_absence_id = #{entity.officialAbsenceId}), 
            #{entity.remakrs}
        );
    """)
    void updateOther(@Param("entity") OtherEntity other);
    //報告書インサート
    @Insert("""
        <script>
            INSERT INTO reports (official_absence_id, status) VALUES(
                #{OAId},
            <if test='reportRequired'>
                'unsubmitted'
            </if>
            <if test='!reportRequired'>
                'unnecessary'
            </if>
            );
        </script>
    """)
    void createReport(@Param("OAId") Integer officialAbsenceId,@Param("reportRequired") boolean reportRequired);
    //ステータス更新
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
    void updateCheck(@Param("OAId") Integer OAId,@Param("type") String type, @Param("check") Boolean check);
    @Update("""
        UPDATE official_absences
        SET status = #{status}
        WHERE official_absence_id = #{OAId};
    """)
    void updateOAStatus(@Param("OAId") Integer OAId, @Param("status") String status);
    @Update("""
        UPDATE official_absences
        SET report_required = #{flag}
        WHERE official_absence_id = #{OAId};
    """)
    void updateReportRequired(@Param("OAId") Integer OAId, @Param("flag") boolean flag);
    //check取得
    @Select("""
        SELECT teacher_check FROM official_absences WHERE official_absence_id = #{OAId};
    """)
    boolean teacherCheckCondition(Integer OAId);
    @Select("""
        SELECT career_check FROM official_absences WHERE official_absence_id = #{OAId};
    """)
    boolean careerCheckCondition(Integer OAId);

    @Select("""
        SELECT * FROM desired_occupations WHERE student_id = #{studentId};
    """)
    DesiredOccupation selectOccupation(@Param("studentId") Integer studentId);

    @Insert("""
    <script>
        INSERT INTO time_tables VALUES
            <foreach item='date' collection='timeTableList' separator=','>
                (#{timeTableInfo.grade}, #{timeTableInfo.classroom}, #{timeTableInfo.course}, #{timeTableInfo.semester}, #{date.weekdayNumber}, #{date.period}, #{date.subjectId})
            </foreach>
        ;
    </script>
    """)
    void createTimeTable(@Param("timeTableInfo") TimeTableInfoForm timeTableInfo, @Param("timeTableList") List<TimeTableEntity> timeTableList);

    @Select("""
            SELECT
                weekday_number,
                period,
                subject_id
            FROM time_tables
            WHERE
                grade = #{classification.grade} AND classroom = #{classification.classroom} AND course = #{classification.course} AND semester = #{classification.semester};
    """)
    List<TimeTableEntity> selectTimeTable(@Param("classification")ClassificationForm classification);

    @Select("""
        SELECT * FROM exception_dates
        ORDER BY exception_day DESC;
    """)
    List<ExceptionDateEntity> selectExceptionDates();

    @Select("""
        SELECT course FROM classifications; 
    """)
    List<String> selectCourses();

    @Insert("""
        INSERT INTO exception_dates VALUES(#{exceptionDateEntity.exceptionDate}, #{exceptionDateEntity.weekdayNumber});
    """)
    void insertExceptionDate(@Param("exceptionDateEntity") ExceptionDateEntity exceptionDateEntity);

    @Delete("""
        DELETE FROM exception_dates
        WHERE exception_day = (SELECT exception_day FROM exception_dates ORDER BY exception_day DESC LIMIT 1 OFFSET #{id});
    """)
    void deleteExceptionDate(@Param("id") Integer id);

    @Delete("""
        DELETE FROM reports
        WHERE official_absence_id = #{oaId};
    """)
    void deleteReport(@Param("oaId") Integer oaId);

    @Update("""
        UPDATE desired_occupations SET business = #{business}
        WHERE student_id = #{studentId};
    """)
    void updateDesiredBusiness(@Param("studentId") Integer studentId, @Param("business") String business);

    @Update("""
        UPDATE desired_occupations SET occupation = #{occupation}
        WHERE student_id = #{studentId};
    """)
    void updateDesiredOccupation(@Param("studentId") Integer studentId, @Param("occupation") String occupation);

    @Select("""
        SELECT EXISTS(SELECT * FROM desired_occupations WHERE student_id = #{studentId});
    """)
    boolean existsDesired(@Param("studentId") Integer studentId);

    @Insert("""
        INSERT INTO desired_occupations VALUES(
            #{studentId}, #{business}, #{occupation}    
        );
    """)
    void insertDesired(@Param("studentId") Integer studentId,@Param("business") String business,@Param("occupation") String occupation);

    @Select("""
        SELECT COUNT(*)
        FROM official_absences;
    """)
    Integer countOA();

    @Select("""
    <script>
        WITH List AS(
            SELECT official_absence_id
            FROM official_absences o
            LEFT JOIN reports r
            USING (official_absence_id)
            WHERE true
             <if test='form.grade != 0 and form.grade != null'>
                AND grade = #{form.grade}
            </if>
            <if test='form.classroom != "all" and form.classroom != null'>
                AND classroom = #{form.classroom}
            </if>
            
            <if test='form.teacherCheckFlag != null'>
                AND o.teacher_check = false
            </if>
            <if test='form.careerCheckFlag != null'>
                AND o.career_check = false
            </if>
            <if test='form.OAStatus != null and !form.OAStatus.isEmpty()'>
                AND o.status IN
                    <foreach item='status' collection='form.OAStatus' open='(' separator=',' close=')'>
                        #{status}
                    </foreach>
            </if>
            <if test='form.reportStatus != null and !form.reportStatus.isEmpty()'>
                <if test='form.andFlag == null'>
                    AND
                </if>
                <if test='form.andFlag'>
                    OR
                </if>
                r.status IN
                <foreach item='reportStatus' collection='form.reportStatus' open='(' separator=',' close=')'>
                    #{reportStatus}
                </foreach>
            </if>
            <if test='form.todayOAFlag'>
                AND official_absence_date = CURRENT_DATE
            </if>
            ORDER BY official_absence_id DESC
        ),
        DateList AS(
            SELECT official_absence_id,
                MIN(official_absence_date) as min,
                MAX(official_absence_date) as max
            FROM official_absence_date_histories d
            WHERE version = (SELECT MAX(version) FROM official_absence_date_histories WHERE official_absence_id = d.official_absence_id)
            GROUP BY official_absence_id
            ORDER BY official_absence_id DESC
        )
        SELECT
            COUNT(DISTINCT official_absence_id)
        FROM official_absences o
        LEFT JOIN official_absence_date_histories d
            USING (official_absence_id)
        LEFT JOIN reports r
            USING (official_absence_id)
        RIGHT JOIN List
            USING (official_absence_id)
        LEFT JOIN DateList dl
            USING (official_absence_id)
    </script>
    """)
    Integer countSearchOA(@Param("form") TeacherOASearchForm form);

    @Select("""
        SELECT
            report_required
        FROM official_absences
        WHERE official_absence_id = #{oaId};
    """)
    boolean getCareerCheckRequired(@Param("oaId") Integer oaId);
}