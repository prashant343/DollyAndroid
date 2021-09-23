package com.pkall.dolly;


import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;



public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

    String[] SubjectValues;
    Context context;
    View view1;
    ViewHolder viewHolder1;
    TextView textView;
    ImageView imageView;

    static Boolean User = false;


    public MyAdapter(Context context1,String[] SubjectValues1, Boolean user){

        SubjectValues = SubjectValues1;
        context = context1;
        User = user;
        Log.e("Adapter", User.toString());
    }

    //public MyAdapter(Context context, ArrayList<String> msgs) {
    //}

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textView;
        public ImageView imageView;
        public  ImageView imageView2;

        public ViewHolder(View v){

            super(v);

            textView = (TextView)v.findViewById(R.id.subject_textview);
            imageView = (ImageView)v.findViewById(R.id.imageView2);
            imageView2 = (ImageView)v.findViewById(R.id.imageView3);


        }
    }


    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        view1 = LayoutInflater.from(context).inflate(R.layout.recyclerview_items,parent,false);


        viewHolder1 = new ViewHolder(view1);

        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){

        if(User) {
           // holder.imageView.setVisibility(View.INVISIBLE);
           // holder.textView.setGravity(Gravity.RIGHT);

        }

        if(!User) {
           // holder.imageView2.setVisibility(View.INVISIBLE);
           // holder.textView.setGravity(Gravity.LEFT);
        }

        //User response
        if(position %2==1)
        {
            holder.imageView.setVisibility(View.INVISIBLE);
            holder.textView.setGravity(Gravity.RIGHT);

        }

        //Dolly response
        if(position %2==0)
        {
            holder.imageView2.setVisibility(View.INVISIBLE);
            //holder.textView.setGravity(Gravity.RIGHT);

        }
        holder.textView.setText(SubjectValues[position]);



    }

    @Override
    public int getItemCount(){

        return SubjectValues.length;
    }
}