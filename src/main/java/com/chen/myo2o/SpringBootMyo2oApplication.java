package com.chen.myo2o;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

//解除自动加载DataSourceAutoConfiguration
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class, scanBasePackages = "com.chen.myo2o")
//@ComponentScan(basePackages = "com.chen.myo2o")//包名

public class SpringBootMyo2oApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootMyo2oApplication.class, args);
    }

}
