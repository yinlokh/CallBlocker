package callblocker.callblocker.features.filters.list;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.wealthfront.magellan.Screen;
import com.wealthfront.magellan.ScreenView;

import java.util.List;

import callblocker.callblocker.R;
import callblocker.callblocker.models.FilterRule;
import io.reactivex.Observable;

public class FiltersView extends FrameLayout implements ScreenView {

    private FilterRulesAdapter filterRulesAdapter = new FilterRulesAdapter();
    private View emptyView;
    private RecyclerView recyclerView;
    private Screen screen;

    public FiltersView(@NonNull Context context) {
        this(context, null, 0);
    }

    public FiltersView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FiltersView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.list_screen, this);
        emptyView = findViewById(R.id.empty_text_view);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        DividerItemDecoration decoration =
                new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.divider));
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(filterRulesAdapter);
    }

    @Override
    public void setScreen(Screen screen) {
        this.screen = screen;
    }

    @Override
    public Screen getScreen() {
        return screen;
    }

    public void setFilterRules(List<FilterRule> filterRules) {
        boolean empty = filterRules.isEmpty();
        emptyView.setVisibility(empty ? VISIBLE : GONE);
        recyclerView.setVisibility(empty ? GONE : VISIBLE);
        filterRulesAdapter.setFilterRules(filterRules);
    }

    public Observable<FilterRule> ruleDeletionClicks() {
        return filterRulesAdapter.ruleDeletionClicks();
    }
}
