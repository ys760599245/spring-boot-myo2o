package com.chen.myo2o.config.dao;

/**
 * @author yss
 * @date 2019/5/27 17:45
 * <p>
 * 包描述   com.chen.myo2o.config.dao
 * 类名称   spring-boot-myo2o
 * 类描述
 */
/*
@Configuration
public class DruidConfig {

    private String username = "WnplV/ietfQ=";

    private String password = "QAHlVoUc49w=";

    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource druid() {
        return new DruidDataSource();
    }

    //配置Druid的监控
//1、配置一个管理后台的Servlet
    @Bean
    public ServletRegistrationBean statViewServlet() {
        ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(),
                "/druid/*");
        Map<String, String> initParams = new HashMap<>();
        initParams.put("loginUsername", DESUtils.getDecryptString(username));
        initParams.put("loginPassword", DESUtils.getDecryptString(password));
        initParams.put("resetEnable", "false");
        initParams.put("allow", "127.0.0.1");//默认就是允许所有访问
        initParams.put("deny", "192.168.15.21");//黑名单
        bean.setInitParameters(initParams);
        return bean;
    }

    //2、配置一个web监控的filter
    @Bean
    public FilterRegistrationBean webStatFilter() {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new WebStatFilter());
        Map<String, String> initParams = new HashMap<>();
        initParams.put("exclusions", "*.js,*.css,/druid/*");
        bean.setInitParameters(initParams);
        bean.setUrlPatterns(Arrays.asList("/*"));
        return bean;
    }


}
*/
