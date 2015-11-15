package de.visi0nary.vTool;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;

public class SetKernelSettingsService extends Service {

    private Process suProcess;
    private DataOutputStream os;

    private SharedPreferences settings;

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        this.suProcess = null;
        this.os = null;
        try {
            // start an SU process
            this.suProcess = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(suProcess.getOutputStream());
                settings = getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);
                setKernelValues();
            os.writeBytes("exit\n");
            os.flush();
            // wait for suProcess to finish.
            // this blocks the service until the process has finished echoing settings
            this.suProcess.waitFor();
        }
        catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Couldn't acquire root access. :(", Toast.LENGTH_LONG).show();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "suProcess exited unexpectedly. :(", Toast.LENGTH_LONG).show();
        } finally {
        }
        stopSelf();
    }

    // do the magic. echo values into /sys
    public void setKernelValues() {
                try {
                    // apply pocket mod setting
                    if(settings.getBoolean(getResources().getString(R.string.res_switch_pocketmod), true))
                        os.writeBytes("echo 1 > /sys/pocket_mod/enable\n");
                    else {
                        os.writeBytes("echo 0 > /sys/pocket_mod/enable\n");
                    }

                    // apply DT2W setting
                    if(settings.getBoolean(getResources().getString(R.string.res_switch_dt2w), true))
                        os.writeBytes("echo 1 > /sys/android_touch/doubletap2wake\n");
                    else {
                        os.writeBytes("echo 0 > /sys/android_touch/doubletap2wake\n");
                    }

                    // apply fsync setting
                    if(settings.getBoolean(getResources().getString(R.string.res_switch_dyn_fsync), true))
                        os.writeBytes("echo 1 > /sys/kernel/dyn_fsync/Dyn_fsync_active\n");
                    else {
                        os.writeBytes("echo 0 > /sys/kernel/dyn_fsync/Dyn_fsync_active\n");
                    }

                    // apply vibration intensity
                    os.writeBytes("echo "+settings.getInt(getResources().getString(R.string.res_txt_vibrator), 5)+" > /sys/kernel/thunderquake_engine/level\n");
                    os.writeBytes("echo 2000 > /sys/class/timed_output/vibrator/enable\n");

                    os.flush();
                    Toast.makeText(getApplicationContext(), "Values applied! Testing vibration...", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

    }
}
