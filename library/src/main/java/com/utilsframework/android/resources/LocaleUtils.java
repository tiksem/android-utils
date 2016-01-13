package com.utilsframework.android.resources;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.util.Locale;

/**
 * Created by CM on 6/17/2015.
 */
public class LocaleUtils {
    public static Resources getStringResourcesInLocale(Context context, Locale locale) {
        Resources standardResources = context.getResources();
        AssetManager assets = standardResources.getAssets();
        DisplayMetrics metrics = standardResources.getDisplayMetrics();
        Configuration config = new Configuration(standardResources.getConfiguration());
        config.locale = locale;
        return new Resources(assets, metrics, config);
    }

    public static void setLocale(Context context, Locale locale) {
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
    }

    public static Locale getLocale(Context context) {
        return context.getResources().getConfiguration().locale;
    }
}
