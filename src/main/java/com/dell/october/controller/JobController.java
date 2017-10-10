package com.dell.october.controller;

import com.dell.october.entity.JobAndTrigger;
import com.dell.october.job.JobWithCronTrigger;
import com.dell.october.service.IJobService;
import com.github.pagehelper.PageInfo;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/job")
public class JobController {
    private static Logger logger = LoggerFactory.getLogger(JobController.class);

    @Autowired
    private IJobService iJobService;

    public void addJob(String jobName, String content, String cronExpression) throws Exception{
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler scheduler = sf.getScheduler();
        scheduler.start();
        Class<?> clazz = Class.forName("com.dell.october.job.JobWithCronTrigger");
        JobWithCronTrigger job = (JobWithCronTrigger)clazz.newInstance();
        job.setContent(content);
        job.setCronExpression(cronExpression);
        JobDetail jobDetail = JobBuilder.newJob(job.getClass()).withIdentity(jobName).build();
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName).withSchedule(scheduleBuilder).build();
        try{
            scheduler.scheduleJob(jobDetail, trigger);
        }catch (SchedulerException e){
            logger.error("创建定时任务失败", e);
        }
    }

    public void pauseJob(String jobName) throws Exception{
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler scheduler = sf.getScheduler();
        scheduler.pauseJob(JobKey.jobKey(jobName));
    }

    public void resumeJob(String jobName) throws Exception{
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler scheduler = sf.getScheduler();
        scheduler.resumeJob(JobKey.jobKey(jobName));
    }

    public void rescheduleJob(String jobName, String cronExpression) throws Exception{
        try {
            SchedulerFactory schedulerFactory = new StdSchedulerFactory();
            Scheduler scheduler = schedulerFactory.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName);
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
            CronTrigger trigger = (CronTrigger)scheduler.getTrigger(triggerKey);
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            scheduler.rescheduleJob(triggerKey, trigger);
        }catch (SchedulerException e){
            logger.error("更新定时任务失败", e);
        }
    }

    public void deleteJob(String jobName) throws Exception{
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        scheduler.pauseTrigger(TriggerKey.triggerKey(jobName));
        scheduler.unscheduleJob(TriggerKey.triggerKey(jobName));
        scheduler.deleteJob(JobKey.jobKey(jobName));
    }

    @GetMapping("/query")
    public Map<String, Object> queryJob(@RequestParam("pageNum")Integer pageNum,@RequestParam("pageSize")Integer pageSize){
        PageInfo<JobAndTrigger> jobAndTrigger = iJobService.getJobAndTriggerDetails(pageNum, pageSize);
        Map<String, Object> map = new HashMap<>();
        map.put("JobAndTrigger", jobAndTrigger);
        map.put("number", jobAndTrigger.getTotal());
        return map;

    }
}
