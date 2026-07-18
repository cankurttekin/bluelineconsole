package net.nhiroki.andmenu.applicationMain.theming.eachTheme;

import android.app.PendingIntent;
import android.content.Context;
import android.util.TypedValue;
import android.widget.RemoteViews;
import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatDelegate;
import net.nhiroki.andmenu.R;
import net.nhiroki.andmenu.applicationMain.BaseWindowActivity;

public class GruvboxTheme extends BaseTheme {
    private static final String THEME_ID = "gruvbox";

    @Override
    public void apply(BaseWindowActivity activity) {
        super.apply(activity);
        activity.setTheme(activity.isHomeActivity() ? R.style.AppThemeGruvboxHome : R.style.AppThemeGruvbox);
        activity.setContentView(R.layout.base_window_layout_default);

        this.setFooterMargin(activity);
        this.registerExitListener(activity, activity.isHomeActivity());
    }

    @Override
    public void applyAccentColor(BaseWindowActivity activity, @ColorInt int color) {
        TypedValue accentColorFromTheme = new TypedValue();
        activity.getTheme().resolveAttribute(R.attr.andmenuAccentColor, accentColorFromTheme, true);
        int themeColor = accentColorFromTheme.data;

        activity.findViewById(R.id.baseWindowDefaultThemeHeaderStartAccent).setBackgroundColor(themeColor);
        activity.findViewById(R.id.baseWindowDefaultThemeFooterEndAccent).setBackgroundColor(themeColor);
        activity.findViewById(R.id.baseWindowDefaultThemeMainLinearLayoutOuter).setBackgroundColor(themeColor);
        activity.findViewById(R.id.baseWindowDefaultThemeMainLayoutTopEdge).setBackgroundColor(themeColor);
    }

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
        return context.getString(R.string.theme_name_gruvbox);
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

    @Override
    public void onCreateFinal(BaseWindowActivity activity) {
        super.onCreateFinal(activity);

        activity.findViewById(R.id.baseWindowDefaultThemeMainLayoutTopEdge).setOnTouchListener(activity.new TitleBarDragOnTouchListener());

        // Make setTint() in onResume() to work
        activity.findViewById(R.id.baseWindowDefaultThemeHeaderAccent).getBackground().mutate();
        activity.findViewById(R.id.baseWindowDefaultThemeFooterAccent).getBackground().mutate();
    }
}
