package quartz.demos.demo11;
import static org.quartz.DateBuilder.futureDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lm on 2016/3/16.
 */

/**
 * 学习总结：
 *  1.执行大量任务的一个例子
 *  2.newJob(DemoSimpleJob.class)
        .withIdentity("job" + count, "group1")
        .requestRecovery()//无论发生撤回还是失败都命令shced重新执行这个job
        .build();

 */
public class LoadDemo {

    private int _numberOfJobs=500;

    public LoadDemo(int inNumberOfJobs) {
        _numberOfJobs = inNumberOfJobs;
    }

    public void run() throws SchedulerException {
        Logger log = LoggerFactory.getLogger(LoadDemo.class);

        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();

        for(int count=1;count<=_numberOfJobs;count++) {
            JobDetail job = newJob(DemoSimpleJob.class)
                    .withIdentity("job" + count, "group1")
                    .requestRecovery()//无论发生撤回还是失败都命令shced重新执行这个job
                    .build();

            long timeDelay = (long) (Math.random() * 25000);
            job.getJobDataMap();
            job.getJobDataMap().put(DemoSimpleJob.DELAY_TIME, timeDelay);


            Trigger trigger = newTrigger().withIdentity("trigger_" + count, "group_1")
                    .startAt(futureDate(10000 + (count * 100), DateBuilder.IntervalUnit.MILLISECOND))
                    .build();
            sched.scheduleJob(job, trigger);
            if (count % 25 == 0) {
                log.info("....scheduled " + count + " jobs");
            }
        }
            sched.start();

            try {
                Thread.sleep(300L * 1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            sched.shutdown(true);

            SchedulerMetaData metaData = sched.getMetaData();
            log.info("Executed "+metaData.getNumberOfJobsExecuted()+" jobs");



    }

    public static void main(String[] args) throws SchedulerException {
        int numberOfJobs=500;
        if (args.length == 1) {
            numberOfJobs = Integer.valueOf(args[0]);

        }

        if (args.length > 1) {
            System.out.println("Usage: java "+LoadDemo.class.getName()+" # of jobs");
            return;
        }

        LoadDemo demo = new LoadDemo(numberOfJobs);
        demo.run();
    }

}
