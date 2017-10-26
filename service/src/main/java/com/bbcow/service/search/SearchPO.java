package com.bbcow.service.search;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by adan on 2017/10/21.
 */
public class SearchPO {
    private String cmd = "ADD";
    private Map<String, Object> fields = new HashMap();

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public Map<String, Object> getFields() {
        return fields;
    }

    public void setFields(Map<String, Object> fields) {
        this.fields = fields;
    }
}
