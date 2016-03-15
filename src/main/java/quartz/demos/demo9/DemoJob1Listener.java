package quartz.demos.demo9;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lm on 2016/3/15.
 */
public class DemoJob1Listener implements JobListener{
    private static Logger _log = LoggerFactory.getLogger(DemoJob1Listener.class);
    //listener的名字
    public String getName() {
       return "job1_to_job2";
    }

    public void jobToBeExecuted(JobExecutionContext jobExecutionContext) {
        _log.info("Job1Listener says : Job is about to be excuted.");
    }

    public void jobExecutionVetoed(JobExecutionContext jobExecutionContext) {
        _log.info("Job1Listener says: job Exeution was vetoed");
    }

    public void jobWasExecuted(JobExecutionContext jobExecutionContext, JobExecutionException e) {
        _log.info("Job1Listener says: Job was excuted.");
        JobDetail job2 = JobBuilder.newJob(DemoSimpleJob2.class)
                .withIdentity("job2").build();
        Trigger trigger=TriggerBuilder.newTrigger().withIdentity("job2Trigger")
                .startNow().build();

        try {
            jobExecutionContext.getScheduler().scheduleJob(job2, trigger);
        } catch (SchedulerException e1) {
            _log.warn("Unable to schedule job2!");
            e1.printStackTrace();
        }

    }

}
