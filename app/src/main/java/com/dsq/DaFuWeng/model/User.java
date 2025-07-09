package com.dsq.DaFuWeng.model;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    private String id;
    private String phone;         // 手机号码作为唯一账号
    private String password;
    private double balance;       // 仅保留必要字段
    private Date createTime;      // 保留创建时间（服务端返回）

    // 无参构造
    public User() {}

    // 有参构造（移除level和isVip）
    public User(String id, String phone, String password, double balance, Date createTime) {
        this.id = id;
        this.phone = phone;
        this.password = password;
        this.balance = balance;
        this.createTime = createTime;
    }



    // Getter和Setter（仅保留现有字段）
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
}