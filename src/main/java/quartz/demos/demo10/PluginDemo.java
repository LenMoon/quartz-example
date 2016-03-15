package quartz.demos.demo10;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SchedulerMetaData;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lm on 2016/3/15.
 */
public class PluginDemo {
    public void run() throws SchedulerException {
        Logger log = LoggerFactory.getLogger(PluginDemo.class);
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched=null;

        try {
            sched = sf.getScheduler();
        } catch (SchedulerException e) {
            log.error(" Unable to load a class - most likely you do not have jta.jar" +
                    "add it there ofr this sample to run.",e);
            return;
        }
        sched.start();

        try {
            Thread.sleep(300L*1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        sched.shutdown(true);


        SchedulerMetaData metaData=sched.getMetaData();
        log.info("Executed "+metaData.getNumberOfJobsExecuted()+" jobs.");

    }

    public static void main(String[] args) throws SchedulerException {
        new PluginDemo().run();

    }
}
