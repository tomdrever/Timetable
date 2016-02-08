package tomdrever.timetable.edit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tomdrever.timetable.R;

public class EditDayFragment extends Fragment {

    public static final String ARG_PAGE = "page";

    private int mPageNumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_edit_day, container, false);

        ((TextView) rootView.findViewById(R.id.testtext)).setText("page" + String.format("%d", mPageNumber));

        return rootView;
    }

    public static EditDayFragment create(int dayNumber) {
        EditDayFragment fragment = new EditDayFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, dayNumber);
        fragment.setArguments(args);
        return fragment;
    }
}