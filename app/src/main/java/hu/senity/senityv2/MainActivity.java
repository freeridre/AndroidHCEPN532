package hu.senity.senityv2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;

import com.airbnb.lottie.Lottie;
import com.airbnb.lottie.LottieAnimationView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static hu.senity.senityv2.SplashActivity.progressDialog;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "1st activity";
    public static final String SHARED_PREF_NAME = "mypref";
    public static String KEY_ID = "UID";
    public static String KEY_ID2 = "EMULATED_UID";
    public static final String PASS_CODE = "PSSCD";
    public String getpasscode = null;
    static Button GetUID;
    static LottieAnimationView gears;
    static LottieAnimationView circle;
    static TextView fetchuidtext;
    TextView HeadLine;
    static boolean Get_UID = false;
    static boolean Bl_Set = false;
    static String GetUid3 = null;

    static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public static BluetoothAdapter BA;
    private static Set<BluetoothDevice> pairedDevices;
    static BluetoothDevice hc05;
    static final String hc05_Mac_addr = "C3:3C:1C:04:00:79";
    //static final String hc05_Mac_addr = "19:10:094DD3";
    static String mSenity_Addr = null;
    private final static String SignedPacket = "4ccb86cc88b0d78895aef37f5352c360";
    static BluetoothSocket btSocket;
    static String sUID = null;
    static String sUID2 = null;
    public static String mDeviceID = null;
    static InputStream inputStream;
    static OutputStream outputstream;
    static String GetUid;
    static String DeviceID;
    Handler handler = new Handler();
    final static ExecutorService executorService = Executors.newSingleThreadExecutor();
    final ExecutorService executorService2 = Executors.newSingleThreadExecutor();
    final String UID_Pck_ID = "243::";
    static String sUID_with_Sgnd_PCK = null;
    static boolean get_UID_OnPause = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_constraintlayout);
        DeviceID = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        BA = BluetoothAdapter.getDefaultAdapter();
        hc05 = BA.getRemoteDevice(hc05_Mac_addr);
        GetUID = findViewById(R.id.getUIDButton);
        HeadLine = findViewById(R.id.HeadLine_txt);

        if (BA == null)
        {
            Toast.makeText(this, "Bluetooth not supported!", Toast.LENGTH_SHORT).show();
            finishAffinity();
        }

        GetUID.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Bl_Set = false;
                Get_UID = true;
                get_UID_OnPause = true;

                BA.enable();
                Intent intent = new Intent(getApplicationContext(), LoadActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        if(Bl_Set)
        {
            //button GetUID;
            fetchuidtext = findViewById(R.id.fetchUID);
            GetUID = findViewById(R.id.getUIDButton);
            gears = findViewById(R.id.animated_gears);
            circle = findViewById(R.id.highlight_gear);
            fetchuidtext.setVisibility(View.VISIBLE);
            GetUID.setVisibility(View.GONE);
            gears.setVisibility(View.GONE);
            circle.setVisibility(View.GONE);



            Bl_Set = false;

            GetUID.setVisibility(View.GONE);
            GetUID.setVisibility(View.INVISIBLE);
            HeadLine.setVisibility(View.GONE);
            HeadLine.setVisibility(View.INVISIBLE);
            //Initialize Progress Dialog
            progressDialog = new ProgressDialog(MainActivity.this);
            //prevent to cancel ProgressDialog
            progressDialog.setCancelable(false);
            //prevent to cancel ProgressDialog with touch
            progressDialog.setCanceledOnTouchOutside(false);
            //Show Dialog
            progressDialog.show();
            //Set Content View
            progressDialog.setContentView(R.layout.progress_dialog_constraintlayout);
            //Set Transparent Background
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            //set it center


            executorService.execute(new Runnable(){
                @Override
                public void run(){
                    //DoInBackground
                    System.out.println("DoInBackground");
                    BlConnection();
                    if(btSocket.isConnected()) {
                        datasend();

                        datareceive();
                    }else {
                        System.out.println("Failed to Connect!");
                        Failed();
                    }
                    if(btSocket.isConnected()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //postExecute
                                System.out.println("postExecute");
                                progressDialog.dismiss();
                                try {
                                    disconnect();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                boolean Bool_UID_Pck_ID_failed = sUID_with_Sgnd_PCK.contains(UID_Pck_ID);
                                if (!Bool_UID_Pck_ID_failed) {
                                    System.out.println("No Signed Packet Found!");
                                    Failed();
                                } else {
                                    Intent intent = new Intent(getApplicationContext(), SuccessActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }


                            }
                        });
                    }
                }
            });
        }
    }


    @Override
    public void onBackPressed(){
        System.out.println("MainActivity: Flag Deleted!");

        Get_UID = false;
        Bl_Set = false;

        super.onBackPressed();


    }
    public void BlConnection(){

        if(!BA.isEnabled())
        {
            BA.enable();
            mDeviceID = BA.getAddress();
            System.out.println("Device ID: " + mDeviceID);
        }
        //pairedDevices =BA.getBondedDevices();
        //if(pairedDevices.size() > 0){

            //for (BluetoothDevice device : pairedDevices) {
                //if (hc05_Mac_addr.toString().equals(device.getAddress())) {
                    //mSenity_Addr = device.toString();
                    System.out.println("Found Sentiy! ");

                    try {
                        btSocket = hc05.createInsecureRfcommSocketToServiceRecord(mUUID);
                        //btSocket = hc05.createRfcommSocketToServiceRecord(mUUID);
                        btSocket.connect();
                    } catch (Exception e){
                        e.printStackTrace();

                    }
                    if(btSocket.isConnected())
                    {
                        /*status_text_bt.setText("Connected to Senity!");
                        status_text_bt.setBackgroundColor(getResources().getColor(R.color.green));*/
                    }else
                    {
                        //System.out.println("Failed no paired devices!");
                        //Failed();
                    }

                    /*MainActivity BLDataReceive = new MainActivity();
                    BLDataReceive.datareceive();*/



                //} else {






                    /*status_text_bt.setText("Senity is not paired yet!");
                    status_text_bt.setBackgroundColor(getResources().getColor(R.color.red));*/
                //}
            //}
            //If Senity device is not found

        //}else{
            /*System.out.println("Failed no paired devices!");
            Failed();*/
            /*status_text_bt.setText("No Paired Devices found!");
            status_text_bt.setBackgroundColor(getResources().getColor(R.color.orange));*/
        //}
    }
    public static void datasend(){

        System.out.println("Device ID: " + DeviceID);
        String Delimiter1 = ",";
        String Delimiter2 = ";";
        String Data_To_Senity = SignedPacket + Delimiter1 + DeviceID + Delimiter2;
        byte[] ByteSignedPacket = Data_To_Senity.getBytes();
        System.out.println("Data to Senity: " + ByteSignedPacket.toString());
        System.out.println("Data Length to Senity: " + ByteSignedPacket.toString().length());
        try
        {
            outputstream = btSocket.getOutputStream();
            outputstream.write(ByteSignedPacket);
        }catch(Exception e){
            e.printStackTrace();

        }
        /*status_text_bt.setText("Data sent to Senity!");
        status_text_bt.setBackgroundColor(getResources().getColor(R.color.green));*/
    }
    public void datareceive(){

        byte[] byterecieve = new byte[21];
        try
        {
            inputStream = btSocket.getInputStream();
            inputStream.read(byterecieve);
        }catch(Exception e){
            e.printStackTrace();
        }
        sUID = new String(byterecieve);
        sUID_with_Sgnd_PCK = sUID;
        sUID2 = sUID;

        String UID_Pck_Delim1 = ":";
        String UID_Pck_Delim2 = "*";
        String UID_Pck_Buff = null;
        String UID_Pck_Buff_2 = null;
        byte[] Edited_UID = new byte[4];
        boolean Bool_UID_Pck_ID = sUID.contains(UID_Pck_ID);
        if(Bool_UID_Pck_ID) {


            sUID2 = sUID2.replace("243::", "").replace("*", "");
            sUID = sUID.replace("243::", "").replaceAll(":", "").replace("*", "");
            saveData();
            Vibrator();
        }else {
            /*System.out.println("Failed didn't get Signed packet!");
            Failed();*/
            /*status_text_bt.setText("Didn't get the UID From Senity!");
            status_text_bt.setBackgroundColor(getResources().getColor(R.color.red));*/

        }

    }
    public boolean disconnect() throws IOException {

        if(outputstream != null) {
            outputstream.close();
        }
        if(inputStream != null) {
            inputStream.close();
        }
        if(btSocket != null) {
            btSocket.close();
        }
        BA.disable();

        if(!btSocket.isConnected())
        {
            return true;

        }else
        {
            return false;
        }
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
    public boolean getSavedData (){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        GetUid = sharedPreferences.getString(KEY_ID, "");


        if(GetUid.isEmpty())
        {
            return true;
        }else
        {
            return false;
        }
    }
    public void getSavedDataForEmulatedUID (){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        GetUid3 = sharedPreferences.getString(KEY_ID2, "");
        System.out.print("Got Emulated UID: ");
        System.out.println(GetUid3);


    }

    public void saveData(){

        sUID = sUID.replace("243::", "").replaceAll(":", "").replace("*", "");
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //It's for show data
        System.out.print("Saved sUID2: ");
        editor.putString(KEY_ID, sUID2);
        editor.apply();
        System.out.println(sUID2);
        //It's for Card emulation
        editor.putString(KEY_ID2 , sUID);
        System.out.print("Saved sUID: ");
        System.out.println(sUID);
        editor.apply();
        //editor.commit();

    }
    public void deleteSavedData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        sharedPreferences.edit().remove(KEY_ID).commit();


    }
    public void Failed()
    {
        progressDialog.dismiss();
        System.out.println("Here");
        handler.removeCallbacksAndMessages(null);


        Intent intent = new Intent(getApplicationContext(), FailedActivity2.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        finish();
    }


}