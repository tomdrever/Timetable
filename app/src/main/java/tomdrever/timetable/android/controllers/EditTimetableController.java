package tomdrever.timetable.android.controllers;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import tomdrever.timetable.R;
import tomdrever.timetable.android.controllers.base.BaseController;
import tomdrever.timetable.android.views.ExpandableGridView;
import tomdrever.timetable.data.Day;
import tomdrever.timetable.data.Timetable;
import tomdrever.timetable.utils.DragShadowBuilder;
import tomdrever.timetable.utils.ViewUtils;

public class EditTimetableController extends BaseController implements View.OnDragListener {

    private Timetable timetable;

    @BindView(R.id.edit_timetable_name) EditText nameEditText;
    @BindView(R.id.edit_timetable_description) EditText descriptionEditText;

    @BindView(R.id.name_input_layout) TextInputLayout nameTextInputLayout;
    @BindView(R.id.description_input_layout) TextInputLayout descriptionTextInputLayout;

    @BindView(R.id.edit_timetable_days) ExpandableGridView daysGridView;

    private DayGridAdapter daysGridAdapter;

    private int viewDayPosition;

    public EditTimetableController() { }

    public EditTimetableController(int index) {
        timetable = new Timetable();
        timetable.setIndex(index);
    }

    public EditTimetableController(Timetable timetable) {
        this.timetable = timetable;
    }

    private void setupActionbar() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = inflater.inflate(R.layout.view_edit_day_actionbar_delete, null);
        v.setOnDragListener(this);

        getActionBar().setDisplayShowCustomEnabled(false);
        getActionBar().setCustomView(v);
    }

    @Override
    public boolean handleBack() {
        String name = nameEditText.getText().toString().trim();
        if (Objects.equals(name, "")) name = null;

        String description = descriptionEditText.getText().toString().trim();

        final Timetable newTimetable = newTimetable(name, description, daysGridAdapter.getItems());

        if (!newTimetable.equals(timetable)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle("Discard changes?");

            // region Buttons
            builder.setPositiveButton("Discard", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    getRouter().popCurrentController();
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            // endregion

            // Create the AlertDialog
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            if (newTimetable.isBlank()) {
                getRouter().pushController(RouterTransaction.with(new TimetableListController())
                        .popChangeHandler(new FadeChangeHandler())
                        .pushChangeHandler(new FadeChangeHandler()));

                return true;
            }

            ViewTimetableController controller = new ViewTimetableController(timetable, viewDayPosition);

            getRouter().pushController(RouterTransaction.with(controller)
                    .popChangeHandler(new FadeChangeHandler())
                    .pushChangeHandler(new FadeChangeHandler()));
        }

        return true;
    }

    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.controller_edit_timetable, container, false);
    }

    @Override
    protected void onViewBound(@NonNull View view) {
        super.onViewBound(view);

        setHasOptionsMenu(true);

        setupActionbar();

        nameEditText.setText(timetable.getName() != null ? timetable.getName() : "");
        nameTextInputLayout.setHint(getActivity().getString(R.string.edit_timetable_name));

        descriptionEditText.setText(timetable.getDescription() != null ? timetable.getDescription() : "");
        descriptionTextInputLayout.setHint(getActivity().getString(R.string.edit_timetable_description));

        daysGridAdapter = new DayGridAdapter(getActivity(), timetable.getDays());

        daysGridView.setAdapter(daysGridAdapter);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_simple_edit, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_save_edit) {
            String name = nameEditText.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(getActivity(), "The timetable needs a name!", Toast.LENGTH_SHORT).show();
                return true;
            }

            String description = descriptionEditText.getText().toString().trim();

            // If this is an existing timetable, get rid of the old version before saving
            if (timetable.getName() != null) getFileManager().delete(timetable.getName());

            ArrayList<Day> days = daysGridAdapter.getItems();

            getFileManager().save(newTimetable(name, description, days));

            // Done - launch view of that timetable
            getRouter().pushController(RouterTransaction.with(
                    new ViewTimetableController(newTimetable(name, description, days)))
                    .popChangeHandler(new FadeChangeHandler())
                    .pushChangeHandler(new FadeChangeHandler()));

            Toast.makeText(getActivity(), name + " saved!", Toast.LENGTH_SHORT).show();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSave(Bundle outState) {
        outState.putParcelable("timetable", timetable);
    }

    @Override
    protected void onRestore(Bundle inState) {
        timetable = inState.getParcelable("timetable");
    }

    private Timetable newTimetable(String name, String description, ArrayList<Day> days) {
        Timetable newTimetable = timetable.cloneItem();
        newTimetable.setName(name);
        newTimetable.setDescription(description);
        newTimetable.setDays(days);

        return newTimetable;
    }

    @Override
    protected boolean showUpNavigation() {
        return true;
    }

    @Override
    protected String getTitle() {
        return timetable.getName() != null ? "Edit " + timetable.getName() : "Create Timetable";
    }

    @Override
    public boolean onDrag(View view, DragEvent event) {
        final int action = event.getAction();

        switch(action) {

            case DragEvent.ACTION_DRAG_STARTED:
                return event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN);

            case DragEvent.ACTION_DRAG_ENTERED: return true;

            case DragEvent.ACTION_DRAG_LOCATION: return true;

            case DragEvent.ACTION_DRAG_EXITED: return true;

            case DragEvent.ACTION_DROP:
                // Gets the timetable containing the dragged data
                ClipData.Item item = event.getClipData().getItemAt(0);

                final int position = Integer.valueOf(item.getText().toString());

                final Day day = daysGridAdapter.getItem(position);

                if (view.getId() == R.id.circle_item_base) {
                    final int targetPosition = (int) view.getTag();

                    // NOTE - no need to do anything if the day hasn't been moved
                    if (position == targetPosition) return true;

                    if (position < targetPosition) {
                        for (int i = position; i < targetPosition; i++) {
                            swap(i, i + 1);
                        }
                    } else {
                        for (int i = position; i > targetPosition; i--) {
                            swap(i, i - 1);
                        }
                    }

                    daysGridAdapter.notifyDataSetChanged();

                    Snackbar.make(view,
                            day.getName() + " moved from " + (position + 1) + " to " + (targetPosition + 1),
                            Snackbar.LENGTH_SHORT).setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (targetPosition < position) {
                                for (int i = targetPosition; i < position; i++) {
                                    swap(i, i + 1);
                                }
                            } else {
                                for (int i = targetPosition; i > position; i--) {
                                    swap(i, i - 1);
                                }
                            }

                            daysGridAdapter.notifyDataSetChanged();
                        }
                    }).show();

                } else if (view.getId() == R.id.delete_day) {
                    daysGridAdapter.remove(position);
                    daysGridAdapter.notifyDataSetChanged();

                    Snackbar.make(view, day.getName() + " deleted", Snackbar.LENGTH_SHORT).setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            daysGridAdapter.add(position, day);
                            daysGridAdapter.notifyDataSetChanged();
                        }
                    }).show();
                }

                return true;

            case DragEvent.ACTION_DRAG_ENDED:
                showDeleteDayIcon(false);
                return true;
        }

        return false;
    }

    private class DayGridAdapter extends BaseAdapter {

        private Context context;
        private ArrayList<Day> items;

        DayGridAdapter(Context context, ArrayList<Day> days) {
            this.context = context;
            this.items = days;
        }

        @Override
        public int getCount() {
            return items.size() + 1;
        }

        @Override
        public Day getItem(int i) {
            return items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final View itemView;

            if (position != items.size()) {
                String text = String.valueOf(getItem(position).getName().toUpperCase().charAt(0));

                int colour = getItem(position).getColour();

                itemView = ViewUtils.createCircleView(inflater, text, colour);

                // NOTE - for all items bar the last, which is the "add new" button
                itemView.setTag(position);

                // region OnClick
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getRouter().pushController(RouterTransaction.with(new EditDayController(daysGridAdapter.getItem(position)))
                                .popChangeHandler(new FadeChangeHandler())
                                .pushChangeHandler(new FadeChangeHandler()));
                    }
                });
                // endregion

                // region OnLongClick
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        // hide toolbar and title
                        showDeleteDayIcon(true);

                        ClipData.Item item = new ClipData.Item(String.valueOf(itemView.getTag()));

                        ClipData dragData = new ClipData(String.valueOf(itemView.getTag()),
                                new String[] { ClipDescription.MIMETYPE_TEXT_PLAIN }, item);

                        View.DragShadowBuilder shadowBuilder = new DragShadowBuilder(itemView);

                        // Starts the drag
                        itemView.startDrag(dragData,  // the data to be dragged
                                shadowBuilder,  // the drag shadow builder
                                null,      // no need to use local data
                                0          // flags (not currently used, set to 0)
                        );

                        return true;
                    }
                });
                // endregion

                itemView.setOnDragListener(EditTimetableController.this);
            } else {
                // NOTE - for the "add new" button
                itemView = ViewUtils.createCircleView(inflater, "+",
                        ContextCompat.getColor(context, R.color.red));

                // The plus for the "add new" button should be slightly larger than the text in other buttons
                TextView textView = (TextView) itemView.findViewById(R.id.circle_item_text);
                textView.setTextSize(28);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Day newDay = new Day();

                        EditDayController editDayController = new EditDayController(newDay);
                        editDayController.setEditingFinishedListener(new EditingFinishedListener() {
                            @Override
                            public void onEditingFinished() {
                                items.add(newDay);
                            }
                        });

                        getRouter().pushController(RouterTransaction.with(editDayController)
                                .popChangeHandler(new FadeChangeHandler())
                                .pushChangeHandler(new FadeChangeHandler()));
                    }
                });
            }

            return itemView;
        }

        public ArrayList<Day> getItems() {
            return items;
        }

        public void remove(int position) {
            items.remove(position);
            notifyDataSetChanged();
        }

        public void add(int position, Day day) {
            items.add(position, day);
            notifyDataSetChanged();
        }

        public void set(int position, Day day) {
            items.set(position, day);
            notifyDataSetChanged();
        }
    }

    private void swap(int position1, int position2) {
        Day day1 = daysGridAdapter.getItem(position1);
        Day day2 = daysGridAdapter.getItem(position2);

        daysGridAdapter.set(position1, day2);
        daysGridAdapter.set(position2, day1);
    }

    private void showDeleteDayIcon(boolean showDelete) {
        getActionBar().setDisplayHomeAsUpEnabled(!showDelete);
        getActionBar().setDisplayShowTitleEnabled(!showDelete);

        setOptionsMenuHidden(showDelete);
        getActionBar().setDisplayShowCustomEnabled(showDelete);
    }
}
