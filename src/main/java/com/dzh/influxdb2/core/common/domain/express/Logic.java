package com.dzh.influxdb2.core.common.domain.express;

/**
 * 逻辑连接符号 and、or
 */
public class Logic implements Express {
    private String logic;

    public Logic(String logic) {
        this.logic = logic;
    }

    public String getLogic() {
        return logic;
    }

    public void setLogic(String logic) {
        this.logic = logic;
    }

    @Override
    public String getSql() {
        return String.format(" %s ", logic);
    }
}
