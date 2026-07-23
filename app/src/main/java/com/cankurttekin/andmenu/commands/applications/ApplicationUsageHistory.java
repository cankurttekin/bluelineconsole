package net.cankurttekin.andmenu.commands.applications;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;

public class ApplicationUsageHistory {
    private static final String PREF_USAGE_PREFIX = "app_usage_count_";

    public static void recordUsage(Context context, String packageName) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int count = prefs.getInt(PREF_USAGE_PREFIX + packageName, 0);
        prefs.edit().putInt(PREF_USAGE_PREFIX + packageName, count + 1).apply();
    }

    public static int getUsageCount(Context context, String packageName) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(PREF_USAGE_PREFIX + packageName, 0);
    }
}
