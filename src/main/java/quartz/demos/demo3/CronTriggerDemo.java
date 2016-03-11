package quartz.demos.demo3;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quartz.examples.example3.SimpleJob;

import java.util.Date;

import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.JobBuilder.newJob;
/**
 * Created by lm on 2016/3/11.
 */

/**
 * 总结：
 * 关键知识点在cron表达式
 * 1.cron表达式有两种形式 second minute hour dayofmonth month dayofweek [year]
 * 2.其中有一些字符串
 *      a.- 代表取件 每个位置都可以使用
 *      b., 代表枚举 每个位置都可以使用
 *      c./ 代表起始和间隔 每个位置都可以使用
 *      d.* 代表任意 每个位置都可以使用
 *      e.L 代表最后 只能在dayofmonth 和 dayofweek中使用 例如 5L代表最后一个星期4
 *      f.W代表有效的工作日，只能出现在dayofmonth 例如5W 如果5号是星期6那么5W就表示4号
 *          如果5号是星期日那么5W就代表6号，它是表示一个最近的工作日，所以如果5号是工作日
 *          那么5W就表示5号
 *      g.LW.这个组合代表这个月的最后一个工作日
 *      h.#代表每个月确定的第几个星期几 只能出现在dayofmonth上面
 */
public class CronTriggerDemo {

    public static void run()throws Exception{
        Logger log = LoggerFactory.getLogger(CronTriggerDemo.class);
        log.info("----初始化Scheduler----");
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();

        log.info("---初始化完成----");
        log.info("---定义任务----");

        //任务可以在sched.start()调用之前定义执行

        //job1 0/20 * * * * ? 代表从0秒开始每隔20秒执行一次
        JobDetail job = newJob(SimpleJob.class).withIdentity("job1", "group1").build();
        CronTrigger trigger =newTrigger().withIdentity("trigger1","group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("0/20 * * * * ?")).build();
        Date ft = sched.scheduleJob(job, trigger);
        log.info(job.getKey()+" has been scheduled to run at "+ft+" and repeat based on expression:"+
        trigger.getCronExpression());

        //job2将从0分钟开始每隔两分钟的第15秒运行一次
        job = newJob(DemoJob.class).withIdentity("job2", "group1").build();

        trigger = newTrigger().withIdentity("trigger2", "group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("15 0/2 * * * ?")).build();
        ft = sched.scheduleJob(job, trigger);

        loginfo(job,log,ft,trigger);

        //job3将在每天的8点到17点之间从零分开始每隔两分钟运行一次
        job = newJob(DemoJob.class).withIdentity("job3", "group1").build();
        trigger = newTrigger().withIdentity("trigger3", "group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0/2 8-17 * * ?"))
                .build();
        ft = sched.scheduleJob(job, trigger);
        loginfo(job,log,ft,trigger);
        //job4将在每天的17点到23点从零分钟开始每隔三分钟运行一次
        job = newJob(DemoJob.class).withIdentity("job4", "group1").build();
        trigger = newTrigger().withIdentity("trigger4", "group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0/3 17-23 * * ?"))
                .build();
        ft = sched.scheduleJob(job, trigger);
        loginfo(job,log,ft,trigger);

        //job5将在每月的1号和15号的上午10点运行
        job = newJob(DemoJob.class).withIdentity("job5", "group1").build();
        trigger = newTrigger().withIdentity("trigger5", "group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 10am 1,15 * ?")).build();


        //job6星期一到星期五每隔30秒运行一次
        job = newJob(DemoJob.class).withIdentity("job6", "group1").build();
        trigger = newTrigger().withIdentity("trigger6", "group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("0,30 * * ? * MON-FRI")).build();
        ft = sched.scheduleJob(job, trigger);
        loginfo(job,log,ft,trigger);

        //job7将在周六周日的0秒和30秒的时候启动
        job = newJob(DemoJob.class).withIdentity("job7","group1").build();
        trigger = newTrigger().withIdentity("trigger7", "group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("0,30 * * ? * SAT,SUN"))
                .build();
        ft = sched.scheduleJob(job, trigger);
        loginfo(job,log,ft,trigger);

        sched.start();
        log.info("已经启动Scheduler");
        log.info("---5分钟后关闭Scheduler----");
        try {
            Thread.sleep(300L*1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("---关闭----");
        sched.shutdown();
        log.info("已经关闭");

        SchedulerMetaData metaData=sched.getMetaData();
        log.info("执行了："+metaData.getNumberOfJobsExecuted()+" jobs");
    }

    public static void loginfo(JobDetail job,Logger log, Date ft, CronTrigger trigger) {
        log.info(job.getKey()+" has been schedule to run at: "+ft+" and repeat based on expression: " +
                trigger.getCronExpression());
    }
    public static void main(String[] args) throws Exception {
        run();
    }

}
