package quartz.demos.demo11;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by lm on 2016/3/16.
 */
public class DemoSimpleJob implements Job{
    private static Logger _log = LoggerFactory.getLogger(DemoSimpleJob.class);
    public static final String DELAY_TIME = "delay time";


    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobKey jobKey = context.getJobDetail().getKey();
        _log.info("Executing job: " + jobKey + " executing at " + new Date());

        long delayTime = context.getJobDetail().getJobDataMap().getLong(DELAY_TIME);

        try {
            Thread.sleep(delayTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        _log.info("Finished executing job：" + jobKey + " at " + new Date());


    }
}
