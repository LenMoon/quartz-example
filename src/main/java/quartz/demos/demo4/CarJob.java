package quartz.demos.demo4;

import org.quartz.*;
import org.slf4j.Logger;

import java.util.Date;


/**
 * Created by lm on 2016/3/11.
 */
public class CarJob implements Job{
    private static Logger _log = org.slf4j.LoggerFactory.getLogger(CarJob.class);

    public static final String FAVORITE_COLOR="favorite_color";
    public static final String RUN_COUNT = "count";

    private int _count=1;
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobKey jobKey = context.getJobDetail().getKey();

        JobDataMap data = context.getJobDetail().getJobDataMap();
        System.out.println(data);
        String color = data.getString(FAVORITE_COLOR);
        int count = data.getInt(RUN_COUNT);
        count++;
        _log.info(jobKey+" 's color: "+color+" and run: "+count+" times,at "+new Date());
        data.put(RUN_COUNT, count);
        _count++;
    }
}
