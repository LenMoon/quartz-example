package quartz.demos.demo5;

import org.quartz.*;

import java.util.Date;

/**
 * Created by lm on 2016/3/14.
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class DemoStatefulDumbJob implements Job{

    public static final String NUM_EXECUTIONS = "NumExecutions";

    public static final String EXECUTION_DELAY = "ExecutionDelay";


    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.err.println("---"+jobExecutionContext.getJobDetail().getKey()+" executing.["+new Date()+"]");

        JobDataMap map = jobExecutionContext.getJobDetail().getJobDataMap();

        int executeCount=0;
        if (map.containsKey(NUM_EXECUTIONS)) {
            executeCount = map.getInt(NUM_EXECUTIONS);
        }

        executeCount++;

        map.put(NUM_EXECUTIONS, executeCount);

        long delay = 5000L;
        if (map.containsKey(EXECUTION_DELAY)) {
            delay = map.getLong(EXECUTION_DELAY);
        }


        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            //
        }
        System.err.println("  -"+jobExecutionContext.getJobDetail().getKey()+" complete ("+executeCount+")");

    }
}
