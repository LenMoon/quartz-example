package quartz.demos.demo6;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quartz.examples.example6.BadJob2;

import static org.quartz.DateBuilder.nextGivenSecondDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
import java.util.Date;

import static org.quartz.DateBuilder.nextGivenSecondDate;

/**
 * Created by lm on 2016/3/14.
 */

/**
 * 学习和总结：
 * 当要执行的任务报错了怎么办呢？
 * 1.默认情况将重复执行错误代码
 * 2.用Quartz的异常将跑出的异常重新包装
 *      （e2=new JobExecutionException(e)）
 *      e2.setRefireImmediately(true);
 *   将立即再次执行，可以在catch语句里面将错误的部分修改，下次将不会
 *   再报错
 * 3.用e2.setUnscheduleAllTriggers(true)通知所有触发器这个任务将不再执行
 *
 */
public class JobExceptionDemo {
    public void run() throws SchedulerException {
        Logger log = LoggerFactory.getLogger(JobExceptionDemo.class);

        log.info("------- Initializing ----------------------");

        // First we must get a reference to a scheduler
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();

        log.info("------- Initialization Complete ------------");

        log.info("------- Scheduling Jobs -------------------");

        // jobs can be scheduled before start() has been called

        // get a "nice round" time a few seconds in the future...
        Date startTime = nextGivenSecondDate(null, 15);

        //job1抛出异常后立即将jobdatamap里面的denominator替换为1然后立即执行
        JobDetail job = newJob(DemoBadJob1.class)
                .withIdentity("badJob1", "group1")
                .usingJobData("denominator",0)
                .build();
        SimpleTrigger trigger =newTrigger().withIdentity("trigger1","group1")
                .startAt(startTime).withSchedule(simpleSchedule().withIntervalInSeconds(10)
                    .repeatForever())
                .build();
        Date ft = sched.scheduleJob(job, trigger);
        log.info(job.getKey() + " will run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every "
                + trigger.getRepeatInterval() / 1000 + " seconds");

        //job2报错后将不再执行
        job = newJob(DemoBadJob2.class).withIdentity("badJob2", "group1").build();

        trigger = newTrigger().withIdentity("trigger2", "group1").startAt(startTime)
                .withSchedule(simpleSchedule().withIntervalInSeconds(5).repeatForever()).build();

        ft = sched.scheduleJob(job, trigger);
        log.info(job.getKey() + " will run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every "
                + trigger.getRepeatInterval() / 1000 + " seconds");
        //job3代表默认情况,将重复执行错误代码

        job = newJob(DemoBadJob3.class).withIdentity("badJob3", "group1")
                .build();
        trigger = newTrigger().withIdentity("trigger3", "group1").startAt(startTime)
                .withSchedule(simpleSchedule().withIntervalInSeconds(5).repeatForever()).build();

        ft = sched.scheduleJob(job, trigger);
        log.info(job.getKey() + " will run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every "
                + trigger.getRepeatInterval() / 1000 + " seconds");
        sched.start();


    }

    public static void main(String[] args) throws SchedulerException {
        new JobExceptionDemo().run();

    }
}
