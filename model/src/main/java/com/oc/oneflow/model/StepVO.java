package com.oc.oneflow.model;

import java.util.List;
import java.util.Map;

public class StepVO {
    private String order;
    private String stepName;
    private String type;

    //hive
    private Map<String, Object> hiveParam;
    private String path;

    //spark
    private String master;
    private String deployMode;
    private String className;

    //hdfs
    private String mode;
    private String source;
    private String destination;

    //script
    private String param;

    //dataloader
    private String sourceDataSource;
    private String destDataSource;
    private String sourcePath;

    private List<String> sourceTables;
    private List<String> destTables;
    private List<String> sourceCSV;

    //custom
    private String function;

    public String getOrder() {
        return order;
    }

    public String getStepName() {
        return stepName;
    }

    public String getType() {
        return type;
    }

    public Map<String, Object> getHiveParam() {
        return hiveParam;
    }

    public String getPath() {
        return path;
    }

    public String getMaster() {
        return master;
    }

    public String getDeployMode() {
        return deployMode;
    }

    public String getClassName() {
        return className;
    }

    public String getMode() {
        return mode;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public String getParam() {
        return param;
    }

    public String getSourceDataSource() {
        return sourceDataSource;
    }

    public String getDestDataSource() {
        return destDataSource;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public List<String> getSourceTables() {
        return sourceTables;
    }

    public List<String> getDestTables() {
        return destTables;
    }

    public List<String> getSourceCSV() {
        return sourceCSV;
    }

    public String getFunction() {
        return function;
    }
}
