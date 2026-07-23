package com.cankurttekin.andmenu.applicationMain.theming.eachTheme;

import android.app.PendingIntent;
import android.content.Context;
import android.view.Gravity;
import android.widget.RemoteViews;

import androidx.annotation.ColorInt;
import androidx.annotation.StringRes;

import com.cankurttekin.andmenu.R;
import com.cankurttekin.andmenu.applicationMain.BaseWindowActivity;

public class DmenuTheme extends BaseTheme {
    private static final String THEME_ID = "dmenu";
    private static final @StringRes int THEME_TITLE_STRING_RES = R.string.theme_name_dmenu;

    @Override
    protected void configureDarkMode() {
    }

    @Override
    public void apply(BaseWindowActivity activity) {
        super.apply(activity);

        if (this.isDarkMode(activity)) {
            activity.setTheme(activity.isHomeActivity() ? R.style.AppThemeDmenuDarkHome : R.style.AppThemeDmenuDark);
        } else {
            activity.setTheme(activity.isHomeActivity() ? R.style.AppThemeDmenuHome : R.style.AppThemeDmenu);
        }
        activity.setContentView(R.layout.base_window_layout_dmenu);

        this.registerExitListener(activity, activity.isHomeActivity());

        this.setWindowLocationGravity(activity, Gravity.TOP);
    }

    @Override
    public void applyAccentColor(BaseWindowActivity activity, @ColorInt int color) {
    }

    @Override
    public String getThemeID() {
        return THEME_ID;
    }

    @Override
    public CharSequence getThemeTitle(Context context) {
        return context.getString(THEME_TITLE_STRING_RES);
    }

    @Override
    protected boolean hasFooter() {
        return true;
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
    public @ColorInt int getDefaultAccentColor(Context context) {
        return 0;
    }

    @Override
    public void onCreateFinal(BaseWindowActivity activity) {
        super.onCreateFinal(activity);
    }
}
