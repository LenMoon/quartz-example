package quartz.demos.demo4;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

/**
 * Created by lm on 2016/3/11.
 */
public class JobStateDemo {
    public static  void run() throws SchedulerException {
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched=sf.getScheduler();


        JobDetail job1 = JobBuilder.newJob(CarJob.class).withIdentity("job1", "group1").build();
        job1.getJobDataMap().put(CarJob.FAVORITE_COLOR, "green");
        job1.getJobDataMap().put(CarJob.RUN_COUNT, 1);
        SimpleTrigger trigger1=TriggerBuilder.newTrigger().withIdentity("trigger1","group1").startAt(new Date())
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(20).withRepeatCount(30)).build();

        sched.scheduleJob(job1, trigger1);
        sched.start();


        try {
            Thread.sleep(60L*1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        SchedulerMetaData schedulerMetaData = sched.getMetaData();
        System.out.println("共执行了多少个job呢？  答案："+schedulerMetaData.getNumberOfJobsExecuted());
    }

    public static void main(String[] args) throws SchedulerException {
        run();
    }
}
