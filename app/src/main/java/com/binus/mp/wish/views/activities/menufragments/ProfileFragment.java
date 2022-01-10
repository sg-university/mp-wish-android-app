package com.binus.mp.wish.views.activities.menufragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.binus.mp.wish.R;
import com.binus.mp.wish.models.Account;
import com.binus.mp.wish.models.Auth;


public class ProfileFragment extends Fragment {


    TextView textViewName, textViewUsername, textViewEmail;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        textViewName = view.findViewById(R.id.fragment_profile_text_view_name);
        textViewUsername = view.findViewById(R.id.fragment_profile_text_view_username);
        textViewEmail = view.findViewById(R.id.fragment_profile_text_view_email);

        Auth auth = Auth.getInstance();
        Account account = auth.getAccount();
        textViewName.setText(account.getName());
        textViewUsername.setText(account.getUsername());
        textViewEmail.setText(account.getEmail());

        return view;
    }
}