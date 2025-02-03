package com.jobportal.JobPortal.Repository;

import com.jobportal.JobPortal.Controller.API.OASubject;
import com.jobportal.JobPortal.Controller.API.OASubjectDTO;
import com.jobportal.JobPortal.Controller.DesiredOccupation;
import com.jobportal.JobPortal.Controller.Form.*;
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
            r.report_id,
            student_id,
            grade,
            classroom,
            course,
            student_name,
            o.status,
            o.reason,
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
        AND (official_absence_id, version) IN (
          SELECT
              official_absence_id,
              MAX(version)
          FROM official_absence_date_histories
          GROUP BY official_absence_id
        )
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
        GROUP BY official_absence_id,r.report_id,student_id,grade,classroom,course,student_name,o.status,o.reason,reportStatus,d.period,report_required, dl.min, dl.max
        ORDER BY official_absence_id DESC, period;
        </script>
    """)
    List<OAListEntity> selectAll(@Param("studentId") Integer studentId, @Param("form")StudentOASearchForm form);
    @Select("""
    <script>
        WITH List AS(
            SELECT official_absence_id, 
                MIN(official_absence_date) as min,
                MAX(official_absence_date) as max
            FROM official_absences o
            LEFT JOIN reports r
            USING (official_absence_id)
            LEFT JOIN official_absence_date_histories d
            USING (official_absence_id)
            WHERE version = (SELECT MAX(version) FROM official_absence_date_histories WHERE official_absence_id = d.official_absence_id)
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
                AND 
                <if test='form.reportStatus != null and !form.reportStatus.isEmpty()'>
                    (
                </if>
                    o.status IN
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
            <if test='form.OAStatus != null and !form.OAStatus.isEmpty() and form.reportStatus != null and !form.reportStatus.isEmpty()'>
                )
            </if>
            GROUP BY o.official_absence_id
            <if test='form.todayOAFlag'>
                HAVING (CURRENT_DATE BETWEEN MIN(d.official_absence_date) and MAX(d.official_absence_date)) OR MAX(d.official_absence_date) = CURRENT_DATE
            </if>
            ORDER BY official_absence_id DESC
            LIMIT #{pageSize} OFFSET (#{page} - 1) * #{pageSize}
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
            r.report_id,
            student_id,
            grade,
            classroom,
            course,
            student_name,
            o.status,
            o.reason,
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
        LEFT JOIN JobList j
            USING (official_absence_id)
        LEFT JOIN SeminarList s
            USING (official_absence_id)
        WHERE (official_absence_id, version) IN (
              SELECT
                  official_absence_id,
                  MAX(version)
              FROM official_absence_date_histories
              GROUP BY official_absence_id
            )
        GROUP BY o.official_absence_id,r.report_id, student_id,course,student_name,o.status,o.reason,reportStatus,report_required,d.period, d.official_absence_date, min, max, j.visit_start_hour, j.visit_start_minute,s.visit_start_hour,s.visit_start_minute
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
        	o.status,
        	o.reason,
        	r.status,
        	submitted_date,
        	career_check_required,
        	o.teacher_check,
        	career_check,
        	submitted_date_histories.version,
        	(SELECT MAX(version) FROM submitted_date_histories WHERE official_absence_id = #{OAId}) AS maxVersion,
    	    student_email
        FROM official_absences o
        LEFT OUTER JOIN reports r
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
            o.status,
            o.reason,
            r.status,
            submitted_date,
            career_check_required,
            o.teacher_check,
            career_check,
            submitted_date_histories.version,
            (SELECT MAX(version) FROM submitted_date_histories WHERE official_absence_id = #{OAId}) AS maxVersion,
            student_email
        FROM official_absences o
        LEFT OUTER JOIN reports r
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
            #{entity.remarks}
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
                (#{timeTableInfo.grade}, #{timeTableInfo.classroom}, #{timeTableInfo.course}, #{timeTableInfo.semester}, #{date.weekdayNumber}, #{date.period}, #{date.subjectId}, #{timeTableInfo.year})
            </foreach>
        ;
    </script>
    """)
    void createTimeTable(@Param("timeTableInfo") TimeTableInfoForm timeTableInfo, @Param("timeTableList") List<TimeTableEntity> timeTableList);

    @Select("""
            SELECT
                weekday_number,
                period,
                subject_id,
                year
            FROM time_tables
            WHERE
                grade = #{classification.grade} AND classroom = #{classification.classroom} AND course = #{classification.course} AND semester = #{classification.semester} AND year = #{classification.year};
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
            LEFT JOIN official_absence_date_histories d
            USING (official_absence_id)
            WHERE version = (SELECT MAX(version) FROM official_absence_date_histories WHERE official_absence_id = d.official_absence_id)
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
            GROUP BY o.official_absence_id
            <if test='form.todayOAFlag'>
                HAVING (CURRENT_DATE BETWEEN MIN(d.official_absence_date) and MAX(d.official_absence_date)) OR MAX(d.official_absence_date) = CURRENT_DATE
            </if>
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

    @Update("""
        UPDATE reports
        SET status = #{status}
        WHERE report_id = #{reportId};
    """)
    void updateReportStatus(@Param("reportId") Integer reportId,@Param("status") String status);

    @Select("""
        SELECT report_id
        FROM reports
        WHERE official_absence_id = #{oaId}
        LIMIT 1;
    """)
    Integer selectReportId(@Param("oaId") Integer oaId);

    @Insert("""
        INSERT INTO report_interview_histories VALUES (
            #{reportId},
            1,
            #{form.interviewerNumber},
            #{form.interviewType},
            #{form.interviewContent},
            #{form.interviewImpressions}
        );
    """)
    void insertInterviewReport(@Param("form") ReportForm form, @Param("reportId") Integer reportId);

    @Insert("""
        INSERT INTO report_briefing_histories VALUES (
            #{reportId},
            1,
            #{form.briefingContent},
            #{form.briefingImpressions}
        );
    """)
    void insertBriefingReport(@Param("form") ReportForm form,@Param("reportId") Integer reportId);

    @Insert("""
        INSERT INTO report_exam_histories VALUES (
            #{reportId},
            1,
            #{form.generalKnowledgeType},
            #{form.nationalLanguageNumber},
            #{form.nationalLanguageType},
            #{form.mathNumber},
            #{form.mathType},
            #{form.englishNumber},
            #{form.englishType},
            #{form.currentAffairsNumber},
            #{form.currentAffairsType},
            #{form.writingTimer},
            #{form.writingNumberOfCharacters},
            #{form.writingTheme},
            #{form.expertiseNumber},
            #{form.expertiseType},
            #{form.jobQuestionNumber},
            #{form.jobQuestionType},
            #{form.SPILanguageSystemNumber},
            #{form.SPINonLanguageSystemNumber},
            #{form.SPIOthersNumber},
            #{form.personalityDiagnosisNumber},
            #{form.personalityDiagnosisType},
            #{form.others},
            #{form.testImpressions},
            #{form.generalKnowledgeNumber}
        );
    """)
    void insertExamReport(@Param("form") ReportForm form,@Param("reportId") Integer reportId);

    @Insert("""
        INSERT INTO report_interview_histories VALUES (
            #{reportId},
            1,
            #{form.interviewerNumber},
            #{form.interviewType},
            #{form.interviewContent},
            #{form.interviewImpressions}
        );
    """)
    void insertInformalCeremonyReport(@Param("form") ReportForm form,@Param("reportId") Integer reportId);

    @Insert("""
        INSERT INTO report_training_histories VALUES (
            #{reportId},
            1,
            #{form.trainingImpressions}
        );
    """)
    void insertTrainingReport(@Param("form") ReportForm form,@Param("reportId") Integer reportId);

    @Insert("""
        INSERT INTO report_other_histories VALUES (
            #{reportId},
            1,
            #{form.activityContent},
            #{form.otherImpressions}
        );
    """)
    void insertOtherReport(@Param("form") ReportForm form, @Param("reportId") Integer reportId);

    @Insert("""
        <script>
        INSERT INTO report_seminar_histories VALUES
        <foreach item='seminarForm' collection='form.seminarForms' index='index' separator=','>
            (#{reportId},
            #{index} + 1 ,
            1,
            #{seminarForm.companyName},
            #{seminarForm.manager},
            #{seminarForm.industry},
            #{seminarForm.seminarImpressions},
            #{seminarForm.seminarEmploymentIntention},
            #{seminarForm.seminarNextAction})
        </foreach>
        ;
        </script>
    """)
    void insertSeminarReport(@Param("form") ReportForm form,@Param("reportId") Integer reportId);

    @Select("""
        SELECT
            o.official_absence_id,
            report_id,
            student_id,
            r.status,
            r.reason,
            h.submitted_date,
            h.version,
            (SELECT MAX(version) FROM report_histories WHERE report_id = #{reportId}) AS maxVersion,
            o.student_email,
            company_name,
            activity_time,
            f.employment_intention,
            f.next_action
        FROM official_absences o
        JOIN reports r
        USING (official_absence_id)
        LEFT JOIN report_histories h
        USING (report_id)
        LEFT JOIN report_job_future_selection f
        USING (report_id)
        WHERE report_id = #{reportId}
        ORDER BY h.version DESC, f.version DESC
        LIMIT 1;
    """)
    ReportInfoEntity selectReportInfo(@Param("reportId") Integer reportId);

    @Select("""
        SELECT 
            report_id,
            interviewer_number,
            interview_type,
            interview_content,
            impressions
        FROM report_interview_histories
        WHERE report_id = #{reportId}
        ORDER BY version DESC
        LIMIT 1;
    """)
    ReportInterviewEntity selectReportInterview(@Param("reportId") Integer reportId);
    @Select("""
        SELECT
            report_id,
            briefing_content,
            impressions
        FROM report_briefing_histories
        WHERE report_id = #{reportId}
        ORDER BY version DESC
        LIMIT 1;
    """)
    ReportBriefingEntity selectReportBriefing(@Param("reportId") Integer reportId);

    @Select("""
        SELECT
            report_id,
            general_knowledge_number,
            general_knowledge_type,
            job_question_number,
            job_question_type,
            spi_language_system_number,
            spi_non_language_system_number,
            spi_others_number,
            personality_diagnosis_number,
            personality_diagnosis_type,
            national_language_number,
            national_language_type,
            math_number,
            math_type,
            english_number,
            english_type,
            current_affairs_number,
            current_affairs_type,
            writing_timer,
            writing_number_of_characters,
            writing_theme,
            expertise_number,
            expertise_type,
            others,
            impressions
        FROM report_exam_histories
        WHERE report_id = #{reportId}
        ORDER BY version DESC
        LIMIT 1;
    """)
    ReportTestEntity selectReportTest(@Param("reportId") Integer reportId);

    @Select("""
        SELECT
            report_id,
            impressions
        FROM report_informal_ceremony_histories
        WHERE report_id = #{reportId}
        ORDER BY version DESC
        LIMIT 1;
    """)
    ReportInformalCeremonyEntity selectReportInformalCeremony(@Param("reportId") Integer reportId);

    @Select("""
        SELECT
            report_id,
            impressions
        FROM report_training_histories
        WHERE report_id = #{reportId}
        ORDER BY version DESC
        LIMIT 1;
    """)
    ReportTrainingEntity selectReportTraining(@Param("reportId") Integer reportId);

    @Select("""
        SELECT
            report_id,
            activity_content,
            impressions
        FROM report_other_histories
        WHERE report_id = #{reportId}
        ORDER BY version DESC
        LIMIT 1;
    """)
    ReportOtherEntity selectReportOther(@Param("reportId") Integer reportId);

    @Select("""
        SELECT
            report_id,
            company_name,
            manager,
            industry,
            impressions,
            employment_intention,
            next_action
        FROM report_seminar_histories
        WHERE report_id = #{reportId} 
        AND version = (SELECT MAX(version) FROM report_seminar_histories WHERE report_id = #{reportId});
    """)
    List<ReportSeminarEntity> selectReportSeminar(@Param("reportId") Integer reportId);

    @Update("""
        <script>
        UPDATE reports
        SET reason = #{form.reason}
        WHERE report_id = #{reportId};
        </script>
    """)
    void updateReportInfo(@Param("form") ReportForm form, @Param("reportId") Integer reportId);

    @Insert("""
        INSERT INTO report_job_future_selection VALUES (
            #{reportId},
            1,
            #{form.employmentIntention},
            #{form.nextAction}
        );
    """)
    void insertJobFuture(@Param("form") ReportForm form,@Param("reportId") Integer reportId);

    @Insert("""
        INSERT INTO report_briefing_histories VALUES (
            #{form.reportId},
            (SELECT MAX(version) FROM report_histories WHERE report_id = #{form.reportId}), 
            #{form.briefingContent},
            #{form.briefingImpressions}
        );
    """)
    void updateReportBriefing(@Param("entity") ReportForm form);

    @Insert("""
        INSERT INTO report_interview_histories VALUES (
            #{form.reportId},
            (SELECT MAX(version) FROM report_histories WHERE report_id = #{form.reportId}), 
            #{form.interviewerNumber},
            #{form.interviewType},
            #{form.interviewContent},
            #{form.interviewImpressions}
        );
    """)
    void updateReportInterview(@Param("form") ReportForm form);

    @Insert("""
        INSERT INTO report_exam_histories VALUES (
            #{form.reportId},
            (SELECT MAX(version) FROM report_histories WHERE report_id = #{form.reportId}), 
            #{form.generalKnowledgeType},
            #{form.jobQuestionNumber},
            #{form.jobQuestionType},
            #{form.SPILanguageSystemNumber},
            #{form.SPINonLanguageSystemNumber},
            #{form.SPIOthersNumber},
            #{form.personalityDiagnosisNumber},
            #{form.personalityDiagnosisType},
            #{form.nationalLanguageNumber},
            #{form.nationalLanguageType},
            #{form.mathNumber},
            #{form.mathType},
            #{form.englishNumber},
            #{form.englishType},
            #{form.currentAffairsNumber},
            #{form.currentAffairsType},
            #{form.writingTimer},
            #{form.writingNumberOfCharacters},
            #{form.writingTheme},
            #{form.expertiseNumber},
            #{form.expertiseType},
            #{form.others},
            #{form.testImpressions},
            #{form.generalKnowledgeNumber}
        );
    """)
    void updateReportTest(@Param("form") ReportForm form);

    @Insert("""
        INSERT INTO report_informal_ceremony_histories VALUES (
            #{form.reportId},
            (SELECT MAX(version) FROM report_histories WHERE report_id = #{form.reportId}), 
            #{form.informalCeremonyImpressions}
        );
    """)
    void updateReportInformalCeremony(@Param("form") ReportForm form);

    @Insert("""
        INSERT INTO report_training_histories VALUES (
            #{form.reportId},
            (SELECT MAX(version) FROM report_histories WHERE report_id = #{form.reportId}),
            #{form.trainingImpressions}
        );
    """)
    void updateReportTraining(@Param("form") ReportForm form);

    @Insert("""
        INSERT INTO report_other_histories VALUES (
            #{form.reportId},
            (SELECT MAX(version) FROM report_histories WHERE report_id = #{form.reportId}),
            #{form.activityContent},
            #{form.othersImpressions}
        );
    """)
    void updateReportOther(@Param("form") ReportForm form);

    @Insert("""
        <script>
        INSERT INTO report_seminar_histories VALUES
        <foreach item='seminarForm' collection='form.seminarForms' separator=',' index='index'>
            (#{form.reportId},
            #{index} + 1,
            (SELECT MAX(version) FROM report_histories WHERE report_id = #{form.reportId}),
            #{seminarForm.companyName},
            #{seminarForm.manager},
            #{seminarForm.industry},
            #{seminarForm.impressions},
            #{seminarForm.seminarEmploymentIntention},
            #{seminarForm.seminarNextAction})
        </foreach>
        ;
        </script>
    """)
    void updateReportSeminar(@Param("form") ReportForm form);

    @Insert("""
        INSERT INTO report_job_future_selection VALUES (
            #{form.reportId},
            (SELECT MAX(version) FROM report_histories WHERE report_id = #{form.reportId}),
            #{form.employmentIntention},
            #{form.nextAction}
        );
    """)
    void updateJobFuture(@Param("form") ReportForm form);

    @Insert("""
        INSERT INTO report_histories VALUES (
            #{reportId},
            1,
            #{form.activityTime},
            CURRENT_DATE,
            #{form.companyName}
        );
    """)
    void insertReportHistories(@Param("reportId") Integer reportId,@Param("form") ReportForm form);

    @Insert("""
        INSERT INTO report_histories VALUES (
            #{reportId},
            (SELECT MAX(version) FROM report_histories WHERE report_id = #{reportId}) + 1,
            #{form.activityTime},
            CURRENT_DATE,
            #{form.companyName}
        );
    """)
    void updateReportHistories(@Param("reportId") Integer reportId,@Param("form") ReportForm form);

    @Select("""
        SELECT
            o.official_absence_id,
            report_id,
            student_id,
            r.status,
            r.reason,
            h.submitted_date,
            h.version,
            (SELECT MAX(version) FROM report_histories WHERE report_id = #{reportId}) AS maxVersion,
            o.student_email,
            activity_time,
            company_name,
            f.employment_intention,
            f.next_action
        FROM official_absences o
        JOIN reports r
        USING (official_absence_id)
        JOIN report_histories h
        USING (report_id)
        LEFT OUTER JOIN report_job_future_selection f
        USING (report_id)
        WHERE report_id = #{reportId}
        AND f.version = #{version}
        AND h.version = #{version}
        LIMIT 1;
    """)
    ReportInfoEntity selectReportInfoByVersion(@Param("reportId") Integer reportId,@Param("version") Integer version);

    @Select("""
        SELECT 
            report_id,
            interviewer_number,
            interview_type,
            interview_content,
            impressions
        FROM report_interview_histories
        WHERE report_id = #{reportId}
        AND version = #{version}
        LIMIT 1;
    """)
    ReportInterviewEntity selectReportInterviewByVersion(@Param("reportId") Integer reportId,@Param("version") Integer version);
    @Select("""
        SELECT
            report_id,
            briefing_content,
            impressions
        FROM report_briefing_histories
        WHERE report_id = #{reportId}
        AND version = #{version}
        LIMIT 1;
    """)
    ReportBriefingEntity selectReportBriefingByVersion(@Param("reportId") Integer reportId,@Param("version") Integer version);
    @Select("""
        SELECT
            report_id,
            general_knowledge_number,
            general_knowledge_type,
            job_question_number,
            job_question_type,
            spi_language_system_number,
            spi_non_language_system_number,
            spi_others_number,
            personality_diagnosis_number,
            personality_diagnosis_type,
            math_number,
            math_type,
            english_number,
            english_type,
            current_affairs_number,
            current_affairs_type,
            writing_timer,
            writing_number_of_characters,
            writing_theme,
            expertise_number,
            expertise_type,
            others,
            impressions
        FROM report_exam_histories
        WHERE report_id = #{reportId}
        AND version = #{version}
        LIMIT 1;
    """)
    ReportTestEntity selectReportTestByVersion(@Param("reportId") Integer reportId,@Param("version") Integer version);
    @Select("""
        SELECT
            report_id,
            impressions
        FROM report_informal_ceremony_histories
        WHERE report_id = #{reportId}
        AND version = #{version}
        LIMIT 1;
    """)
    ReportInformalCeremonyEntity selectReportInformalCeremonyByVersion(@Param("reportId") Integer reportId,@Param("version") Integer version);
    @Select("""
        SELECT
            report_id,
            impressions
        FROM report_training_histories
        WHERE report_id = #{reportId}
        AND version = #{version}
        LIMIT 1;
    """)
    ReportTrainingEntity selectReportTrainingByVersion(@Param("reportId") Integer reportId,@Param("version") Integer version);
    @Select("""
        SELECT
            report_id,
            activity_content,
            impressions
        FROM report_other_histories
        WHERE report_id = #{reportId}
        AND version = #{version}
        LIMIT 1;
    """)
    ReportOtherEntity selectReportOtherByVersion(@Param("reportId") Integer reportId,@Param("version") Integer version);
    @Select("""
        SELECT
            report_id,
            company_name,
            manager,
            industry,
            impressions,
            employment_intention,
            next_action
        FROM report_seminar_histories
        WHERE report_id = #{reportId} 
        AND version = #{version};
    """)
    List<ReportSeminarEntity> selectReportSeminarByVersion(@Param("reportId") Integer reportId,@Param("version") Integer version);

    @Select("""
        SELECT status
        FROM official_absences
        WHERE official_absence_id = #{OAId};
    """)
    String selectOAStatus(@Param("OAId") Integer OAId);
    @Select("""
        SELECT status
        FROM reports
        WHERE report_id = #{reportId};
    """)
    String selectReportStatus(@Param("reportId") Integer reportId);

    @Select("""
        SELECT official_absence_id
        FROM reports
        WHERE report_id = #{reportId}
        LIMIT 1;
    """)
    Integer selectOAId(@Param("reportId") Integer reportId);

    @Select("""
        SELECT student_id
        FROM official_absences
        WHERE official_absence_id = #{OAId};
    """)
    int selectStudentId(@Param("OAId") Integer OAId);

    @Insert("""
        <script>
        INSERT INTO approved_leave_requests VALUES
            <foreach item='date' collection='dateEntities' separator=','>
                (#{OAId},
                #{studentId},
                #{date.officialAbsenceDate},
                #{date.period},
                false)
            </foreach>
        ;
        </script>
    """)
    void insertApplovedLeaveRequests(@Param("OAId") Integer OAId,@Param("studentId") Integer studentId,@Param("dateEntities") List<OADateInfoEntity> dateEntities);

    @Delete("""
        DELETE FROM report_interview_histories WHERE report_id = #{reportId};
    """)
    void deleteReportJobInterview(@Param("reportId") Integer reportId);
    @Delete("""
        DELETE FROM report_briefing_histories WHERE report_id = #{reportId};
    """)
    void deleteReportBriefing(@Param("reportId") Integer reportId);
    @Delete("""
        DELETE FROM report_exam_histories WHERE report_id = #{reportId};
    """)
    void deleteReportTest(@Param("reportId") Integer reportId);
    @Delete("""
        DELETE FROM report_informal_ceremony_histories WHERE report_id = #{reportId};
    """)
    void deleteReportInformalCeremony(@Param("reportId") Integer reportId);
    @Delete("""
        DELETE FROM report_training_histories WHERE report_id = #{reportId};
    """)
    void deleteReportTraining(@Param("reportId") Integer reportId);
    @Delete("""
        DELETE FROM report_other_histories WHERE report_id = #{reportId};
    """)
    void deleteReportJobOther(@Param("reportId") Integer reportId);
    @Delete("""
        DELETE FROM report_seminar_histories WHERE report_id = #{reportId};
    """)
    void deleteReportSeminar(@Param("reportId") Integer reportId);
    @Delete("""
        DELETE FROM report_job_future_selection WHERE report_id = #{reportId};
    """)
    void deleteJobFutureSelection(@Param("reportId") Integer reportId);
    @Delete("""
        DELETE FROM report_histories WHERE report_id = #{reportId};
    """)
    void deleteReportHistories(@Param("reportId") Integer reportId);

    @Select("""
        <script>
        SELECT
            o.official_absence_id,
            report_id,
            o.student_id,
            r.status,
            r.reason,
            h.submitted_date,
            h.version,
            (SELECT MAX(version) FROM report_histories) AS maxVersion,
            o.student_email,
            h.company_name,
            activity_time,
            f.employment_intention,
            f.next_action
        FROM official_absences o
        JOIN reports r
        USING (official_absence_id)
        JOIN report_histories h
        USING (report_id)
        LEFT OUTER JOIN report_job_future_selection f
        USING (report_id, version)
        LEFT OUTER JOIN report_seminar_histories s
        USING (report_id, version)
        WHERE (report_id, h.version) IN (
            SELECT
                report_id,
                MAX(version)
            FROM report_histories
            GROUP BY report_id
        )
        <if test="companyName != '' and companyName != null">
            AND ( h.company_name LIKE concat('%',#{companyName},'%')
            OR s.company_name LIKE concat('%',#{companyName},'%'))
        </if>
        AND r.status = #{status}
        GROUP BY o.official_absence_id, o.student_id, report_id, student_id, r.status, r.reason, h.submitted_date, h.version,o.student_email, activity_time,f.employment_intention, f.next_action, h.company_name
        ORDER BY report_id;
        </script>
    """)
    List<ReportInfoEntity> selectReportInfosByCompanyName(@Param("companyName") String companyName, @Param("page") Integer page, @Param("maxPage") Integer maxPage,@Param("status") String status);

    @Select("""
        <script>
        SELECT
            COUNT(h.report_id)
        FROM official_absences o
        JOIN reports r
        USING (official_absence_id)
        JOIN report_histories h
        USING (report_id)
        LEFT OUTER JOIN report_job_future_selection f
        USING (report_id, version)
        LEFT OUTER JOIN report_seminar_histories s
        USING (report_id, version)
        WHERE (report_id, h.version) IN (
            SELECT
                report_id,
                MAX(version)
            FROM report_histories
            GROUP BY report_id
        )
        <if test="companyName != '' and companyName != null">
            AND (h.company_name LIKE concat('%',#{companyName},'%')
            OR s.company_name LIKE concat('%',#{companyName},'%'))
        </if>
        AND r.status = #{status}
        ;
        </script>
    """)
    Integer countReportInfosByCompanyName(@Param("companyName") String companyName,@Param("status") String status);

    @Delete("""
        DELETE FROM reports WHERE official_absence_id = #{OAId};
    """)
    void deleteReportMain(@Param("OAId") Integer OAId);

    @Select("""
        SELECT
            student_id,
            official_absence_date,
            period
        FROM approved_leave_requests
        WHERE reflected_flag = false
        LIMIT 1;
    """)
    OASubject selectOASubject();

    @Update("""
        UPDATE approved_leave_requests
        SET reflected_flag = true
        WHERE student_id = #{subject.studentId}
        AND official_absence_date = #{subject.officialAbsenceDate}
        AND period = #{subject.period};
    """)
    void updateOASubject(@Param("subject") OASubjectDTO subject);
}