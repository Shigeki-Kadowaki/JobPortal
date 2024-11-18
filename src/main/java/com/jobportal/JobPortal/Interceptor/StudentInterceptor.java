package com.jobportal.JobPortal.Interceptor;


import com.jobportal.JobPortal.Controller.Student;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
public class StudentInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Student student = new Student();
        //本番ではヘッダーから取得する
        student.setId(40104);
        student.setSurname("Kiya");
        //
        String url = request.getRequestURI();
        String id = url.replaceAll("[^0-9]","");
        int studentId = Integer.parseInt(id.substring(0,5));//urlから取得したid
        //ヘッダーから取得したidとurlから取得したidが違うとトップページに戻される
        if(studentId != student.getId()){
            response.sendRedirect("/jobportal/");
        }
        request.setAttribute("student",student);
        return true;
    }

}


