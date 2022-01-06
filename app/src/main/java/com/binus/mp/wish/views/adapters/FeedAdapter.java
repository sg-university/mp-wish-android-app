package com.binus.mp.wish.views.adapters;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.binus.mp.wish.R;
import com.binus.mp.wish.models.Post;
import com.binus.mp.wish.views.activities.FeedDetailActivity;

import java.util.List;
import java.util.UUID;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder>{
    private List<Post> listPosts;

    public FeedAdapter(List<Post> list){
        this.listPosts = list;
    }

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater =LayoutInflater.from(parent.getContext());
        View listFeed = inflater.inflate(R.layout.list_feed,parent,false);
        return new FeedViewHolder(listFeed);
    }

    @Override
    public void onBindViewHolder(FeedAdapter.FeedViewHolder holder, int position) {
        Post post = listPosts.get(position);
        holder.title.setText(post.getTitle());
        holder.description.setText(post.getContent());
        holder.idPost = post.getId();
        holder.create_at.setText(post.getCreatedAt().toString());
        holder.readBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), FeedDetailActivity.class);
                intent.putExtra("id",holder.idPost+"");
                view.getContext().startActivity(intent);
            }
        });
        Log.i("FeedActivity","post"+listPosts.size());
    }

    public List<Post> getListPosts() {
        return listPosts;
    }

    public void setListPosts(List<Post> listPosts) {
        this.listPosts = listPosts;
    }

    @Override
    public int getItemCount() {
        return listPosts.size();
    }

    public static class FeedViewHolder extends RecyclerView.ViewHolder{
        TextView title,description,author,create_at;
        UUID idPost;
        Button readBtn;
        public FeedViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titlePost);
            description = itemView.findViewById(R.id.descriptionPost);

            author = itemView.findViewById(R.id.authorPost);
            readBtn = itemView.findViewById(R.id.readButton);
            create_at = itemView.findViewById(R.id.datePost);
        }
    }
}
