package com.cankurttekin.andmenu.interfaces;

import com.cankurttekin.andmenu.applicationMain.MainActivity;

public interface EventLauncher {
    /**
     * launch corresponding event from activity
     * @param activity Source activity that triggers new activity
     */
    void launch(MainActivity activity);
}
