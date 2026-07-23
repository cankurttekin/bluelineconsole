package net.cankurttekin.andmenu.interfaces;

import net.cankurttekin.andmenu.applicationMain.MainActivity;

public interface EventLauncher {
    /**
     * launch corresponding event from activity
     * @param activity Source activity that triggers new activity
     */
    void launch(MainActivity activity);
}
