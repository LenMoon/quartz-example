package quartz.demos.demo7;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by lm on 2016/3/15.
 */

/**
 * 可中断的Job
 */
public class DemoDumbInterruptableJob implements InterruptableJob{

    private static Logger _log = LoggerFactory.getLogger(DemoDumbInterruptableJob.class);

    private boolean _interrupted=false;

    private JobKey _jobKey=null;

    public void execute(JobExecutionContext context) throws JobExecutionException {
        _jobKey = context.getJobDetail().getKey();
        _log.info("--- "+_jobKey+" executing at "+new Date());

        try {
            for(int i=0;i<4;i++) {

                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (_interrupted) {
                    _log.info("--- "+_jobKey+" ---Interrrupted ... bailing out!");
                    return;
                }

            }
        } finally {
            _log.info("--- " + _jobKey + " completed at: " + new Date());
        }


    }

    public void interrupt() throws UnableToInterruptJobException {
        _log.info("--- " + _jobKey + " -- INTERRUPTING --");
        _interrupted=true;
    }
}
