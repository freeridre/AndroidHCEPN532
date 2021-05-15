package hu.senity.senityv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import com.github.ybq.android.spinkit.style.ThreeBounce;
import static hu.senity.senityv2.MainActivity.Get_UID;
import static hu.senity.senityv2.MainActivity.Bl_Set;
import static hu.senity.senityv2.MainActivity.SHARED_PREF_NAME;
import static hu.senity.senityv2.MainActivity.KEY_ID;
import static hu.senity.senityv2.MainActivity.GetUid;
import static hu.senity.senityv2.EnterPassCodeActivity.SuccessPass;
import static hu.senity.senityv2.MainActivity.get_UID_OnPause;


public class LoadActivity extends AppCompatActivity {
    public static ProgressBar progressBar;
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        progressBar = (ProgressBar)findViewById(R.id.SpinkitLoading);
        //SpinkitLoading.getWindow().setBackgroundResource(android.R.color.transparent);
        ThreeBounce threeBounce = new ThreeBounce();
        runOnUiThread(new Runnable(){
            @Override
            public void run(){
                progressBar.setIndeterminateDrawable(threeBounce);
            }
        });
        handler.postDelayed(new Runnable() {



            @Override
            public void run() {

                //If there is no saved UID and no get UID pressed yet
                if(!Get_UID && getSavedData () ) {
                    //Enter the app
                    System.out.println("LoadActivity: Go to MainActivity!");
                    Intent intent2 = new Intent();
                    intent2 = new Intent(getApplicationContext(), MainActivity.class);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent2);
                    finish();
                //If there is saved UID
                }else if(!getSavedData ()){
                    System.out.println("LoadActivity: Go to UID Page!");
                    Intent intent2 = new Intent();
                    intent2 = new Intent(getApplicationContext(), UIDActivity.class);
                    //intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent2);
                    finish();
                }
                //If get UID button pressed and no saved UID on the device and Password entered
                else if(getSavedData () && SuccessPass && Get_UID)
                {
                    System.out.println("LoadActivity: Go to fetch UID!");
                    SuccessPass = false;
                    //It only starts connection if Bl_Set is enabled
                    Bl_Set = true;
                    Get_UID = false;
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                //If no saved data, button pressed, no code entered
                }else if(Get_UID && getSavedData () && !SuccessPass )
                {
                    System.out.println("LoadActivity: Go to enter pass code!");
                    Intent intent = new Intent(getApplicationContext(), EnterPassCodeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        }, 1000);
    }
    @Override
    public void onBackPressed(){
        System.out.println("LoadActivity: Back pressed!");

        Get_UID = false;
        Bl_Set = false;
        SuccessPass = false;

        handler.removeCallbacksAndMessages(null);
        super.onBackPressed();


    }
    @Override
    public void onPause(){

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

            return false;
        }
    }
}