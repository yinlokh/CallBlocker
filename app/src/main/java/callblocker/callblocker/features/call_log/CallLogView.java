package callblocker.callblocker.features.call_log;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.wealthfront.magellan.Screen;
import com.wealthfront.magellan.ScreenView;

import callblocker.callblocker.database.CallHistoryStore;

public class CallLogView extends FrameLayout implements ScreenView {

    private Screen screen;

    public CallLogView(@NonNull Context context) {
        this(context, null, 0);
    }

    public CallLogView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CallLogView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        RecyclerView recyclerView = new RecyclerView(context);
        CallLogAdapter callLogAdapter = new CallLogAdapter();
        addView(recyclerView);
        recyclerView.setAdapter(callLogAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        CallHistoryStore callHistoryStore = new CallHistoryStore(context);
        callLogAdapter.setCallHistoryList(callHistoryStore.getCallHistory());
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
