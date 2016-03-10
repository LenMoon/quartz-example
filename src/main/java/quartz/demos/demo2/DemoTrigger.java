package quartz.demos.demo2;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;
import java.util.Date;

/**
 * Created by lm on 2016/3/10.
 */

/**
 * 学习总结：
 * 1.可以通过newTrigger().withScheule(SimpleSchedule.withIntervalInSeconds(X).withRepeatCount(X)).build
 *      来设定运行规则
 * 2.Scheduler.scheduleJob(job,trigger)，可以在scheduler.start()之前和之后调用
 * 3.scheduler.reScheduleJob(trigger.getKey,trigger)来重新调用任务
 * 4.Job job=newJob(XXX.class).withIdentity("xxx","xxx").storeDurably().build
 *      sched.addJob(job,true);
 *         sched.triggerJob(jobKey("job8","group1"));
 *         来直接调用Job
 */
public class DemoTrigger {
    public void run() throws Exception{
        Logger log = LoggerFactory.getLogger(DemoTrigger.class);
        log.info("---初始化---");

        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();

        log.info("初始化Scheduler完成");

        log.info("调度Job");

        //Jobs可以在sched.start()之前调用
        //生成一个X秒之后的时间
        Date startTime = DateBuilder.nextGivenSecondDate(null, 15);
        //job1将只调用一次

        JobDetail job = newJob(DemoJob.class).withIdentity("job1", "group1").build();
        SimpleTrigger trigger = (SimpleTrigger) newTrigger().withIdentity("trigger1", "group1").startAt(startTime).build();

        //将Job和触发器假如Scheduler容器中运行

        Date ft=sched.scheduleJob(job, trigger);

        log.info(job.getKey()+" will run at: "+ft+" and repeat: "+trigger.getRepeatCount()+" and every "+trigger.getRepeatInterval()/1000+" seconds");


        //job2 只调用一次
        job = newJob(DemoJob.class).withIdentity("job2","group1").build();

        trigger = (SimpleTrigger) newTrigger().withIdentity("trigger2","group1").startAt(startTime).build();

        ft = sched.scheduleJob(job, trigger);
        log.info(job.getKey()+" will run at: "+ft+" and repeat: "+trigger.getRepeatCount()+" times, every" +
                trigger.getRepeatInterval()/1000+" seconds");

        //job3运行11次（其中运行一次然后重复10次）
        //job3每10秒运行一次
        job = newJob(DemoJob.class).withIdentity("job3", "group1").build();
        trigger = (SimpleTrigger) newTrigger().withIdentity("trigger3", "group1").startAt(startTime).
        withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(10).withRepeatCount(10)).build();

        ft = sched.scheduleJob(job, trigger);
        log.info(job.getKey()+" will run at "+ft+" and repeat: "+trigger.getRepeatCount()+" times,every "+
        trigger.getRepeatInterval()/1000+" seconds");

        //job3将绑定另一个触发器
        //这次将只重复两次

        trigger = newTrigger().withIdentity("trigger3", "group2").startAt(startTime)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(10).withRepeatCount(2))
                .forJob(job).build();

        ft = sched.scheduleJob(trigger);

        log.info(job.getKey()+" will [also] at: "+ft+" and repeat: "+trigger.getRepeatCount()+" times,every"+
        trigger.getRepeatInterval()/1000+" seconds");

        //job4将运行6次 运行一次重复5次
        //job4 每10秒运行一次
        job = newJob(DemoJob.class).withIdentity("job4", "group1").build();
        trigger=newTrigger().withIdentity("trigger4","group1").startAt(startTime)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(10).withRepeatCount(5))
                .build();
        ft=sched.scheduleJob(job,trigger);
        log.info(job.getKey()+" will run at: "+ft+" and repeat: "+trigger.getRepeatCount()+" times,every" +
                trigger.getRepeatInterval()/1000+" seconds");

        //job5 将在5分钟后运行一次
        job = newJob(DemoJob.class).withIdentity("job5", "group1").build();

        trigger = (SimpleTrigger) newTrigger().withIdentity("trigger", "group1")
                .startAt(DateBuilder.futureDate(5, DateBuilder.IntervalUnit.MINUTE)).build();

        ft = sched.scheduleJob(job, trigger);
        log.info(job.getKey()+" will run at: "+ft+" and repeat: "+trigger.getRepeatCount()+
        "times,every "+trigger.getRepeatInterval()/1000+" times");

        //job6 每40秒一次 一直运行下去
        job=newJob(DemoJob.class).withIdentity("job6","group1").build();

        trigger=newTrigger().withIdentity("trigger6","group1z").startAt(startTime)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(40).repeatForever())
                .build();
        ft = sched.scheduleJob(job, trigger);
        log.info(job.getKey()+" will run at: "+ft+" and repeat: "+trigger.getRepeatCount()+"times,every" +
                trigger.getRepeatInterval()+"seconds");

        log.info("----启动 Scheduler----");
        sched.start();
        log.info("---Scheduler已经启动----");

        //jobs 也可以在Scheduler启动后调用
        //job7 将运行20次，每5分钟一次

        job=newJob(DemoJob.class).withIdentity("job7","group1").build();

        trigger=newTrigger().withIdentity("trigger7","group1").startAt(startTime)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(5).withRepeatCount(20))
                .build();
        ft = sched.scheduleJob(job, trigger);
        log.info(job.getKey()+" will run at: "+ft+" and repeat "+trigger.getRepeatCount()+" times " +
                ",every "+trigger.getRepeatInterval()/1000+" seconds");

        //工作可以直接调用，而不是等待一个触发器
        job=newJob(DemoJob.class).withIdentity("job8","group1").storeDurably().build();

        sched.addJob(job,true);
        log.info("'Manually' triggering job8...");

        sched.triggerJob(JobKey.jobKey("job8","group1"));
        log.info("---Waiting 30 seconds...----");


        try {
            Thread.sleep(10L*1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //jobs 可以被重新调用
        //job7 将被立即调用 然后每秒一次重复10次

        log.info("---reschedulng...-0--");
        trigger=newTrigger().withIdentity("trigger7","group1").startAt(startTime)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(1)
                .withRepeatCount(10)).build();

        ft = sched.rescheduleJob(trigger.getKey(), trigger);

        log.info("job7 rescheduled to run at: "+ft);
        log.info("---Waiting five minutes...---");
    }

    public static void main(String[] args) throws Exception {
        DemoTrigger demo = new DemoTrigger();
        demo.run();

    }
}
