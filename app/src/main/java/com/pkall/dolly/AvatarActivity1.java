package com.pkall.dolly;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;


public class AvatarActivity1 extends AppCompatActivity {

    LinearLayout l1,l2;
    ScrollView scrollView1;
    int id;
    String TAG="Avatar Activity";
    String uri = "@drawable/avatar_jpg";
    View V;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avataractivity);

        l1 = findViewById(R.id.linearlayout1);
        l2 = findViewById(R.id.linearlayoutinsidescrollview1);

        //scrollView1 = findViewById(R.id.scrollview1);


        for (int i =1; i<6; i++)
        {
            id = getResources().getIdentifier("avatar_" + i, "drawable", getPackageName());
           // Log.d(TAG, "onCreate: " + id);
           // imageViewface.setImageResource(id);
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setLayoutParams(new ViewGroup.LayoutParams(500, 500));

            //imageView.setImageResource(id);
           // l2.addView(imageView);
            //setContentView(linearLayout);

        }


        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(AvatarActivity1.this,LandingActivity.class);
                startActivity(i);
            }
        });

        l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: " + view.getTag().toString());
            }
        });


    }

    public void loadavatarts()
    {

    }

    @Override
    public void onBackPressed() { }


}
