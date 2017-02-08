package tomdrever.timetable.utils;

import android.content.Context;
import android.support.annotation.AnimRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import tomdrever.timetable.R;

public class ViewUtils {
    public static View createCircleView(LayoutInflater inflater, String text, int color) {
        View view = inflater.inflate(R.layout.circle_item_view, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.circle_item_image);
        imageView.setImageResource(R.drawable.circle);
        imageView.setColorFilter(color);

        TextView textView = (TextView) view.findViewById(R.id.circle_item_text);
        textView.setTextSize(22);
        textView.setText(text);

        return view;
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