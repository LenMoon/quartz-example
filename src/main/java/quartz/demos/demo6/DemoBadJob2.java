package quartz.demos.demo6;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by lm on 2016/3/14.
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class DemoBadJob2 implements Job {

    private static Logger _log = LoggerFactory.getLogger(DemoBadJob2.class);
    private int calculation;
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobKey jobKey = context.getJobDetail().getKey();
        _log.info("---" + jobKey + " executing at " + new Date());

        try {
            int zero =0;
            calculation=4815/zero;
        } catch (Exception e) {
            _log.info("---error in job!");
            JobExecutionException e2 = new JobExecutionException(e);
            e2.setUnscheduleAllTriggers(true);
            throw e2;
        }


        _log.info("---" + jobKey + " completed at " + new Date());
    }
}
