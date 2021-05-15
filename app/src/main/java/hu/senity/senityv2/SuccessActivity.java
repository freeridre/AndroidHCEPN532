package hu.senity.senityv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import static hu.senity.senityv2.MainActivity.Bl_Set;
import static hu.senity.senityv2.MainActivity.Get_UID;
import static hu.senity.senityv2.EnterPassCodeActivity.SuccessPass;

public class SuccessActivity extends AppCompatActivity {
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        new Handler().postDelayed(new Runnable() {

            //handler.postDelayed(new Runnable(){

            @Override
            public void run() {
                //Show UID
                Intent intent = new Intent(getApplicationContext(), UIDActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000);
    }
    @Override
    public void onBackPressed(){

        System.out.println("SuccessActivity: Back pressed!");

        Get_UID = false;
        Bl_Set = false;
        SuccessPass = false;
        handler.removeCallbacksAndMessages(null);
        finishAffinity();
        moveTaskToBack(true);
        super.onBackPressed();



    }
}