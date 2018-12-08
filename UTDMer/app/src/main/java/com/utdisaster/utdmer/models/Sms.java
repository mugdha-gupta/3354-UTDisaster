package com.utdisaster.utdmer.models;

import java.sql.Timestamp;
import java.util.Objects;

public class Sms implements Comparable<Sms> {
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
    public int compareTo(Sms obj) {
        return this.time.compareTo(obj.getTime());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sms sms = (Sms) o;
        return readState == sms.readState &&
                Objects.equals(id, sms.id) &&
                Objects.equals(address, sms.address) &&
                Objects.equals(msg, sms.msg) &&
                Objects.equals(time, sms.time) &&
                Objects.equals(folderName, sms.folderName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, address, msg, readState, time, folderName);
    }

    @Override
    public String toString() {
        if("inbox".equals(folderName)) {
            return "From: " + address + "\tId: " + id + "\nMessage: " + msg + "\nTimestamp: " + time;
        }
        if("sent".equals(folderName)) {
            return "To: " + address + "\tId: " + id + "\nMessage: " + msg + "\nTimestamp: " + time;
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
