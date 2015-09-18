package com.utilsframework.android;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import com.utils.framework.CollectionUtils;
import com.utils.framework.Predicate;

import java.util.Iterator;
import java.util.List;

/**
 * Created by CM on 6/17/2015.
 */
public class AndroidUtilities {
    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        }
        catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static void startActivityWithExtras(Context context, Class<? extends Activity> aClass, Intent extras) {
        Intent intent = new Intent(context, aClass);
        intent.putExtras(extras);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, Class<? extends Activity> aClass) {
        Intent intent = new Intent(context, aClass);
        context.startActivity(intent);
    }

    public static void startActivityForResult(Activity activity, Class<? extends Activity> aClass, int requestCode) {
        Intent intent = new Intent(activity, aClass);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void startActivityForResult(Fragment fragment, Class<? extends Activity> aClass, int requestCode) {
        Intent intent = new Intent(fragment.getActivity(), aClass);
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void startActivityForResult(android.support.v4.app.Fragment fragment, Class<? extends Activity> aClass, int requestCode) {
        Intent intent = new Intent(fragment.getActivity(), aClass);
        fragment.startActivityForResult(intent, requestCode);
    }

    public static ActivityManager.RunningAppProcessInfo getForegroundApp(Context context) {
        ActivityManager.RunningAppProcessInfo result = null, info = null;

        ActivityManager activityManager = getActivityManager(context);
        
        List <ActivityManager.RunningAppProcessInfo> l = activityManager.getRunningAppProcesses();
        Iterator <ActivityManager.RunningAppProcessInfo> i = l.iterator();
        while(i.hasNext()){
            info = i.next();
            if(info.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                    && !isRunningService(context, info.processName)){
                result=info;
                break;
            }
        }
        return result;
    }

    public static ActivityManager getActivityManager(Context context) {
        return (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    }

    public static ComponentName getActivityForApp(Context context, ActivityManager.RunningAppProcessInfo target){
        ComponentName result=null;
        ActivityManager.RunningTaskInfo info;

        if(target==null)
            return null;

        ActivityManager activityManager = getActivityManager(context);
        List<ActivityManager.RunningTaskInfo> l = activityManager.getRunningTasks(9999);
        Iterator<ActivityManager.RunningTaskInfo> i = l.iterator();

        while(i.hasNext()){
            info=i.next();
            if(info.baseActivity.getPackageName().equals(target.processName)){
                result=info.topActivity;
                break;
            }
        }

        return result;
    }

    public static boolean isRunning(Context context, String processName) {
        ActivityManager activityManager = getActivityManager(context);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        return CollectionUtils.find(runningAppProcesses, new Predicate<ActivityManager.RunningAppProcessInfo>() {
            @Override
            public boolean check(ActivityManager.RunningAppProcessInfo item) {
                return item.processName.equals(processName);
            }
        }) != null;
    }

    public static boolean isStillActive(Context context, ActivityManager.RunningAppProcessInfo process,
                                        ComponentName activity)
    {
        // activity can be null in cases, where one app starts another. for example, astro
        // starting rock player when a move file was clicked. we dont have an activity then,
        // but the package exits as soon as back is hit. so we can ignore the activity
        // in this case
        if(process==null)
            return false;

        ActivityManager.RunningAppProcessInfo currentFg = getForegroundApp(context);
        ComponentName currentActivity=getActivityForApp(context, currentFg);

        if(currentFg!=null && currentFg.processName.equals(process.processName) &&
                (activity==null || currentActivity.compareTo(activity)==0))
            return true;

        return false;
    }

    public static boolean isRunningService(Context context, String processName){
        if(processName == null || processName.isEmpty()) {
            return false;
        }

        ActivityManager.RunningServiceInfo service;

        ActivityManager activityManager = getActivityManager(context);
        List <ActivityManager.RunningServiceInfo> l = activityManager.getRunningServices(9999);
        Iterator <ActivityManager.RunningServiceInfo> i = l.iterator();
        while(i.hasNext()){
            service = i.next();
            if(service.process.equals(processName))
                return true;
        }

        return false;
    }
}
