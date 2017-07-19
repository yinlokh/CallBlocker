package callblocker.callblocker.features.filters.list;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.FrameLayout;

import com.jakewharton.rxbinding2.view.RxView;
import com.wealthfront.magellan.Screen;
import com.wealthfront.magellan.ScreenView;

import java.util.List;

import callblocker.callblocker.R;
import callblocker.callblocker.models.FilterRule;
import io.reactivex.Observable;

public class FiltersView extends FrameLayout implements ScreenView {

    private FilterRulesAdapter filterRulesAdapter = new FilterRulesAdapter();
    private RecyclerView recyclerView;
    private Screen screen;
    private Button addButton;

    public FiltersView(@NonNull Context context) {
        this(context, null, 0);
    }

    public FiltersView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FiltersView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.filter_rules_screen, this);
        addButton = (Button) findViewById(R.id.add_button);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
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
        filterRulesAdapter.setFilterRules(filterRules);
    }

    public Observable<Object> addButtonClicks() {
        return RxView.clicks(addButton);
    }

    public Observable<FilterRule> ruleDeletionClicks() {
        return filterRulesAdapter.ruleDeletionClicks();
    }
}
