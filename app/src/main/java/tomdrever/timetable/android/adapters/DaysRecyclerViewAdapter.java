package tomdrever.timetable.android.adapters;

/*
public class DaysRecyclerViewAdapter extends RecyclerView.Adapter<DaysRecyclerViewAdapter.DayViewHolder> {
    private ArrayList<Day> days;

    private final CardTouchedListener listener;

    private boolean showDragHandle;

    public DaysRecyclerViewAdapter(ArrayList<Day> days, boolean showDragHandle, CardTouchedListener listener) {
        this.days = days;
        this.listener = listener;
        this.showDragHandle = showDragHandle;
    }

    @Override
    public DayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_card, parent, false);

        return new DayViewHolder(view, showDragHandle);
    }

    @Override
    public void onBindViewHolder(final DayViewHolder holder, int position) {
        holder.dayNameView.setText(days.get(position).getName());

        holder.itemView.setTag(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onCardClicked(holder, holder.getAdapterPosition());
            }
        });

        holder.dragHandleView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) ==
                        MotionEvent.ACTION_DOWN) {
                    listener.onCardDragHandleTouched(holder, holder.getAdapterPosition());
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    static class DayViewHolder extends RecyclerView.ViewHolder{
	    private View itemView;

	    private TextView dayNameView;
        private ImageView dragHandleView;

        DayViewHolder(View itemView, boolean showDragHandle) {
            super(itemView);
            this.itemView = itemView;

            dayNameView = (TextView) itemView.findViewById(R.id.day_name_card_title);

            dragHandleView = (ImageView) itemView.findViewById(R.id.day_card_drag_handle);

            if (!showDragHandle) dragHandleView.setVisibility(View.GONE);
        }
    }
}*/