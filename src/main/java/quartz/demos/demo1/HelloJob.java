package quartz.demos.demo1;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by lm on 2016/3/10.
 */
public class HelloJob implements Job {
    private static Logger log = LoggerFactory.getLogger(HelloJob.class);

    public void execute(JobExecutionContext jobCtx) throws JobExecutionException {
        log.info("Now run:"+jobCtx.getJobDetail().getKey().getName()+"at :"+new Date());
    }
}
