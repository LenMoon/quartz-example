package quartz.demos.demo12;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by lm on 2016/3/16.
 */
public class RemoteClientDemo {
    public void run() throws SchedulerException, IOException {
        Logger log = LoggerFactory.getLogger(RemoteClientDemo.class);

        Properties client = new Properties();
        client.load(this.getClass().getClassLoader().getResourceAsStream("client.properties"));
        SchedulerFactory sf = new StdSchedulerFactory(client);
        Scheduler sched = sf.getScheduler();


        JobDetail job = newJob(DemoSimpleJob.class)
                .withIdentity("remotelyAddedJob", "default")
                .build();

        JobDataMap map = job.getJobDataMap();
        map.put(DemoSimpleJob.MESSAGE, "远程添加任务已经执行");

        Trigger trigger = newTrigger()
                .withIdentity("remotelyAddedTrigger", "defalut")
                .forJob(job.getKey())
                .withSchedule(cronSchedule("/5 * * ? * *"))
                .build();
        sched.scheduleJob(job, trigger);
        log.info("远程任务执行");

    }


    public static void main(String[] args) throws SchedulerException, IOException {
        new RemoteClientDemo().run();

    }
}
