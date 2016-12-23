package tomdrever.timetable.android.controllers;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler;

import java.util.ArrayList;

import butterknife.BindView;
import tomdrever.timetable.R;
import tomdrever.timetable.android.controllers.base.BaseController;
import tomdrever.timetable.data.Day;
import tomdrever.timetable.data.Timetable;

public class EditTimetableController extends BaseController {

    private Timetable timetable;

    private ArrayList<Day> days;

    @BindView(R.id.edit_timetable_name) EditText nameEditText;
    @BindView(R.id.edit_timetable_description) EditText descriptionEditText;

    @BindView(R.id.name_input_layout) TextInputLayout nameTextInputLayout;
    @BindView(R.id.description_input_layout) TextInputLayout descriptionTextInputLayout;

    @BindView(R.id.edit_timetable_days) GridView daysGridView;

    private DayGridAdapter dayGridAdapter;

    public EditTimetableController() { }

    public EditTimetableController(int index) {
        // Is new timetable!
        timetable = new Timetable();
        timetable.setIndex(index);

        days = new ArrayList<>(timetable.getDays());
    }

    public EditTimetableController(Timetable timetable) {
        this.timetable = timetable;
        days = new ArrayList<>(timetable.getDays());
    }

    @Override
    public boolean handleBack() {
        // TODO - "Discard changes?"
        return super.handleBack();
    }

    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.controller_edit_timetable, container, false);
    }

    @Override
    protected void onViewBound(@NonNull View view) {
        super.onViewBound(view);

        setHasOptionsMenu(true);

        nameEditText.setText(timetable.getName() != null ? timetable.getName() : "");
        nameTextInputLayout.setHint(getActivity().getString(R.string.edit_timetable_name));

        descriptionEditText.setText(timetable.getDescription() != null ? timetable.getDescription() : "");
        descriptionTextInputLayout.setHint(getActivity().getString(R.string.edit_timetable_description));

        dayGridAdapter = new DayGridAdapter(getActivity());

        daysGridView.setAdapter(dayGridAdapter);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_simple_edit, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_save_edit) {
            // Save timetable
            // Check input
            String name = nameEditText.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(getActivity(), "The timetable needs a name!", Toast.LENGTH_SHORT).show();
            } else {
                String description = descriptionEditText.getText().toString().trim();

                // If this is an existing timetable, get rid of the old version before saving
                if (timetable.getName() != null) getFileManager().delete(timetable.getName());

                timetable.setName(name);
                timetable.setDescription(description);

                timetable.setDays(days);

                getFileManager().save(timetable);

                // Done - launch view of that timetable
                getRouter().pushController(RouterTransaction.with(new ViewTimetableController(timetable))
                        .popChangeHandler(new FadeChangeHandler())
                        .pushChangeHandler(new FadeChangeHandler()));
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected boolean showUpNavigation() {
        return true;
    }

    @Override
    protected String getTitle() {
        return timetable.getName() != null ? "Edit " + timetable.getName() : "Create Timetable";
    }

    private class DayGridAdapter extends BaseAdapter {

        private Context context;

        DayGridAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return days.size() + 1;
        }

        @Override
        public Day getItem(int i) {
            return days.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final View itemView;

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                itemView = inflater.inflate(R.layout.day_grid_item_view, null);
            } else {
                itemView = convertView;
            }

            ImageView imageView = (ImageView) itemView.findViewById(R.id.day_grid_image);
            TextView textView = (TextView) itemView.findViewById(R.id.day_grid_text);

            imageView.setImageResource(R.drawable.circle);

            if (position != days.size()) {
                imageView.setColorFilter(Color.rgb(50, 50, 255));
                itemView.setTag(position);

                textView.setText(String.valueOf(getItem(position).getName().charAt(0)) + position);

                // region OnClick
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Todo - launch EditDayController with day clicked
                    }
                });
                // endregion

                // region OnLongClick
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        // Todo - handle long click; being drag

                        ClipData.Item item = new ClipData.Item(String.valueOf(itemView.getTag()));

                        ClipData dragData = new ClipData(String.valueOf(itemView.getTag()), new String[] {ClipDescription.MIMETYPE_TEXT_PLAIN}, item);

                        View.DragShadowBuilder shadow = new DragShadowBuilder(itemView);

                        // Starts the drag

                        itemView.startDrag(dragData,  // the data to be dragged
                                shadow,  // the drag shadow builder
                                null,      // no need to use local data
                                0          // flags (not currently used, set to 0)
                        );

                        return true;
                    }
                });
                // endregion

                // region Dragging
                itemView.setOnDragListener(new View.OnDragListener() {
                    @Override
                    public boolean onDrag(View view, DragEvent event) {
                        final int action = event.getAction();

                        switch(action) {

                            case DragEvent.ACTION_DRAG_STARTED:

                                // Determines if this View can accept the dragged data
                                if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {

                                    // returns true to indicate that the View can accept the dragged data.
                                    return true;

                                }

                                // Returns false. During the current drag and drop operation, this View will
                                // not receive events again until ACTION_DRAG_ENDED is sent.
                                return false;

                            case DragEvent.ACTION_DRAG_ENTERED:

                                return true;

                            case DragEvent.ACTION_DRAG_LOCATION:

                                // Ignore the event
                                return true;

                            case DragEvent.ACTION_DRAG_EXITED:

                                return true;

                            case DragEvent.ACTION_DROP:

                                // Gets the item containing the dragged data
                                ClipData.Item item = event.getClipData().getItemAt(0);

                                // Gets the text data from the item.
                                int position = Integer.valueOf(item.getText().toString());

                                int targetPosition = (int) view.getTag();

                                if (position < targetPosition) {
                                    for (int i = position; i < targetPosition; i++) {
                                        swap(i, i + 1);
                                    }
                                } else {
                                    for (int i = position; i > targetPosition; i--) {
                                        swap(i, i - 1);
                                    }
                                }

                                synchronized (dayGridAdapter) {
                                    dayGridAdapter.notifyDataSetChanged();
                                }

                                // Invalidates the view to force a redraw
                                itemView.invalidate();

                                // Returns true. DragEvent.getResult() will return true.
                                return true;

                            case DragEvent.ACTION_DRAG_ENDED:

                                // Does a getResult(), and displays what happened.
                                if (event.getResult()) {
                                    Toast.makeText(getActivity(), "The drop was handled.", Toast.LENGTH_LONG);

                                } else {
                                    Toast.makeText(getActivity(), "The drop didn't work.", Toast.LENGTH_LONG);

                                }

                                // returns true; the value is ignored.
                                return true;
                        }

                        return false;
                    }
                });
                // endregion
            } else {
                imageView.setColorFilter(Color.rgb(255, 50, 50));
                textView.setText("+");

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        days.add(new Day("New"));

                        // Todo - launch EditDayController for new day
                    }
                });
            }

            return itemView;
        }
    }

    private void swap(int position1, int position2) {
        Day day1 = days.get(position1);
        // Update?

        Day day2 = days.get(position2);
        // Update?

        days.set(position1, day2);
        days.set(position2, day1);
    }
}
