package callblocker.callblocker.features.filters.add;

import android.content.Context;

import com.google.common.collect.ImmutableList;
import com.wealthfront.magellan.Screen;

import callblocker.callblocker.R;
import callblocker.callblocker.core.MainActivity;
import callblocker.callblocker.core.PhoneNumberChecker;
import callblocker.callblocker.database.FilterListStore;
import callblocker.callblocker.models.FilterRule;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class AddFilterScreen extends Screen<AddFilterView> {

    private Disposable fabClicksSubscription;
    private Disposable testPhoneNumbersSubscription;

    @Override
    protected AddFilterView createView(Context context) {
        return new AddFilterView(context);
    }

    @Override
    public String getTitle(Context context) {
        return "Add Filter Rule";
    }

    @Override
    protected void onShow(final Context context) {
        super.onShow(context);

        final FilterListStore filterListStore = new FilterListStore(context);

        testPhoneNumbersSubscription = getView().changes().subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object unused) throws Exception {
                boolean blocked = new PhoneNumberChecker(ImmutableList.of(getRuleFromFields()))
                        .shouldBlockNumber(getView().getTestPhoneNumber());
                getView().setTestPassed(blocked);
            }
        });

        MainActivity activity = (MainActivity) getActivity();
        activity.showFloatingActionButton(R.drawable.content_save);
        fabClicksSubscription = activity.floatingActionButtonClicks().subscribe(
                new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        filterListStore.addFilterEntry(getRuleFromFields());
                        getNavigator().goBack();
                    }
                });
    }

    @Override
    protected void onHide(Context context) {
        super.onHide(context);
        fabClicksSubscription.dispose();
        testPhoneNumbersSubscription.dispose();
    }

    private FilterRule getRuleFromFields() {
        return FilterRule.create(
                System.currentTimeMillis(),
                getView().getFilterType(),
                getView().getValue());
    }
}
