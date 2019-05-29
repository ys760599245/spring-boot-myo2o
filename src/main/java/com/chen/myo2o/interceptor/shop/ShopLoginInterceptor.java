package com.chen.myo2o.interceptor.shop;

import com.chen.myo2o.entity.PersonInfo;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 店家管理系统登录验证拦截器
 */
public class ShopLoginInterceptor extends HandlerInterceptorAdapter {
    /**
     * 主要做事前拦截 即用户操作发生之前 改写preHandle里面的逻辑 进行拦截
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
        //从Session中取出用户信息
        Object userObj = request.getSession().getAttribute("user");
        if (userObj != null) {
            //若用户信息不为空 则将Session里面的用户信息转化成PersonInfo实体类对象
            PersonInfo user = (PersonInfo) userObj;
            //做空值判断 确保userid不为空 并且该账户的可用状态为1 并且用户类
            if (user != null && user.getUserId() != null && user.getUserId() > 0 && user.getEnableStatus() == 1
                    && user.getShopOwnerFlag() == 1) {
                //若通过验证则返回为true 拦截器返回true之后 用户接下来的操作正常进行
                return true;
            }
        }
        //若不满登录验证 则直接跳转到账号登录界面
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<script>");
        out.println("window.open ('" + request.getContextPath() + "/shop/ownerlogin','_self')");
        out.println("</script>");
        out.println("</html>");
        return false;
    }
}
