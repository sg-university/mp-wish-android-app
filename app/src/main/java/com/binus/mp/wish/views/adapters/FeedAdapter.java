package com.binus.mp.wish.views.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.binus.mp.wish.R;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder>{
    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =LayoutInflater.from(parent.getContext());
        View listFeed = inflater.inflate(R.layout.list_feed,parent,false);
        return new FeedViewHolder(listFeed);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedAdapter.FeedViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class FeedViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView price,name,subPrice,quantity,id;
        Button delBtn;
        public FeedViewHolder(View itemView) {
            super(itemView);


        }
    }
}
