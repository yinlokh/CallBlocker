package callblocker.callblocker.features.filters.add;

import android.content.Context;
import android.widget.Toast;

import com.google.common.collect.ImmutableList;
import com.wealthfront.magellan.Screen;

import callblocker.callblocker.core.PhoneNumberChecker;
import callblocker.callblocker.database.FilterListStore;
import callblocker.callblocker.models.FilterRule;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class AddFilterScreen extends Screen<AddFilterView> {

    private Disposable addClicksSubscription;
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
        addClicksSubscription = getView().addButtonClicks().subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                filterListStore.addFilterEntry(getRuleFromFields());
                getNavigator().goBack();
            }
        });
        testPhoneNumbersSubscription = getView().testPhoneNumbers().subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                boolean blocked = new PhoneNumberChecker(ImmutableList.of(getRuleFromFields()))
                        .shouldBlockNumber(s);
                if (blocked) {
                    Toast.makeText(context, "Blocked", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Not Blocked", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onHide(Context context) {
        super.onHide(context);
        addClicksSubscription.dispose();
        testPhoneNumbersSubscription.dispose();
    }

    private FilterRule getRuleFromFields() {
        return FilterRule.create(
                System.currentTimeMillis(),
                getView().getFilterType(),
                getView().getValue());
    }
}
