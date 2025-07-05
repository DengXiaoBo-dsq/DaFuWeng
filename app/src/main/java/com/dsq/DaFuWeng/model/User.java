//package com.dsq.DaFuWeng.model;
//
//import java.io.Serializable;
//
//public class User implements Serializable {
//    private String id;
//    private String username;
//    private String password;
//    private String phone;
//    private double balance;
//    private int level;
//    private long createTime;
//    private boolean isVip;
//
//    public User() {
//    }
//
//    public User(String id, String username, String password, String phone, double balance, int level, long createTime, boolean isVip) {
//        this.id = id;
//        this.username = username;
//        this.password = password;
//        this.phone = phone;
//        this.balance = balance;
//        this.level = level;
//        this.createTime = createTime;
//        this.isVip = isVip;
//    }
//
//    // Getters and Setters
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getPhone() {
//        return phone;
//    }
//
//    public void setPhone(String phone) {
//        this.phone = phone;
//    }
//
//    public double getBalance() {
//        return balance;
//    }
//
//    public void setBalance(double balance) {
//        this.balance = balance;
//    }
//
//    public int getLevel() {
//        return level;
//    }
//
//    public void setLevel(int level) {
//        this.level = level;
//    }
//
//    public long getCreateTime() {
//        return createTime;
//    }
//
//    public void setCreateTime(long createTime) {
//        this.createTime = createTime;
//    }
//
//    public boolean isVip() {
//        return isVip;
//    }
//
//    public void setVip(boolean vip) {
//        isVip = vip;
//    }
//}
package com.dsq.DaFuWeng.model;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    private String id;
    private String phone;         // 手机号码作为唯一账号
    private String password;
    private double balance;
    private int level;
    private Date createTime;
    private boolean isVip;

    public User() {
    }

    public User(String id, String phone, String password, double balance, int level, Date createTime, boolean isVip) {
        this.id = id;
        this.phone = phone;
        this.password = password;
        this.balance = balance;
        this.level = level;
        this.createTime = createTime;
        this.isVip = isVip;
    }

    // Getter and Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public boolean isVip() { return isVip; }
    public void setVip(boolean vip) { isVip = vip; }
}