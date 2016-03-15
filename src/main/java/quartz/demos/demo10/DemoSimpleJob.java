package quartz.demos.demo10;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Set;

/**
 * Created by lm on 2016/3/15.
 */
public class DemoSimpleJob implements Job{
    private static Logger _log = LoggerFactory.getLogger(DemoSimpleJob.class);

    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobKey jobKey = context.getJobDetail().getKey();

        _log.info("Executing job: " + jobKey + " executing at: " + new Date());
        if (context.getMergedJobDataMap().size() > 0) {
            Set<String>keys=context.getMergedJobDataMap().keySet();
            for (String key : keys) {
                String val = context.getMergedJobDataMap().getString(key);
                _log.info(" - jobDataMap entry: "+key+" : "+val);
            }
        }
        context.setResult("hello");

    }
}
