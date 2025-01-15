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
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class MainService {

    @Autowired
    private final MainRepository repository;
    private final RestTemplateAutoConfiguration restTemplateAutoConfiguration;

    final Integer pageSize = 10;
    public final Subject[][] subjects = {
            {new Subject(1,"情報システム演習"),new Subject(2,"情報システム演習"),new Subject(3,"資格対策"),new Subject(4,"資格対策"),new Subject(5,"プレゼンテーション")},
            {new Subject(6,"システム開発Ⅰ"),new Subject(7,"システム開発Ⅰ"),new Subject(8,"IT応用")},
            {new Subject(9,"システム開発Ⅱ"),new Subject(10,"システム開発Ⅱ")},
            {new Subject(13,"システム開発Ⅱ実習"),new Subject(14,"システム開発Ⅱ実習")},
            {new Subject(17,"システム開発Ⅰ実習"),new Subject(18,"システム開発Ⅰ実習")}
    };

    public final Map<String, String> colors = new HashMap<>(){
        {
            put("受理", "list-group-item-success");
            put("未受理", "list-group-item-warning");
            put("却下", "list-group-item-danger");
            put("未提出", "list-group-item-dark");
            put("不要", "list-group-item-light");
        }
    };

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

    public void createOA(OAMainEntity entity){repository.insertMainOA(entity);
    }
    
    public void createOADates(List<OADatesEntity> entity, Integer officialAbsenceId){
        repository.insertOADates(entity, officialAbsenceId);
    }
    
    public void createJobSearch(JobSearchEntity jobSearchEntity) {repository.insertJobSearch(jobSearchEntity);
    }
    
    public void createSeminar(SeminarEntity seminarEntity) {repository.insertSeminar(seminarEntity);
    }
    
    public void createBereavement(BereavementEntity bereavementEntity) {
        repository.insertBereavement(bereavementEntity);
    }
    
    public void createAttendanceBan(AttendanceBanEntity attendanceBanEntity) {
        repository.insertAttendanceBan(attendanceBanEntity);
    }
    
    public void createOther(OtherEntity otherEntity) {repository.insertOther(otherEntity);
    }
    
    public void createSubmitted(Integer officialAbsenceId) {
        repository.createSubmittedDate(officialAbsenceId,LocalDate.now());
    }
    //報告書作成(検索用仮データインサート)
    
    public void createReport(Integer officialAbsenceId, boolean reportRequired) {
        repository.createReport(officialAbsenceId, reportRequired);
    }

    //List取得
    public List<OAListEntity> findAllOAs(Integer studentId, StudentOASearchForm form){return repository.selectAll(studentId, form);
    }
    public List<OAListEntity> teacherFindAllOAs(TeacherOASearchForm form, Integer page, Integer pageSize){
        return repository.teacherFindAllOAs(form, page, pageSize);
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
    
    public void deleteDate(Integer OAId) {repository.deleteDate(OAId);
    }
    
    public void deleteMain(Integer OAId) {repository.deleteMain(OAId);
    }
    
    public void deleteJobSearch(Integer OAId) {repository.deleteJobSearch(OAId);
    }
    
    public void deleteSeminar(Integer OAId) {repository.deleteSeminar(OAId);
    }
    
    public void deleteBereavement(Integer OAId) {repository.deleteBereavement(OAId);
    }
    
    public void deleteAttendanceBan(Integer OAId) {repository.deleteAttendanceBan(OAId);
    }
    
    public void deleteOther(Integer OAId) {repository.deleteOther(OAId);
    }
    
    public void deleteSubmittedDate(Integer OAId) {repository.deleteSubmittedDate(OAId);
    }
    //再提出
    
    public void updateSubmittedDate(Integer OAId) {repository.updateSubmittedDate(OAId, LocalDate.now());
    }
    
    public void updateOADates(List<OADatesEntity> dateList, Integer OAId) {repository.updateOADates(dateList, OAId);
    }
    
    public void updateJobSearch(JobSearchEntity jobEntity) {repository.updateJobSearch(jobEntity);
    }
    
    public void updateSeminar(SeminarEntity seminar) {repository.updateSeminar(seminar);
    }
    
    public void updateBereavement(BereavementEntity bereavement) {repository.updateBereavement(bereavement);
    }
    
    public void updateAttendanceBan(AttendanceBanEntity attendanceBan) {
        repository.updateAttendanceBan(attendanceBan);
    }
    
    public void updateOther(OtherEntity other) {repository.updateOther(other);
    }
    //ステータス更新
    
    public void updateOAStatus(Integer OAId, String status) {repository.updateOAStatus(OAId, status);
    }
    
    public void updateReportRequired(Integer OAId, boolean flag) {repository.updateReportRequired(OAId, flag);
    }
    
    public void updateCheck(Integer OAId, String type, Boolean check) {
        repository.updateCheck(OAId, type, check);
        boolean careerCheckRequired = getCareerCheckRequired(OAId);
        if(careerCheckRequired) {
            if(checkConditionJudge(OAId, true)){updateOAStatus(OAId, "acceptance");}
        }else{
            if(checkConditionJudge(OAId, false)){updateOAStatus(OAId, "acceptance");}
        }
    }
    public boolean getCareerCheckRequired(Integer OAId) {
        return repository.getCareerCheckRequired(OAId);
    }

    //重複データを排除するために、ListをMapにするメソッド
    public Map<String, List<OALessonsDTO>> toLessonInfoDTO(List<OADateInfoEntity> allInfoDTO) {
        return allInfoDTO.stream().collect(
                Collectors.groupingBy(OADateInfoEntity::officialAbsenceDate,LinkedHashMap::new, toList())
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
                        existsReport(k.status()),
                        k.reason().getJapaneseName(),
                        existsReport(k.reportStatus()),
                        k.reportRequired(),
                        dateFormat(k.startDate()),
                        dateFormat(k.endDate()),
                        k.endDate().isBefore(today) || k.endDate().isEqual(today),
                        k.jobSearchVisitStartHour(),
                        k.jobSearchVisitStartMinute(),
                        k.seminarVisitStartHour(),
                        k.seminarVisitStartMinute()
                        ),LinkedHashMap::new, toList())).entrySet().stream()
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
                        padLeft(String.valueOf(entry.getKey().get(13)),2,'0'),
                        padLeft(String.valueOf(entry.getKey().get(14)),2,'0'),
                        padLeft(String.valueOf(entry.getKey().get(15)),2,'0'),
                        padLeft(String.valueOf(entry.getKey().get(16)),2,'0'),
                        entry.getValue().stream().map(OAListEntity::period).collect(toList())
                )).toList();
    }

    public static String padLeft(String target, int length, char padChar) {
        int targetLength = target.length();
        if (targetLength >= length) {
            return target;
        }
        return String.valueOf(padChar).repeat(length - targetLength) + target;
    }

    //yyyy-mm-ddをyyyy年mm月dd日(曜日)にする
    public static String dateFormat(LocalDate date) {
        String yyyy = String.valueOf(date.getYear());
        String mm = String.valueOf(date.getMonthValue());
        String dd = String.valueOf(date.getDayOfMonth());
        String dow = switch (date.getDayOfWeek()) {
            case SUNDAY -> "(日)";
            case MONDAY -> "(月)";
            case TUESDAY -> "(火)";
            case WEDNESDAY -> "(水)";
            case THURSDAY -> "(木)";
            case FRIDAY -> "(金)";
            case SATURDAY -> "(土)";
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
                Collectors.groupingBy(OADateInfoEntity::officialAbsenceDate,LinkedHashMap::new, toList())
        ).entrySet().stream().collect(
            Collectors.toMap(
                e->e.getKey().toString().replaceAll("-",""),
                e->e.getValue().stream().map(v->v.period().toString()).toList()
            )
        );
    }

    public String postOA(BindingResult bindingResult, HttpServletRequest request, OAMainForm form, Model model) {
        Student student = (Student) request.getAttribute("student");
        Integer studentId = student.getGno();
        model.addAttribute("studentId",studentId);
        model.addAttribute("mode", "create");
        List<ExceptionDateEntity> exceptionDates = getExceptionDates();
        //学校で使う用
        //Subject[][] subjects = service.getSubjectArr(service.setClassification(student));
        model.addAttribute("subjects", subjects);
        model.addAttribute("exceptionDates", exceptionDates);
        if(bindingResult.hasErrors()){
            System.out.println("error");
            return "OAForm";
        }
        OAMainEntity mainEntity = form.toMainEntity(studentId, student);
        createOA(mainEntity);
        Integer officialAbsenceId = mainEntity.getOfficialAbsenceId();
        List<OADatesEntity> dateList = form.toDatesEntity();
        createSubmitted(officialAbsenceId);
        createOADates(dateList, officialAbsenceId);
        createReport(officialAbsenceId, mainEntity.getReportRequired());
        switch (mainEntity.getReason()){
            case jobSearch -> {
                JobSearchEntity jobSearchEntity = form.toJobSearchEntity(officialAbsenceId);
                createJobSearch(jobSearchEntity);
            }
            case seminar -> {
                SeminarEntity seminarEntity = form.toSeminarEntity(officialAbsenceId);
                createSeminar(seminarEntity);
            }
            case bereavement -> {
                BereavementEntity bereavementEntity = form.toBereavementEntity(officialAbsenceId);
                createBereavement(bereavementEntity);
            }
            case attendanceBan -> {
                AttendanceBanEntity attendanceBanEntity = form.toAttendanceBanEntity(officialAbsenceId);
                createAttendanceBan(attendanceBanEntity);
            }
            case other -> {
                OtherEntity otherEntity = form.toOtherEntity(officialAbsenceId);
                createOther(otherEntity);
            }
        }
        return "redirect:/jobportal/student/{studentId}/OAList";
    }

    public String getStudentOAList(Integer studentId, StudentOASearchForm form, Model model) {
        List<OAListEntity> listEntity = findAllOAs(studentId, form);
        if(!listEntity.isEmpty()) {
            List<OAListDTO> listDTO = toListEntity(listEntity);
            model.addAttribute("mainList", listDTO);
        }
        model.addAttribute("searchForm", form);
        model.addAttribute("colors", colors);
        return "OAList";
    }

    public String getOAInfo(OAMainInfoEntity mainInfoEntity, List<OADateInfoEntity> dateInfoEntities, Integer OAId, Model model, String teacher, String mode) {
        if(!dateInfoEntities.isEmpty()){
            Map<String, List<OALessonsDTO>> lessonInfoEntities = toLessonInfoDTO(dateInfoEntities);
            OAMainInfoDTO mainInfoDTO = mainInfoEntity.toInfoDTO();
            switch (mainInfoEntity.reason()){
                case jobSearch -> {
                    JobSearchEntity  jobSearch = findJobSearchInfo(OAId);
                    model.addAttribute("jobSearchInfo", jobSearch);
                }
                case seminar -> {
                    SeminarEntity seminar = findSeminarInfo(OAId);
                    model.addAttribute("seminarInfo", seminar);
                }
                case bereavement -> {
                    BereavementEntity bereavement = findBereavementInfo(OAId);
                    model.addAttribute("bereavementInfo", bereavement);
                }
                case attendanceBan -> {
                    AttendanceBanEntity attendanceBan = findAttendanceBanInfo(OAId);
                    model.addAttribute("attendanceBanInfo", attendanceBan);
                }
                case other -> {
                    OtherEntity other = findOtherInfo(OAId);
                    model.addAttribute("otherInfo", other);
                }
            }
            model.addAttribute("lessonInfo", lessonInfoEntities);
            model.addAttribute("mainInfo", mainInfoDTO);
            model.addAttribute("mode", mode);
        }
        return teacher + "OAInfo";
    }
    public String getOAInfoByVersion(OAMainInfoEntity mainInfoEntity, List<OADateInfoEntity> dateInfoEntities, Integer OAId, Model model, Integer version) {
        if(!dateInfoEntities.isEmpty()){
            Map<String, List<OALessonsDTO>> lessonInfoEntities = toLessonInfoDTO(dateInfoEntities);
            OAMainInfoDTO mainInfoDTO = mainInfoEntity.toInfoDTO();
            switch (mainInfoEntity.reason()){
                case jobSearch -> {
                    JobSearchEntity  jobSearch = findJobSearchInfoByVersion(OAId, version);
                    model.addAttribute("jobSearchInfo", jobSearch);
                }
                case seminar -> {
                    SeminarEntity seminar = findSeminarInfoByVersion(OAId, version);
                    model.addAttribute("seminarInfo", seminar);
                }
                case bereavement -> {
                    BereavementEntity bereavement = findBereavementInfoByVersion(OAId, version);
                    model.addAttribute("bereavementInfo", bereavement);
                }
                case attendanceBan -> {
                    AttendanceBanEntity attendanceBan = findAttendanceBanInfoByVersion(OAId, version);
                    model.addAttribute("attendanceBanInfo", attendanceBan);
                }
                case other -> {
                    OtherEntity other = findOtherInfoByVersion(OAId, version);
                    model.addAttribute("otherInfo", other);
                }
            }
            model.addAttribute("lessonInfo", lessonInfoEntities);
            model.addAttribute("mainInfo", mainInfoDTO);
            model.addAttribute("mode", "read");
        }
        return "OAInfo";
    }

    public String rePostOA(BindingResult bindingResult, Integer OAId, OAMainForm form) {
        if(bindingResult.hasErrors()){
            System.out.println("error");
            return "OAForm";
        }
        updateSubmittedDate(OAId);
        List<OADatesEntity> dateList = form.toDatesEntity();
        updateOADates(dateList, OAId);
        switch (OAReason.valueOf(form.getReasonForAbsence())){
            case jobSearch -> {
                JobSearchEntity jobSearch = form.toJobSearchEntity(OAId);
                updateJobSearch(jobSearch);
            }
            case seminar -> {
                SeminarEntity seminar = form.toSeminarEntity(OAId);
                updateSeminar(seminar);
            }
            case bereavement -> {
                BereavementEntity bereavement = form.toBereavementEntity(OAId);
                updateBereavement(bereavement);
            }
            case attendanceBan -> {
                AttendanceBanEntity attendanceBan = form.toAttendanceBanEntity(OAId);
                updateAttendanceBan(attendanceBan);
            }
            case other -> {
                OtherEntity other = form.toOtherEntity(OAId);
                updateOther(other);
            }
        }
        updateOAStatus(OAId, "unaccepted");
        updateCheck(OAId, "teacher", false);
        updateCheck(OAId, "career", false);
        return "redirect:/jobportal/student/{studentId}/OAList";
    }

    public String getTeacherOAList(Integer currentPage, TeacherOASearchForm form, Model model, HttpSession session) {
        if (currentPage == 0) {
            Integer sessionPage = (Integer) session.getAttribute("currentPage");
            currentPage = (sessionPage == null) ? 1 : sessionPage;
        }
        session.setAttribute("currentPage", currentPage);
        List<OAListEntity> listEntity = teacherFindAllOAs(form, currentPage, pageSize);
        if(!listEntity.isEmpty()) {
            List<OAListDTO> listDTO = toListEntity(listEntity);
            model.addAttribute("mainList", listDTO);
        }
        Integer count = countSearchOA(form);
        int maxSize = (int)Math.ceil((double) count / pageSize);
        int displayPageCount = Math.min(maxSize, 5);
        int start = Math.max(1, Math.min(currentPage - (displayPageCount - 1) / 2, maxSize - displayPageCount + 1));
        int end = Math.min(maxSize, start + displayPageCount - 1);
        model.addAttribute("searchForm", form);
        model.addAttribute("colors", colors);
        model.addAttribute("maxSize", maxSize);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("start", start);
        model.addAttribute("end", end);
        model.addAttribute("mode", "list");
        return "teacher_OAList";
    }

    public void searchStudent(String studentId) {
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


//    
//    public void insertOADates(exampleForm form){
//        repository.insert(form);
//    }

//    
//    public void saveDate(OADatesEntity datesEntity) {
//        datesEntity.OAPeriods().forEach((key, values) -> {
//            System.out.println(key + ": " + values);
//            values.forEach(value->repository.insertOADates(key, value));
//        });
//    }

    public String postReport(BindingResult bindingResult, HttpServletRequest request, OAMainForm form, Model model) {
        Student student = (Student) request.getAttribute("student");
        Integer studentId = student.getGno();
        model.addAttribute("studentId",studentId);
        model.addAttribute("mode", "create");
        List<ExceptionDateEntity> exceptionDates = getExceptionDates();
        //学校で使う用
        //Subject[][] subjects = service.getSubjectArr(service.setClassification(student));
        model.addAttribute("subjects", subjects);
        model.addAttribute("exceptionDates", exceptionDates);
        if(bindingResult.hasErrors()){
            System.out.println("error");
            return "OAForm";
        }
        OAMainEntity mainEntity = form.toMainEntity(studentId, student);
        createOA(mainEntity);
        Integer officialAbsenceId = mainEntity.getOfficialAbsenceId();
        List<OADatesEntity> dateList = form.toDatesEntity();
        createSubmitted(officialAbsenceId);
        createOADates(dateList, officialAbsenceId);
        createReport(officialAbsenceId, mainEntity.getReportRequired());
        switch (mainEntity.getReason()){
            case jobSearch -> {
                JobSearchEntity jobSearchEntity = form.toJobSearchEntity(officialAbsenceId);
                createJobSearch(jobSearchEntity);
            }
            case seminar -> {
                SeminarEntity seminarEntity = form.toSeminarEntity(officialAbsenceId);
                createSeminar(seminarEntity);
            }
            case bereavement -> {
                BereavementEntity bereavementEntity = form.toBereavementEntity(officialAbsenceId);
                createBereavement(bereavementEntity);
            }
            case attendanceBan -> {
                AttendanceBanEntity attendanceBanEntity = form.toAttendanceBanEntity(officialAbsenceId);
                createAttendanceBan(attendanceBanEntity);
            }
            case other -> {
                OtherEntity otherEntity = form.toOtherEntity(officialAbsenceId);
                createOther(otherEntity);
            }
        }
        return "redirect:/jobportal/student/{studentId}/OAList";
    }
}
