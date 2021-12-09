package com.oc.oneflow.master;
import com.oc.oneflow.common.utils.ConfigUtil;
import com.oc.oneflow.model.ConfigVO;
import com.oc.oneflow.model.StepVO;
import com.oc.oneflow.model.TaskVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.util.List;

@SpringBootApplication(scanBasePackages={"com.oc.oneflow.common.utils","com.oc.oneflow.master"}, exclude = {DataSourceAutoConfiguration.class })



public class Application {

    @Autowired
    private ConfigUtil configUtil;


    private static final Logger appLogger = LoggerFactory.getLogger(Application.class);
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    public void run() throws FileNotFoundException {
        ConfigVO configVO = configUtil.getConfigVO();
        List<ConfigVO.DataSource> dataSources = configVO.getDataSource();
        System.out.println(dataSources.get(0).getName());
        configVO.getTasks().forEach(taskVO -> {
            String taskId = taskVO.getTaskId();
            String taskName = taskVO.getTaskName();
            String cron = taskVO.getCron();
            taskVO.getSteps().forEach(stepVO -> {
                String type = stepVO.getType();
                if (type.equals("hive")){
                    System.out.println("this is hive");
                }
            });
        });
    }
}
