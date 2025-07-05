package com.dsq.DaFuWeng.model;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

public class UserParticipation implements Serializable {
    private String id;
    private String userId;
    private String lotteryId;
    private String lotteryName;     // 彩票名称
    private double price;           // 价格
    private int participants;      // 参与人数
    private String time;            // 参与时间
    private String status;          // 状态
    private String winner;          // 中奖者

    public UserParticipation() {
    }

    public UserParticipation(String id, String lotteryName, double price, int participants,
                             String time, String status, String winner) {
        this.id = id;
        this.lotteryName = lotteryName;
        this.price = price;
        this.participants = participants;
        this.time = time;
        this.status = status;
        this.winner = winner;
    }

    // 添加缺失的getter方法
    // 添加缺失的setter方法
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setLotteryId(String lotteryId) {
        this.lotteryId = lotteryId;
    }
    public String getLotteryName() {
        return lotteryName;
    }

    public void setLotteryName(String lotteryName) {
        this.lotteryName = lotteryName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getParticipants() {
        return participants;
    }

    public void setParticipants(int participants) {
        this.participants = participants;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    // UserRepository.java中需添加的方法

}