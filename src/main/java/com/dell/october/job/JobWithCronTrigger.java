package com.dell.october.job;

import com.dell.october.config.QuartzConfig;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.stereotype.Component;

@Component
@DisallowConcurrentExecution
public class JobWithCronTrigger implements Job{
    private final static Logger logger = LoggerFactory.getLogger("JobWithCronTrigger");

    private String cronExpression;
    private String content;

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("Running JobWithCronTrigger | frequency {}", cronExpression);
    }

    @Bean(name = "jobWithCronTriggerBean")
    public JobDetailFactoryBean sampleJob(){
        return QuartzConfig.createJobDetail(this.getClass());
    }

    @Bean(name = "jobWithCronTriggerBeanTrigger")
    public CronTriggerFactoryBean sampleJobTrigger(@Qualifier("jobWithCronTriggerBean")JobDetail jobDetail){
        return QuartzConfig.createCronTrigger(jobDetail, cronExpression);
    }
}
