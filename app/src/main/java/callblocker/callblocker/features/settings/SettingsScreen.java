package callblocker.callblocker.features.settings;

import android.content.Context;

import com.wealthfront.magellan.Screen;

import java.util.ArrayList;
import java.util.List;

import callblocker.callblocker.core.CallBlockerPreferences;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class SettingsScreen extends Screen<SettingsView> {

    private CallBlockerPreferences preferences;
    private List<Disposable> subscriptions = new ArrayList<>();

    @Override
    protected SettingsView createView(Context context) {
        preferences = new CallBlockerPreferences(context);
        return new SettingsView(context);
    }

    @Override
    public String getTitle(Context context) {
        return "Settings";
    }

    @Override
    protected void onShow(Context context) {
        super.onShow(context);

        getView().setWhitelistContactsToggleState(preferences.getWhitelistAllContacts());
        getView().setPauseBlockingToggleState(preferences.getPauseBlocking());
        subscriptions.add(getView().whitelistContactsToggleChanges()
                .distinctUntilChanged()
                .subscribe(
                new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean toggled) throws Exception {
                        preferences.setWhitelistAllContacts(toggled);
                    }
                }
        ));
        subscriptions.add(getView().pauseBlockingsToggleChanges()
                .distinctUntilChanged()
                .subscribe(
                new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean toggled) throws Exception {
                        preferences.setPauseBlocking(toggled);
                    }
                }
        ));
    }

    @Override
    protected void onHide(Context context) {
        super.onHide(context);

        while(!subscriptions.isEmpty()) {
            subscriptions.get(subscriptions.size() - 1).dispose();
            subscriptions.remove(subscriptions.size() - 1);
        }
    }
}
