package com.jobportal.JobPortal.Service;

import com.jobportal.JobPortal.Controller.API.OASubject;
import com.jobportal.JobPortal.Controller.API.OASubjectDTO;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.jobportal.JobPortal.Service.OAStatus.unnecessary;
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

    /*
    * 数字から曜日を返す。
    *
    * 基本、使う範囲は1~5まで。
    * */
    public static String weekDayFormat(Integer number) {
        return switch (number) {
            case 0 -> "日";
            case 1 -> "月";
            case 2 -> "火";
            case 3 -> "水";
            case 4 -> "木";
            case 5 -> "金";
            case 6 -> "土";
            default -> "";
        };
    }

    //OA作成
    public void createOA(OAMainEntity entity){
        repository.insertMainOA(entity);
    }
    
    public void createOADates(List<OADatesEntity> entity, Integer officialAbsenceId){
        repository.insertOADates(entity, officialAbsenceId);
    }
    
    public void createJobSearch(JobSearchEntity jobSearchEntity) {
        repository.insertJobSearch(jobSearchEntity);
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
    public List<OAListEntity> findAllOAs(Integer studentId, StudentOASearchForm form){
        return repository.selectAll(studentId, form);
    }
    public List<OAListEntity> teacherFindAllOAs(TeacherOASearchForm form, Integer page, Integer pageSize){
        return repository.teacherFindAllOAs(form, page, pageSize);
    }
    //info取得
    public OAMainInfoEntity findMainInfo(Integer oaId) {
        return  repository.selectMainInfo(oaId);
    }
    public List<OADateInfoEntity> findDateInfo(Integer oaId) {
        return repository.selectDateInfo(oaId);
    }
    public JobSearchEntity findJobSearchInfo(Integer oaId) {
        return repository.selectJobSearchInfo(oaId);
    }
    public SeminarEntity findSeminarInfo(Integer oaId) {
        return repository.selectSeminarInfo(oaId);
    }
    public BereavementEntity findBereavementInfo(Integer oaId) {
        return repository.selectBereavementInfo(oaId);
    }
    public AttendanceBanEntity findAttendanceBanInfo(Integer oaId) {
        return repository.selectAttendanceBanInfo(oaId);
    }
    public OtherEntity findOtherInfo(Integer oaId) {
        return repository.selectOtherInfo(oaId);
    }
    //過去versionInfo取得
    public OAMainInfoEntity findMainInfoByVersion(Integer oaId, Integer version) {
        return  repository.selectMainInfoByVersion(oaId, version);
    }
    public List<OADateInfoEntity> findDateInfoByVersion(Integer oaId, Integer version) {
        return repository.selectDateInfoByVersion(oaId, version);
    }
    public JobSearchEntity findJobSearchInfoByVersion(Integer oaId, Integer version) {
        return repository.selectJobSearchInfoByVersion(oaId, version);
    }
    public SeminarEntity findSeminarInfoByVersion(Integer oaId, Integer version) {
        return repository.selectSeminarInfoByVersion(oaId, version);
    }
    public BereavementEntity findBereavementInfoByVersion(Integer oaId, Integer version) {
        return repository.selectBereavementInfoByVersion(oaId, version);
    }
    public AttendanceBanEntity findAttendanceBanInfoByVersion(Integer oaId, Integer version) {
        return repository.selectAttendanceBanInfoByVersion(oaId, version);
    }
    public OtherEntity findOtherInfoByVersion(Integer oaId, Integer version) {
        return repository.selectOtherInfoByVersion(oaId, version);
    }
    //削除
    
    public void deleteDate(Integer OAId) {
        repository.deleteDate(OAId);
    }
    
    public void deleteMain(Integer OAId) {
        repository.deleteMain(OAId);
    }
    
    public void deleteJobSearch(Integer OAId) {
        repository.deleteJobSearch(OAId);
    }
    
    public void deleteSeminar(Integer OAId) {
        repository.deleteSeminar(OAId);
    }
    
    public void deleteBereavement(Integer OAId) {
        repository.deleteBereavement(OAId);
    }
    
    public void deleteAttendanceBan(Integer OAId) {
        repository.deleteAttendanceBan(OAId);
    }
    
    public void deleteOther(Integer OAId) {
        repository.deleteOther(OAId);
    }
    
    public void deleteSubmittedDate(Integer OAId) {
        repository.deleteSubmittedDate(OAId);
    }
    //再提出
    
    public void updateOASubmittedDate(Integer OAId) {
        repository.updateSubmittedDate(OAId, LocalDate.now());
    }
    
    public void updateOADates(List<OADatesEntity> dateList, Integer OAId) {
        repository.updateOADates(dateList, OAId);
    }
    
    public void updateJobSearch(JobSearchEntity jobEntity) {
        repository.updateJobSearch(jobEntity);
    }
    
    public void updateSeminar(SeminarEntity seminar) {
        repository.updateSeminar(seminar);
    }
    
    public void updateBereavement(BereavementEntity bereavement) {
        repository.updateBereavement(bereavement);
    }
    
    public void updateAttendanceBan(AttendanceBanEntity attendanceBan) {
        repository.updateAttendanceBan(attendanceBan);
    }
    
    public void updateOther(OtherEntity other) {
        repository.updateOther(other);
    }
    //ステータス更新
    
    public void updateOAStatus(Integer OAId, String status) {
        repository.updateOAStatus(OAId, status);
    }
    
    public void updateReportRequired(Integer OAId, boolean flag) {
        repository.updateReportRequired(OAId, flag);
    }

    /*
    * 第2引数で受け取った先生タイプのチェックを、第3引数に変える。
    *
    * 先生タイプは"teacher"か"career"
    * 承認されたらtrue、再提出されたらfalseになる。
    * チェックが変わったタイミングでその公欠届が承認済みか判定し、承認済みなら公欠届のステータスを変える。
    * */
    public void updateCheck(Integer OAId, String type, Boolean check) {
        repository.updateCheck(OAId, type, check);
        //該当公欠届のcareerチェックが必須かどうか取得。
        boolean careerCheckRequired = getCareerCheckRequired(OAId);
        //careerチェックが必須ならteacherとcareerのチェックを確認。
        //careerチェックが不要ならteacherのみのチェックを確認。
        if(careerCheckRequired) {
            if(checkConditionJudge(OAId, true)){updateOAStatus(OAId, "acceptance");}
        }else{
            if(checkConditionJudge(OAId, false)){updateOAStatus(OAId, "acceptance");}
        }
    }
    //該当公欠届のcareerチェックが必須かどうか返す。
    public boolean getCareerCheckRequired(Integer OAId) {
        return repository.getCareerCheckRequired(OAId);
    }

    /*
    * 重複データを排除するために、ListをMapにするメソッド
    *
    * データベースには2次元構造でデータが入っているが、htmlで扱いやすくするためにMapにする。
    * keyは公欠日、valueは公欠授業とその名前のリスト。
    * 公欠日はLocalDateから日本語のデータ(yyyy年MM月dd日(曜日))にフォーマットする。
    * */
    public Map<String, List<OALessonsDTO>> toLessonInfoDTO(List<OADateInfoEntity> allInfoDTO) {
        return allInfoDTO.stream().collect(
                Collectors.groupingBy(OADateInfoEntity::officialAbsenceDate,LinkedHashMap::new, toList())
        ).entrySet().stream().collect(
                Collectors.toMap(
                        e->dateFormat(e.getKey()),
                        e->e.getValue().stream().map(v-> new OALessonsDTO(v.period(),v.lessonName())).toList()
                )
        );
    }
    /*
    *　重複データを排除するために、公欠授業をListにするメソッド。
    *
    *　この方法よりも、最初に授業以外のデータを取得して後から授業データをくっつけるほうが効率がいいので、時間があったらリファクタリングする。
    *
    * */
    public List<OAListDTO> toListEntity(List<OAListEntity> listEntity) {
        LocalDate today = LocalDate.now();
        return listEntity.stream()
                .collect(Collectors.groupingBy(k -> Arrays.asList(
                        k.officialAbsenceId(),
                        k.reportId(),
                        k.studentId(),
                        k.grade(),
                        k.classroom(),
                        k.course(),
                        k.name(),
                        existsReport(k.OAStatus()),
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
                ), LinkedHashMap::new, toList())).entrySet().stream()
                .map(entry -> new OAListDTO(
                        (Integer) entry.getKey().get(0),
                        (Integer) entry.getKey().get(1),
                        (Integer) entry.getKey().get(2),
                        (Integer) entry.getKey().get(3),
                        entry.getKey().get(4).toString(),
                        entry.getKey().get(5).toString(),
                        entry.getKey().get(6).toString(),
                        entry.getKey().get(7).toString(),
                        entry.getKey().get(8).toString(),
                        entry.getKey().get(9).toString(),
                        (Boolean) entry.getKey().get(10),
                        entry.getKey().get(11).toString(),
                        entry.getKey().get(12).toString(),
                        (Boolean) entry.getKey().get(13),
                        padLeft(String.valueOf(entry.getKey().get(14)), 2, '0'),
                        padLeft(String.valueOf(entry.getKey().get(15)), 2, '0'),
                        padLeft(String.valueOf(entry.getKey().get(16)), 2, '0'),
                        padLeft(String.valueOf(entry.getKey().get(17)), 2, '0'),
                        entry.getValue().stream().map(OAListEntity::period).collect(toList())
                )).toList();
    }

    /*
    * targetの文字列長がlengthになるまでpadCharを付けて返すメソッド。
    *
    * 主に時間のフォーマットで使う。例 : 9:5→09:05
    * */
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

    /*
    * 就活公欠再提出Form用のデータを返すメソッド。
    * */
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
    /*
     * セミナー公欠再提出Form用のデータを返すメソッド。
     * */
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
    /*
     * 忌引公欠再提出Form用のデータを返すメソッド。
     * */
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
    /*
     * 出席停止公欠再提出Form用のデータを返すメソッド。
     * */
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
    /*
     * その他公欠再提出Form用のデータを返すメソッド。
     * */
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
    /*
    * 該当公欠届のチェック状況を返すメソッド。
    *
    * flagがtrueならteacherとcareerの、falseならteacherのみのチェック状況を返す。
    * flagはcareerCheckRequiredによる。
    * */
    public boolean checkConditionJudge(Integer OAId, boolean flag) {
        if(flag){
            return repository.teacherCheckCondition(OAId) && repository.careerCheckCondition(OAId);
        }else{
            return repository.teacherCheckCondition(OAId);
        }
    }
    /*
    * アクセスしたアカウントの情報をヘッダーから取得し、返すメソッド。
    *
    * SSOのデータには生徒か教職員かは直接記入されていないため、その判定も行う。
    * */
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
        return map;
    }

    /*
    * 学生データ取得api呼び出し(javaバージョン。jsバージョンはlist.jsにあります)
    *
    * 学籍番号から学年、クラス、出席番号、学科名、コース名を取得できる。
    *
    * */
    public Student getStudentInfo(Integer studentId){
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://172.16.0.3/api/students/" + studentId;
        ResponseEntity<Student[]> response = restTemplate.exchange(url, HttpMethod.GET, null, Student[].class);
        List<Student> sl = Arrays.asList(Objects.requireNonNull(response.getBody()));
        return sl.getFirst();
    }
    public DesiredOccupation getOccupation(Integer studentId) {
        return repository.selectOccupation(studentId);
    }
    /*
    * 科目一覧取得api呼び出し
    *
    * 学年・クラスから科目一覧を取得できる。
    * */
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
    //現在の日付が前期か後期か判定する。
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
    //報告書を破棄する
    //報告書のステータスをunsubmitted(未提出)にする。
    //あくまでも報告書を未提出状態に戻すだけで、報告書テーブルには残る。
    @Transactional
    public void deleteReports(Integer reportId) {
        ReportInfoEntity info = repository.selectReportInfo(reportId);
        if(!info.getStatus().equals(unnecessary)){
            switch (info.getReason()){
                case jobInterview -> {
                    repository.deleteReportJobInterview(reportId);
                    repository.deleteJobFutureSelection(reportId);
                }
                case briefing -> {
                    repository.deleteReportBriefing(reportId);
                    repository.deleteJobFutureSelection(reportId);
                }
                case test -> {
                    repository.deleteReportTest(reportId);
                    repository.deleteJobFutureSelection(reportId);
                }
                case informalCeremony -> {
                    repository.deleteReportInformalCeremony(reportId);
                    repository.deleteJobFutureSelection(reportId);
                }
                case training -> {
                    repository.deleteReportTraining(reportId);
                    repository.deleteJobFutureSelection(reportId);
                }
                case jobOther -> {
                    repository.deleteReportJobOther(reportId);
                    repository.deleteJobFutureSelection(reportId);
                }
                case seminar -> {
                    repository.deleteReportSeminar(reportId);
                }
            }
            repository.deleteReportHistories(reportId);
            repository.updateReportStatus(reportId, "unsubmitted");
        }
    }
    /*
    * 学生情報区分から時間割を取得する。
    *
    * 授業がない箇所は(id:-1,name:"")になる。
    * */
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
    //生徒クラスから生徒の区分をセットする。
    //時間割を取得するのに必要。
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
    /*
    * 重複データを排除するために、ListをMapにするメソッド。
    *
    * keyは公欠日、valueは公欠時限のList。
    *
    * */
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
    /*
    * 公欠届提出メソッド
    *
    * postされたデータに学生情報を追加し、公欠届を作成、インサートする。
    * その他には、提出日、公欠日、報告書、公欠特有データもインサートする。
    * 報告書はあくまでもステータス取得用の仮データをインサートする。
    * */
    @Transactional
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
    /*
    * 公欠届List取得メソッド
    *
    *
    * */
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
    /*
    * 公欠届詳細取得メソッド
    *
    *
    * */
    public String getOAInfo(OAMainInfoEntity mainInfoEntity, List<OADateInfoEntity> dateInfoEntities, Integer OAId, Model model, String teacher, String mode) {
        model.addAttribute("mode", mode);
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
        }
        return teacher + "OAInfo";
    }
    /*
    * 公欠届別バージョン詳細取得メソッド
    *
    *
    * */
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
    /*
    * 公欠届再提出メソッド
    *
    * 再提出すると、ステータスは強制的に未受理になる。
    * 先生のチェックステータスもfalseになる。
    * */
    @Transactional
    public String repostOA(BindingResult bindingResult, Integer OAId, OAMainForm form, Model model) {
        if(bindingResult.hasErrors()){
            System.out.println("error");
            model.addAttribute("mode", "edit");
            return "OAForm";
        }
        updateOASubmittedDate(OAId);
        List<OADatesEntity> dateList = form.toDatesEntity();
        updateOADates(dateList, OAId);
        switch (OAReason.valueOf(form.getReasonForAbsence())){
            case jobSearch -> {
                JobSearchEntity jobSearch = form.toJobSearchEntity(OAId);
                updateJobSearch(jobSearch);
                updateCheck(OAId, "teacher", false);
                updateCheck(OAId, "career", false);
            }
            case seminar -> {
                SeminarEntity seminar = form.toSeminarEntity(OAId);
                updateSeminar(seminar);
                updateCheck(OAId, "teacher", false);
                updateCheck(OAId, "career", false);
            }
            case bereavement -> {
                BereavementEntity bereavement = form.toBereavementEntity(OAId);
                updateBereavement(bereavement);
                updateCheck(OAId, "teacher", false);
            }
            case attendanceBan -> {
                AttendanceBanEntity attendanceBan = form.toAttendanceBanEntity(OAId);
                updateAttendanceBan(attendanceBan);
                updateCheck(OAId, "teacher", false);
            }
            case other -> {
                OtherEntity other = form.toOtherEntity(OAId);
                updateOther(other);
                updateCheck(OAId, "teacher", false);
            }
        }
        updateOAStatus(OAId, "unaccepted");
        return "redirect:/jobportal/student/{studentId}/OAList";
    }
    /*
    * 先生側からの公欠届List取得メソッド
    *
    * ページ指定によってページが読み込まれた場合、セッションにページを保存する。
    * 別ページからのページ遷移や検索によってページが読み込まれた場合、セッションからページ番号を呼び出す。
    * */
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
        //以下ページング処理
        Integer count = countSearchOA(form);
        int maxSize = (int)Math.ceil((double) count / pageSize);
        int displayPageCount = Math.min(maxSize, 5);
        int start = Math.max(1, Math.min(currentPage - (displayPageCount - 1) / 2, maxSize - displayPageCount + 1));
        int end = Math.min(maxSize, start + displayPageCount - 1);
        model.addAttribute("maxSize", maxSize);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("start", start);
        model.addAttribute("end", end);
        model.addAttribute("searchForm", form);
        model.addAttribute("colors", colors);
        model.addAttribute("mode", "list");
        return "teacher_OAList";
    }
    /*
    * 報告書提出メソッド
    *
    *
    * */
    @Transactional
    public String postReport(BindingResult bindingResult, Integer OAId, ReportForm form, Model model) {
        //バインディングエラーが起きたときにデータを返すための処理。
        List<OADateInfoEntity> dateInfoEntities = findDateInfo(OAId);
        model.addAttribute("OADate",dateInfoEntities);
        JobSearchEntity jobSearch = findJobSearchInfo(OAId);
        model.addAttribute("jobSearch",jobSearch);
        model.addAttribute("ReportForm",form);
        model.addAttribute("reason", form.getReason());
        if(bindingResult.hasErrors()){
            System.out.println("error");
            model.addAttribute("mode","create");
            return "reportForm";
        }
        //バインディングエラーが起きなかったときの処理。
        Integer reportId = repository.selectReportID(OAId);
        repository.insertReportHistories(reportId, form);
        repository.updateReportStatus(reportId, "unaccepted");
        repository.updateReportInfo(form, reportId);
        //就活希望(しない、検討中、受験する、内定済み)のうち、受験する以外は次のアクションが存在しないので、nullをセットする。
        if(ReportType.valueOf(form.getReason()) != ReportType.seminar){
            if(!form.getEmploymentIntention().equals("takingExam")) form.setNextAction(null);
            repository.insertJobFuture(form, reportId);
        }
        switch (ReportType.valueOf(form.getReason())){
            case jobInterview -> {
                repository.insertInterviewReport(form, reportId);
            }
            case briefing -> {
                repository.insertBriefingReport(form, reportId);
            }
            case test -> {
                repository.insertExamReport(form, reportId);
            }
            case informalCeremony -> {
                repository.insertInformalCeremonyReport(form, reportId);
            }
            case training -> {
                repository.insertTrainingReport(form, reportId);
            }
            case jobOther -> {
                repository.insertOtherReport(form, reportId);
            }
            case seminar -> {
                //就活希望(しない、検討中、受験する、内定済み)のうち、受験する以外は次のアクションが存在しないので、nullをセットする。
                form.getSeminarForms().stream()
                    .filter(f -> !"takingExam".equals(f.getSeminarEmploymentIntention()))
                    .forEach(f -> f.setSeminarNextAction(null));
                repository.insertSeminarReport(form, reportId);
            }
        }
        return "redirect:/jobportal/student/{studentId}/OAList";
    }
    /*
    * 報告書ステータス変更メソッド
    *
    * 変更があった際に、公欠届と報告書が両方受理になったか判定するメソッドも呼び出す。
    * */
    public void updateReportStatus(Integer reportId, String status){
        repository.updateReportStatus(reportId, status);
        Integer OAId = repository.selectOAId(reportId);
        checkOAAndReportCondition(OAId, reportId);
    }
    /*
    * 公欠届と報告書のステータスをチェックするメソッド
    *
    * 公欠届が「受理」で、報告書が「受理」か「不要」ならその公欠日を公欠システム反映テーブルにインサートする。
    * */
    public void checkOAAndReportCondition(Integer OAId, Integer reportId){
        OAStatus status = OAStatus.valueOf(repository.selectOAStatus(OAId));
        OAStatus reportOAStatus = OAStatus.valueOf(repository.selectReportStatus(reportId));
        if(status.equals(OAStatus.acceptance) && (reportOAStatus.equals(OAStatus.acceptance) || reportOAStatus.equals(unnecessary))){
            Integer studentId = repository.selectStudentId(OAId);
            List<OADateInfoEntity> dateEntities = repository.selectDateInfo(OAId);
            repository.insertApplovedLeaveRequests(OAId, studentId, dateEntities);
        }
    }
    /*
    * 報告書共通情報取得メソッド
    * */
    public ReportInfoEntity getReportInfo(Integer reportId) {
        return repository.selectReportInfo(reportId);
    }
    /*
     * 面接報告書情報取得メソッド
     * */
    public ReportInterviewEntity getInterviewEntity(Integer reportId) {
        return repository.selectReportInterview(reportId);
    }
    /*
     * 説明会報告書情報取得メソッド
     * */
    public ReportBriefingEntity getBriefingEntity(Integer reportId) {
        return repository.selectReportBriefing(reportId);
    }
    /*
     * 試験報告書情報取得メソッド
     * */
    public ReportTestEntity getTestEntity(Integer reportId) {
        return repository.selectReportTest(reportId);
    }
    /*
     * 内定式報告書情報取得メソッド
     * */
    public ReportInformalCeremonyEntity getInformalCeremonyEntity(Integer reportId) {
        return repository.selectReportInformalCeremony(reportId);
    }
    /*
     * 研修報告書情報取得メソッド
     * */
    public ReportTrainingEntity getTrainingEntity(Integer reportId) {
        return repository.selectReportTraining(reportId);
    }
    /*
     * その他報告書情報取得メソッド
     * */
    public ReportOtherEntity getOtherEntity(Integer reportId) {
        return repository.selectReportOther(reportId);
    }
    /*
     * セミナー報告書情報取得メソッド
     * */
    public List<ReportSeminarEntity> getSeminarEntity(Integer reportId) {
        return repository.selectReportSeminar(reportId);
    }
    /*
    * 報告書再提出用の、面接報告書データ作成メソッド
    * */
    public ReportForm toReportForm(ReportInfoEntity mainInfo, ReportInterviewEntity interviewEntity) {
        return new ReportForm(
            mainInfo,
            interviewEntity
        );
    }
    /*
     * 報告書再提出用の、説明会報告書データ作成メソッド
     * */
    public ReportForm toReportForm(ReportInfoEntity mainInfo, ReportBriefingEntity briefingEntity) {
        return new ReportForm(
            mainInfo,
            briefingEntity
        );
    }
    /*
     * 報告書再提出用の、試験報告書データ作成メソッド
     * */
    public ReportForm toReportForm(ReportInfoEntity mainInfo, ReportTestEntity testEntity) {
        return new ReportForm(
            mainInfo,
            testEntity
        );
    }
    /*
     * 報告書再提出用の、内定式報告書データ作成メソッド
     * */
    public ReportForm toReportForm(ReportInfoEntity mainInfo, ReportInformalCeremonyEntity informalCeremonyEntity) {
        return new ReportForm(
            mainInfo,
            informalCeremonyEntity
        );
    }
    /*
     * 報告書再提出用の、研修報告書データ作成メソッド
     * */
    public ReportForm toReportForm(ReportInfoEntity mainInfo, ReportTrainingEntity trainingEntity) {
        return new ReportForm(
            mainInfo,
                trainingEntity
        );
    }
    /*
     * 報告書再提出用の、その他報告書データ作成メソッド
     * */
    public ReportForm toReportForm(ReportInfoEntity mainInfo, ReportOtherEntity otherEntity) {
        return new ReportForm(
            mainInfo,
            otherEntity
        );
    }
    /*
     * 報告書再提出用の、セミナー報告書データ作成メソッド
     * */
    public ReportForm toReportForm(ReportInfoEntity mainInfo, List<ReportSeminarEntity> seminarEntities) {
        return new ReportForm(
            mainInfo,
            seminarEntities
        );
    }
    /*
    * 報告書再提出メソッド
    *
    * 報告書履歴テーブル、報告書特有テーブル、報告書ステータスをアップデートする。
    * セミナー以外は就職希望テーブルもアップデートする。
    * セミナーはセミナーテーブルに就職希望が含まれている。
    * */
    @Transactional
    public String repostReport(Integer reportId, ReportForm form, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return "reportForm";
        }
        form.setReportId(reportId);
        updateReportHistories(reportId, form);
        switch (ReportType.valueOf(form.getReason())){
            case jobInterview -> {
                updateReportInterview(form);
                updateJobFuture(form);
            }
            case briefing -> {
                updateReportBriefing(form);
                updateJobFuture(form);
            }
            case test -> {
                updateReportTest(form);
                updateJobFuture(form);
            }
            case informalCeremony -> {
                updateReportInformalCeremony(form);
                updateJobFuture(form);
            }
            case training -> {
                updateReportTraining(form);
                updateJobFuture(form);
            }
            case jobOther -> {
                updateReportOther(form);
                updateJobFuture(form);
            }
            case seminar -> {
                updateReportSeminar(form);
            }
        }
        updateReportStatus(reportId, "unaccepted");
        return "redirect:/jobportal/student/{studentId}/OAList";
    }

    private void updateReportHistories(Integer reportId, ReportForm form) {
        repository.updateReportHistories(reportId, form);
    }
    private void updateReportBriefing(ReportForm form) {
        repository.updateReportBriefing(form);
    }
    private void updateReportInterview(ReportForm form) {
        repository.updateReportInterview(form);
    }
    private void updateReportTest(ReportForm form) {
        repository.updateReportTest(form);
    }
    private void updateReportInformalCeremony(ReportForm form) {
        repository.updateReportInformalCeremony(form);
    }
    private void updateReportTraining(ReportForm form) {
        repository.updateReportTraining(form);
    }
    private void updateReportOther(ReportForm form) {
        repository.updateReportOther(form);
    }
    private void updateReportSeminar(ReportForm form) {
        repository.updateReportSeminar(form);
    }
    private void updateJobFuture(ReportForm form) {
        repository.updateJobFuture(form);
    }

    public ReportInfoEntity getReportInfoByVersion(Integer reportId, Integer version) {
        return repository.selectReportInfoByVersion(reportId, version);
    }
    public ReportInterviewEntity getInterviewEntityByVersion(Integer reportId, Integer version) {
        return repository.selectReportInterviewByVersion(reportId, version);
    }
    public ReportBriefingEntity getBriefingEntityByVersion(Integer reportId, Integer version) {
        return repository.selectReportBriefingByVersion(reportId, version);
    }
    public ReportTestEntity getTestEntityByVersion(Integer reportId, Integer version) {
        return repository.selectReportTestByVersion(reportId, version);
    }
    public ReportInformalCeremonyEntity getInformalCeremonyEntityByVersion(Integer reportId, Integer version) {
        return repository.selectReportInformalCeremonyByVersion(reportId, version);
    }
    public ReportTrainingEntity getTrainingEntityByVersion(Integer reportId, Integer version) {
        return repository.selectReportTrainingByVersion(reportId, version);
    }
    public ReportOtherEntity getOtherEntityByVersion(Integer reportId, Integer version) {
        return repository.selectReportOtherByVersion(reportId, version);
    }
    public List<ReportSeminarEntity> getSeminarEntityByVersion(Integer reportId, Integer version) {
        return repository.selectReportSeminarByVersion(reportId, version);
    }
    public Integer getReportId(Integer OAId) {
        return repository.selectReportID(OAId);
    }
    /*
    * 公欠削除メソッド
    * */
    @Transactional
    public void deleteOA(Integer OAId) {
        OAMainInfoEntity mainInfoEntity = findMainInfo(OAId);
        switch (mainInfoEntity.reason()){
            case jobSearch -> {
                deleteJobSearch(OAId);
            }
            case seminar -> {
                deleteSeminar(OAId);
            }
            case bereavement -> {
                deleteBereavement(OAId);
            }
            case attendanceBan -> {
                deleteAttendanceBan(OAId);
            }
            case other -> {
                deleteOther(OAId);
            }
        }
        deleteDate(OAId);
        deleteSubmittedDate(OAId);
        deleteReports(OAId);
        deleteReportMain(OAId);
        deleteMain(OAId);
    }
    /*
    * 報告書削除メソッド
    * */
    private void deleteReportMain(Integer OAId) {
        repository.deleteReportMain(OAId);
    }
    /*
    * 報告書一覧取得メソッド
    *
    * htmlでリスト表示するために、全ての報告書を一つのクラスにまとめる。
    * そのために、まず共通データを取得、報告書の内容によって特有データ取得、２つのデータを合体する。
    * */
    public List<ReportLogEntity> searchReportLogs(String companyName, Integer page,String status) {
        List<ReportLogEntity> logEntities = new ArrayList<>();
        List<ReportInfoEntity> infoEntities = repository.selectReportInfosByCompanyName(companyName, page, pageSize, status);
        for (ReportInfoEntity reportInfoEntity : infoEntities) {
            switch (reportInfoEntity.getReason()){
                case jobInterview -> {
                    ReportInterviewEntity interviewEntity = repository.selectReportInterview(reportInfoEntity.getReportId());
                    logEntities.add(new ReportLogEntity(reportInfoEntity, interviewEntity));
                }
                case briefing -> {
                    ReportBriefingEntity briefingEntity = repository.selectReportBriefing(reportInfoEntity.getReportId());
                    logEntities.add(new ReportLogEntity(reportInfoEntity, briefingEntity));
                }
                case test -> {
                    ReportTestEntity testEntity = repository.selectReportTest(reportInfoEntity.getReportId());
                    logEntities.add(new ReportLogEntity(reportInfoEntity, testEntity));
                }
                case informalCeremony -> {
                    ReportInformalCeremonyEntity informalCeremonyEntity = repository.selectReportInformalCeremony(reportInfoEntity.getReportId());
                    logEntities.add(new ReportLogEntity(reportInfoEntity, informalCeremonyEntity));
                }
                case training -> {
                    ReportTrainingEntity trainingEntity = repository.selectReportTraining(reportInfoEntity.getReportId());
                    logEntities.add(new ReportLogEntity(reportInfoEntity, trainingEntity));
                }
                case jobOther -> {
                    ReportOtherEntity otherEntity = repository.selectReportOther(reportInfoEntity.getReportId());
                    logEntities.add(new ReportLogEntity(reportInfoEntity, otherEntity));
                }
                case seminar -> {
                    List<ReportSeminarEntity> seminarEntities = repository.selectReportSeminar(reportInfoEntity.getReportId());
                    logEntities.add(new ReportLogEntity(reportInfoEntity, seminarEntities));
                }
            }
        }
        return logEntities;
    }
    /*
    * 報告書一覧のカウントを取得する。
    *
    * ページングのために使う。
    * */
    public Integer countSearchReportLogs(String companyName, String status) {
        return repository.countReportInfosByCompanyName(companyName,status);
    }
    /*
    * 受理済みの公欠の公欠日を返す。
    * */
    public OASubject getOASubject() {
        return repository.selectOASubject();
    }
    /*
    * 公欠システム反映済みの公欠日のフラグをtrueにする。
    * */
    public void putOASubject(OASubject subject) {
        repository.updateOASubject(new OASubjectDTO(subject.studentId(), LocalDate.parse(subject.officialAbsenceDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")), subject.period()));
    }
}
