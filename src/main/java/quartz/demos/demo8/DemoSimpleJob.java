package quartz.demos.demo8;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by lm on 2016/3/15.
 */
public class DemoSimpleJob implements Job {

    private static Logger _log = LoggerFactory.getLogger(DemoSimpleJob.class);


    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobKey jobkey = context.getJobDetail().getKey();
        _log.info("simpleJOb says: "+jobkey+" excuting at "+ new Date());
    }
}
