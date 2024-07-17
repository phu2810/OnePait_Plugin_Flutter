package vn.qsoft.onepait.onepait;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class WidgetUpdateScheduler {

    public static final int WIDGET_REQUEST_CODE = 191001;

    private static int[] getActiveWidgetIds(Context context, Class widgetClass){
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] ids = appWidgetManager.getAppWidgetIds(new ComponentName(context, widgetClass));
        return ids;
    }

    public static void scheduleWidgetUpdate(Context context, Class widgetClass) {
        if(getActiveWidgetIds(context, widgetClass)!=null && getActiveWidgetIds(context, widgetClass).length>0) {
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            PendingIntent pi = getWidgetAlarmIntent(context, widgetClass);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());

            am.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), (10*60*1000), pi);
        }
    }

    private static PendingIntent getWidgetAlarmIntent(Context context, Class widgetClass) {
        Intent intent = new Intent(context, widgetClass)
                .setAction(BaseAppWidget.ACTION_AUTO_UPDATE)
                .putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,getActiveWidgetIds(context, widgetClass));
        PendingIntent pi = PendingIntent.getBroadcast(context, WIDGET_REQUEST_CODE, intent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        return pi;
    }

    public static void clearWidgetUpdate(Context context, Class widgetClass) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(getWidgetAlarmIntent(context, widgetClass));
    }

    public static void updateWidgetNow(Context context, Class widgetClass) {
        Intent intent = new Intent(context, widgetClass)
                .setAction(BaseAppWidget.ACTION_AUTO_UPDATE)
                .putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,getActiveWidgetIds(context, widgetClass));
        context.sendBroadcast(intent);
    }
}