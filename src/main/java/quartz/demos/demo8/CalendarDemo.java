package quartz.demos.demo8;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.calendar.AnnualCalendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.quartz.DateBuilder.dateOf;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by lm on 2016/3/15.
 */

/**
 * 学习和总结：
 *      1.AnnualCalendar.setDayExcluded()，表示排除某一天
 *          最好不要在排除那天启动，否则调度作业的时间不确定
 *     2.sched.addCalendar("holidays", holidays, false, false);
 *          后面的两个false分别表示替换和更新原来的AnnualCalendar
 *     3..modifiedByCalendar("holidays")通过日历的名字将这个日历应用
 *          到触发器上
 */
public class CalendarDemo {

    public void run() throws SchedulerException {
        final Logger log = LoggerFactory.getLogger(CalendarDemo.class);

        log.info("初始化");

        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();

        AnnualCalendar holidays = new AnnualCalendar();

        Calendar fourthOfJuly = new GregorianCalendar(2016, 3, 15);

        //重新定义一个被排除(true)或者被包含的时间点（false）
        holidays.setDayExcluded(fourthOfJuly, true);
        Calendar halloween = new GregorianCalendar(2016, 6, 1);
        holidays.setDayExcluded(halloween, true);
        Calendar chrismas = new GregorianCalendar(2016, 11, 25);
        holidays.setDayExcluded(chrismas,true);


        //两个false分别代表替换和更新原来的AnnualCalendar
        sched.addCalendar("holidays", holidays, false, false);

        Date runDate = DateBuilder.dateOf(14, 50, 10, 15, 3);
        JobDetail job = newJob(DemoSimpleJob.class)
                .withIdentity("job1", "group1")
                .build();

        SimpleTrigger trigger = newTrigger().withIdentity("trigger1", "group1")
                .withSchedule(simpleSchedule().withIntervalInHours(1)
                        .repeatForever()).modifiedByCalendar("holidays")
                .build();


        Date firstRunTime = sched.scheduleJob(job, trigger);

        log.info(job.getKey()+" will run at: "+firstRunTime+" and repeat: "+
            trigger.getRepeatCount()+" times,every "+trigger.getRepeatInterval()/1000+" seconds");

        sched.start();

        try {
            Thread.sleep(30L*1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        sched.shutdown(true);

        SchedulerMetaData metaData = sched.getMetaData();
        log.info("Executed "+ metaData.getNumberOfJobsExecuted()+" jobs");



    }

    public static void main(String[] args) throws SchedulerException {
        new CalendarDemo().run();

    }

}
