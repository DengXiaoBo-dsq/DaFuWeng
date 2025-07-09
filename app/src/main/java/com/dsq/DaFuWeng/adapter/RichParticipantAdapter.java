// RichParticipantAdapter.java
package com.dsq.DaFuWeng.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.dsq.DaFuWeng.R;
import com.dsq.DaFuWeng.model.UserParticipation;
import java.util.HashMap;
import java.util.List;

public class RichParticipantAdapter extends RecyclerView.Adapter<RichParticipantAdapter.ViewHolder> {

    private Context context;
    private List<?> participations; // 使用泛型支持多种数据类型
    private String currentUserId;
    private boolean isHashMapData = false; // 标记数据类型

    public RichParticipantAdapter(Context context, String currentUserId) {
        this.context = context;
        this.currentUserId = currentUserId;
    }

    // 设置UserParticipation类型的数据
    public void setParticipations(List<UserParticipation> participations) {
        this.participations = participations;
        this.isHashMapData = false;
        notifyDataSetChanged();
    }

    public void setParticipationsFromServer(List<HashMap<String, Object>> participations) {
        this.participations = participations;
        this.isHashMapData = true;
        // 添加日志验证数据是否正确传入
        Log.d("Adapter", "刷新参与者列表，数量: " + (participations != null ? participations.size() : 0));
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_participant, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (participations == null || participations.isEmpty()) return;

        if (isHashMapData) {
            // 服务端返回字段：participationTime、userId、phone（日志中明确返回）
            HashMap<String, Object> participant = (HashMap<String, Object>) participations.get(position);
            holder.tvId.setText(String.valueOf(participant.get("userId"))); // 正确绑定userId
            holder.tvPhone.setText((String) participant.get("phone")); // 正确绑定phone
            holder.tvWinCount.setText("参与时间: " + participant.get("participationTime")); // 正确绑定参与时间
        } else {
            // 兼容旧数据（如果需要）
            UserParticipation item = (UserParticipation) participations.get(position);
            holder.tvId.setText(String.valueOf(item.getUserId()));
            holder.tvPhone.setText(item.getPhone());
            holder.tvWinCount.setText(String.valueOf(item.getWinCount()));
        }

        // 高亮当前用户（确保userId转换正确）
        String currentUserIdStr = String.valueOf(currentUserId);
        String userIdStr = isHashMapData ?
                String.valueOf(((HashMap<String, Object>) participations.get(position)).get("userId")) :
                String.valueOf(((UserParticipation) participations.get(position)).getUserId());
        if (currentUserIdStr.equals(userIdStr)) {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.light_gray));
        } else {
            holder.itemView.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
        }
    }
    @Override
    public int getItemCount() {
        return participations == null ? 0 : participations.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvPhone, tvWinCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tv_id);
            tvPhone = itemView.findViewById(R.id.tv_phone);
            tvWinCount = itemView.findViewById(R.id.tv_win_count);
        }
    }
}