package tomdrever.timetable.android.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import tomdrever.timetable.R;
import tomdrever.timetable.data.Period;

public class PeriodBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private Period period;
    private Unbinder unbinder;

    @BindView(R.id.bottom_sheet_period_name) TextView nameTextView;
    @BindView(R.id.bottom_sheet_period_name_layout) View nameBackground;

    public static PeriodBottomSheetDialogFragment newPeriodBottomSheetDialogFragment(Period period) {
        PeriodBottomSheetDialogFragment fragment = new PeriodBottomSheetDialogFragment();
        fragment.period = period;
        return fragment;
    }

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int state) {
            if (state == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) { }
    };

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        View view = View.inflate(getContext(), R.layout.fragment_dialog_bottom_sheet_period, null);
        dialog.setContentView(view);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) view.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if(behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
            ((BottomSheetBehavior) behavior).setPeekHeight(300);
        }

        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onStart() {
        super.onStart();

        nameTextView.setText(period.getName());
        nameBackground.setBackgroundColor(period.getColour());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("period", period);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (period == null) period = savedInstanceState.getParcelable("period");

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        unbinder = null;
    }
}
