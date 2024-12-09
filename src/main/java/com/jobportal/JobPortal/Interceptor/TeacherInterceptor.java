package com.jobportal.JobPortal.Interceptor;

import com.jobportal.JobPortal.Service.MainService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

public class TeacherInterceptor implements HandlerInterceptor {

    @Autowired
    MainService service;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("teacherPreHandle");
        String group = "生徒";
        //生徒だと先生ページにはアクセスできない
//        if(!group.equals("先生")){
//            response.sendRedirect("/jobportal/");
//            return false;
//        }

        //localでテスト用
        String teacherMail = "40104kk@saisen.ac.jp";

        //ssoから取得する用
//        Map<String, String> map = service.getPersonInfo(response, request);
//        String teacherMail = map.get("mellon-email");
        request.setAttribute("mail", teacherMail);
        return true;
    }

}
