package com.jobportal.JobPortal.Interceptor;

import com.jobportal.JobPortal.Service.MainService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

public class TeacherInterceptor implements HandlerInterceptor {

    @Autowired
    MainService service;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("teacherPreHandle");
        Map<String, String> map = service.getPersonInfo(response, request);
        String group = map.get("group");
        //生徒だと先生ページにはアクセスできない
//        if(!group.equals("先生")){
//            response.sendRedirect("/");
//            return false;
//        }
        return true;
    }

}
