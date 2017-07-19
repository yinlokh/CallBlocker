package callblocker.callblocker.features.whitelist;

import android.content.Context;

import com.wealthfront.magellan.Screen;

public class WhitelistScreen extends Screen<WhitelistView> {

    @Override
    protected WhitelistView createView(Context context) {
        return new WhitelistView(context);
    }

    @Override
    public String getTitle(Context context) {
        return "Whitelist";
    }
}
