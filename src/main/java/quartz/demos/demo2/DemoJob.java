package quartz.demos.demo2;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by lm on 2016/3/10.
 */
public class DemoJob implements Job{
    private static Logger _log = LoggerFactory.getLogger(DemoJob.class);

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobKey jobKey = jobExecutionContext.getJobDetail().getKey();
        _log.info("DemoJob says: "+jobKey+" excuting at "+new Date());
    }
}
