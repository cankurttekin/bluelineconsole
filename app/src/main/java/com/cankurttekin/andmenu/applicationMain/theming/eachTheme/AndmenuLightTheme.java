package net.cankurttekin.andmenu.applicationMain.theming.eachTheme;

import android.content.Context;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatDelegate;

import net.cankurttekin.andmenu.R;

public class AndmenuLightTheme extends AndmenuDefaultTheme{
    private static final String THEME_ID = "light";
    private static final @StringRes int THEME_TITLE_STRING_RES = R.string.theme_name_light;


    @Override
    protected void configureDarkMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

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
