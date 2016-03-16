package quartz.demos.demo12;

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
    public static final String MESSAGE = "msg";
    private Logger _log = LoggerFactory.getLogger(DemoSimpleJob.class);


    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobKey jobKey = context.getJobDetail().getKey();
        String message = context.getJobDetail().getJobDataMap().getString(MESSAGE);

        _log.info("DemoSimpleJob: " + jobKey + " executing at: " + new Date());
        _log.info("DemoSimpleJob: "+message);
    }
}
