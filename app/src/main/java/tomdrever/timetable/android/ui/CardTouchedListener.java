package tomdrever.timetable.android.ui;

import android.support.v7.widget.RecyclerView;

public interface CardTouchedListener {
    void onCardClicked(RecyclerView.ViewHolder viewHolder, int position);
    void onCardDragHandleTouched(RecyclerView.ViewHolder viewHolder, int position);
}