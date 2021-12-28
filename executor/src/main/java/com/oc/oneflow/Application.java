package com.oc.oneflow;
import com.oc.oneflow.common.utils.ConfigUtil;
import com.oc.oneflow.executor.service.HiveService;
import com.oc.oneflow.executor.service.impl.HiveServiceimpl;
import com.oc.oneflow.model.ConfigVO;
import com.oc.oneflow.model.StepVO;
import com.oc.oneflow.model.TaskVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.util.List;

@SpringBootApplication
public class Application {

    @Autowired
    private ConfigUtil configUtil;
    private HiveService hiveService;

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
            appLogger.info("Get task "+taskId+" Config");
            taskVO.getSteps().forEach(stepVO -> {
                appLogger.info("Get step "+stepVO.getOrder()+"'s Config");
                String type = stepVO.getType();
                if (type.equals("hive")){
                    appLogger.info("run Hive");
                    hiveService.runHql(stepVO.getPath(), stepVO.getHiveParam());
                }
            });
        });
    }
}
