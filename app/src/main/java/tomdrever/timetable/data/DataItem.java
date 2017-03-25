package tomdrever.timetable.data;

import android.os.Parcel;
import android.os.Parcelable;

public abstract class DataItem<T> implements Parcelable {
    public abstract T cloneItem();

    // 99.9% of the time you can just ignore this
    @Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    @Override
    public abstract void writeToParcel(Parcel out, int flags);
}
