package com.pkall.dolly;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import java.util.Timer;

import static java.lang.Thread.sleep;

public class SplashActivity extends AppCompatActivity {


    int delaytime = 5000;
    TextView loadingtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        Timer timer = new Timer();
        loadingtext = findViewById(R.id.loadingtext);
        final String[] loadingtextstr = {"Loading"};
        String dot = ".";
        final String[] newstr = {""};


        SharedPreferences settings=getSharedPreferences("prefs",0);
        boolean firstRun=settings.getBoolean("firstRun",false);

        if(firstRun==false)//if running for first time
        {
            SharedPreferences.Editor editor=settings.edit();
            editor.putBoolean("firstRun",true);
            editor.commit();

           // try
           // {
                //Display for 10 seconds
              //  sleep(10000);
           // }
            //catch (InterruptedException e)
            //{
                // TODO: handle exception
              //  e.printStackTrace();
           // }



            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //displayData();
                    Intent i=new Intent(SplashActivity.this, AvatarActivity1.class);
                    startActivity(i);
                }
            }, 10000);

/*            Intent i=new Intent(SplashActivity.this,ChatActivity.class);
            startActivity(i);
        */

        }
        else
        {

            Intent i=new Intent(SplashActivity.this, AvatarActivity1.class);
            startActivity(i);
            //finish();
        }



        Thread timer1 = new Thread() {
            //int talk = 1;

            int z = 10, id;
            String newstr1 = "";
            //Drawable d;
            public void run() {
                try {
                    sleep(200);
                    //      if(talk == 1){
                    for (z = 10; z < 200; z++) {
                        // while(state) {
                        //if (z < 130) {
                        sleep(2000);
                        // z = z+1;
                        // sleep(pausetime);

                        loadingtextstr[0] += ".";
                        loadingtext.setText(loadingtextstr[0]);

                        //newstr = newstr.concat(".");
                        if(true) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                   //loadingtextstr = loadingtextstr.concat(dot);

                                   // String abc = loadingtextstr.getT
                                    //loadingtext.setText(newstr);

                                }
                            });

                            if(z>=130)
                            {
                                z = 10;
                            }
                           // Log.d("value of z", "aa" +z);

                        }

                        //}
                        //else {
                        //   z = 0;
                        //  }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    System.out.println("finally");
                }
            }
        };
        timer1.start();


    }
    @Override
    public void onBackPressed() { }


}
