package vn.qsoft.onepait.firebase;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import vn.qsoft.onepait.onepait.OnePaitWidget;
import vn.qsoft.onepait.onepait.WidgetUpdateScheduler;

public class FirebaseService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage message) {
        super.onMessageReceived(message);
        WidgetUpdateScheduler.updateWidgetNow(getApplicationContext(), OnePaitWidget.class);
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d("+++", "onNewToken: " + token);
    }
}
