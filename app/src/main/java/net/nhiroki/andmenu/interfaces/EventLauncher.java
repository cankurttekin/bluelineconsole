package net.nhiroki.andmenu.interfaces;

import net.nhiroki.andmenu.applicationMain.MainActivity;

public interface EventLauncher {
    /**
     * launch corresponding event from activity
     * @param activity Source activity that triggers new activity
     */
    void launch(MainActivity activity);
}
