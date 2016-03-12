package tomdrever.timetable.edit;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import tomdrever.timetable.R;
import tomdrever.timetable.databinding.ActivityEditTimetableBinding;
import tomdrever.timetable.structure.Day;
import tomdrever.timetable.structure.Period;
import tomdrever.timetable.structure.TimetableContainer;

public class EditTimetableActivity extends AppCompatActivity {

    private TimetableContainer timetableContainer; // TODO - make observable

    private ViewPager viewPager;
    private EditTimetablePagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get timetable
        Intent intent = getIntent();
        timetableContainer = new Gson().fromJson(intent.getStringExtra("timetabledetailsjson"), TimetableContainer.class);

        // Bind timetable to layout
        ActivityEditTimetableBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_timetable);
        binding.setTimetable(timetableContainer);

        // Setup layout
        Toolbar toolbar = (Toolbar)findViewById(R.id.edit_timetable_toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager)findViewById(R.id.edit_timetable_pager);
        viewPager.setOffscreenPageLimit(0);

        ArrayList<Fragment> frags = new ArrayList<>();
        for (Day day:timetableContainer.timetable.getDays()) {
            frags.add(EditDayFragment.newInstance(day));
        }

        pagerAdapter = new EditTimetablePagerAdapter(getSupportFragmentManager(), frags);
        viewPager.setAdapter(pagerAdapter);

        // Init fab
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.new_day_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get current day
                // add new day at position (current day positon) + 1
                Toast.makeText(getBaseContext(), String.format("New day at index %d, pls!", viewPager.getCurrentItem()), Toast.LENGTH_SHORT).show();

                // create new day
                Day newDay = new Day("Today");
                newDay.addPeriod(new Period("First"));
                // add to timetablecontainer
                timetableContainer.timetable.addDay(newDay);
                // add to viewpager
                int newFragIndex = pagerAdapter.addFragment(EditDayFragment.newInstance(newDay));
                pagerAdapter.notifyDataSetChanged();
                viewPager.setCurrentItem(newFragIndex, true);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_timetable, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit_timetable:
                Intent intent = new Intent();
                intent.putExtra("timetabledetailsjson", new Gson().toJson(timetableContainer));

                if (getIntent().getBooleanExtra("isnewtimetable", false)){ // is a new timetable
                    setResult(100, intent);
                }
                else { // not new, so editing
                    setResult(200, intent);
                }
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // TODO - separate into separate class, for use in ViewDay
    private class EditTimetablePagerAdapter extends FragmentStatePagerAdapter{
        private List<Fragment> fragments;
        public EditTimetablePagerAdapter(FragmentManager manager, List<Fragment> fragments){
            super(manager);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public int addFragment(Fragment fragment){
            return addFragment(fragment, fragments.size());
        }

        public int addFragment(Fragment fragment, int position){
            fragments.add(position, fragment);
            return position;
        }

        public int removeFragment(ViewPager pager, Fragment fragment)
        {
            return removeFragment(pager, fragments.indexOf(fragment));
        }

        public int removeFragment(ViewPager pager, int position)
        {
            pager.setAdapter(null);
            fragments.remove(position);
            pager.setAdapter(this);

            return position;
        }
        /*
        @Override
        public Object instantiateItem (ViewGroup container, int position)
        {
            View view = fragments.get(position).getView();
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            container.removeView(fragments.get(position).getView());
        }*/
    }
}
