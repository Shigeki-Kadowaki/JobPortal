package com.jobportal.JobPortal.Service;

import com.jobportal.JobPortal.Controller.DesiredOccupation;
import com.jobportal.JobPortal.Controller.Form.*;
import com.jobportal.JobPortal.Controller.Student;
import com.jobportal.JobPortal.Controller.Subject;
import com.jobportal.JobPortal.Repository.MainRepository;
import com.jobportal.JobPortal.Service.DTO.OALessonsDTO;
import com.jobportal.JobPortal.Service.DTO.OAListDTO;
import com.jobportal.JobPortal.Service.DTO.OAMainInfoDTO;
import com.jobportal.JobPortal.Service.Entity.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

@Service
@RequiredArgsConstructor
public class MainService {

    @Autowired
    private final MainRepository repository;
    private final RestTemplateAutoConfiguration restTemplateAutoConfiguration;

    public static String weekDayFormat(Integer number) {
        return switch (number) {
            case 1 -> "月";
            case 2 -> "火";
            case 3 -> "水";
            case 4 -> "木";
            case 5 -> "金";
            default -> "";
        };
    }

    //OA作成
    @Transactional
    public void createOA(OAMainEntity entity){repository.insertMainOA(entity);
    }
    @Transactional
    public void createOADates(List<OADatesEntity> entity, Integer officialAbsenceId){
        repository.insertOADates(entity, officialAbsenceId);
    }
    @Transactional
    public void createJobSearch(JobSearchEntity jobSearchEntity) {repository.insertJobSearch(jobSearchEntity);
    }
    @Transactional
    public void createSeminar(SeminarEntity seminarEntity) {repository.insertSeminar(seminarEntity);
    }
    @Transactional
    public void createBereavement(BereavementEntity bereavementEntity) {
        repository.insertBereavement(bereavementEntity);
    }
    @Transactional
    public void createAttendanceBan(AttendanceBanEntity attendanceBanEntity) {
        repository.insertAttendanceBan(attendanceBanEntity);
    }
    @Transactional
    public void createOther(OtherEntity otherEntity) {repository.insertOther(otherEntity);
    }
    @Transactional
    public void createSubmitted(Integer officialAbsenceId) {
        repository.createSubmittedDate(officialAbsenceId,LocalDate.now());
    }
    //報告書作成(検索用仮データインサート)
    @Transactional
    public void createReport(Integer officialAbsenceId, boolean reportRequired) {
        repository.createReport(officialAbsenceId, reportRequired);
    }
    //List取得
    public List<OAListEntity> findAllOAs(Integer studentId, StudentOASearchForm form){return repository.selectAll(studentId, form);
    }
    public List<OAListEntity> teacherFindAllOAs(TeacherOASearchForm form, Integer page, Integer pageSize){return repository.teacherFindAllOAs(form, page, pageSize);
    }
    //info取得
    public OAMainInfoEntity findMainInfo(Integer oaId) {return  repository.selectMainInfo(oaId);
    }
    public List<OADateInfoEntity> findDateInfo(Integer oaId) {return repository.selectDateInfo(oaId);
    }
    public JobSearchEntity findJobSearchInfo(Integer oaId) {return repository.selectJobSearchInfo(oaId);
    }
    public SeminarEntity findSeminarInfo(Integer oaId) {return repository.selectSeminarInfo(oaId);
    }
    public BereavementEntity findBereavementInfo(Integer oaId) {return repository.selectBereavementInfo(oaId);
    }
    public AttendanceBanEntity findAttendanceBanInfo(Integer oaId) {return repository.selectAttendanceBanInfo(oaId);
    }
    public OtherEntity findOtherInfo(Integer oaId) {return repository.selectOtherInfo(oaId);
    }
    //過去versionInfo取得
    public OAMainInfoEntity findMainInfoByVersion(Integer oaId, Integer version) {return  repository.selectMainInfoByVersion(oaId, version);
    }
    public List<OADateInfoEntity> findDateInfoByVersion(Integer oaId, Integer version) {return repository.selectDateInfoByVersion(oaId, version);
    }
    public JobSearchEntity findJobSearchInfoByVersion(Integer oaId, Integer version) {return repository.selectJobSearchInfoByVersion(oaId, version);
    }
    public SeminarEntity findSeminarInfoByVersion(Integer oaId, Integer version) {return repository.selectSeminarInfoByVersion(oaId, version);
    }
    public BereavementEntity findBereavementInfoByVersion(Integer oaId, Integer version) {return repository.selectBereavementInfoByVersion(oaId, version);
    }
    public AttendanceBanEntity findAttendanceBanInfoByVersion(Integer oaId, Integer version) {return repository.selectAttendanceBanInfoByVersion(oaId, version);
    }
    public OtherEntity findOtherInfoByVersion(Integer oaId, Integer version) {return repository.selectOtherInfoByVersion(oaId, version);
    }
    //削除
    @Transactional
    public void deleteDate(Integer OAId) {repository.deleteDate(OAId);
    }
    @Transactional
    public void deleteMain(Integer OAId) {repository.deleteMain(OAId);
    }
    @Transactional
    public void deleteJobSearch(Integer OAId) {repository.deleteJobSearch(OAId);
    }
    @Transactional
    public void deleteSeminar(Integer OAId) {repository.deleteSeminar(OAId);
    }
    @Transactional
    public void deleteBereavement(Integer OAId) {repository.deleteBereavement(OAId);
    }
    @Transactional
    public void deleteAttendanceBan(Integer OAId) {repository.deleteAttendanceBan(OAId);
    }
    @Transactional
    public void deleteOther(Integer OAId) {repository.deleteOther(OAId);
    }
    @Transactional
    public void deleteSubmittedDate(Integer OAId) {repository.deleteSubmittedDate(OAId);
    }
    //再提出
    @Transactional
    public void updateSubmittedDate(Integer OAId) {repository.updateSubmittedDate(OAId, LocalDate.now());
    }
    @Transactional
    public void updateOADates(List<OADatesEntity> dateList, Integer OAId) {repository.updateOADates(dateList, OAId);
    }
    @Transactional
    public void updateJobSearch(JobSearchEntity jobEntity) {repository.updateJobSearch(jobEntity);
    }
    @Transactional
    public void updateSeminar(SeminarEntity seminar) {repository.updateSeminar(seminar);
    }
    @Transactional
    public void updateBereavement(BereavementEntity bereavement) {repository.updateBereavement(bereavement);
    }
    @Transactional
    public void updateAttendanceBan(AttendanceBanEntity attendanceBan) {
        repository.updateAttendanceBan(attendanceBan);
    }
    @Transactional
    public void updateOther(OtherEntity other) {repository.updateOther(other);
    }
    //ステータス更新
    @Transactional
    public void updateOAStatus(Integer OAId, String status) {repository.updateOAStatus(OAId, status);
    }
    @Transactional
    public void updateReportRequired(Integer OAId, boolean flag) {repository.updateReportRequired(OAId, flag);
    }
    @Transactional
    public void updateCheck(Integer OAId, String type, boolean check) {repository.updateCheck(OAId, type, check);
    }

    //重複データを排除するために、ListをMapにするメソッド
    public Map<String, List<OALessonsDTO>> toLessonInfoDTO(List<OADateInfoEntity> allInfoDTO) {
        return allInfoDTO.stream().collect(
                Collectors.groupingBy(OADateInfoEntity::officialAbsenceDate)
        ).entrySet().stream().collect(
                Collectors.toMap(
                        e->e.getKey().toString(),
                        e->e.getValue().stream().map(v-> new OALessonsDTO(v.period(),v.lessonName())).toList()
                )
        );
    }
    //公欠授業をリスト化
    public List<OAListDTO> toListEntity(List<OAListEntity> listEntity) {
        LocalDate today = LocalDate.now();
        return listEntity.stream()
                .collect(Collectors.groupingBy(k -> Arrays.asList(
                        k.officialAbsenceId(),
                        k.studentId(),
                        k.grade(),
                        k.classroom(),
                        k.course(),
                        k.name(),
                        k.status(),
                        k.reason(),
                        k.reportStatus(),
                        k.reportRequired(),
                        k.startDate(),
                        k.endDate(),
                        k.endDate().isBefore(today) || k.endDate().isEqual(today)
                        ))).entrySet().stream()
                .map(entry -> new OAListDTO(
                        (Integer) entry.getKey().get(0),
                        (Integer) entry.getKey().get(1),
                        (Integer) entry.getKey().get(2),
                        entry.getKey().get(3).toString(),
                        entry.getKey().get(4).toString(),
                        entry.getKey().get(5).toString(),
                        entry.getKey().get(6).toString(),
                        entry.getKey().get(7).toString(),
                        entry.getKey().get(8).toString(),
                        (Boolean) entry.getKey().get(9),
                        entry.getKey().get(10).toString(),
                        entry.getKey().get(11).toString(),
                        (Boolean)entry.getKey().get(12),
                        entry.getValue().stream().map(OAListEntity::period).collect(Collectors.toList())
                )).toList();
    }
    //yyyy-mm-ddをyyyy年mm月dd日(曜日)にする
    public static String dateFormat(LocalDate date) {
        String yyyy = date.toString().substring(0,4);
        String mm = date.toString().substring(5,7);
        String dd = date.toString().substring(8,10);
        String dow = "";
        Calendar cal = Calendar.getInstance();
        cal.set(parseInt(yyyy), parseInt(mm) - 1, parseInt(dd));
        dow = switch (cal.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY -> "(日)";
            case Calendar.MONDAY -> "(月)";
            case Calendar.TUESDAY -> "(火)";
            case Calendar.WEDNESDAY -> "(水)";
            case Calendar.THURSDAY -> "(木)";
            case Calendar.FRIDAY -> "(金)";
            case Calendar.SATURDAY -> "(土)";
            default -> dow;
        };
        return yyyy + "年" + mm + "月" + dd + "日" + dow;
    }
    //statusを日本語化
    public static String existsReport(OAStatus status) {
        if(status == null){
            return "未提出";
        }else {
            return status.getJapaneseName();
        }
    }
    public OAMainForm toJobSearchForm(OAMainInfoDTO mainInfoDTO, Map<String, List<String>> OAPeriods, JobSearchEntity jobSearch) {
        return new OAMainForm(
            jobSearch,
            mainInfoDTO.reason(),
            OAPeriods,
            mainInfoDTO.reportRequired(),
            jobSearch.work().toString(),
            jobSearch.companyName(),
            jobSearch.address(),
            jobSearch.remarks(),
            jobSearch.visitStartHour(),
            jobSearch.visitStartMinute()
        );
    }
    public OAMainForm toSeminarForm(OAMainInfoDTO mainInfoDTO, Map<String, List<String>> OAPeriods, SeminarEntity seminar) {
        return new OAMainForm(
                seminar,
                mainInfoDTO.reason(),
                OAPeriods,
                mainInfoDTO.reportRequired(),
                seminar.seminarName(),
                seminar.location(),
                seminar.venueName(),
                seminar.remarks(),
                seminar.visitStartHour(),
                seminar.visitStartMinute()
        );
    }
    public OAMainForm toBereavementForm(OAMainInfoDTO mainInfoDTO, Map<String, List<String>> OAPeriods, BereavementEntity bereavement) {
        return new OAMainForm(
                bereavement,
                mainInfoDTO.reason(),
                OAPeriods,
                mainInfoDTO.reportRequired(),
                bereavement.remarks(),
                bereavement.deceasedName(),
                bereavement.relationship()
        );
    }
    public OAMainForm toAttendanceBanForm(OAMainInfoDTO mainInfoDTO, Map<String, List<String>> OAPeriods, AttendanceBanEntity ban) {
        return new OAMainForm(
                ban,
                mainInfoDTO.reason(),
                OAPeriods,
                mainInfoDTO.reportRequired(),
                ban.banReason(),
                ban.remarks()
        );
    }
    public OAMainForm toOtherForm(OAMainInfoDTO mainInfoDTO, Map<String, List<String>> OAPeriods, OtherEntity other) {
        return new OAMainForm(
                other,
                mainInfoDTO.reason(),
                OAPeriods,
                mainInfoDTO.reportRequired(),
                other.otherReason(),
                other.remarks()
        );
    }
    public boolean checkConditionJudge(Integer OAId, boolean flag) {
        if(flag){
            return repository.teacherCheckCondition(OAId) && repository.careerCheckCondition(OAId);
        }else{
            return repository.teacherCheckCondition(OAId);
        }
    }

    public Map<String, String> getPersonInfo(HttpServletResponse response, HttpServletRequest request) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, String > map = new HashMap<>();
        List<String> values = new ArrayList<>();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String value = new String(request.getHeader(headerName).getBytes(StandardCharsets.ISO_8859_1),
                    StandardCharsets.UTF_8);
            values.add(value);
            map.put(headerName, value);
        }
        String group;
        if(values.contains("bdab862e-69fc-4932-ab21-96a46e05881f")){
            group = "教職員";
        }else {
            group = "学生";
        }
        map.put("group", group);
//        map.forEach((k,v)->{
//            System.out.println("k: " + k);
//            System.out.println("v: " + v);
//        });
        return map;
    }

    //学生データ取得api呼び出し(javaバージョン。jsバージョンはlist.jsにあります)
    public Student getStudentInfo(Integer studentId){
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://172.16.0.3/api/students/" + studentId;
        ResponseEntity<Student[]> response = restTemplate.exchange(url, HttpMethod.GET, null, Student[].class);
        //System.out.println(list);
        List<Student> sl = Arrays.asList(Objects.requireNonNull(response.getBody()));
        System.out.println(sl.getFirst());
        return sl.getFirst();
    }

    public DesiredOccupation getOccupation(Integer studentId) {
        return repository.selectOccupation(studentId);
    }

    public List<String> getSubjects(ClassificationForm classification) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://172.16.0.3/api/subjects/" + classification.getGrade()+ "/" + classification.getClassroom();
        ResponseEntity<String[]> response = restTemplate.exchange(url, HttpMethod.GET, null, String[].class);
        return Arrays.asList(Objects.requireNonNull(response.getBody()));
    }

    //授業一覧を授業idと授業タイトルに変換
    public Map<Integer, String> toSubjectInfos(List<String> lessons) {
        Map<Integer, String> subjectMap = new HashMap<>();
        Pattern roundParenthesesPattern = Pattern.compile("\\((.*?)\\)");
        Pattern squareParenthesessPattern = Pattern.compile("\\[(.*?)]");
        lessons.forEach(e->{
            //[id] name(course) (teacher) => id
            Matcher idMatcher = squareParenthesessPattern.matcher(e.split(" ")[0]);
            int lessonId = -1;
            if(idMatcher.find()){
                lessonId = parseInt(idMatcher.group(1));
            }
            //[id] name(course) (teacher) => name(course)
            String lessonInfo = e.split(" ")[1];
            subjectMap.put(lessonId, lessonInfo);
            //先生取得は無し
            //[id] name(course) (teacher) => teacher
//            String lessonTeacher = "";
//            if(e.split(" ").length >= 3){
//                Matcher teacherMatcher = roundParenthesesPattern.matcher(e.split(" ")[2]);
//                if(teacherMatcher.find()){
//                    lessonTeacher = teacherMatcher.group(1);
//                }
//            }
            //コース取得は無し
            //[id] name(course) (teacher) => course
//            Matcher courseMatcher = roundParenthesesPattern.matcher(lessonInfo);
//            if(courseMatcher.find()){
//                if(Objects.equals(courseMatcher.group(1), classification.getCourse())){
//                    subjectMap.put(lessonId, lessonInfo);
//                }
//            }else{
//                subjectMap.put(lessonId, lessonInfo);
//            }
        });
        return subjectMap;
    }

    public void createTimeTable(TimeTableInfoForm timeTableInfo, List<TimeTableEntity> timeTableEntity) {
        repository.createTimeTable(timeTableInfo, timeTableEntity);
    }

    public String semesterBetween(LocalDate today) {
        int month = today.getMonthValue();
        //4月から9月の間
        if( 4 <= month && month <= 9) return "second";
        return "first";
    }

    public List<TimeTableEntity> getTimeTable(ClassificationForm classification) {
        return repository.selectTimeTable(classification);
    }

    public List<ExceptionDateEntity> getExceptionDates() {
        return repository.selectExceptionDates();
    }

    public List<String> getCourses() {
        return repository.selectCourses();
    }

    public void createExceptionDate(ExceptionDateEntity exceptionDateEntity) {
        repository.insertExceptionDate(exceptionDateEntity);
    }

    public void deleteExceptionDate(Integer id) {
        repository.deleteExceptionDate(id);
    }

    public void deleteReport(Integer oaId) {
        repository.deleteReport(oaId);
    }

    public Subject[][] getSubjectArr(ClassificationForm classification) {
        //該当区分時間割(id)取得
        List<TimeTableEntity> timeTableEntities = getTimeTable(classification);
        //該当区分授業情報取得
        List<String> subjectAllList = getSubjects(classification);
        Map<Integer, String> subjectMap = toSubjectInfos(subjectAllList);
        Subject[][] subjects = new Subject[5][5];
        for (int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                subjects[i][j] = new Subject(-1, "");
            }
        }
        timeTableEntities.forEach(e->{
            subjects[e.period()-1][e.weekdayNumber()-1] = new Subject(e.subjectId(), subjectMap.get(e.subjectId()));
        });
        return subjects;
    }

    public ClassificationForm setClassification(Student student) {
        //今日が前期か後期か取得
        LocalDate today = LocalDate.now();
        String semester = semesterBetween(today);
        //学生情報セット
        ClassificationForm classification = new ClassificationForm();
        classification.setGrade(student.getGrade());
        classification.setClassroom(student.getClassroom());
        classification.setCourse(student.getCourse());
        classification.setSemester(semester);

        return classification;
    }

    public void updateDesiredBusiness(Integer studentId, String business) {
        repository.updateDesiredBusiness(studentId, business);
    }

    public void updateDesiredOccupation(Integer studentId, String occupation) {
        repository.updateDesiredOccupation(studentId, occupation);
    }

    public boolean existsDesired(Integer studentId) {
        return repository.existsDesired(studentId);
    }

    public void insertDesired(Integer studentId, String business, String occupation) {
        repository.insertDesired(studentId, business, occupation);
    }

    public Integer countOA() {
        return repository.countOA();
    }

    public Integer countSearchOA(TeacherOASearchForm form) {
        return repository.countSearchOA(form);
    }

    public Map<String, List<String>> toOAPeriods(List<OADateInfoEntity> dateInfoEntities) {
        return dateInfoEntities.stream().collect(
                Collectors.groupingBy(OADateInfoEntity::officialAbsenceDate)
        ).entrySet().stream().collect(
            Collectors.toMap(
                e->e.getKey().toString(),
                e->e.getValue().stream().map(v->v.period().toString()).toList()
            )
        );
    }
//    public Map<LocalDate, List<Integer>> toLessonList(List<OAListDTO> list) {
//        Map<LocalDate, List<Integer>> map = new TreeMap<>();
//        List<Integer> lessonList = new ArrayList<>();
//        LocalDate prevDate = list.getFirst().officialAbsenceDate();
//        for (OAListDTO oaListDTO : list) {
//            if(prevDate.toString().equals(oaListDTO.officialAbsenceDate().toString())){
//                lessonList.add(oaListDTO.period());
//            }else {
//                map.put(prevDate, lessonList);
//                prevDate = oaListDTO.officialAbsenceDate();
//                lessonList = new ArrayList<>(List.of(oaListDTO.period()));
//            }
//        }
//        map.put(prevDate, lessonList);
//        return map;
//    }

//    public List<OAMainListDTO> toMainList(List<OAListEntity> list) {
//        return null;
//    }


//    @Transactional
//    public void insertOADates(exampleForm form){
//        repository.insert(form);
//    }

//    @Transactional
//    public void saveDate(OADatesEntity datesEntity) {
//        datesEntity.OAPeriods().forEach((key, values) -> {
//            System.out.println(key + ": " + values);
//            values.forEach(value->repository.insertOADates(key, value));
//        });
//    }
}
