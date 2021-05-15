package hu.senity.senityv2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;


import com.github.ybq.android.spinkit.style.ChasingDots;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.github.ybq.android.spinkit.style.Wave;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static hu.senity.senityv2.MainActivity.Bl_Set;
import static hu.senity.senityv2.MainActivity.GetUid;
import static hu.senity.senityv2.MainActivity.Get_UID;
import static hu.senity.senityv2.MainActivity.KEY_ID;
import static hu.senity.senityv2.MainActivity.PASS_CODE;
import static hu.senity.senityv2.MainActivity.SHARED_PREF_NAME;


public class SplashActivity extends AppCompatActivity {

    public static ProgressDialog progressDialog;
    public String getpasscode = null;
    public static ProgressBar progressBar;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //Load the password
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        getpasscode = sharedPreferences.getString(PASS_CODE, "");
        //Initialize Progress Dialog
        //progressDialog = new ProgressDialog(SplashActivity.this);
        //Show Dialog
        //progressDialog.show();
        //Set Content View
        //progressDialog.setContentView(R.layout.progress_dialog);
        //Set Transparent Background
        //progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        progressBar = (ProgressBar)findViewById(R.id.Spinkit2);
        ThreeBounce threeBounce = new ThreeBounce();


        /*new Handler().post(new Runnable(){
            @Override
            public void run(){
                progressBar.setIndeterminateDrawable(wave);
            }
        });*/
        runOnUiThread(new Runnable(){
           @Override
            public void run(){
               progressBar.setIndeterminateDrawable(threeBounce);

           }
        });

        /*Handler handler = new Handler();

        new Thread(new Runnable() {
            public void run(){

                try{

                    Thread.sleep(10000);
                    progressBar = (ProgressBar)findViewById(R.id.Spinkit2);
                    Wave wave = new Wave();
                    progressBar.setIndeterminateDrawable(wave);
                }
                catch(Exception e){ }
                handler.post(new Runnable(){
                   public void run(){
                       new Thread(new Runnable(){
                           public void run(){
                               if (getpasscode.isEmpty() && getSavedData()) {
                                   //progressBar.setVisibility(View.INVISIBLE);
                                   //progressDialog.dismiss();
                                   //If there is no password
                                   Intent intent = new Intent(getApplicationContext(), CreatePassCodeActivity.class);
                                   startActivity(intent);
                                   finish();
                               } else if (!getpasscode.isEmpty()) {
                                   //progressBar.setVisibility(View.INVISIBLE);
                                   //progressDialog.dismiss();
                                   //If there is password
                                   Intent intent = new Intent(getApplicationContext(), EnterPassCodeActivity.class);
                                   startActivity(intent);
                                   finish();
                               }
                           }
                       }).start();

                   }
                });
            }
        }).start();*/


        handler.postDelayed(new Runnable() {

            //handler.postDelayed(new Runnable(){

            @Override
            public void run() {


                if (getpasscode.isEmpty() && getSavedData()) {

                    //If there is no password


                    Intent intent = new Intent(getApplicationContext(), CreatePassCodeActivity.class);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else if (!getpasscode.isEmpty()) {
                    //progressBar.setVisibility(View.INVISIBLE);
                    //progressDialog.dismiss();
                    //If there is password

                    /*Intent intent = new Intent(getApplicationContext(), LoadActivity.class);
                    startActivity(intent);*/

                    Intent intent = new Intent(getApplicationContext(), EnterPassCodeActivity.class);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }


            }
        }, 1000);

        
    }
    @Override
    public void onBackPressed(){
        System.out.println("SplashActivity: Flag Deleted!");

        Get_UID = false;
        Bl_Set = false;
        finishAffinity();
        handler.removeCallbacksAndMessages(null);
        super.onBackPressed();
        //moveTaskToBack(true);

    }
    @Override
    public void onPause(){
        Get_UID = false;
        Bl_Set = false;
        super.onPause();
        handler.removeCallbacksAndMessages(null);

        finishAffinity();
    }


    public boolean getSavedData (){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        GetUid = sharedPreferences .getString(KEY_ID, "");


        if(GetUid.isEmpty())
        {
            return true;
        }else
        {
            //emu_tag_text_id_bt.setText(GetUid);
            return false;
        }
    }

}