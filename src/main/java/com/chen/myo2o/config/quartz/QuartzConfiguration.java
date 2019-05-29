package com.chen.myo2o.config.quartz;

import org.springframework.context.annotation.Configuration;

/**
 * @author yss
 * @date 2019/5/25 15:55
 * <p>
 * 包描述   com.chen.myo2o.config.quartzConfiguration
 * 类名称   spring-boot-myo2o
 * 类描述
 * 定时调度
 */
@Configuration
public class QuartzConfiguration {

  /*  @Autowired
    private ProductSellDailyService productSellDailyService;
    @Autowired
    private MethodInvokingJobDetailFactoryBean jobDetailFactory;
    @Autowired
    private CronTriggerFactoryBean productSellDailyTriggerFactory;


    *//**
     * 创建jobDetail 并且返回
     *
     * @return
     *//*
    @Bean(name = "jobDetailFactory")
    public MethodInvokingJobDetailFactoryBean createJobDetail() {
        //new 出JobDetailFactory 此工厂 主要来制作一个JobDetail 即可制作一个任务
        //由于我们所做的定时任务根本上讲就是执行一个方法 所以用这个工程比较方便
        MethodInvokingJobDetailFactoryBean jobDetailFactoryBean = new MethodInvokingJobDetailFactoryBean();
        //设置jobDetail的名字
        jobDetailFactoryBean.setName("product_sell_daily_job");
        //设置jobDetail组名
        jobDetailFactoryBean.setGroup("job_product_sell_daily_group");
        //对于相同的jobDetail  当指定过个trigger时间 很可能在第一个job完成之前 第二个job
        //指定concurrent 设置为False 多个job不会并发运行 很可能第一个job完成之前 第二个job
        jobDetailFactoryBean.setConcurrent(false);
        //指定运行任务的类
        jobDetailFactoryBean.setTargetObject(productSellDailyService);
        //指定运行任务的方法
        jobDetailFactoryBean.setTargetMethod("dailyCalculate");
        return jobDetailFactoryBean;
    }

    *//**
     * 创建  cronTrigger并且返回
     * <p>
     * * @return
     *//*
    @Bean(name = "productSellDailyTriggerFactory")
    public CronTriggerFactoryBean createProductSellDailyTrigger() {
        //创建triggerFactory实例 用来创建 trigger
        CronTriggerFactoryBean triggerFactory = new CronTriggerFactoryBean();
        //设定triggerFactory的名字
        triggerFactory.setName("product_sell_daily_trigger");
        //设定triggerFactory的组名
        triggerFactory.setGroup("job_product_sell_daily_group");
        //绑定jobDetail
        triggerFactory.setJobDetail(jobDetailFactory.getObject());
        //设定cron表达式
        triggerFactory.setCronExpression("0/3 * * * * ? *");
        return triggerFactory;
    }

    *//**
     * 创建调度工厂并且返回
     *
     * @return
     *//*
    @Bean("schedulerFactory")
    public SchedulerFactoryBean creaeSchedulerFactory() {
        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        schedulerFactory.setTriggers(productSellDailyTriggerFactory.getObject());
        return schedulerFactory;
    }*/
}
