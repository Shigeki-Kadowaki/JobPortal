package com.jobportal.JobPortal.Repository;

import com.jobportal.JobPortal.Service.JobSearchEntity;
import com.jobportal.JobPortal.Service.OADatesEntity;
import com.jobportal.JobPortal.Service.OAMainEntity;
import org.apache.ibatis.annotations.*;

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
    @Options(useGeneratedKeys=true,keyProperty ="entity.officialAbsenceId")
    void insertMainOA(@Param("entity")OAMainEntity entity,@Param("id") Integer id);

//日時インサート
@Insert("""
            <script>
            INSERT INTO official_absence_dates
            VALUES
            <foreach collection="dateList" item="date" separator=",">
                (#{OfficialAbsenceId}, #{date.OADate}, #{date.OAPeriod})
            </foreach>
            ;
        </script>
        """)
    void insertOADates(@Param("dateList") List<OADatesEntity> dates, @Param("OfficialAbsenceId") Integer OfficialAbsenceId);

    //就活情報インサート
    @Insert("""
            <script>
            INSERT INTO official_absence_dates
            VALUES
            <foreach collection="dateList" item="date" separator=",">
                (#{OfficialAbsenceId}, #{date.OADate}, #{date.OAPeriod})
            </foreach>
            ;
            </script>
        """)
    void insertJobSearch(@Param("entity") JobSearchEntity jobSearchEntity, @Param("OfficialAbsenceId") Integer OfficialAbsenceId);
}
//
//form.getOAPeriods().forEach((key, values) -> {
//        System.out.println(key);
//            values.forEach(System.out::println);
//        });