package hu.senity.senityv2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import static hu.senity.senityv2.MainActivity.Bl_Set;
import static hu.senity.senityv2.MainActivity.Get_UID;

public class FailedActivity2 extends AppCompatActivity {
    Handler handler = new Handler();
    private RelativeLayout failedactivity2View = null;
    private ConstraintLayout failedactivity2Viewconstraint = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_failed2_constraintlayout);
        //failedactivity2View = (RelativeLayout) findViewById(R.id.failedactivity2View);
        failedactivity2Viewconstraint = (ConstraintLayout) findViewById(R.id.failedactivity2Viewconstraint);

        /*runOnUiThread(new Runnable(){
            @Override
            public void run(){

            }
        });*/

        Vibrator();
        Bl_Set = false;
        Get_UID = false;
        /*handler.postDelayed(new Runnable() {

            //handler.postDelayed(new Runnable(){

            @Override
            public void run() {
                //handler.removeCallbacksAndMessages(null);
                Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }, 6000);*/

        failedactivity2Viewconstraint.setOnTouchListener(new View.OnTouchListener() {
        //failedactivity2View.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    handler.removeCallbacksAndMessages(null);
                    Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });
        //MainActivity.executorService.shutdownNow();
    }

    @Override
    public void onBackPressed()
    {
        handler.removeCallbacksAndMessages(null);
        /*Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();*/
        finishAffinity();
        moveTaskToBack(true);
        super.onBackPressed();
    }
    public void Vibrator(){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }

}