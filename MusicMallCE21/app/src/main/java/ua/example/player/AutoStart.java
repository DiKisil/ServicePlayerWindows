package ua.example.player;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;



/**
 * Created by Diana 2018.
 */

public class AutoStart extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context, AndroidBuildingMusicPlayerActivity.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);
    }
}
