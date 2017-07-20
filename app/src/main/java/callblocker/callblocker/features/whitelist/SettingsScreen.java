package callblocker.callblocker.features.whitelist;

import android.content.Context;

import com.wealthfront.magellan.Screen;

public class SettingsScreen extends Screen<SettingsView> {

    @Override
    protected SettingsView createView(Context context) {
        return new SettingsView(context);
    }

    @Override
    public String getTitle(Context context) {
        return "Whitelist";
    }
}
