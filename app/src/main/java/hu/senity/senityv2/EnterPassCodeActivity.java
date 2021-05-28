package hu.senity.senityv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.WindowManager;
import static hu.senity.senityv2.MainActivity.Bl_Set;

import static hu.senity.senityv2.MainActivity.GetUid;
import static hu.senity.senityv2.MainActivity.Get_UID;
import static hu.senity.senityv2.MainActivity.KEY_ID;
import static hu.senity.senityv2.MainActivity.PASS_CODE;
import static hu.senity.senityv2.MainActivity.SHARED_PREF_NAME;
import in.codeshuffle.typewriterview.TypeWriterView;
import static hu.senity.senityv2.MainActivity.get_UID_OnPause;

public class EnterPassCodeActivity extends AppCompatActivity {
    EditText editText;
    Button button;
    String getpasscode = null;
    TypeWriterView typeWriterView;
    static boolean SuccessPass = false;
    Handler handler = new Handler();
    String SecurityCode = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_enter_pass_code_constraintlayout);
        SecurityCode = "06304249992";
        //Load the password
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        getpasscode = sharedPreferences.getString(PASS_CODE, "");

        editText = (EditText) findViewById(R.id.editTextTextPassword);
        button = (Button) findViewById(R.id.enter);
        //Create Object and refer to layout view
        typeWriterView = (TypeWriterView)findViewById(R.id.typeWriterView);

        //Setting each character animation delay
        typeWriterView.setDelay(50);
        //Setting music effect On/Off
        typeWriterView.setWithMusic(false);

        //Animating Text
        typeWriterView.animateText("ENTER YOUR CRENDETIALS...");

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String text = editText.getText().toString();
                if(text.equals(getpasscode)){
                    if(Get_UID) {
                        System.out.println("Set SuccessPass to true!");
                        SuccessPass = true;
                    }else
                    {
                        SuccessPass = false;
                        System.out.println("Set SuccessPass to false!");
                    }
                    System.out.println("Get_UID is false!");
                    Intent intent = new Intent(getApplicationContext(), LoadActivity.class);
                    //startActivityForResult(intent, 0);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }else if(!text.equals(getpasscode) && !text.equals(SecurityCode))
                {
                    //Wrong password
                    editText.setText(null);
                    Toast.makeText(EnterPassCodeActivity.this, "Wrong password!", Toast.LENGTH_SHORT).show();
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 500 milliseconds
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        v.vibrate(500);
                    }
                //Itt nem müködik
                }else if(text.equals(SecurityCode))
                {
                    Toast.makeText(EnterPassCodeActivity.this, "Security Password entered!", Toast.LENGTH_SHORT).show();
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 500 milliseconds
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        v.vibrate(500);
                    }
                    System.out.println("Go to change password!");
                    Intent intent = new Intent(getApplicationContext(), CreatePassCodeActivity.class);
                    //startActivityForResult(intent, 0);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        System.out.println("EnterPassCodeActivity: Flag Deleted Back pressed!");
        Get_UID = false;
        Bl_Set = false;
        SuccessPass = false;
        handler.removeCallbacksAndMessages(null);
        finishAffinity();
        moveTaskToBack(true);

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