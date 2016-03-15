package quartz.demos.demo7;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.quartz.DateBuilder.nextGivenSecondDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
import java.util.Date;

/**
 * Created by lm on 2016/3/15.
 */

/**
 * 学习和总结
 *
 *  1.当Job实现了InterruptableJob也必须实现interrupt()方法
 *  2.实现了InterruptableJob接口的Job是可中断的方法,可以调用shced.interrrupt(JobKey key)将这个方法中断
 *  3.当Sched调用interrupt()方法时实际上回调用Job的interrrupt()方法
 *  4.用户可以利用这一点，通过自己的逻辑实现可中断的Job
 */
public class InterruptDemo {
    public void run() throws SchedulerException {
        final Logger log = LoggerFactory.getLogger(InterruptDemo.class);

        log.info("初始化");

        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();

        log.info("----初始化完成---");

        Date startTime = nextGivenSecondDate(null, 15);
        JobDetail job=newJob(DemoDumbInterruptableJob.class)
                .withIdentity("job1","group1")
                .build();

        SimpleTrigger trigger = newTrigger().withIdentity("trigger1", "group1")
                .withSchedule(simpleSchedule()
                        .withIntervalInSeconds(5).repeatForever())
                .build();
        Date ft = sched.scheduleJob(job, trigger);

        log.info(job.getKey() + " will run at: " + ft + " and repeat : " + trigger
                .getRepeatCount() + " times ,every " + trigger.getRepeatInterval() / 1000 + " seconds");

        sched.start();

        for(int i=0;i<50;i++) {
            try {
                Thread.sleep(7000L);
                //中断Job
                sched.interrupt(job.getKey());

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        sched.shutdown(true);

        SchedulerMetaData metaData=sched.getMetaData();
        log.info("执行了 "+metaData.getNumberOfJobsExecuted()+" 次");


    }

    public static void main(String[] args) throws SchedulerException {
        new InterruptDemo().run();

    }


}
