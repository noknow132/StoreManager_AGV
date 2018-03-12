package com.entity;
/**
 * @author 罗欢欢
 * @date 2018-2-3
 * @remark 运行步骤实体
 */
public class RunStep {
    private String runStepId;

    private Integer runCode;

    private String stepName;

    private String plcId;

    public String getRunStepId() {
        return runStepId;
    }

    public void setRunStepId(String runStepId) {
        this.runStepId = runStepId == null ? null : runStepId.trim();
    }

    public Integer getRunCode() {
        return runCode;
    }

    public void setRunCode(Integer runCode) {
        this.runCode = runCode;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName == null ? null : stepName.trim();
    }

    public String getPlcId() {
        return plcId;
    }

    public void setPlcId(String plcId) {
        this.plcId = plcId == null ? null : plcId.trim();
    }
}