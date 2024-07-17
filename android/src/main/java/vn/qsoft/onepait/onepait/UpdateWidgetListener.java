package vn.qsoft.onepait.onepait;

import android.content.Context;
import android.widget.RemoteViews;

public interface UpdateWidgetListener<T> {
    T onPreUpdate(int widgetId, RemoteViews views);
    T runUpdateTaskInBackground(Context context, int widgetId, RemoteViews views, T data);
    void onPostUpdate(int widgetId, RemoteViews views, T data);

    int getWidgetLayoutId();
}
