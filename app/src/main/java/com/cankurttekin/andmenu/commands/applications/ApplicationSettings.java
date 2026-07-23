package net.cankurttekin.andmenu.commands.applications;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;
import java.util.HashSet;
import java.util.Set;

public class ApplicationSettings {
    private static final String PREF_HIDDEN_APPS = "pref_hidden_apps";
    private static final String PREF_CUSTOM_LABEL_PREFIX = "pref_app_custom_label_";

    public static void hideApp(Context context, String packageName) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> hiddenApps = new HashSet<>(prefs.getStringSet(PREF_HIDDEN_APPS, new HashSet<>()));
        hiddenApps.add(packageName);
        prefs.edit().putStringSet(PREF_HIDDEN_APPS, hiddenApps).apply();
    }

    public static void unhideApp(Context context, String packageName) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> hiddenApps = new HashSet<>(prefs.getStringSet(PREF_HIDDEN_APPS, new HashSet<>()));
        hiddenApps.remove(packageName);
        prefs.edit().putStringSet(PREF_HIDDEN_APPS, hiddenApps).apply();
    }

    public static boolean isHidden(Context context, String packageName) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> hiddenApps = prefs.getStringSet(PREF_HIDDEN_APPS, new HashSet<>());
        return hiddenApps.contains(packageName);
    }

    public static void setCustomLabel(Context context, String packageName, String label) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        if (label == null || label.isEmpty()) {
            prefs.edit().remove(PREF_CUSTOM_LABEL_PREFIX + packageName).apply();
        } else {
            prefs.edit().putString(PREF_CUSTOM_LABEL_PREFIX + packageName, label).apply();
        }
    }

    public static String getCustomLabel(Context context, String packageName) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(PREF_CUSTOM_LABEL_PREFIX + packageName, null);
    }
}
