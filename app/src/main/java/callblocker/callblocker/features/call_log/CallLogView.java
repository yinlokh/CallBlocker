package callblocker.callblocker.features.call_log;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.wealthfront.magellan.Screen;
import com.wealthfront.magellan.ScreenView;

import java.util.List;

import callblocker.callblocker.R;
import callblocker.callblocker.database.CallHistoryStore;
import callblocker.callblocker.models.CallHistory;

public class CallLogView extends FrameLayout implements ScreenView {

    private CallLogAdapter callLogAdapter = new CallLogAdapter();
    private TextView emptyTextView;
    private Screen screen;

    public CallLogView(@NonNull Context context) {
        this(context, null, 0);
    }

    public CallLogView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CallLogView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflate(context, R.layout.list_screen, this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setAdapter(callLogAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        DividerItemDecoration decoration =
                new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.divider));
        recyclerView.addItemDecoration(decoration);

        emptyTextView = (TextView) findViewById(R.id.empty_text_view);
        emptyTextView.setText("No intercepted calls yet.");
    }

    @Override
    public void setScreen(Screen screen) {
        this.screen = screen;
    }

    @Override
    public Screen getScreen() {
        return screen;
    }

    public void setCallHistory(List<CallHistory> callHistoryList) {
        boolean empty = callHistoryList.isEmpty();
        emptyTextView.setVisibility(empty ? VISIBLE : GONE);
        callLogAdapter.setCallHistoryList(callHistoryList);
    }
}
