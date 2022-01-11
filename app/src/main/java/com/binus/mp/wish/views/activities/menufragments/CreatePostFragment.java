package com.binus.mp.wish.views.activities.menufragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.binus.mp.wish.R;
import com.binus.mp.wish.apis.PostApi;
import com.binus.mp.wish.contracts.Result;
import com.binus.mp.wish.controllers.Controller;
import com.binus.mp.wish.models.Account;
import com.binus.mp.wish.models.Auth;
import com.binus.mp.wish.models.Post;

import java.sql.Timestamp;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CreatePostFragment extends Fragment implements View.OnClickListener {

    public CreatePostFragment() {
        // Required empty public constructor
    }

    EditText editTextTitle, editTextContent;
    Button buttonCreate;
    View viewCreatePostFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_post, container, false);
        viewCreatePostFragment = view;

        editTextTitle = view.findViewById(R.id.fragment_create_post_edit_text_title);
        editTextContent = view.findViewById(R.id.fragment_create_post_edit_text_content);
        buttonCreate = view.findViewById(R.id.fragment_create_post_button_create);

        buttonCreate.setOnClickListener(this);

        return view;
    }


    private void createPost() {
        Account acc = Auth.getInstance().getAccount();
        String title = editTextTitle.getText().toString();
        String content = editTextContent.getText().toString();
        Post post = new Post(UUID.randomUUID(), acc.getId(), title, content, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));

        Controller<PostApi> controller = new Controller<>(PostApi.class);
        Call<Result<Post>> call = controller.getApi().createOne(post);
        call.enqueue(new Callback<Result<Post>>() {
            @Override
            public void onResponse(Call<Result<Post>> call, Response<Result<Post>> response) {
                Result<Post> result = response.body();
                assert result != null;
                switch (result.getStatus()) {
                    case "created":
                        Toast.makeText(getActivity(), "Create Success!", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(getActivity(), "Create Failed", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<Result<Post>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.fragment_create_post_button_create:
                if (editTextTitle.getText().toString().isEmpty() || editTextContent.getText().toString().isEmpty()) {
                    Toast.makeText(view.getContext(), "Title and Content must be inputted!", Toast.LENGTH_SHORT).show();
                } else {
                    createPost();
                }
                break;
        }
    }
}