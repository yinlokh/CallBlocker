package callblocker.callblocker.core;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.jakewharton.rxbinding2.support.v7.widget.RxToolbar;
import com.jakewharton.rxbinding2.view.RxView;
import com.wealthfront.magellan.NavigationType;
import com.wealthfront.magellan.Navigator;
import com.wealthfront.magellan.Screen;
import com.wealthfront.magellan.ScreenLifecycleListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import callblocker.callblocker.R;
import callblocker.callblocker.features.call_log.CallLogScreen;
import callblocker.callblocker.features.filters.list.FiltersScreen;
import callblocker.callblocker.features.settings.SettingsScreen;
import callblocker.callblocker.models.DrawerOption;
import callblocker.callblocker.common.widget.DrawerRecyclerAdapter;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

import static android.Manifest.permission.ANSWER_PHONE_CALLS;
import static android.Manifest.permission.PROCESS_OUTGOING_CALLS;
import static android.Manifest.permission.READ_CALL_LOG;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 2345;
    private static final DrawerOption DRAWER_OPTION_CALL_LOG = DrawerOption.create("Call Log", R.drawable.phone);
    private static final DrawerOption DRAWER_OPTION_FILTERS = DrawerOption.create("Filters", R.drawable.filter);
    private static final DrawerOption DRAWER_OPTION_SETTINGS = DrawerOption.create("Settings", R.drawable.settings);
    private static final List<DrawerOption> DRAWER_OPTIONS = ImmutableList.<DrawerOption>builder()
            .add(DRAWER_OPTION_CALL_LOG)
            .add(DRAWER_OPTION_FILTERS)
            .add(DRAWER_OPTION_SETTINGS)
            .build();

    private FloatingActionButton fab;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private DrawerRecyclerAdapter drawerRecyclerAdapter = new DrawerRecyclerAdapter();
    private RecyclerView drawerRecycler;
    private Screen callLogScreen = new CallLogScreen();
    private Screen filtersScreen = new FiltersScreen();
    private Screen settingsScreen = new SettingsScreen();
    private Navigator navigator = Navigator.withRoot(callLogScreen).build();
    private Set<Screen> drawerEnabledScreens =
            ImmutableSet.of(callLogScreen, filtersScreen, settingsScreen);

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
                drawerLayout.setDrawerLockMode(drawerEnabled()
                        ? DrawerLayout.LOCK_MODE_UNLOCKED
                        : DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }

            @Override
            public void onHide(Screen screen) { }
        });

        requestPermissions();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(findViewById(R.id.drawer))) {
            drawerLayout.closeDrawers();
        } else if (!navigator.handleBack()) {
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

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        if (requestCode != PERMISSION_REQUEST_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

       for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PERMISSION_GRANTED) {
                finish();
            }
       }
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
            goToSubscreen(filtersScreen);
        } else if (DRAWER_OPTION_SETTINGS == drawerOption) {
            goToSubscreen(settingsScreen);
        }
    }

    private void goToCallLog() {
        if (navigator.isCurrentScreen(callLogScreen)) {
            return;
        }
        navigator.goBackToRoot(NavigationType.GO);
    }

    private void goToSubscreen(Screen screen) {
        if (navigator.isCurrentScreen(screen)) {
            return;
        } else if (navigator.isCurrentScreen(callLogScreen)) {
            navigator.goTo(screen);
        } else {
            navigator.replace(screen);
        }
    }

    private boolean drawerEnabled() {
        return drawerEnabledScreens.contains(navigator.currentScreen());
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }

        List<String> permissions = new ArrayList<>();
        permissions.add(READ_PHONE_STATE);
        permissions.add(READ_CALL_LOG);
        permissions.add(READ_CONTACTS);
        permissions.add(PROCESS_OUTGOING_CALLS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            permissions.add(ANSWER_PHONE_CALLS);
        }

        List<String> permissionRequests = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PERMISSION_GRANTED) {
                permissionRequests.add(permission);
            }
        }

        String requestArray[] = new String[permissionRequests.size()];
        if (!permissionRequests.isEmpty()) {
            requestPermissions(permissionRequests.toArray(requestArray), PERMISSION_REQUEST_CODE);
        }
    }
}
