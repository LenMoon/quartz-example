package quartz.demos.demo9;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.KeyMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;
/**
 * Created by lm on 2016/3/15.
 */

/**
 * 学习和总结：
 *
 */
public class ListenerDemo {

    public void run() throws SchedulerException {
        Logger log = LoggerFactory.getLogger(ListenerDemo.class);

        log.info("初始化");

        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();

        JobDetail job = newJob(DemoSimpleJob1.class).withIdentity("job1")
                .build();
        Trigger trigger = newTrigger().withIdentity("trigger1").startNow().build();

        JobListener listener = new DemoJob1Listener();
        Matcher<JobKey> matcher = KeyMatcher.keyEquals(job.getKey());
        sched.getListenerManager().addJobListener(listener,matcher);

        sched.scheduleJob(job, trigger);

        sched.start();

        try {
            Thread.sleep(30L*1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //等待当前任务完成
        sched.shutdown(true);


        SchedulerMetaData metaData = sched.getMetaData();

        log.info("Excuted " + metaData.getNumberOfJobsExecuted() + " jobs");

    }

    public static void main(String[] args) throws SchedulerException {
        new ListenerDemo().run();

    }


}
