package callblocker.callblocker.core;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.google.common.collect.ImmutableList;
import com.jakewharton.rxbinding2.support.v7.widget.RxToolbar;
import com.jakewharton.rxbinding2.view.RxView;
import com.wealthfront.magellan.NavigationType;
import com.wealthfront.magellan.Navigator;
import com.wealthfront.magellan.Screen;
import com.wealthfront.magellan.ScreenLifecycleListener;

import java.util.List;

import callblocker.callblocker.R;
import callblocker.callblocker.features.call_log.CallLogScreen;
import callblocker.callblocker.features.filters.list.FiltersScreen;
import callblocker.callblocker.models.DrawerOption;
import callblocker.callblocker.common.widget.DrawerRecyclerAdapter;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    private static final DrawerOption DRAWER_OPTION_CALL_LOG = DrawerOption.create("Call Log", R.drawable.phone);
    private static final DrawerOption DRAWER_OPTION_FILTERS = DrawerOption.create("Filters", R.drawable.filter);
    private static final List<DrawerOption> DRAWER_OPTIONS = ImmutableList.<DrawerOption>builder()
            .add(DRAWER_OPTION_CALL_LOG)
            .add(DRAWER_OPTION_FILTERS)
            .build();

    private FloatingActionButton fab;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private DrawerRecyclerAdapter drawerRecyclerAdapter = new DrawerRecyclerAdapter();
    private RecyclerView drawerRecycler;
    private Screen callLogScreen = new CallLogScreen();
    private Screen filtersScreen = new FiltersScreen();
    private Navigator navigator = Navigator.withRoot(callLogScreen).build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerRecycler = (RecyclerView) findViewById(R.id.drawer);
        drawerRecycler.setLayoutManager(new LinearLayoutManager(this));
        drawerRecycler.setAdapter(drawerRecyclerAdapter);
        drawerRecyclerAdapter.setDrawerOptions(DRAWER_OPTIONS);
        drawerRecyclerAdapter.drawerOptionSelections().subscribe(new Consumer<DrawerOption>() {
            @Override
            public void accept(DrawerOption drawerOption) throws Exception {
                onDrawerOptionSelected(drawerOption);
                drawerLayout.closeDrawers();
            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.menu);
        toolbar.setTitle("Call Log");
        toolbar.setTitleTextColor(0xffffffff);
        RxToolbar.navigationClicks(toolbar).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                if (drawerEnabled()) {
                    drawerLayout.openDrawer(findViewById(R.id.drawer));
                } else {
                    navigator.goBack();
                }
            }
        });

        navigator.onCreate(this, savedInstanceState);
        navigator.addLifecycleListener(new ScreenLifecycleListener() {
            @Override
            public void onShow(Screen screen) {
                toolbar.setTitle(screen.getTitle(MainActivity.this));
                toolbar.setNavigationIcon(drawerEnabled() ? R.drawable.menu : R.drawable.arrow_left);
            }

            @Override
            public void onHide(Screen screen) { }
        });
    }

    @Override
    public void onBackPressed() {
        if (!navigator.handleBack()) {
            super.onBackPressed();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        navigator.onPause(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        navigator.onResume(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        navigator.onDestroy(this);
    }

    public void showFloatingActionButton(int iconResId) {
        fab.setImageResource(iconResId);
        fab.show();
    }

    public void hideFloatingActionButton() {
        fab.hide();
    }

    public Observable<Object> floatingActionButtonClicks() {
        return RxView.clicks(fab);
    }

    private void onDrawerOptionSelected(DrawerOption drawerOption) {
        if (DRAWER_OPTION_CALL_LOG == drawerOption) {
            goToCallLog();
        } else if (DRAWER_OPTION_FILTERS == drawerOption) {
            goToFilters();
        }
    }

    private void goToCallLog() {
        if (navigator.isCurrentScreen(callLogScreen)) {
            return;
        }
        navigator.goBackToRoot(NavigationType.GO);
    }

    private void goToFilters() {
        if (navigator.isCurrentScreen(filtersScreen)) {
            return;
        }

        navigator.goTo(filtersScreen);
    }

    private boolean drawerEnabled() {
        return navigator.currentScreen() == callLogScreen
                || navigator.currentScreen() == filtersScreen;
    }
}
