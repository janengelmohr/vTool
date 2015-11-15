package de.visi0nary.vTool;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class SetKernelSettingsOnStartupReceiver extends BroadcastReceiver {
    public SetKernelSettingsOnStartupReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // get settings storage reference
        final SharedPreferences settings = context.getSharedPreferences(context.getPackageName() + "_preferences", context.MODE_PRIVATE);
        // get current setting
        boolean applyOnStartup = settings.getBoolean(context.getResources().getString(R.string.text_apply_on_boot), false);
        // check if the values should be applied on startup
                if (applyOnStartup) {
                context.startService(new Intent(context, SetKernelSettingsService.class));
                }
    }
}
