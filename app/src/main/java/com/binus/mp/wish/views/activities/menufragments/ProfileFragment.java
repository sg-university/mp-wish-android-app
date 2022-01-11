package com.binus.mp.wish.views.activities.menufragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.binus.mp.wish.R;
import com.binus.mp.wish.models.Account;
import com.binus.mp.wish.models.Auth;
import com.binus.mp.wish.views.activities.LoginActivity;


public class ProfileFragment extends Fragment implements View.OnClickListener {
    TextView textViewName, textViewUsername, textViewEmail;
    Button buttonLogout;

    View viewProfileFragment;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        viewProfileFragment = view;
        textViewName = view.findViewById(R.id.fragment_profile_text_view_name);
        textViewUsername = view.findViewById(R.id.fragment_profile_text_view_username);
        textViewEmail = view.findViewById(R.id.fragment_profile_text_view_email);
        buttonLogout = view.findViewById(R.id.fragment_profile_button_logout);
        buttonLogout.setOnClickListener(this);

        Auth auth = Auth.getInstance();
        Account account = auth.getAccount();
        textViewName.setText(account.getName());
        textViewUsername.setText(account.getUsername());
        textViewEmail.setText(account.getEmail());

        return view;
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.fragment_profile_button_logout:
                Auth.deleteInstance();
                intent = new Intent(view.getContext(), LoginActivity.class);
                break;
        }

        if (intent != null) {
            viewProfileFragment.getContext().startActivity(intent);
        }
    }
}