package com.chen.myo2o.web.local;

import com.chen.myo2o.dto.LocalAuthExecution;
import com.chen.myo2o.entity.LocalAuth;
import com.chen.myo2o.entity.PersonInfo;
import com.chen.myo2o.enums.LocalAuthStateEnum;
import com.chen.myo2o.service.LocalAuthService;
import com.chen.myo2o.util.CodeUtil;
import com.chen.myo2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "local", method = {RequestMethod.GET, RequestMethod.POST})
public class LocalAuthController {
    @Autowired
    private LocalAuthService localAuthService;

    /**
     * 将用户信息与平台账号进行绑定
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/bindlocalauth", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> bindLocalAuth(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        //验证码校验
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMg", "输入了错误的验证码");
            return modelMap;
        }
        //获取输入的账号
        String useName = HttpServletRequestUtil.getString(request, "username");
        //获取输入的密码
        String password = HttpServletRequestUtil.getString(request, "password");
        //从session中获取当前用户信息（用户一旦通过微信登录之后，便能获取到用户的信息）
        PersonInfo personInfo = (PersonInfo) request.getSession().getAttribute("user");
        //非空判断，要求账号密码以及当前的用户session非空
        if (useName != null && password != null && personInfo != null && personInfo.getUserId() != null) {
            //创建LocalAuth对象并赋值
            LocalAuth localAuth = new LocalAuth();
            localAuth.setUserName(useName);
            localAuth.setPersonInfo(personInfo);
            localAuth.setPassword(password);
            //绑定账号
            LocalAuthExecution execution = localAuthService.bindLocalAuth(localAuth);
            if (execution.getState() == LocalAuthStateEnum.SUCCESS.getState()) {
                modelMap.put("success", true);
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", execution.getStateInfo());
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "用户名或密码不能为空");
        }
        return modelMap;
    }

    @RequestMapping(value = "/changelocalpwd", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> changeLocalPwd(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        //验证码校验
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入了错误的验证码");
            return modelMap;
        }
        String username = HttpServletRequestUtil.getString(request, "username");
        String password = HttpServletRequestUtil.getString(request, "password");
        String newPassword = HttpServletRequestUtil.getString(request, "newPassword");

        PersonInfo personInfo = (PersonInfo) request.getSession().getAttribute("user");
        if (username != null && password != null && newPassword != null
                && personInfo != null && personInfo.getUserId() != null
                && !password.equals(newPassword)) {
            //查看原先账号，看看与输入的账号是否一致，不一致认为非法操作
            LocalAuth localAuth = localAuthService.getLocalAuthByUserId(personInfo.getUserId());
            if (localAuth == null || !localAuth.getUserName().equals(username)) {
                //不一致则直接退出
                modelMap.put("success", false);
                modelMap.put("errMsg", "输入的账号非本次登录的账号");
                return modelMap;
            }
            LocalAuthExecution execution = localAuthService.modifyLocalAuth(personInfo.getUserId(), username, password, newPassword);
            if (execution.getState() == LocalAuthStateEnum.SUCCESS.getState()) {
                modelMap.put("success", true);

            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", execution.getStateInfo());
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入密码");
        }
        return modelMap;
    }

    @RequestMapping(value = "/logincheck", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> logincheck(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        //获取是否需要进行验证码校验的标识符
        boolean needVerify = HttpServletRequestUtil.getBoolean(request, "needVerify");
        if (needVerify && !CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入了错误的验证码");
            return modelMap;
        }
        //获取输入的账号
        String username = HttpServletRequestUtil.getString(request, "username");
        //获取输入的密码
        String password = HttpServletRequestUtil.getString(request, "password");
        //非空校验
        if (username != null && password != null) {
            //传入账号和密码去获取平台账号信息
            LocalAuth localAuth = localAuthService.getLocalAuthByUserNameAndPwd(username, password);
            if (localAuth != null) {
                //若能取到账号信息则登录成功
                modelMap.put("success", true);
                //同时在session里设置用户信息
                request.getSession().setAttribute("user", localAuth.getPersonInfo());
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", "用户名或密码错误");
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "用户名和密码均不能为空");
        }
        return modelMap;

    }


    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> logout(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        request.getSession().setAttribute("user", null);
        modelMap.put("success", true);
        return modelMap;
    }

}
