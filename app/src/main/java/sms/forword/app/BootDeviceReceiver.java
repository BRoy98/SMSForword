package sms.forword.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class BootDeviceReceiver extends BroadcastReceiver {

    private static final String TAG_BOOT_BROADCAST_RECEIVER = "BOOT_BROADCAST_RECEIVER";

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        //String message = "BootDeviceReceiver onReceive, action is " + action;
        //Toast.makeText(context, message, Toast.LENGTH_LONG).show();

        Log.d(TAG_BOOT_BROADCAST_RECEIVER, action);

        if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {

            startServiceDirectly(context);
        }
    }

    private void startServiceDirectly(Context context) {
        try {
            Intent startServiceIntent = new Intent(context, MySmsService.class);
            context.startService(startServiceIntent);
        } catch (Exception ex) {
            Log.e(TAG_BOOT_BROADCAST_RECEIVER, ex.getMessage(), ex);
        }
    }
}
