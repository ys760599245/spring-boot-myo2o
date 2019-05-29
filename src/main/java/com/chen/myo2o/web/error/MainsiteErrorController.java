package com.chen.myo2o.web.error;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author yss
 * @date 2019/5/27 17:02
 * <p>
 * 包描述   com.chen.myo2o.web.error
 * 类名称   spring-boot-myo2o
 * 类描述
 */
public class MainsiteErrorController implements ErrorController {
    private static final String ERROR_PATH = "/error";

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    @RequestMapping(value = ERROR_PATH)
    public String handleError() {
        return "error/404";
    }

}
