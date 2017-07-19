package callblocker.callblocker.features.filters.list;

import android.content.Context;

import com.wealthfront.magellan.Screen;

import callblocker.callblocker.database.FilterListStore;
import callblocker.callblocker.features.filters.add.AddFilterScreen;
import callblocker.callblocker.models.FilterRule;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class FiltersScreen extends Screen<FiltersView> {

    Disposable addClicksSubscription;
    Disposable deleteClicksSubscription;

    @Override
    protected FiltersView createView(Context context) {
        return new FiltersView(context);
    }

    @Override
    public String getTitle(Context context) {
        return "Filters";
    }

    @Override
    protected void onShow(Context context) {
        super.onShow(context);

        final FilterListStore filterListStore = new FilterListStore(context);
        getView().setFilterRules(filterListStore.getFilterEntries());
        addClicksSubscription = getView().addButtonClicks().subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                getNavigator().goTo(new AddFilterScreen());
            }
        });
        deleteClicksSubscription = getView().ruleDeletionClicks().subscribe(new Consumer<FilterRule>() {
            @Override
            public void accept(FilterRule filterRule) throws Exception {
                filterListStore.removeFilterEntry(filterRule);
                getView().setFilterRules(filterListStore.getFilterEntries());
            }
        });
    }

    @Override
    protected void onHide(Context context) {
        super.onHide(context);
        addClicksSubscription.dispose();
        deleteClicksSubscription.dispose();
    }
}
