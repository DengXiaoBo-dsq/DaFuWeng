package com.dsq.DaFuWeng.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.dsq.DaFuWeng.R;
import com.dsq.DaFuWeng.model.UserParticipation;
import java.util.List;

public class ParticipationAdapter extends RecyclerView.Adapter<ParticipationAdapter.ViewHolder> {

    private List<UserParticipation> participationList;

    public ParticipationAdapter(List<UserParticipation> participationList) {
        this.participationList = participationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_participation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserParticipation participation = participationList.get(position);
        holder.tvLotteryName.setText(participation.getLotteryName());
        holder.tvPrice.setText("¥" + participation.getPrice());  // 优化价格显示
        holder.tvParticipants.setText(String.valueOf(participation.getParticipants()) + "人参与");
        holder.tvTime.setText(participation.getTime());
    }

    @Override
    public int getItemCount() {
        return participationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvLotteryName;
        public TextView tvPrice;
        public TextView tvParticipants;
        public TextView tvTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLotteryName = itemView.findViewById(R.id.tv_lottery_name);
            tvPrice = itemView.findViewById(R.id.tv_price);       // 修正为tv_price
            tvParticipants = itemView.findViewById(R.id.tv_participants); // 修正为tv_participants
            tvTime = itemView.findViewById(R.id.tv_participation_time);
        }
    }
}