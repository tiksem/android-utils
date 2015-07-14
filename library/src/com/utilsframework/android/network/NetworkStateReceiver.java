package com.utilsframework.android.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import com.utilsframework.android.AndroidUtilities;
import com.utilsframework.android.threading.Tasks;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Created by CM on 7/14/2015.
 */

/*Can be used only by LocalBroadcastManager*/
public class NetworkStateReceiver extends BroadcastReceiver {
    private Queue<Runnable> onConnectedQueue = new ArrayDeque<>();
    private Queue<Runnable> onDisconnectedQueue = new ArrayDeque<>();
    private Context context;

    private NetworkStateReceiver() {}

    public static NetworkStateReceiver register(Context context) {
        NetworkStateReceiver receiver = new NetworkStateReceiver();
        receiver.context = context;
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, intentFilter);
        return receiver;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (AndroidNetwork.isOnline(context)) {
            Tasks.executeAndClearQueue(onConnectedQueue);
        } else {
            Tasks.executeAndClearQueue(onDisconnectedQueue);
        }
    }

    public void executeWhenNetworkDisconnected(Runnable runnable) {
        if (!AndroidNetwork.isOnline(context)) {
            runnable.run();
        } else {
            onDisconnectedQueue.add(runnable);
        }
    }

    public void executeWhenNetworkConnected(Runnable runnable) {
        if (AndroidNetwork.isOnline(context)) {
            runnable.run();
        } else {
            onConnectedQueue.add(runnable);
        }
    }

    public void unregister(Context context) {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
    }
}
