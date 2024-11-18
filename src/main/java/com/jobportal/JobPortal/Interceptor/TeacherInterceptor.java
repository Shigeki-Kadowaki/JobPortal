package com.jobportal.JobPortal.Interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class TeacherInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("teacherPreHandle");
        String group = "生徒";
        //生徒だと先生ページにはアクセスできない
//        if(!group.equals("先生")){
//            response.sendRedirect("/jobportal/");
//            return false;
//        }
        return true;
    }

}
