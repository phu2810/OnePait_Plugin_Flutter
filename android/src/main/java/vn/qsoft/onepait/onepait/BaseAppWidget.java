package vn.qsoft.onepait.onepait;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public abstract class BaseAppWidget<T> extends AppWidgetProvider implements UpdateWidgetListener<T> {

    private UpdateWidgetListener<T> updateWidgetListener = this;
    public static final String ACTION_AUTO_UPDATE =
            "com.example.androidwidget.AUTO_UPDATE";

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d("+++", "onReceive: " + intent.getAction());
        if(intent!=null && intent.getAction()!=null &&
                intent.getAction().equals(ACTION_AUTO_UPDATE)){
            onUpdate(context);
        }
    }

    private void onUpdate(Context context) {
        AppWidgetManager appWidgetManager =
                AppWidgetManager.getInstance(context.getApplicationContext());
        ComponentName thisAppWidgetComponentName = getComponentName(context.getApplicationContext());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidgetComponentName);
        Log.d("+++", "onUpdate222: " + appWidgetIds.length + " " + thisAppWidgetComponentName);
        onUpdate(context, appWidgetManager, appWidgetIds);
    }
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int i = 0; i < appWidgetIds.length; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        WidgetUpdateScheduler.scheduleWidgetUpdate(context, getClass());
    }
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        WidgetUpdateScheduler.clearWidgetUpdate(context, getClass());
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        WidgetUpdateScheduler.clearWidgetUpdate(context, getClass());
    }

    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), updateWidgetListener.getWidgetLayoutId());
        T data = updateWidgetListener.onPreUpdate(appWidgetId, views);
        appWidgetManager.updateAppWidget(appWidgetId, views);
        new UpdateWidgetTask<T>(context, appWidgetId, views, data, updateWidgetListener).execute();
    }
    public ComponentName getComponentName(Context context) {
        return new ComponentName(context, getClass());
    }
}

