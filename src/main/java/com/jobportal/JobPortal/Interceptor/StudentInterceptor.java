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
        Student student = new Student();
        //localでテスト用
        student.setId(99999);
        student.setSurname("YourSurName");
        student.setGivenname("YourGivenName");
        student.setGroup("生徒");
        student.setGrade(2);
        student.setClassroom("A");
        student.setCno(99);
        student.setDepartment("YourDepartment");
        student.setCourse("YourCourse");

        //ssoから取得する用
//        Map<String, String> map = service.getPersonInfo(response, request);
//        String ssoStudentId = map.get("mellon-email").substring(0, 5);
//        student.setId(Integer.parseInt(ssoStudentId));
//        student.setSurname(map.get("mellon-surname"));



        String url = request.getRequestURI();
        String urlStudentId = url.replaceAll("[^0-9]","");
        int studentId = Integer.parseInt(urlStudentId.substring(0,5));//urlから取得したid
        //ヘッダーから取得したidとurlから取得したidが違うとトップページに戻される
        if(studentId != student.getId()){
            response.sendRedirect("/jobportal/");
            return false;
        }
        request.setAttribute("student",student);
        return true;
    }

}


