package tomdrever.timetable.utils;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.annotation.AnimRes;
import android.support.annotation.ColorInt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import tomdrever.timetable.R;
import tomdrever.timetable.android.fragments.OnSeekBarChangeListener;

public class ViewUtils {
    public static View createCircleView(LayoutInflater inflater, String text, int colour) {
        View view = inflater.inflate(R.layout.view_circle_item, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.circle_item_image);
        imageView.setImageResource(R.drawable.circle);
        imageView.setColorFilter(colour);

        TextView textView = (TextView) view.findViewById(R.id.circle_item_text);
        textView.setTextSize(22);
        textView.setText(text);

        return view;
    }

    public static void setUpSeekBar(SeekBar seekBar, int max, int progress, @ColorInt int colour,
                                    OnSeekBarChangeListener listener) {
        seekBar.setMax(max);
        seekBar.setProgress(progress);
        seekBar.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(
                colour, PorterDuff.Mode.SRC_IN));
        seekBar.getThumb().setColorFilter(new PorterDuffColorFilter(
                colour, PorterDuff.Mode.SRC_IN));
        seekBar.setOnSeekBarChangeListener(listener);
    }

    public static void scale(View view, float scaleFactor) {
        view.setScaleX(scaleFactor);
        view.setScaleY(scaleFactor);
    }

    public static void startAnimation(View view, @AnimRes int animationId, Context context){
        Animation animation = AnimationUtils.loadAnimation(context, animationId);
        animation.setDuration(250);
        view.startAnimation(animation);
    }
}
