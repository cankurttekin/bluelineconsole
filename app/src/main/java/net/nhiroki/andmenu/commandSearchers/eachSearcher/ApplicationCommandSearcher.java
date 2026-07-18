package net.nhiroki.andmenu.commandSearchers.eachSearcher;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.PreferenceManager;

import net.nhiroki.andmenu.R;
import net.nhiroki.andmenu.applicationMain.MainActivity;
import net.nhiroki.andmenu.commandSearchers.lib.StringMatchStrategy;
import net.nhiroki.andmenu.commands.applications.ApplicationDatabase;
import net.nhiroki.andmenu.commands.applications.ApplicationSettings;
import net.nhiroki.andmenu.commands.applications.ApplicationUsageHistory;
import net.nhiroki.andmenu.dataStore.cache.ApplicationInformation;
import net.nhiroki.andmenu.interfaces.CandidateEntry;
import net.nhiroki.andmenu.interfaces.CommandSearcher;
import net.nhiroki.andmenu.interfaces.EventLauncher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicationCommandSearcher implements CommandSearcher {
    private ApplicationDatabase applicationDatabase;

    public ApplicationCommandSearcher() {
    }

    @Override
    public void refresh(Context context) {
        this.applicationDatabase = new ApplicationDatabase(context);
    }

    @Override
    public void close() {
        this.applicationDatabase.close();
    }

    @Override
    public boolean isPrepared() {
        return this.applicationDatabase.isPrepared();
    }

    @Override
    public void waitUntilPrepared() {
        this.applicationDatabase.waitUntilPrepared();
    }

    @Override
    @NonNull
    public List<CandidateEntry> searchCandidateEntries(String query, Context context) {
        List<CandidateEntry> candidates = new ArrayList<>();

        final boolean matchAllApplications = query.equalsIgnoreCase("all_apps");
        final boolean matchHiddenApplications = query.equalsIgnoreCase("hidden_apps");

        final int matchStrategy = StringMatchStrategy.getStrategyPreference(context);

        List<Pair<Integer, CandidateEntry>> appCandidates = new ArrayList<>();
        for (ApplicationInformation applicationInformation : applicationDatabase.getApplicationInformationList()) {
            boolean isHidden = ApplicationSettings.isHidden(context, applicationInformation.getPackageName());
            if (isHidden && !matchHiddenApplications) {
                continue;
            }

            String customLabel = ApplicationSettings.getCustomLabel(context, applicationInformation.getPackageName());
            final String appLabel = (customLabel != null) ? customLabel : applicationInformation.getLabel();

            final ApplicationInfo androidApplicationInfo = applicationDatabase.getAndroidApplicationInfo(applicationInformation.getPackageName());

            if (matchAllApplications || (matchHiddenApplications && isHidden)) {
                appCandidates.add(new Pair<>(0, new AppOpenCandidateEntry(context, applicationInformation, androidApplicationInfo, appLabel)));
                continue;
            }

            int appLabelMatchResult = StringMatchStrategy.match(query, appLabel, false, matchStrategy);
            if (appLabelMatchResult != -1) {
                appCandidates.add(new Pair<>(appLabelMatchResult, new AppOpenCandidateEntry(context, applicationInformation, androidApplicationInfo, appLabel)));
                continue;
            }

            int packageNameMatchResult = StringMatchStrategy.match(query, applicationInformation.getPackageName(), false, matchStrategy);
            if (packageNameMatchResult != -1) {
                appCandidates.add(new Pair<>(100000 + packageNameMatchResult, new AppOpenCandidateEntry(context, applicationInformation, androidApplicationInfo, appLabel)));
                //noinspection UnnecessaryContinue
                continue;
            }
        }

        final boolean sortByUsage = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("pref_apps_sort_by_usage", false);

        if (sortByUsage) {
            final Map<String, Integer> usageCache = new HashMap<>();
            for (Pair<Integer, CandidateEntry> pair : appCandidates) {
                String pkg = ((AppOpenCandidateEntry) pair.second).getPackageName();
                if (!usageCache.containsKey(pkg)) {
                    usageCache.put(pkg, ApplicationUsageHistory.getUsageCount(context, pkg));
                }
            }

            Collections.sort(appCandidates, (o1, o2) -> {
                int scoreCompare = o1.first.compareTo(o2.first);
                if (scoreCompare != 0) {
                    return scoreCompare;
                }
                Integer u1 = usageCache.get(((AppOpenCandidateEntry) o1.second).getPackageName());
                Integer u2 = usageCache.get(((AppOpenCandidateEntry) o2.second).getPackageName());
                int usage1 = (u1 != null) ? u1 : 0;
                int usage2 = (u2 != null) ? u2 : 0;
                return Integer.compare(usage2, usage1); // Descending
            });
        } else {
            Collections.sort(appCandidates, (o1, o2) -> o1.first.compareTo(o2.first));
        }

        for (Pair<Integer, CandidateEntry> entry : appCandidates) {
            candidates.add(entry.second);
        }

        return candidates;
    }

    private static class AppOpenCandidateEntry implements CandidateEntry {
        private final ApplicationInformation applicationInformation;
        private final ApplicationInfo androidApplicationInfo;
        private final String title;
        private final boolean displayPackageName;

        public String getPackageName() {
            return applicationInformation.getPackageName();
        }

        // Getting app title in Android is slow, so app title also should be given via constructor from cache.
        AppOpenCandidateEntry(Context context, ApplicationInformation applicationInformation, ApplicationInfo androidApplicationInfo, String appTitle) {
            this.applicationInformation = applicationInformation;
            this.androidApplicationInfo = androidApplicationInfo;
            this.title = appTitle;
            this.displayPackageName = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("pref_apps_show_package_name", false);
        }

        @Override
        @NonNull
        public String getTitle() {
            return title;
        }

        @Override
        public View getView(MainActivity mainActivity) {
            if(!displayPackageName) {
                return null;
            }

            String packageName = AppOpenCandidateEntry.this.applicationInformation.getPackageName();
            TextView packageNameView = new TextView(mainActivity);
            packageNameView.setText(packageName);
            packageNameView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            return packageNameView;
        }

        @Override
        public boolean hasEvent() {
            return true;
        }

        @Override
        public EventLauncher getEventLauncher(final Context context) {
            return activity -> {
                String packageName = AppOpenCandidateEntry.this.applicationInformation.getPackageName();
                ApplicationUsageHistory.recordUsage(activity, packageName);
                Intent intent = activity.getPackageManager().getLaunchIntentForPackage(AppOpenCandidateEntry.this.applicationInformation.getPackageName());
                if (packageName.equals(context.getPackageName())) {
                    // special case that happens to some curious behavior in home app
                    activity.finishIfNotHome();
                    activity.startActivity(new Intent(activity, MainActivity.class));
                    return;
                }
                if (intent == null) {
                    Toast.makeText(activity, String.format(activity.getString(R.string.error_failure_not_found_opening_application_with_class), packageName), Toast.LENGTH_LONG).show();
                    return;
                }
                activity.startActivity(intent);
                activity.finishIfNotHome();
            };
        }

        @Override
        public boolean hasLongView() {
            return false;
        }

        @Override
        public Drawable getIcon(Context context) {
            return context.getPackageManager().getApplicationIcon(androidApplicationInfo);
        }

        @Override
        public void onLongClick(MainActivity activity) {
            String packageName = applicationInformation.getPackageName();
            boolean isHidden = ApplicationSettings.isHidden(activity, packageName);

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle(title);
            builder.setItems(new CharSequence[]{
                    activity.getString(R.string.menu_item_app_info),
                    activity.getString(R.string.menu_item_rename),
                    isHidden ? activity.getString(R.string.menu_item_unhide) : activity.getString(R.string.menu_item_hide),
                    activity.getString(R.string.menu_item_uninstall)
            }, (dialog, which) -> {
                switch (which) {
                    case 0: // App Info
                        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + packageName));
                        activity.startActivity(intent);
                        break;
                    case 1: // Rename
                        AlertDialog.Builder renameBuilder = new AlertDialog.Builder(activity);
                        renameBuilder.setTitle(String.format(activity.getString(R.string.dialog_title_rename_format), title));
                        final EditText input = new EditText(activity);
                        input.setText(title);
                        renameBuilder.setView(input);
                        renameBuilder.setPositiveButton(R.string.button_ok, (dialog1, which1) -> {
                            ApplicationSettings.setCustomLabel(activity, packageName, input.getText().toString());
                            activity.refreshSearch();
                        });
                        renameBuilder.setNegativeButton(R.string.button_cancel, (dialog1, which1) -> dialog1.cancel());
                        renameBuilder.show();
                        break;
                    case 2: // Hide / Unhide
                        if (isHidden) {
                            ApplicationSettings.unhideApp(activity, packageName);
                        } else {
                            ApplicationSettings.hideApp(activity, packageName);
                        }
                        activity.refreshSearch();
                        break;
                    case 3: // Uninstall
                        try {
                            Uri packageUri = Uri.parse("package:" + packageName);
                            Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageUri);
                            uninstallIntent.setData(packageUri);
                            activity.startActivity(uninstallIntent);
                        } catch (Exception e) {
                            Toast.makeText(activity, "Could not start uninstaller", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            });
            builder.show();
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
