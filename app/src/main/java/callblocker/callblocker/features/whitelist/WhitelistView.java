package callblocker.callblocker.features.whitelist;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.wealthfront.magellan.Screen;
import com.wealthfront.magellan.ScreenView;

public class WhitelistView extends FrameLayout implements ScreenView {

    private Screen screen;

    public WhitelistView(@NonNull Context context) {
        this(context, null, 0);
    }

    public WhitelistView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WhitelistView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundColor(0xFF0000FF);
    }

    @Override
    public void setScreen(Screen screen) {
        this.screen = screen;
    }

    @Override
    public Screen getScreen() {
        return screen;
    }
}
