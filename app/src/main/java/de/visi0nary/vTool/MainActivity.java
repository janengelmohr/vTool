package de.visi0nary.vTool;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;


public class MainActivity extends Activity {

    private boolean onStartup;
    private boolean is_fsync;
    private boolean is_dt2w;
    private boolean is_pocketmod;
    private int vibration_intensity;
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // get settings storage reference
        settings = getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);

        initializeSwitchStates();

    }

    private void initializeSwitchStates() {

        // get settings for "DT2W" switch
        is_dt2w = settings.getBoolean(getResources().getString(R.string.res_switch_dt2w), true);
        // get switch reference
        Switch doubletap2wake = (Switch) findViewById(R.id.switch_dt2w);
        doubletap2wake.setChecked(is_dt2w);
        // register a new listener that writes a button click in the settings storage
        doubletap2wake.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    settings.edit().putBoolean(getResources().getString(R.string.res_switch_dt2w), true).apply();
                } else {
                    settings.edit().putBoolean(getResources().getString(R.string.res_switch_dt2w), false).apply();
                }
            }
        });

        // get settings for "PocketMod" switch
        is_pocketmod = settings.getBoolean(getResources().getString(R.string.res_switch_pocketmod), true);
        // get switch reference
        Switch pocketmod = (Switch) findViewById(R.id.switch_pocketmod);
        pocketmod.setChecked(is_pocketmod);
        // register a new listener that writes a button click in the settings storage
        pocketmod.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    settings.edit().putBoolean(getResources().getString(R.string.res_switch_pocketmod), true).apply();
                } else {
                    settings.edit().putBoolean(getResources().getString(R.string.res_switch_pocketmod), false).apply();
                }
            }
        });

        // get settings for "FSYNC" switch
        is_fsync = settings.getBoolean(getResources().getString(R.string.res_switch_dyn_fsync), true);
        // get switch reference
        Switch fsync = (Switch) findViewById(R.id.switch_dyn_fsync);
        fsync.setChecked(is_fsync);
        // register a new listener that writes a button click in the settings storage
        fsync.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    settings.edit().putBoolean(getResources().getString(R.string.res_switch_dyn_fsync), true).apply();
                } else {
                    settings.edit().putBoolean(getResources().getString(R.string.res_switch_dyn_fsync), false).apply();
                }
            }
        });

        // get settings for "FSYNC" switch
        vibration_intensity = settings.getInt(getResources().getString(R.string.res_txt_vibrator), 5);
        // get switch reference
        SeekBar vibration = (SeekBar) findViewById(R.id.seekBar_vibrator);
        vibration.setProgress(vibration_intensity);
        // register a new listener that writes a button click in the settings storage
        vibration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                settings.edit().putInt(getResources().getString(R.string.res_txt_vibrator), i).apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // do nothing
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // do nothing
            }
        });

        // get current setting for the "on start up" switch
        onStartup = settings.getBoolean(getResources().getString(R.string.text_apply_on_boot), false);
        // get switch reference
        Switch startup = (Switch) findViewById(R.id.switch_onStartup);
        startup.setChecked(onStartup);
        // register a new listener that writes a button click in the settings storage
        startup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    settings.edit().putBoolean(getResources().getString(R.string.text_apply_on_boot), true).apply();
                }
                else {
                    settings.edit().putBoolean(getResources().getString(R.string.text_apply_on_boot), false).apply();
                }
            }
        });
    }




    public void onButtonClick(View view) {
        startService(new Intent(this, SetKernelSettingsService.class));
    }

    public void onDonateButtonClick(View view) {
        Toast.makeText(getApplicationContext(), "Thanks, you motivate me to develop great features!", Toast.LENGTH_SHORT).show();
        Uri uri = Uri.parse("http://forum.xda-developers.com/donatetome.php?u=3784300");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

}
