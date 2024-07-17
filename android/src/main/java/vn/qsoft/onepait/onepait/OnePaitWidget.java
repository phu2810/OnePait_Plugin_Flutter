package vn.qsoft.onepait.onepait;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.RemoteViews;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class OnePaitWidgetData {
    private Date date;
    private Bitmap image;
    private boolean isSuccess;

    // Constructor
    public OnePaitWidgetData(Date date, Bitmap image, boolean isSuccess) {
        this.date = date;
        this.image = image;
        this.isSuccess = isSuccess;
    }

    // Getters
    public Date getDate() {
        return date;
    }

    public Bitmap getImage() {
        return image;
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}

public class OnePaitWidget extends BaseAppWidget<OnePaitWidgetData> {
    private static final String DOG_API_URL = "https://dog.ceo/api/breeds/image/random";
    private static final String CACHE_FILE_NAME = "doggy.png";

    @Override
    public OnePaitWidgetData onPreUpdate(int widgetId, RemoteViews views) {
        views.setTextViewText(R.id.tvWidget, "fetching data...");
        return new OnePaitWidgetData(new Date(), null, true);
    }

    @Override
    public OnePaitWidgetData runUpdateTaskInBackground(Context context, int widgetId, RemoteViews views, OnePaitWidgetData data) {
        try {
            // Fetch JSON data
            URL url = new URL(DOG_API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            int read;
            byte[] buffer = new byte[1024];
            while ((read = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, read);
            }

            String jsonResponse = byteArrayOutputStream.toString();
            inputStream.close();
            byteArrayOutputStream.close();

            JSONObject jsonObject = new JSONObject(jsonResponse);
            String imageUrl = jsonObject.getString("message");

            // Download image from URL
            URL imageURL = new URL(imageUrl);
            connection = (HttpURLConnection) imageURL.openConnection();
            connection.connect();
            inputStream = connection.getInputStream();
            byteArrayOutputStream = new ByteArrayOutputStream();

            while ((read = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, read);
            }

            byte[] imageData = byteArrayOutputStream.toByteArray();
            inputStream.close();
            byteArrayOutputStream.close();

            Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);

            // Cache the image
            cacheImage(context, bitmap);

            return new OnePaitWidgetData(new Date(), bitmap, true);
        } catch (Exception e) {
            e.printStackTrace();
            return new OnePaitWidgetData(new Date(), null, false);
        }
    }

    private void cacheImage(Context context, Bitmap bitmap) {
        try {
            FileOutputStream fos = context.openFileOutput(CACHE_FILE_NAME, Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPostUpdate(int widgetId, RemoteViews views, OnePaitWidgetData data) {
        if (data.isSuccess()) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            String currentTime = sdf.format(data.getDate());
            views.setTextViewText(R.id.tvWidget, currentTime);
            views.setImageViewBitmap(R.id.imageView, data.getImage());
        } else {
            views.setTextViewText(R.id.tvWidget, "Error fetching data");
        }
    }

    @Override
    public int getWidgetLayoutId() {
        return R.layout.onepait_widget;
    }
}

