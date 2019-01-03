package callblocker.callblocker.features.call_log;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;

import com.wealthfront.magellan.Screen;

import java.util.List;

import callblocker.callblocker.R;
import callblocker.callblocker.core.MainActivity;
import callblocker.callblocker.database.CallHistoryStore;
import callblocker.callblocker.core.StickyService;
import callblocker.callblocker.features.filters.add.AddFilterScreen;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class CallLogScreen extends Screen<CallLogView> {

    private Disposable fabClicksSubscription;
    private Intent serviceIntent = new Intent();

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

        CallHistoryStore callHistoryStore = new CallHistoryStore(context);
        getView().setCallHistory(callHistoryStore.getCallHistory());
        serviceIntent.setClass(context, StickyService.class);
        final MainActivity activity = ((MainActivity) getActivity());
        activity.showFloatingActionButton(getFABIcon());
        fabClicksSubscription = activity.floatingActionButtonClicks()
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (isServiceRunning()) {
                            activity.stopService(serviceIntent);
                        } else {
                            activity.startService(serviceIntent);
                        }
                        activity.showFloatingActionButton(getFABIcon());
                    }
                });
    }

    private int getFABIcon() {
        return isServiceRunning()
                ? android.R.drawable.ic_media_pause
                : android.R.drawable.ic_media_play;
    }

    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context
                .ACTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }

        List<RunningServiceInfo> runningServices = manager.getRunningServices(Integer.MAX_VALUE);
        if (runningServices == null) {
            return false;
        }

        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (StickyService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
