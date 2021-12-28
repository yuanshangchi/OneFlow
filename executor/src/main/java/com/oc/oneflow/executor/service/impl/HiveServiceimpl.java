package com.oc.oneflow.executor.service.impl;

import com.oc.oneflow.executor.service.HiveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.io.PathResource;
import org.springframework.data.hadoop.hive.HiveScript;
import org.springframework.data.hadoop.hive.HiveTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@EnableAutoConfiguration
public class HiveServiceimpl implements HiveService {
    @Autowired
    private JdbcTemplate hiveJdbcTemplate;

    @Autowired
    private HiveTemplate hiveTemplate;

    private static final Logger appLogger = LoggerFactory.getLogger(HiveServiceimpl.class);
    @Override
    public void execute(String sql) {
        appLogger.info("run hive execute");
        appLogger.info(sql);
        hiveJdbcTemplate.execute(sql);
        appLogger.info("hive sql executed");
    }

    @Override
    public List<Map<String, Object>> queryForList(String sql){
        appLogger.info("run hive queryForList");
        appLogger.info(sql);
        List<Map<String, Object>> resMap = hiveJdbcTemplate.queryForList(sql);
        appLogger.info("hive sql executed");
        return resMap;
    }

    @Override
    public Map<String, Object> queryForMap(String sql){
        appLogger.info("run hive queryForMap");
        appLogger.info(sql);
        Map<String, Object> resMap = hiveJdbcTemplate.queryForMap(sql);
        appLogger.info("hive sql executed");
        return resMap;
    }

    @Override
    public void runHql(String filePath, Map<String, Object> paramMap){
        appLogger.info("run hive runHql");
        HiveScript hiveScript = new HiveScript(new PathResource(filePath), paramMap);
        hiveTemplate.executeScript(hiveScript);
        appLogger.info("hql run executed");
    }
}
