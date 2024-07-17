package vn.qsoft.onepait;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.google.firebase.messaging.FirebaseMessaging;
import io.flutter.embedding.android.FlutterActivity;

public class MainActivity extends FlutterActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseMessaging.getInstance().getToken()
                .addOnSuccessListener(token -> Log.d("+++", "onNewToken: " + token))
                .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Failed to get token", Toast.LENGTH_SHORT).show());

    }
}
