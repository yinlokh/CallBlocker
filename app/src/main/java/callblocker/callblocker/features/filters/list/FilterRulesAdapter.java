package callblocker.callblocker.features.filters.list;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import callblocker.callblocker.models.FilterRule;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

public class FilterRulesAdapter extends
        RecyclerView.Adapter<FilterRulesAdapter.FilterRuleViewHolder> {

    private List<FilterRule> filterRules = new ArrayList<>();
    private PublishSubject<FilterRule> ruleDeletion = PublishSubject.create();

    public void setFilterRules(List<FilterRule> filterRules) {
        this.filterRules = filterRules;
        notifyDataSetChanged();
    }

    @Override
    public FilterRuleViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        FilterRuleItemView view = new FilterRuleItemView(viewGroup.getContext());
        return new FilterRuleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FilterRuleViewHolder viewHolder, int i) {
        final FilterRule filterRule = filterRules.get(i);
        viewHolder.filterRuleItemView.setFilterRule(filterRule);
        Disposable subscription = viewHolder.filterRuleItemView.deleteClicks().subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                ruleDeletion.onNext(filterRule);
            }
        });
        viewHolder.deleteClickSubscription = subscription;
    }

    @Override
    public void onViewRecycled(FilterRuleViewHolder viewHolder) {
        super.onViewRecycled(viewHolder);

        if (viewHolder.deleteClickSubscription != null) {
            viewHolder.deleteClickSubscription.dispose();
            viewHolder.deleteClickSubscription = null;
        }
    }

    @Override
    public void onViewAttachedToWindow(FilterRuleViewHolder viewHolder) {
        super.onViewAttachedToWindow(viewHolder);

        RecyclerView.LayoutParams lp =
                (RecyclerView.LayoutParams) viewHolder.filterRuleItemView.getLayoutParams();
        lp.width = RecyclerView.LayoutParams.MATCH_PARENT;
        viewHolder.filterRuleItemView.setLayoutParams(lp);
    }

    @Override
    public int getItemCount() {
        return filterRules.size();
    }

    public Observable<FilterRule> ruleDeletionClicks() {
        return ruleDeletion;
    }

    class FilterRuleViewHolder extends RecyclerView.ViewHolder {

        final FilterRuleItemView filterRuleItemView;
        Disposable deleteClickSubscription;

        public FilterRuleViewHolder(FilterRuleItemView itemView) {
            super(itemView);

            filterRuleItemView = itemView;
        }
    }
}
