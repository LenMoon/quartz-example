package quartz.demos.demo13;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by lm on 2016/3/16.
 */
public class DemoSimpleRecoveryJob implements Job{

    private static Logger _log = LoggerFactory.getLogger(DemoSimpleRecoveryJob.class);
    private static final String COUNT = "count";

    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobKey jobKey = context.getJobDetail().getKey();
        if (context.isRecovering()) {
            _log.info("DemoSimpleRecoveryJob: " + jobKey + " RECOVERING at " + new Date());
        } else {
            _log.info("DemoSimpleRecoveryJob: " + jobKey + "starting at " + new Date());
        }
        long delay=10L*1000L;

        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        JobDataMap data = context.getJobDetail().getJobDataMap();
        int count;
        if (data.containsKey(COUNT)) {
            count = data.getInt(COUNT);
        } else {
            count=0;
        }
        count++;
        data.put(COUNT, count);

        _log.info("DemoSimpleRecoveryJob: "+jobKey+" done at "+new Date()+"\r\n" +
                "Execution #"+count);
    }
}
