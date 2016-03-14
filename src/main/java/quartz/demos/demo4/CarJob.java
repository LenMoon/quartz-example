package quartz.demos.demo4;

import org.quartz.*;
import org.slf4j.Logger;

import java.util.Date;


/**
 * Created by lm on 2016/3/11.
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class CarJob implements Job{
    private static Logger _log = org.slf4j.LoggerFactory.getLogger(CarJob.class);

    public static final String FAVORITE_COLOR="favorite_color";
    public static final String RUN_COUNT = "count";

    private int _count=1;
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobKey key = context.getJobDetail().getKey();
        JobDataMap data = context.getJobDetail().getJobDataMap();
        int count = data.getInt(RUN_COUNT);
        String favorite_color = data.getString(FAVORITE_COLOR);

        _log.info(key+" run times: "+count+" and favorite_color is: "+favorite_color);
        data.put(RUN_COUNT, ++count);
    }
}
