package net.nhiroki.bluelineconsole.applicationMain.theming.eachTheme;

import android.app.PendingIntent;
import android.content.Context;
import android.view.Gravity;
import android.widget.RemoteViews;
import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatDelegate;
import net.nhiroki.bluelineconsole.R;
import net.nhiroki.bluelineconsole.applicationMain.BaseWindowActivity;

public class DmenuTheme extends BaseTheme {
    private static final String THEME_ID = "dmenu";

    @Override
    public void apply(BaseWindowActivity activity) {
        super.apply(activity);
        activity.setTheme(activity.isHomeActivity() ? R.style.AppThemeDmenuHome : R.style.AppThemeDmenu);
        activity.setContentView(R.layout.base_window_layout_dmenu);
        this.registerExitListener(activity, activity.isHomeActivity());

        this.setWindowLocationGravity(activity, Gravity.TOP);
    }

    @Override
    public void applyAccentColor(BaseWindowActivity activity, @ColorInt int color) {}

    @Override
    protected void configureDarkMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    }

    @Override
    public String getThemeID() {
        return THEME_ID;
    }

    @Override
    public CharSequence getThemeTitle(Context context) {
        return context.getString(R.string.theme_name_dmenu);
    }

    @Override
    protected boolean hasFooter() {
        return false;
    }

    @Override
    public boolean supportsAccentColor() {
        return false;
    }

    @Override
    public RemoteViews createRemoteViewsForWidget(Context context, PendingIntent pendingIntent) {
        return null;
    }

    @Override
    public int getDefaultAccentColor(Context context) {
        return 0;
    }
}
