package tomdrever.timetable.android;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import tomdrever.timetable.R;
import tomdrever.timetable.android.listeners.CardTouchedListener;
import tomdrever.timetable.data.Period;

import java.util.ArrayList;

public class PeriodsRecyclerViewAdapter extends RecyclerView.Adapter<PeriodsRecyclerViewAdapter.PeriodViewHolder> {
    private ArrayList<Period> periods;

    private CardTouchedListener cardTouchedListener;

	public PeriodsRecyclerViewAdapter(ArrayList<Period> periods, CardTouchedListener cardTouchedListener) {
		this.periods = periods;
		this.cardTouchedListener = cardTouchedListener;
	}

	@Override
    public PeriodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
	    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.period_card, parent, false);

	    return new PeriodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PeriodViewHolder holder, int position) {
		holder.periodNameView.setText(periods.get(position).getName());

	    holder.itemView.setTag(position);

	    holder.itemView.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View view) {
			    cardTouchedListener.onCardClicked(holder, holder.getAdapterPosition());
		    }
	    });
    }

    @Override
    public int getItemCount() {
        return periods.size();
    }

    static class PeriodViewHolder extends RecyclerView.ViewHolder {
	    private View itemView;

	    private TextView periodNameView;

        PeriodViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;

	        this.periodNameView = (TextView) itemView.findViewById(R.id.period_name_card_title);
        }
    }
}
