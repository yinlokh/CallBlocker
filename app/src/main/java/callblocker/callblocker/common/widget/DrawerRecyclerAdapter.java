package callblocker.callblocker.common.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import callblocker.callblocker.models.DrawerOption;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class DrawerRecyclerAdapter extends
        RecyclerView.Adapter<DrawerRecyclerAdapter.DrawerRecyclerViewHolder> {

    private List<DrawerOption> drawerOptions = new ArrayList<>();
    private BehaviorSubject<DrawerOption> drawerOptionSelection = BehaviorSubject.create();

    public void setDrawerOptions(List<DrawerOption> drawerOptions) {
        this.drawerOptions = drawerOptions;
        notifyDataSetChanged();
    }

    @Override
    public DrawerRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        DrawerOptionView view = new DrawerOptionView(viewGroup.getContext());
        return new DrawerRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DrawerRecyclerViewHolder viewHolder, int i) {
        final DrawerOption drawerOption = drawerOptions.get(i);
        viewHolder.drawerOptionView.setTitle(drawerOption.title());
        viewHolder.drawerOptionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerOptionSelection.onNext(drawerOption);
            }
        });
    }

    @Override
    public void onViewAttachedToWindow(DrawerRecyclerViewHolder viewHolder) {
        super.onViewAttachedToWindow(viewHolder);

        RecyclerView.LayoutParams lp =
                (RecyclerView.LayoutParams) viewHolder.drawerOptionView.getLayoutParams();
        lp.width = RecyclerView.LayoutParams.MATCH_PARENT;
        viewHolder.drawerOptionView.setLayoutParams(lp);
    }

    @Override
    public int getItemCount() {
        return drawerOptions.size();
    }

    public Observable<DrawerOption> drawerOptionSelections() {
        return drawerOptionSelection;
    }

    class DrawerRecyclerViewHolder extends RecyclerView.ViewHolder {

        final DrawerOptionView drawerOptionView;

        public DrawerRecyclerViewHolder(DrawerOptionView itemView) {
            super(itemView);

            drawerOptionView = itemView;
        }
    }
}
