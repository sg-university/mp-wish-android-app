package com.binus.mp.wish.views.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.binus.mp.wish.R;
import com.binus.mp.wish.apis.AccountApi;
import com.binus.mp.wish.contracts.Result;
import com.binus.mp.wish.controllers.Controller;
import com.binus.mp.wish.models.Account;
import com.binus.mp.wish.models.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private Account accTemp;
    private CommentViewHolder holder;
    MutableLiveData<List<Comment>> itemData;


    public MutableLiveData<List<Comment>> getItemData() {
        return itemData;
    }

    public void setItemData(List<Comment> itemData) {
        this.itemData.setValue(itemData);
        notifyDataSetChanged();
    }

    public CommentAdapter() {
        this.itemData = new MutableLiveData<List<Comment>>();
        this.itemData.setValue(new ArrayList<Comment>());
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View listFeed = inflater.inflate(R.layout.list_comment, parent, false);
        return new CommentViewHolder(listFeed);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        this.holder = holder;
        Comment comment = this.itemData.getValue().get(position);
        UUID idAuth = comment.getCreatorAccountId();
        findAuthorComment(idAuth);

//        Log.i("CommentAdapter", "post" + listComment.size());
    }

    private void findAuthorComment(UUID idAuth) {
        Controller<AccountApi> controller = new Controller<>(AccountApi.class);
        Call<Result<Account>> call = controller.getApi().readOne(idAuth);
        call.enqueue(new Callback<Result<Account>>() {
            @Override
            public void onResponse(Call<Result<Account>> call, Response<Result<Account>> response) {
                if (response.isSuccessful()) {
                    Result<Account> result = response.body();
                    assert result != null;
                    if (result.getStatus().equals("read")) {
                        Log.i("Comment Adapter", "result : not null");
                        setAccTemp(result.getContent());
                    } else {
                        Log.i("Comment Adapter", "result : null");
                    }
                } else {
                    Log.i("CommentAdapter", "error : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Result<Account>> call, Throwable t) {

            }
        });
    }

    private void setAccTemp(Account account) {
        this.accTemp = account;
        holder.author.setText(accTemp.getName());
        holder.description.setText(accTemp.getName());
    }


    @Override
    public int getItemCount() {
        return itemData.getValue().size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView description, author;

        public CommentViewHolder(View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.commentAuthor);
            description = itemView.findViewById(R.id.commentContent);
        }
    }
}