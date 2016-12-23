package tomdrever.timetable.android.controllers;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import tomdrever.timetable.data.Timetable;

public class EditTimetableController extends BaseController {

    private Timetable timetable;

    @BindView(R.id.edit_timetable_name) EditText nameEditText;
    @BindView(R.id.edit_timetable_description) EditText descriptionEditText;

    @BindView(R.id.name_input_layout) TextInputLayout nameTextInputLayout;
    @BindView(R.id.description_input_layout) TextInputLayout descriptionTextInputLayout;

    @BindView(R.id.edit_timetable_days) GridView daysGridView;

    public EditTimetableController() { }

    public EditTimetableController(int index) {
        // Is new timetable!
        timetable = new Timetable();
        timetable.setIndex(index);
    }

    public EditTimetableController(Timetable timetable) {
        this.timetable = timetable;
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

        daysGridView.setAdapter(new DayGridAdapter(getApplicationContext()));

        daysGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_edit_timetable, menu);
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

    class DayGridAdapter extends BaseAdapter {

        private Context context;

        private ArrayList<Integer> imageViewIds;
        private ArrayList<Integer> textViewIds;

        public DayGridAdapter(Context context) {
            this.context = context;

            imageViewIds = new ArrayList<>();
            textViewIds = new ArrayList<>();
        }

        @Override
        public int getCount() {
            return timetable.getDays().size()+ 1;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View itemView;

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                itemView = inflater.inflate(R.layout.day_grid_item_view, null);
            } else {
                itemView = convertView;
            }

            ImageView imageView = (ImageView) itemView.findViewById(R.id.day_grid_image);
            TextView textView = (TextView) itemView.findViewById(R.id.day_grid_text);

            imageView.setImageResource(R.drawable.circle);

            if (position != timetable.getDays().size()) {
                imageView.setColorFilter(Color.rgb(50, 50, 255));
                textView.setText(String.valueOf(timetable.getDays().get(position).getName().charAt(0)));
            } else {
                imageView.setColorFilter(Color.rgb(255, 50, 50));
                textView.setText("+");
            }

            return itemView;
        }
    }
}
