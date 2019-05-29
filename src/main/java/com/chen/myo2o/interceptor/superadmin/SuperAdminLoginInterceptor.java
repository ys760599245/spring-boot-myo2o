package com.chen.myo2o.interceptor.superadmin;

import com.chen.myo2o.entity.PersonInfo;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @author
 */
public class SuperAdminLoginInterceptor extends HandlerInterceptorAdapter {
    /**
     * 主要做事前拦截即用户操作 发生前 改写preHandle里面的逻辑 进行用户操作权限拦截
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        //从Session中获取当前选择的店铺
        Object userObj = request.getSession().getAttribute("user");
        if (userObj != null) {
            PersonInfo user = (PersonInfo) userObj;
            if (user != null && user.getUserId() != null && user.getUserId() > 0 && user.getAdminFlag() != null
                    && user.getAdminFlag() == 1) {
                return true;
            }
        }
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<script>");
        out.println("window.open ('" + request.getContextPath() + "/superadmin/login','_top')");
        out.println("</script>");
        out.println("</html>");
        return false;
    }
}
