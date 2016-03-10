package quartz.demos.demo2;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by lm on 2016/3/10.
 */
public class Demo2Trigger {
    public static void run() throws SchedulerException {
        Logger log = LoggerFactory.getLogger(Demo2Trigger.class);

        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();

        //测试如果超过执行时间会执行吗？   测试结果是将会在可以运行的时候立即运行
//        Date startTime = DateBuilder.evenMinuteDate(new Date());
//        JobDetail job = JobBuilder.newJob(DemoJob.class).withIdentity("job1","group1").build();
//        SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder.newTrigger().withIdentity("trigger", "group1").startAt(startTime).build();
//        Date ft= sched.scheduleJob(job, trigger);
//        log.info(job.getKey()+"will run at :"+ft);
//
//        log.info("休息一会儿");
//        try {
//            Thread.sleep(60L*1000L);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        log.info("休息结束 at:"+new Date());
//        sched.start();


        //测试不用trigger直接执行任务?  测试结果是可以不用trigger直接调用job但是如果不storeDurably()会抛出异常

//        JobDetail job = JobBuilder.newJob(DemoJob.class).withIdentity("job2", "group1").storeDurably().build();
//        sched.addJob(job,true);
//        sched.triggerJob(JobKey.jobKey("job2","group1"));
//        sched.start();

        //测试对已执行完的任务再次调用?  其实这个方法不是对已完成的任务进行调用而是对在运行的任务进行再次定义
        //(是再次定义哦  原来的规则就没有了)

        JobDetail job=JobBuilder.newJob(DemoJob.class).withIdentity("job3","group1").build();


        sched.start();
        SimpleTrigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger3", "group1").startAt(DateBuilder.nextGivenSecondDate(null,1))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(1).withRepeatCount(2))
                .build();
        Date ft=sched.scheduleJob(job, trigger);

        log.info(job.getKey()+" will run at: "+ft+" and repeat "+trigger.getRepeatCount()+" times,every" +
                trigger.getRepeatInterval()+" seconds");
        log.info("休息一会儿");
        try {
            Thread.sleep(10L*1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("休息结束 at: "+new Date());
        trigger=newTrigger().withIdentity("trigger3","group1").startAt(new Date())
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(1)
                        .withRepeatCount(10)).build();

        ft = sched.rescheduleJob(trigger.getKey(),trigger);

        log.info(trigger.getJobKey()+" will run at: "+ft+" repeat "+trigger.getRepeatCount()+" times，every "+trigger.getRepeatInterval()/1000+" seconds");

        try {
            Thread.sleep(5L*60L*1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        sched.shutdown();

    }

    public static void main(String[] args) throws SchedulerException {
        run();
    }
}
