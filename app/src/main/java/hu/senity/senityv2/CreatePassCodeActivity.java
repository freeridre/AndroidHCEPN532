package hu.senity.senityv2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.backup.BackupManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.app.backup.BackupAgentHelper;
import android.app.backup.SharedPreferencesBackupHelper;

import in.codeshuffle.typewriterview.TypeWriterView;

import static hu.senity.senityv2.MainActivity.Bl_Set;
import static hu.senity.senityv2.MainActivity.Get_UID;
import static hu.senity.senityv2.MainActivity.PASS_CODE;
import static hu.senity.senityv2.MainActivity.SHARED_PREF_NAME;
import static hu.senity.senityv2.MyBackUpPlace.*;

public class CreatePassCodeActivity extends AppCompatActivity {

    static EditText editText1, editText2;
    Button button;
    TypeWriterView typeWriterView_enter, typeWriterView_reenter;
    Handler handler = new Handler();
    static boolean Conf_boolean = false;
    private BackupManager backupManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pass_code_constraintlayout);

        editText1 = (EditText) findViewById(R.id.editTextTextPassword1);
        editText2 = (EditText) findViewById(R.id.editTextTextPassword2);
        button = (Button) findViewById(R.id.button);
        //Create Object and refer to layout view
        typeWriterView_enter = (TypeWriterView)findViewById(R.id.typeWriterView_enterpass);
        typeWriterView_reenter = (TypeWriterView)findViewById(R.id.typeWriterView_reenterpass);

        //Setting each character animation delay
        typeWriterView_enter.setDelay(50);
        typeWriterView_reenter.setDelay(50);
        //Setting music effect On/Off
        typeWriterView_enter.setWithMusic(false);
        typeWriterView_reenter.setWithMusic(false);

        //Animating Text
        typeWriterView_enter.animateText("ENTER NEW PASSCODE");
        typeWriterView_reenter.animateText("ENTER THE PASSCODE AGAIN");
        
        button.setOnClickListener(new View.OnClickListener(){
           @Override
            public void onClick(View view)
           {
               Conf_boolean = true;
               String text1 = editText1.getText().toString();
               String text2 = editText2.getText().toString();
               Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
               if(text1.isEmpty() || text2.isEmpty()){
                    //There is no password
                   Toast.makeText(CreatePassCodeActivity.this, "No password entered!", Toast.LENGTH_SHORT).show();
                   editText1.setText(null);
                   editText2.setText(null);
                   // Vibrate for 500 milliseconds
                   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                       v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                   } else {
                       //deprecated in API 26
                       v.vibrate(500);
                   }

               }else
               {
                    if(text1.equals(text2))
                    {
                        //Save the password
                        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(PASS_CODE, text1);
                        editor.apply();
                        //Show saved Passcode in Logcat
                        String passcodetest = sharedPreferences.getString(PASS_CODE, "");
                        System.out.print("CreatePassCodeActivity: Saved Passcode: ");
                        System.out.println(passcodetest);
                        //Save Backup
                        //backupManager.dataChanged();
                        new BackupManager(getApplicationContext()).dataChanged();
                        System.out.print("BackUp (PassCode) Issued...");
                        //Enter to Loading activity
                        Intent intent = new Intent(getApplicationContext(), LoadActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        //Enter the app
                        /*Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();*/
                    }else
                    {
                        //Passwords are missmatched
                        Toast.makeText(CreatePassCodeActivity.this, "Password missmatch!", Toast.LENGTH_SHORT).show();
                        editText1.setText(null);
                        editText2.setText(null);
                        // Vibrate for 500 milliseconds
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            //deprecated in API 26
                            v.vibrate(500);
                        }
                    }
               }
           }
        });

    }

    /*@Override
    public void onBackPressed(){
        System.out.println("Password released by back button!");
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PASS_CODE, "");
        editor.apply();
        handler.removeCallbacksAndMessages(null);
        super.onBackPressed();
        //moveTaskToBack(true);

    }*/



}
