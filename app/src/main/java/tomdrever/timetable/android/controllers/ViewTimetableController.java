package tomdrever.timetable.android.controllers;

import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler;

import butterknife.BindView;
import tomdrever.timetable.R;
import tomdrever.timetable.android.controllers.base.BaseController;
import tomdrever.timetable.android.fragments.DayFragment;
import tomdrever.timetable.data.Timetable;

public class ViewTimetableController extends BaseController {

    private final int intialPosition;
    private Timetable timetable;

    @BindView(R.id.days_pager) ViewPager daysViewPager;
    @BindView(R.id.days_tablayout) TabLayout periodsTabLayout;

    private DaysPagerAdapter daysAdapter;

    public ViewTimetableController() {
        intialPosition = 0;
    }
    public ViewTimetableController(Timetable timetable) {
        this.timetable = timetable;
        intialPosition = 0;
    }
    public ViewTimetableController(Timetable timetable, int position) {
        this.timetable = timetable;
        intialPosition = position;
    }

    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.controller_view_timetable, container, false);
    }

    @Override
    protected void onViewBound(@NonNull View view) {
        super.onViewBound(view);

        setHasOptionsMenu(true);

        getArgs();

        daysAdapter = new DaysPagerAdapter(getAppCombatActivity().getSupportFragmentManager());
        daysViewPager.setAdapter(daysAdapter);
        daysViewPager.setCurrentItem(intialPosition);

        periodsTabLayout.setupWithViewPager(daysViewPager);
        periodsTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_view_timetable, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_edit_view) {
            getRouter().pushController(RouterTransaction.with(new EditTimetableController(timetable, daysViewPager.getCurrentItem()))
                    .popChangeHandler(new FadeChangeHandler())
                    .pushChangeHandler(new FadeChangeHandler()));
            // TODO - maybe this should update its timetable as a onControllerFinished
        }

        return true;
    }

    @Override
    protected String getTitle() {
        return timetable.getName();
    }

    @Override
    protected String getSubtitle() {
        return timetable.getDescription();
    }

    @Override
    public boolean handleBack() {
        getRouter().pushController(RouterTransaction.with(new TimetableListController())
                .popChangeHandler(new FadeChangeHandler())
                .pushChangeHandler(new FadeChangeHandler()));

        return true;
    }

    @Override
    protected boolean showUpNavigation() {
        return true;
    }

    private class DaysPagerAdapter extends FragmentStatePagerAdapter {

        public DaysPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return DayFragment.newDayFragment(timetable.getDays().get(position));
        }

        @Override
        public int getCount() {
            return timetable.getDays().size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return timetable.getDays().get(position).getName();
        }
    }
}
