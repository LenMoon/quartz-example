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
public class DemoBadJob1 implements Job{
    private static Logger _log = LoggerFactory.getLogger(DemoBadJob1.class);
    private int calculation;

    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobKey jobKey=context.getJobDetail().getKey();
        JobDataMap dataMap=context.getJobDetail().getJobDataMap();

        int denominator = dataMap.getInt("denominator");
        _log.info("---" + jobKey + " executing at " + new Date() + " with denominator " + denominator);

        try {
            calculation=4815/denominator;
        } catch (Exception e) {
            _log.info("---ERROR IN JOB!");
            JobExecutionException e2 = new JobExecutionException(e);
            //修改除数的值，所以下次将不会再失败
            dataMap.put("denominator", 1);
            //立即执行
            e2.setRefireImmediately(true);
            throw e2;
        }
        _log.info("---"+jobKey+" completed at "+new Date());
    }
}
