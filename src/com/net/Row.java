package com.net;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by calc on 02.04.14.
 */
public class Row {
    private int id;
    private boolean isClosed = false;
    private boolean isCmdDone = true;
    private String cmd = "";
    private String cmdReturn = "";

    public Row(int id) {
        this.id = id;
    }

    public Row() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean isClosed) {
        this.isClosed = isClosed;
    }

    public boolean isCmdDone() {
        return isCmdDone;
    }

    public void setCmdDone(boolean isCmdDone) {
        this.isCmdDone = isCmdDone;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getCmdReturn() {
        return cmdReturn;
    }

    public void setCmdReturn(String cmdReturn) {
        this.cmdReturn = cmdReturn;
    }

    public void save(){

    }

    public void load(){

    }

    @Override
    public String toString() {
        /*return "Row{" +
                "id=" + id +
                ", isClosed=" + isClosed +
                ", isCmdDone=" + isCmdDone +
                ", cmd='" + cmd + '\'' +
                ", cmdReturn='" + cmdReturn + '\'' +
                '}';*/
        try {
            return "id=" + id +
                    "&closed=" + isClosed +
                    "&cmd_done=" + isCmdDone +
                    "&cmd=" + URLEncoder.encode(cmd,"UTF-8") +
                    "&cmd_ret=" + URLEncoder.encode(cmdReturn,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new NetException(e.getMessage());
        }
    }
}
