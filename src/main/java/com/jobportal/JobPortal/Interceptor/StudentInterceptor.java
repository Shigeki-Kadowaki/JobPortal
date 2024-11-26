package com.jobportal.JobPortal.Interceptor;


import com.jobportal.JobPortal.Controller.Student;
import com.jobportal.JobPortal.Service.MainService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

//@RequestMapping("/jobportal")
@RequiredArgsConstructor
public class StudentInterceptor implements HandlerInterceptor {

    @Autowired
    MainService service;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("studentPreHandle");
        //localでテスト用
        Student student = new Student();
        student.setGno(99999);
        student.setSurname("YourSurName");
        student.setGivenname("YourGivenName");
        student.setGroup("生徒");
        student.setGrade(2);
        student.setClassroom("A");
        student.setCno(99);
        student.setDepartment("YourDepartment");
        student.setCourse("YourCourse");

        //ssoから取得する用
        //ssoには、学籍番号、名前が含まれている。
        //apiからのデータには、学年、クラス、出席番号、学科、コースが含まれている。
        //両方のデータをStudentクラスにsetする。
//        Map<String, String> map = service.getPersonInfo(response, request);
//        String ssoStudentId = map.get("mellon-email").substring(0, 5);
//        Student student = service.getStudentInfo(Integer.parseInt(ssoStudentId));
//        student.setSurname(map.get("mellon-surname"));
//        student.setGivenname(map.get("mellon-givenname"));
//        System.out.println(ssoStudentId);


        //urlからのデータ
        String url = request.getRequestURI();
        String urlStudentId = url.replaceAll("[^0-9]","");
        int studentId = Integer.parseInt(urlStudentId.substring(0,5));//urlから取得したid
        //ヘッダーから取得したidとurlから取得したidが違うとトップページに戻される
        if(studentId != student.getGno()){
            response.sendRedirect("/jobportal/");
            return false;
        }
        request.setAttribute("student",student);
        return true;
    }

}


