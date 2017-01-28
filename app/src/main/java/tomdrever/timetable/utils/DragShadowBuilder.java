package tomdrever.timetable.utils;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.View;

import tomdrever.timetable.R;

// Adapted from the android guide on drag and drop
public class DragShadowBuilder extends View.DragShadowBuilder {

    private static Drawable shadow;

    public DragShadowBuilder(View v) {
        super(v);

        // Creates a draggable image that will fill the Canvas provided by the system.
        shadow = v.getContext().getResources().getDrawable(R.drawable.circle, null);
        shadow.setTint(Color.LTGRAY);
    }

    @Override
    public void onProvideShadowMetrics (Point size, Point touch) {

        int width, height;

        width = getView().getHeight();
        height = getView().getHeight();

        // The drag shadow is a ColorDrawable. This sets its dimensions to be the same as the
        // Canvas that the system will provide. As a result, the drag shadow will fill the
        // Canvas.
        shadow.setBounds(0, 0, width, height);

        // Sets the size parameter's width and height values. These get back to the system
        // through the size parameter.
        size.set(width, height);

        // Sets the touch point's position to be in the middle of the drag shadow
        touch.set(width / 2, height / 2);
    }

    // Defines a callback that draws the drag shadow in a Canvas that the system constructs
    // from the dimensions passed in onProvideShadowMetrics().
    @Override
    public void onDrawShadow(Canvas canvas) {

        // Draws the ColorDrawable in the Canvas passed in from the system.
        shadow.draw(canvas);
    }
}