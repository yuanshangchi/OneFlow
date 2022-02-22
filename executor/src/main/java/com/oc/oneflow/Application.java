package com.oc.oneflow;
import com.oc.oneflow.common.utils.ConfigUtil;
import com.oc.oneflow.executor.job.HiveJob;
import com.oc.oneflow.executor.listener.OrderListener;
import com.oc.oneflow.executor.service.HiveService;
import com.oc.oneflow.model.scheduler.JobDescriptor;
import com.oc.oneflow.model.vo.ConfigVO;
import com.oc.oneflow.model.vo.StepVO;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.util.*;

@SpringBootApplication
public class Application {

    @Autowired
    private ConfigUtil configUtil;
    private Scheduler scheduler;

    private static final Logger appLogger = LoggerFactory.getLogger(Application.class);
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    public void run() throws FileNotFoundException {
        appLogger.info("Oneflow begin to run");
        ConfigVO configVO = configUtil.getConfigVO();
        appLogger.info("Get Config VO");
        List<ConfigVO.DataSource> dataSources = configVO.getDataSource();
        appLogger.info("dataSources");
        configVO.getTasks().forEach(taskVO -> {
            String taskId = taskVO.getTaskId();
            String taskName = taskVO.getTaskName();
            String cron = taskVO.getCron();
            JobDescriptor jobDescriptor = new JobDescriptor();
            jobDescriptor.setName(taskName);
            jobDescriptor.setGroup(taskName);
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("taskName", taskName);
            paramMap.put("taskId", taskId);
            paramMap.put("cron", cron);
            jobDescriptor.setDataMap(paramMap);
            OrderListener orderListener = new OrderListener(taskName + "OrderListener");

            appLogger.info("Get task "+taskId+" Config");
            taskVO.getSteps().sort(new Comparator<StepVO>() {
                @Override
                public int compare(StepVO o1, StepVO o2) {
                    return Integer.parseInt(o1.getOrder()) - Integer.parseInt(o2.getOrder());
                }
            });
            Queue<JobDetail> jobDetailQueue = new LinkedList<>();
            taskVO.getSteps().forEach(stepVO -> {
                appLogger.info("Get step "+stepVO.getOrder()+"'s Config");
                String type = stepVO.getType();
                if (type.equals("hive")){
                    jobDescriptor.setJobClazz(HiveJob.class);
                   paramMap.put("path", stepVO.getPath());
                   paramMap.put("hiveParam", stepVO.getHiveParam());
                } else if (type.equals("spark")){
                    // spark
                }
                JobDetail jobDetail = jobDescriptor.buildJobDetail();
                jobDetailQueue.add(jobDetail);

            });
            Trigger jobTrigger = TriggerBuilder.newTrigger().withIdentity(taskName)
                    .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                    .build();
            try {
                scheduler.getListenerManager().addJobListener(orderListener);
                if (!jobDetailQueue.isEmpty()) {
                    JobDetail initialJobDetail = jobDetailQueue.poll();
                    scheduler.addJob(initialJobDetail, true);
                    scheduler.scheduleJob(initialJobDetail, jobTrigger);
                } else {
                    appLogger.warn(taskName + "Step list is empty");
                }

            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        });
    }
}
