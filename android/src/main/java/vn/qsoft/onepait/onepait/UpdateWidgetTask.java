package vn.qsoft.onepait.onepait;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.RemoteViews;

public class UpdateWidgetTask<T> extends AsyncTask<Void, Void, T> {

    private Context context;
    private int appWidgetId;
    private RemoteViews views;
    private UpdateWidgetListener<T> listener;
    private T data;
    public UpdateWidgetTask(Context context, int appWidgetId, RemoteViews views, T data, UpdateWidgetListener<T> listener) {
        this.listener = listener;
        this.context = context;
        this.data = data;
        this.appWidgetId = appWidgetId;
        this.views = views;
    }
    @Override
    protected T doInBackground(Void... voids) {
        return listener.runUpdateTaskInBackground(context, appWidgetId, views, data);
    }

    @Override
    protected void onPostExecute(T data) {
        super.onPostExecute(data);
        listener.onPostUpdate(appWidgetId, views, data);
        AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId, views);
    }
}