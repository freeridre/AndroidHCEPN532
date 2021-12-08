package hu.senity.senityv2;
import android.app.backup.BackupAgentHelper;
import android.app.backup.SharedPreferencesBackupHelper;
import static hu.senity.senityv2.MainActivity.SHARED_PREF_NAME;

public class MyBackUpPlace extends BackupAgentHelper{
    // A key to uniquely identify the set of backup data
    static final String PREFS_BACKUP_KEY = "prefs";
    @Override
    public void onCreate() {
        SharedPreferencesBackupHelper helper = new SharedPreferencesBackupHelper(this, MainActivity.SHARED_PREF_NAME);
        addHelper(PREFS_BACKUP_KEY, helper);
        System.out.print("BackUp Created...");
    }
}
