package quartz.demos.demo12;

import org.apache.log4j.spi.LoggerFactory;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SchedulerMetaData;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;

import java.io.IOException;
import java.rmi.server.RemoteServer;
import java.util.Properties;

/**
 * Created by lm on 2016/3/16.
 */

/**
 * 学习和总结：
 *  1.可以远程创建任务
 *  2.通过将server.properties和client.properties配置文件传入schedulerfactory
 *      可以实现远程任务添加
 */
public class RemoteServerDemo {

    public void run() throws SchedulerException, IOException {
        Logger log = org.slf4j.LoggerFactory.getLogger(RemoteServer.class);
        log.info("初始化");

        Properties server = new Properties();
        server.load(this.getClass().getClassLoader().getResourceAsStream("server.properties"));
        SchedulerFactory sf = new StdSchedulerFactory(server);
        Scheduler sched = sf.getScheduler();

        sched.start();

        try {
            Thread.sleep(60L*1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        sched.shutdown(true);

        SchedulerMetaData metaData=sched.getMetaData();
        log.info("共执行了： "+metaData.getNumberOfJobsExecuted()+" 个任务！");

    }

    public static void main(String[] args) throws SchedulerException, IOException {
        new RemoteServerDemo().run();
    }
}
