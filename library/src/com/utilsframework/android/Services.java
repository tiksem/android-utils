package com.utilsframework.android;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * User: Tikhonenko.S
 * Date: 06.08.14
 * Time: 21:01
 */
public class Services {
    public static <T extends Service> void start(Context context, Class<T> serviceClass) {
        Intent intent = new Intent(context, serviceClass);
        context.startService(intent);
    }

    public static interface Connection<BinderType> {
        BinderType getBinder();
        void unbind();
    }

    public static interface OnBind<BinderType> {
        public void onBind(Connection<BinderType> connection);
    }

    public static <ServiceType extends Service,
            BinderType extends IBinder> void bind(final Context context,
                                                  Class<ServiceType> serviceClass,
                                                  final OnBind<BinderType> onBind) {
        Intent intent = new Intent(context, serviceClass);
        context.bindService(intent, new ServiceConnection() {
            ServiceConnection serviceConnection;

            @Override
            public void onServiceConnected(ComponentName name, final IBinder service) {
                serviceConnection = this;
                onBind.onBind(new Connection<BinderType>() {
                    @Override
                    public BinderType getBinder() {
                        return (BinderType)service;
                    }

                    @Override
                    public void unbind() {
                        context.unbindService(serviceConnection);
                    }
                });
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        }, Context.BIND_AUTO_CREATE);
    }
}
