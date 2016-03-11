package quartz.demos.demo3;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by lm on 2016/3/11.
 */
public class DemoJob implements Job {
    private static Logger _log = LoggerFactory.getLogger(DemoJob.class);
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobKey key=jobExecutionContext.getJobDetail().getKey();
        System.out.println(key+" was runed at "+new Date());
    }
}
