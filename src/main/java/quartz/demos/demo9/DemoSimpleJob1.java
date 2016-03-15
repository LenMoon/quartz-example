package quartz.demos.demo9;

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
public class DemoSimpleJob1 implements Job{

    private static Logger _log = LoggerFactory.getLogger(DemoSimpleJob1.class);

    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobKey jobKey=context.getJobDetail().getKey();
        _log.info("DemoSimple job1 says:" + jobKey + " executing at " + new Date());
    }
}
