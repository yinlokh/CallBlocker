package callblocker.callblocker.features.settings;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Switch;

import com.wealthfront.magellan.Screen;
import com.wealthfront.magellan.ScreenView;

import callblocker.callblocker.R;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class SettingsView extends FrameLayout implements ScreenView {

    private Screen screen;
    private Switch whitelistContactsToggle;
    private Switch pauseBlockingToggle;
    private BehaviorSubject<Boolean> whitelistContactsToggleState = BehaviorSubject.create();
    private BehaviorSubject<Boolean> pauseBlockingToggleState = BehaviorSubject.create();

    public SettingsView(@NonNull Context context) {
        this(context, null, 0);
    }

    public SettingsView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingsView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.settings_screen, this);
        whitelistContactsToggle = (Switch) findViewById(R.id.whitelist_contacts_toggle);
        pauseBlockingToggle = (Switch) findViewById(R.id.pause_blocking_toggle);
        whitelistContactsToggle.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        whitelistContactsToggleState.onNext(b);
                    }
                });
        pauseBlockingToggle.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        pauseBlockingToggleState.onNext(b);
                    }
                });
    }

    @Override
    public void setScreen(Screen screen) {
        this.screen = screen;
    }

    @Override
    public Screen getScreen() {
        return screen;
    }

    public Observable<Boolean> whitelistContactsToggleChanges() {
        return whitelistContactsToggleState;
    }

    public Observable<Boolean> pauseBlockingsToggleChanges() {
        return pauseBlockingToggleState;
    }

    public void setWhitelistContactsToggleState(boolean toggled) {
        whitelistContactsToggle.setChecked(toggled);
    }

    public void setPauseBlockingToggleState(boolean toggled) {
        whitelistContactsToggle.setChecked(toggled);
    }
}
