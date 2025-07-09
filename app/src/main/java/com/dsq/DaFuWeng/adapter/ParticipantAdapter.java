package com.dsq.DaFuWeng.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.dsq.DaFuWeng.R;
import com.dsq.DaFuWeng.model.UserParticipation;
import java.util.List;

// 参与记录适配器（显示用户所有参与的活动）
public class ParticipantAdapter extends RecyclerView.Adapter<ParticipantAdapter.ViewHolder> {

    private Context context;
    private List<UserParticipation> participations; // 参与记录列表
    private String currentUserId; // 当前用户ID（用于高亮显示）

    // 构造方法
    public ParticipantAdapter(Context context, String currentUserId) {
        this.context = context;
        this.currentUserId = currentUserId;
    }

    // 设置数据
    public void setParticipations(List<UserParticipation> participations) {
        this.participations = participations;
        notifyDataSetChanged(); // 刷新列表
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 加载列表项布局
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_participation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (participations == null || participations.isEmpty()) return;

        UserParticipation item = participations.get(position);

        // 绑定数据到控件
        holder.tvLotteryName.setText(item.getLotteryName());
        holder.tvPrice.setText("价格: ¥" + item.getPrice());
        holder.tvParticipants.setText("参与人数: " + item.getParticipants());
        holder.tvTime.setText("时间: " + item.getTime());
        holder.tvStatus.setText("状态: " + item.getStatus());

        // 中奖状态显示（如果是中奖记录，显示"已中奖"）
        if ("1".equals(item.getWinner()) || "已中奖".equals(item.getWinner())) {
            holder.tvWinner.setText("已中奖");
            holder.tvWinner.setVisibility(View.VISIBLE);
        } else {
            holder.tvWinner.setVisibility(View.GONE);
        }

        // 高亮当前用户的记录（背景设为浅灰色）
        if (currentUserId != null && currentUserId.equals(item.getUserId())) {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.light_gray));
        } else {
            // 非当前用户用默认背景
            holder.itemView.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
        }
    }

    @Override
    public int getItemCount() {
        return participations == null ? 0 : participations.size();
    }

    // 视图持有者
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvLotteryName, tvPrice, tvParticipants, tvTime, tvStatus, tvWinner;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLotteryName = itemView.findViewById(R.id.tv_lottery_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvParticipants = itemView.findViewById(R.id.tv_participants);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvWinner = itemView.findViewById(R.id.tv_winner);
        }
    }
}