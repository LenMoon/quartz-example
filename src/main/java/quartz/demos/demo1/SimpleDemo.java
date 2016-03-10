package quartz.demos.demo1;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by lm on 2016/3/10.
 */

/**
 * 总结：一个最简单的例子
 *1.创建一个实现了Job接口的Job类
 * 2.初始化了一个Scheduler  --通过new StdSchedulerFactory().getScheduler();
 * 3.创建一个JobDetail关联我们创建的Job类
 * 4.定义一个Trigger，设置启动时间
 * 5.将JobDetail和Trigger假如到Scheduler中并关联起来
 * 6.运行Scheduler.start();
 * 7.最后关闭Scheduler.shutdown();
 */
public class SimpleDemo {
    public static void run() throws Exception {
        Logger log = LoggerFactory.getLogger(SimpleDemo.class);
        log.info("----初始化---------");

        //首先必须获取一个Scheduler的引用
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();

        log.info("----调度Job------");

        //定义一个Job并将它绑定到HelloJob类

        JobDetail job = JobBuilder.newJob(HelloJob.class).withIdentity("job1", "group1").build();

        //触发Job启动时间
        //设置启动时间为一分钟后
        Date runTime = DateBuilder.evenMinuteDate(new Date());

        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1").startAt(runTime).build();
        //让Quartz容器用触发器调度指定的工作

        sched.scheduleJob(job, trigger);

        log.info(job.getKey()+"will run at:"+runTime);

        sched.start();

        log.info("-----Started Scheduler ------");

        log.info("-----Waiting 65 seconds ...-----");

        try {
            Thread.sleep(60L*1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.info("----关闭----");
        sched.shutdown();
        log.info("----关闭完成-----");

    }

    public static void main(String[] args) throws Exception {
        run();
    }
}
