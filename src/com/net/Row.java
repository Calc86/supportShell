package com.net;


/**
 * Created by calc on 02.04.14.
 *
 */
public abstract class Row {
    private int id;
    private boolean isClosed = false;
    private boolean isAccepted = true;
    private boolean isCmdDone = true;
    private String cmd = "";
    private String cmdReturn = "";

    public Row(int id) {
        this.id = id;
    }

    public Row() {
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean isAccepted) {
        this.isAccepted = isAccepted;
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

    public abstract boolean save();

    public abstract boolean load();
}
