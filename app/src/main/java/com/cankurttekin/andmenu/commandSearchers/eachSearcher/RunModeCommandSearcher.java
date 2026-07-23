package com.cankurttekin.andmenu.commandSearchers.eachSearcher;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.Settings;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import com.cankurttekin.andmenu.applicationMain.MainActivity;
import com.cankurttekin.andmenu.interfaces.CandidateEntry;
import com.cankurttekin.andmenu.interfaces.CommandSearcher;
import com.cankurttekin.andmenu.interfaces.EventLauncher;

import java.util.ArrayList;
import java.util.List;

public class RunModeCommandSearcher implements CommandSearcher {
    public static final String PREF_RUN_MODE_ENABLED_KEY = "pref_main_run_mode";
    private static final String COMMAND_SYS = "sys";

    @Override
    public void refresh(Context context) {}

    @Override
    public void close() {}

    @Override
    public boolean isPrepared() {
        return true;
    }

    @Override
    public void waitUntilPrepared() {}

    @Override
    @NonNull
    public List<CandidateEntry> searchCandidateEntries(String s, Context context) {
        List<CandidateEntry> ret = new ArrayList<>();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        if (!prefs.getBoolean(PREF_RUN_MODE_ENABLED_KEY, false)) {
            return ret;
        }

        String query = s.trim().toLowerCase();
        if (query.isEmpty()) {
            return ret;
        }

        // Handle "sys" command
        if (COMMAND_SYS.startsWith(query)) {
            ret.addAll(getSysEntries(""));
        } else if (query.startsWith(COMMAND_SYS + " ")) {
            String subQuery = query.substring(COMMAND_SYS.length() + 1).trim();
            ret.addAll(getSysEntries(subQuery));
        }

        return ret;
    }

    private List<CandidateEntry> getSysEntries(String subQuery) {
        List<CandidateEntry> entries = new ArrayList<>();
        if ("wifi".startsWith(subQuery)) {
            String action = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) 
                ? Settings.Panel.ACTION_WIFI : Settings.ACTION_WIFI_SETTINGS;
            entries.add(new SettingsIntentCandidateEntry(COMMAND_SYS + " wifi", action));
        }
        if ("bluetooth".startsWith(subQuery)) {
            entries.add(new SettingsIntentCandidateEntry(COMMAND_SYS + " bluetooth", Settings.ACTION_BLUETOOTH_SETTINGS));
        }
        if ("mobile_data".startsWith(subQuery) || "mobile data".startsWith(subQuery)) {
            String action = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) 
                ? Settings.Panel.ACTION_INTERNET_CONNECTIVITY : Settings.ACTION_DATA_ROAMING_SETTINGS;
            entries.add(new SettingsIntentCandidateEntry(COMMAND_SYS + " mobile_data", action));
        }
        if ("location".startsWith(subQuery)) {
            entries.add(new SettingsIntentCandidateEntry(COMMAND_SYS + " location", Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
        if ("airplane_mode".startsWith(subQuery) || "airplane mode".startsWith(subQuery)) {
            String action = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) 
                ? Settings.Panel.ACTION_INTERNET_CONNECTIVITY : Settings.ACTION_AIRPLANE_MODE_SETTINGS;
            entries.add(new SettingsIntentCandidateEntry(COMMAND_SYS + " airplane_mode", action));
        }
        return entries;
    }

    private static class SettingsIntentCandidateEntry implements CandidateEntry {
        private final String title;
        private final String action;

        SettingsIntentCandidateEntry(String title, String action) {
            this.title = title;
            this.action = action;
        }

        @Override
        @NonNull
        public String getTitle() {
            return title;
        }

        @Override
        public View getView(MainActivity mainActivity) {
            return null;
        }

        @Override
        public boolean hasLongView() {
            return false;
        }

        @Override
        public EventLauncher getEventLauncher(final Context context) {
            return activity -> {
                Intent intent = new Intent(action);
                activity.startActivity(intent);
                activity.finish();
            };
        }

        @Override
        public Drawable getIcon(Context context) {
            return null;
        }

        @Override
        public boolean hasEvent() {
            return true;
        }

        @Override
        public boolean isSubItem() {
            return false;
        }

        @Override
        public boolean viewIsRecyclable() {
            return true;
        }
    }
}
