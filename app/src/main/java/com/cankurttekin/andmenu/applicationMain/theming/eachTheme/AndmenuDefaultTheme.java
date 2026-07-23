package com.cankurttekin.andmenu.applicationMain.theming.eachTheme;

import android.app.PendingIntent;
import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RemoteViews;

import androidx.annotation.ColorInt;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.drawable.DrawableCompat;

import com.cankurttekin.andmenu.R;
import com.cankurttekin.andmenu.applicationMain.BaseWindowActivity;

public class AndmenuDefaultTheme extends BaseTheme {
    private static final String THEME_ID = "blue_line_console";
    private static final @StringRes int THEME_TITLE_STRING_RES = R.string.theme_name_default;


    @Override
    protected void configureDarkMode() {
        // AppCompatDelegate.setLocalNightMode, behaves strangely; sometimes activity does not come up.
        // AppCompatDelegate.setDefaultNightMode(), on the other hand, seems to crash other activities already launched;
        // Currently only the solution I could find was finishing all activities before calling setDefaultNightMode.
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    }

    @Override
    public void apply(BaseWindowActivity activity) {
        super.apply(activity);

        if (this.isDarkMode(activity)) {
            activity.setTheme(activity.isHomeActivity() ? R.style.AppThemeAndmenuDarkHome : R.style.AppThemeAndmenuDark);
        } else {
            activity.setTheme(activity.isHomeActivity() ? R.style.AppThemeAndmenuHome : R.style.AppThemeAndmenu);
        }
        activity.setContentView(R.layout.base_window_layout_default);

        this.setFooterMargin(activity);

        this.registerExitListener(activity, activity.isHomeActivity());
    }

    @Override
    public void applyAccentColor(BaseWindowActivity activity, @ColorInt int color) {
        DrawableCompat.setTint(activity.findViewById(R.id.baseWindowDefaultThemeHeaderAccent).getBackground(), color);
        DrawableCompat.setTint(activity.findViewById(R.id.baseWindowDefaultThemeFooterAccent).getBackground(), color);
        activity.findViewById(R.id.baseWindowDefaultThemeHeaderStartAccent).setBackgroundColor(color);
        activity.findViewById(R.id.baseWindowDefaultThemeFooterEndAccent).setBackgroundColor(color);
        activity.findViewById(R.id.baseWindowDefaultThemeMainLinearLayoutOuter).setBackgroundColor(color);
        activity.findViewById(R.id.baseWindowDefaultThemeMainLayoutTopEdge).setBackgroundColor(color);
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
    public void onCreateFinal(BaseWindowActivity activity) {
        super.onCreateFinal(activity);

        activity.findViewById(R.id.baseWindowDefaultThemeMainLayoutTopEdge).setOnTouchListener(activity.new TitleBarDragOnTouchListener());

        // Make setTint() in onResume() to work
        activity.findViewById(R.id.baseWindowDefaultThemeHeaderAccent).getBackground().mutate();
        activity.findViewById(R.id.baseWindowDefaultThemeFooterAccent).getBackground().mutate();
    }

    @Override
    public void enableWindowAnimationForElement(BaseWindowActivity activity) {
        super.enableWindowAnimationForElement(activity);
        activity.enableWindowAnimationForEachViewGroup(activity.findViewById(R.id.baseWindowDefaultThemeMainLinearLayoutOuter));
        activity.enableWindowAnimationForEachViewGroup(activity.findViewById(R.id.baseWindowDefaultThemeHeaderStartAccent));
        activity.enableWindowAnimationForEachViewGroup(activity.findViewById(R.id.baseWindowDefaultThemeFooterEndAccent));
        activity.enableWindowAnimationForEachViewGroup(activity.findViewById(R.id.baseWindowDefaultThemeHeaderImagePart));
        activity.enableWindowAnimationForEachViewGroup(activity.findViewById(R.id.baseWindowDefaultThemeFooterImagePart));
    }

    @Override
    public void disableWindowAnimationForElement(BaseWindowActivity activity) {
        super.disableWindowAnimationForElement(activity);
        activity.disableWindowAnimationForEachViewGroup(activity.findViewById(R.id.baseWindowDefaultThemeMainLinearLayoutOuter));
        activity.disableWindowAnimationForEachViewGroup(activity.findViewById(R.id.baseWindowDefaultThemeHeaderStartAccent));
        activity.disableWindowAnimationForEachViewGroup(activity.findViewById(R.id.baseWindowDefaultThemeFooterEndAccent));
        activity.disableWindowAnimationForEachViewGroup(activity.findViewById(R.id.baseWindowDefaultThemeHeaderImagePart));
        activity.disableWindowAnimationForEachViewGroup(activity.findViewById(R.id.baseWindowDefaultThemeFooterImagePart));
    }

    @Override
    public boolean supportsAccentColor() {
        return true;
    }

    @Override
    public void changeBaseWindowElementSizeForAnimation(BaseWindowActivity activity, boolean visible) {
        super.changeBaseWindowElementSizeForAnimation(activity, visible);

        final View centerLLOuter = activity.findViewById(R.id.baseWindowDefaultThemeMainLinearLayoutOuter);
        if (centerLLOuter == null) {
            return;
        }
        final LinearLayout.LayoutParams centerLPOuter = (LinearLayout.LayoutParams) centerLLOuter.getLayoutParams();
        if (visible) {
            centerLPOuter.height = activity.isSmallWindow() ? LinearLayout.LayoutParams.WRAP_CONTENT : LinearLayout.LayoutParams.MATCH_PARENT;
        } else {
            centerLPOuter.height = 0;
        }
        centerLLOuter.setLayoutParams(centerLPOuter);
    }

    @Override
    public RemoteViews createRemoteViewsForWidget(Context context, PendingIntent pendingIntent) {
        int layoutId = this.isDarkMode(context) ? R.layout.widget_launcher_dark_theme : R.layout.widget_launcher_default_theme;
        RemoteViews ret =  new RemoteViews(context.getPackageName(), layoutId);
        ret.setOnClickPendingIntent(R.id.widgetLauncherRootLinearLayout, pendingIntent);
        return ret;
    }

    @Override
    public @ColorInt int getDefaultAccentColor(Context context) {
        TypedValue accentColorFromTheme = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.andmenuAccentColor, accentColorFromTheme, true);

        return accentColorFromTheme.data;
    }
}
