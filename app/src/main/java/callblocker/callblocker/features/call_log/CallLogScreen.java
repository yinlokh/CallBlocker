package callblocker.callblocker.features.call_log;

import android.content.Context;

import com.wealthfront.magellan.Screen;

public class CallLogScreen extends Screen<CallLogView> {

    @Override
    protected CallLogView createView(Context context) {
        return new CallLogView(context);
    }

    @Override
    public String getTitle(Context context) {
        return "Call Log";
    }

    @Override
    protected void onShow(Context context) {
        super.onShow(context);
    }
}
