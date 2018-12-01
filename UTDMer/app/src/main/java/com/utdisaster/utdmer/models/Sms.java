package com.utdisaster.utdmer.models;

import java.sql.Timestamp;

public class Sms {
    private String id;
    private String address;
    private String msg;
    private boolean readState;
    private Timestamp time;
    private String folderName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isReadState() {
        return readState;
    }

    public void setReadState(boolean readState) {
        this.readState = readState;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    @Override
    public String toString() {
        if("inbox".equals(folderName)) {
            return "From: " + address + "\nMessage: " + msg + "\nTimestamp: " + time;
        }
        if("sent".equals(folderName)) {
            return "To: " + address + "\nMessage: " + msg + "\nTimestamp: " + time;
        }
        return "Sms{" +
                "id='" + id + '\'' +
                ", address='" + address + '\'' +
                ", msg='" + msg + '\'' +
                ", readState=" + readState +
                ", time=" + time +
                ", folderName='" + folderName + '\'' +
                '}';
    }
}
