package com.example.qyu.Adapters;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.qyu.R;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    List<String> rating;
    List<String> msg;

    public ReviewAdapter(List<String> rating, List<String> msg) {
        this.rating = rating;
        this.msg = msg;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_layout, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        String message = msg.get(position);
        String rate = rating.get(position);
        holder.setMsg(message);
        holder.setRating(rate);
    }

    @Override
    public int getItemCount() {
        return msg.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView msg;
        LinearLayout linearLayout;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.msgRV);
            linearLayout = itemView.findViewById(R.id.rv_container);
        }

        public void setMsg(String message) {
            msg.setText(message.substring(1,message.length()-1));
        }

        public void setRating(String rating) {
            for (int x = 0; x < linearLayout.getChildCount(); x++) {
                ImageView starView = (ImageView) linearLayout.getChildAt(x);
                starView.setImageTintList(ColorStateList.valueOf(Color.parseColor("#bebebe")));
                int count = (int) ((Double.parseDouble(rating)));
                if (x < count/2) {
                    starView.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FFBB00")));
                }
            }
        }
    }
}
