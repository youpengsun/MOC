package com.sap.moc.vo;

public class BaseMessage {
    private String touser;
    private String topart;
    private String totag;
    private String msgtype;
    private String agentid;
    public String getTouser() {
        return touser;
    }
    public void setTouser(String touser) {
        this.touser = touser;
    }
    public String getTopart() {
        return topart;
    }
    public void setTopart(String topart) {
        this.topart = topart;
    }
    public String getTotag() {
        return totag;
    }
    public void setTotag(String totag) {
        this.totag = totag;
    }
    public String getMsgtype() {
        return msgtype;
    }
    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }
    public String getAgentid() {
        return agentid;
    }
    public void setAgentid(String agentId) {
        this.agentid = agentId;
    }
}
