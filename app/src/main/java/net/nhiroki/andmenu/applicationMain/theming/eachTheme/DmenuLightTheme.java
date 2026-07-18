package net.nhiroki.andmenu.applicationMain.theming.eachTheme;

import android.content.Context;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatDelegate;
import net.nhiroki.andmenu.R;

public class DmenuLightTheme extends DmenuTheme {
    private static final String THEME_ID = "dmenu_light";
    private static final @StringRes int THEME_TITLE_STRING_RES = R.string.theme_name_dmenu_light;

    @Override
    protected void configureDarkMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    @Override
    protected boolean isDarkMode(Context context) {
        return false;
    }

    @Override
    public String getThemeID() {
        return THEME_ID;
    }

    @Override
    public CharSequence getThemeTitle(Context context) {
        return context.getString(THEME_TITLE_STRING_RES);
    }
}
