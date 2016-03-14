package quartz.demos.demo4;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

import static org.quartz.DateBuilder.nextGivenSecondDate;

/**
 * Created by lm on 2016/3/11.
 */

/**
 * 学习总结：
 *      1.通过这个例子可以发现每次运行的Job对象都是新建的所以job里面的成员变量的自增没用
 *      2.可以通过 jobDataMap,来保存job执行需要用到的一些上下文 同一个JobDetail的JobDatail之间是共享的,不同JobDataMpa
 *      之间是不共享的
 *      3.另外一个很重要的是注解标签，如果没在Job类上面加指定的标签将运行时将不能改变joobDataMap中的值（由于太马虎为这个问题苦恼了好久）
 *      @PersistJobDataAfterExecution
 *      @DisallowConcurrentExecution
 */
public class JobStateDemo {
    public   void run() throws SchedulerException {
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched=sf.getScheduler();
        Date startTime = nextGivenSecondDate(null, 10);

        //测试 同一个JobDetail的JobDataMap是共享的

        JobDetail job1 = JobBuilder.newJob(CarJob.class).withIdentity("job1", "group1").build();
        job1.getJobDataMap().put(CarJob.FAVORITE_COLOR, "green");
        job1.getJobDataMap().put(CarJob.RUN_COUNT, 1);
        SimpleTrigger trigger1=TriggerBuilder.newTrigger().withIdentity("trigger1","group1").startAt(startTime)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(3).withRepeatCount(30)).build();

        SimpleTrigger trigger2 = TriggerBuilder.newTrigger().forJob(job1).withIdentity("trigger2", "group1").startAt(startTime)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(1).withRepeatCount(10))
                .build();

        sched.scheduleJob(job1, trigger1);
       sched.scheduleJob(trigger2);



        //测试 不同的JobDetail的JobDataMap不共享

        JobDetail job2 = JobBuilder.newJob(CarJob.class).withIdentity("job2", "group1").build();
        job2.getJobDataMap().put(CarJob.FAVORITE_COLOR, "grown");
        job2.getJobDataMap().put(CarJob.RUN_COUNT, 1);
        SimpleTrigger trigger3 = TriggerBuilder.newTrigger().withIdentity("trigger3", "group1")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withRepeatCount(5).withIntervalInSeconds(4))
                .build();

        sched.scheduleJob(job2, trigger3);

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
        new JobStateDemo().run();
    }
}
