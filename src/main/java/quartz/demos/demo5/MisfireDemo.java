package quartz.demos.demo5;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by lm on 2016/3/14.
 */

/**
 *
 * 学习总结：当错过任务怎么办？
 * 以下给出一些Quartz的Misfire处理规则
 * Quartz 的 Misfire处理规则:
 调度(scheduleJob)或恢复调度(resumeTrigger,resumeJob)后不同的misfire对应的处理规则

 CronTrigger

 withMisfireHandlingInstructionDoNothing
 ——不触发立即执行
 ——等待下次Cron触发频率到达时刻开始按照Cron频率依次执行

 withMisfireHandlingInstructionIgnoreMisfires
 ——以错过的第一个频率时间立刻开始执行
 ——重做错过的所有频率周期后
 ——当下一次触发频率发生时间大于当前时间后，再按照正常的Cron频率依次执行

 withMisfireHandlingInstructionFireAndProceed
 ——以当前时间为触发频率立刻触发一次执行
 ——然后按照Cron频率依次执行


 SimpleTrigger

 withMisfireHandlingInstructionFireNow
 ——以当前时间为触发频率立即触发执行
 ——执行至FinalTIme的剩余周期次数
 ——以调度或恢复调度的时刻为基准的周期频率，FinalTime根据剩余次数和当前时间计算得到
 ——调整后的FinalTime会略大于根据starttime计算的到的FinalTime值

 withMisfireHandlingInstructionIgnoreMisfires
 ——以错过的第一个频率时间立刻开始执行
 ——重做错过的所有频率周期
 ——当下一次触发频率发生时间大于当前时间以后，按照Interval的依次执行剩下的频率
 ——共执行RepeatCount+1次

 withMisfireHandlingInstructionNextWithExistingCount
 ——不触发立即执行
 ——等待下次触发频率周期时刻，执行至FinalTime的剩余周期次数
 ——以startTime为基准计算周期频率，并得到FinalTime
 ——即使中间出现pause，resume以后保持FinalTime时间不变


 withMisfireHandlingInstructionNowWithExistingCount
 ——以当前时间为触发频率立即触发执行
 ——执行至FinalTIme的剩余周期次数
 ——以调度或恢复调度的时刻为基准的周期频率，FinalTime根据剩余次数和当前时间计算得到
 ——调整后的FinalTime会略大于根据starttime计算的到的FinalTime值

 withMisfireHandlingInstructionNextWithRemainingCount
 ——不触发立即执行
 ——等待下次触发频率周期时刻，执行至FinalTime的剩余周期次数
 ——以startTime为基准计算周期频率，并得到FinalTime
 ——即使中间出现pause，resume以后保持FinalTime时间不变

 withMisfireHandlingInstructionNowWithRemainingCount
 ——以当前时间为触发频率立即触发执行
 ——执行至FinalTIme的剩余周期次数
 ——以调度或恢复调度的时刻为基准的周期频率，FinalTime根据剩余次数和当前时间计算得到
 ——调整后的FinalTime会略大于根据starttime计算的到的FinalTime值
 MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_REMAINING_REPEAT_COUNT
 ——此指令导致trigger忘记原始设置的starttime和repeat-count
 ——触发器的repeat-count将被设置为剩余的次数
 ——这样会导致后面无法获得原始设定的starttime和repeat-count值
 */
public class MisfireDemo {
    public void run() throws SchedulerException {
        Logger log = LoggerFactory.getLogger(MisfireDemo.class);

        log.info("----初始化-----");

        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();

        Date startTime = DateBuilder.nextGivenSecondDate(null, 15);
        //Job1当一个Job执行的时间大于trigger设置的时间trigger的时间将
        //失效。
        JobDetail job = JobBuilder.newJob(DemoStatefulDumbJob.class)
                .withIdentity("statefulJob1", "group1")
                .usingJobData(DemoStatefulDumbJob.EXECUTION_DELAY, 10000L)
                .build();
        SimpleTrigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(3).withRepeatCount(3))
                .build();

        Date ft = sched.scheduleJob(job, trigger);
        log.info(job.getKey()+" will run at: "+ft+" and repeat: "+trigger.getRepeatCount()+" times,every" +
                trigger.getRepeatInterval()/1000+" seconds");


        //job2

        job = JobBuilder.newJob(DemoStatefulDumbJob.class).withIdentity("statefulJob2", "group1")
                .usingJobData(DemoStatefulDumbJob.EXECUTION_DELAY, 10000L).build();
        trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger2", "group1")
                .startAt(startTime)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(3)
                        .withRepeatCount(3).withMisfireHandlingInstructionNowWithExistingCount())
                .build();

        ft = sched.scheduleJob(job, trigger);
        log.info(job.getKey()+" will run at: "+ft+" and repeat: "+trigger.getRepeatCount()+"times,every " +
                +trigger.getRepeatInterval()/1000+" seconds");


        sched.start();

        try {
            Thread.sleep(600L*1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sched.shutdown(true);

        SchedulerMetaData metaData = sched.getMetaData();
        log.info("共执行了 "+metaData.getNumberOfJobsExecuted()+" 次任务！");

    }

    public static void main(String[] args) throws SchedulerException {
        new MisfireDemo().run();
    }

}
