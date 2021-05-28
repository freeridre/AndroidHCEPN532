package hu.senity.senityv2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.backup.BackupManager;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.widget.TextView;

import static hu.senity.senityv2.MainActivity.KEY_ID;
import static hu.senity.senityv2.MainActivity.SHARED_PREF_NAME;
import static hu.senity.senityv2.MainActivity.sUID;
import static hu.senity.senityv2.MainActivity.sUID2;

public class UIDActivity extends AppCompatActivity {
    TextView UID_txt;
    NfcAdapter nfcAdapter;
    private BackupManager backupManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_u_i_d_constraintlayout);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        UID_txt = (TextView) findViewById(R.id.textView5_Actual_UID_txt);
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        sUID2 = sharedPreferences.getString(KEY_ID, "");
        UID_txt.setText(sUID2);
        new BackupManager(getApplicationContext()).dataChanged();
        //backupManager.dataChanged();
        System.out.println("BackUp (UID) Issued...");
    }
    /*@Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
        moveTaskToBack(true);

    }*/
}