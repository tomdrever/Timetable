package tomdrever.timetable.android.ui.view;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import tomdrever.timetable.R;
import tomdrever.timetable.android.ui.CardTouchedListener;
import tomdrever.timetable.data.TimetableContainer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

class TimetablesOverviewRecyclerViewAdapter extends RecyclerView.Adapter<TimetablesOverviewRecyclerViewAdapter.TimetableDetailViewHolder> {
    private ObservableArrayList<TimetableContainer> timetableContainers;

    private Context context;

    private final CardTouchedListener listener;

    TimetablesOverviewRecyclerViewAdapter(ObservableArrayList<TimetableContainer> timetableContainers, Context context, CardTouchedListener listener){
        this.timetableContainers = timetableContainers;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public TimetableDetailViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timetable_card, parent, false);

        return new TimetableDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TimetableDetailViewHolder holder, final int i) {
        // Format and add new details
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        holder.timetableDateCreatedView.setText(context.getResources().getString(R.string.timetable_card_date,
                df.format(timetableContainers.get(i).getDateCreated())));
        String desc = timetableContainers.get(i).getDescription();
        holder.timetableDescriptionView.setText(!Objects.equals(desc, "") ? desc : "No description"); // android api stuffs. leave as !=
        holder.timetableNameView.setText(timetableContainers.get(i).getName());

        holder.itemView.setTag(i);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onCardClicked(holder, holder.getAdapterPosition());
            }
        });

        holder.dragHandleView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    listener.onCardDragHandleTouched(holder, holder.getAdapterPosition());
                }
                return false;
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return timetableContainers.size();
    }

    static class TimetableDetailViewHolder extends RecyclerView.ViewHolder {
        private TextView timetableNameView;
        private TextView timetableDateCreatedView;
        private TextView timetableDescriptionView;
        private View itemView;
        private ImageView dragHandleView;

        TimetableDetailViewHolder(final View itemView) {
            super(itemView);
            this.itemView = itemView;

            timetableNameView = (TextView) itemView.findViewById(R.id.timetable_card_name);
            timetableDateCreatedView = (TextView) itemView.findViewById(R.id.timetable_date_created);
            timetableDescriptionView = (TextView) itemView.findViewById(R.id.timetable_description);
            dragHandleView = (ImageView) itemView.findViewById(R.id.timetable_card_drag_handle);
        }
    }
}