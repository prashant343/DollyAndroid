package com.pkall.dolly.ui.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.pkall.dolly.ChatActivity;
import com.pkall.dolly.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private ConstraintLayout c1;
    private LinearLayout l1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        c1 =  root.findViewById(R.id.constraint1);
        l1 = root.findViewById(R.id.linearLayoutl1);

        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(), ChatActivity.class);
                startActivity(i);
            }
        });

        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent i=new Intent(getActivity(), ChatActivity.class);
                //startActivity(i);
            }
        });



        //final TextView textView = root.findViewById(R.id.text_chat);

        //homeViewModel.getText().observe(this, new Observer<String>() {
         //   @Override
          //  public void onChanged(@Nullable String s) {
           //     textView.setText(s);
           // }
       // });






        return root;
    }
}