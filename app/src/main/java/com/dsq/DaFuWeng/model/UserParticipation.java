package com.dsq.DaFuWeng.model;

import java.io.Serializable;

public class UserParticipation implements Serializable {
    private String id;             // 参与记录ID
    private String userId;         // 参与者用户ID（用于判断是否为当前用户）
    private String phone;          // 新增：参与者手机号（账号，用于显示）
    private String lotteryId;      // 活动ID
    private String lotteryName;    // 彩票/活动名称
    private double price;          // 参与价格
    private int participants;      // 该活动总参与人数
    private String time;           // 参与时间
    private String status;         // 参与状态
    private String winner;         // 中奖者标识（如"1"表示中奖，"0"未中奖）
    private int winCount;          // 新增：中奖次数（累计）

    public UserParticipation() {
    }

    // 完善构造方法（包含新增字段）
    public UserParticipation(String id, String userId, String phone, String lotteryName,
                             double price, int participants, String time, String status,
                             String winner, int winCount) {
        this.id = id;
        this.userId = userId;
        this.phone = phone;
        this.lotteryName = lotteryName;
        this.price = price;
        this.participants = participants;
        this.time = time;
        this.status = status;
        this.winner = winner;
        this.winCount = winCount;
    }

    // 补充所有字段的getter和setter（关键修改）
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; } // 新增：获取参与者ID（用于对比当前用户）
    public void setUserId(String userId) { this.userId = userId; }

    public String getPhone() { return phone; } // 新增：获取手机号（显示账号）
    public void setPhone(String phone) { this.phone = phone; }

    public String getLotteryId() { return lotteryId; } // 新增：获取活动ID
    public void setLotteryId(String lotteryId) { this.lotteryId = lotteryId; }

    public String getLotteryName() { return lotteryName; }
    public void setLotteryName(String lotteryName) { this.lotteryName = lotteryName; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getParticipants() { return participants; }
    public void setParticipants(int participants) { this.participants = participants; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getWinner() { return winner; }
    public void setWinner(String winner) { this.winner = winner; }

    public int getWinCount() { return winCount; } // 新增：获取中奖次数（显示）
    public void setWinCount(int winCount) { this.winCount = winCount; }
}