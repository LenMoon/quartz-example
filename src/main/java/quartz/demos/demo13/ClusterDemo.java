package quartz.demos.demo13;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.quartz.DateBuilder.futureDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by lm on 2016/3/16.
 */
public class ClusterDemo {
    private static Logger _log = LoggerFactory.getLogger(ClusterDemo.class);

    public void run(boolean inClearJobs,boolean inScheduleJobs) throws Exception{
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();
        if (inClearJobs) {
            _log.warn("****Deleting existing jobs/triggers *****");
            sched.clear();
        }
        if (inScheduleJobs) {
            _log.info("---scheduling jobs---");
            String schedId = sched.getSchedulerInstanceId();

            int count=1;
            JobDetail job = newJob(DemoSimpleRecoveryJob.class)
                    .withIdentity("job_" + count, schedId)
                    .requestRecovery()
                    .build();

            SimpleTrigger trigger = newTrigger().withIdentity("trigger_" + count, schedId)
                    .startAt(futureDate(1, DateBuilder.IntervalUnit.SECOND))
                    .withSchedule(simpleSchedule().withRepeatCount(20).withIntervalInSeconds(5))
                    .build();

        }




    }

}
