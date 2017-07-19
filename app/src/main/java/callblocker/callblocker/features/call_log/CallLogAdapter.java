package callblocker.callblocker.features.call_log;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import callblocker.callblocker.models.CallHistory;
import callblocker.callblocker.models.DrawerOption;
import io.reactivex.subjects.BehaviorSubject;

public class CallLogAdapter extends
        RecyclerView.Adapter<CallLogAdapter.CallLogRecyclerViewHolder> {

    private List<CallHistory> callHistoryList = new ArrayList<>();
    private BehaviorSubject<DrawerOption> drawerOptionSelection = BehaviorSubject.create();

    public void setCallHistoryList(List<CallHistory> callHistoryList) {
        this.callHistoryList = callHistoryList;
        notifyDataSetChanged();
    }

    @Override
    public CallLogRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        CallLogItemView view = new CallLogItemView(viewGroup.getContext());
        return new CallLogRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CallLogRecyclerViewHolder viewHolder, int i) {
        final CallHistory callHistory = callHistoryList.get(i);
        viewHolder.callLogItemView.setCallHistory(callHistory);
    }

    @Override
    public void onViewAttachedToWindow(CallLogRecyclerViewHolder viewHolder) {
        super.onViewAttachedToWindow(viewHolder);

        RecyclerView.LayoutParams lp =
                (RecyclerView.LayoutParams) viewHolder.callLogItemView.getLayoutParams();
        lp.width = RecyclerView.LayoutParams.MATCH_PARENT;
        viewHolder.callLogItemView.setLayoutParams(lp);
    }

    @Override
    public int getItemCount() {
        return callHistoryList.size();
    }

    class CallLogRecyclerViewHolder extends RecyclerView.ViewHolder {

        final CallLogItemView callLogItemView;

        public CallLogRecyclerViewHolder(CallLogItemView itemView) {
            super(itemView);

            callLogItemView = itemView;
        }
    }
}
