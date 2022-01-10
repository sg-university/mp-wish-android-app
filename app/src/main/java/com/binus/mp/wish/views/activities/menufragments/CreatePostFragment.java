package com.binus.mp.wish.views.activities.menufragments;

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


public class CreatePostFragment extends Fragment {


    public CreatePostFragment() {
        // Required empty public constructor
    }
    EditText etTitle,etContent;
    Button submit;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_post, container, false);

        etTitle = view.findViewById(R.id.titleET);
        etContent = view.findViewById(R.id.descriptionET);
        submit = view.findViewById(R.id.submitPost);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etTitle.getText().toString().isEmpty() || etContent.getText().toString().isEmpty()){
                    Toast.makeText(view.getContext(), "Title and Content must be inputted!", Toast.LENGTH_SHORT).show();
                }else{
                    insertPost();
                }
            }
        });

        return view;
    }


    private void insertPost(){
        Account acc = Auth.getInstance().getAccount();
        String title = etTitle.getText().toString();
        String content = etContent.getText().toString();
        Post post = new Post(UUID.randomUUID(),acc.getId(),title,content,new Timestamp(System.currentTimeMillis()),new Timestamp(System.currentTimeMillis()));

        Controller<PostApi> controller = new Controller<>(PostApi.class);
        Call<Result<Post>> call = controller.getApi().createOne(post);
        call.enqueue(new Callback<Result<Post>>() {
            @Override
            public void onResponse(Call<Result<Post>> call, Response<Result<Post>> response) {
                Toast.makeText(getActivity(), "Input Success!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Result<Post>> call, Throwable t) {

            }
        });
    }
//    private void createPostSuccess(){
//        Intent intent = new Intent(view.getContext(), LoginActivity.class);
//        startActivity(intent);

//    }
}