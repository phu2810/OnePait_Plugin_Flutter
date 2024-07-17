## Chuẩn bị
1. Cài Flutter
2. IDE: Xcode, Android Studio
3. Chạy `flutter doctor -v` để đảm bảo không có vấn đề gì
## Thêm plugin OnePait
1. Thêm dependence vào podspec.yaml

```
dependencies:
  ...
  onepait:
    git:
      url: https://github.com/phu2810/OnePait_Plugin_Flutter.git
      ref: main
``` 
2. Chạy lệnh cập nhật thư viện

```
flutter pub get
```
## Thêm widget vào Android
1. Mở một máy ảo android và chạy `flutter run` chờ chaỵ thành công
2. Tắt terminal và mở project bằng android studio (Đường dẫn: `project_root/android`)
3. Khai báo widget trong manifest
```
        <receiver android:name="vn.qsoft.onepait.onepait.OnePaitWidget"
            android:exported="true">
            <intent-filter>
                <action android:name="android.appwidget.action.APPWIDGET_UPDATE"/>
            </intent-filter>
            <intent-filter>
                <action android:name="com.example.androidwidget.AUTO_UPDATE" />
            </intent-filter>
            <meta-data
                android:name="android.appwidget.provider"
                android:resource="@xml/onepait_widget_info"/>
        </receiver>
```
4. App đã được thêm widget thành công, run app rồi taọ thử 1 widget mới tại màn hình home
5. Cập nhật app widget thông qua FirebaseMessagingService bằng cách gọi hàm `WidgetUpdateScheduler.updateWidgetNow(getApplicationContext(), OnePaitWidget.class);`. Ví dụ:
```
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
```
## Thêm widget vào iOS
1. Mở một máy ảo iOS và chạy `flutter run` chờ chaỵ thành công
2. Tắt terminal và mở project bằng XCode (Đường dẫn: `project_root/ios`)
3. Tạo Widget extension
Chọn project -> Targets -> New Target -> Widget Extension -> Đặt tên widget
4. Add pod lib cho WidgetExtension: Tìm file Podfile trong project ios và thêm đoạn sau:
```
target 'OnePaitWidgetExtension' do
  use_frameworks!
  use_modular_headers!
  pod 'iOSOnePaitWidget', '0.1.5'
end
```
Sau đó chạy `pod install` để cập nhật lib cho WidgetExtension

5. Tìm code widget extension trong WidgetExtension vừa taọ và sửa thành
```
import WidgetKit
import SwiftUI
import iOSOnePaitWidget

struct AppWidget: Widget {
    let kind: String = "OnePaitWidget"

    var body: some WidgetConfiguration {
        return OnePaitWidget.getWidgetConfiguration()
    }
}
```
==> Run project và tạo thử 1 widget tại màn hình Home

6. Cập nhật widget real time thông qua notification. Tạo NotificationExtension:
Chọn project -> Targets -> New Target -> NotificationExtension -> Đặt tên NotificationExtension

Mở file NotificationService.swift vừa được tạo ra và thêm hàm `WidgetCenter.shared.reloadAllTimelines()` trong hàm `didReceive(_ request: UNNotificationRequest, withContentHandler contentHandler: @escaping (UNNotificationContent) -> Void)`
Ví dụ:

```
import UserNotifications
import WidgetKit

class NotificationService: UNNotificationServiceExtension {

    var contentHandler: ((UNNotificationContent) -> Void)?
    var bestAttemptContent: UNMutableNotificationContent?

    override func didReceive(_ request: UNNotificationRequest, withContentHandler contentHandler: @escaping (UNNotificationContent) -> Void) {
        WidgetCenter.shared.reloadAllTimelines()
        self.contentHandler = contentHandler
        bestAttemptContent = (request.content.mutableCopy() as? UNMutableNotificationContent)
        
        if let bestAttemptContent = bestAttemptContent {
            // Modify the notification content here...
            bestAttemptContent.title = "\(bestAttemptContent.title) [modified]"
            
            contentHandler(bestAttemptContent)
        }
    }
    
    override func serviceExtensionTimeWillExpire() {
        // Called just before the extension will be terminated by the system.
        // Use this as an opportunity to deliver your "best attempt" at modified content, otherwise the original push payload will be used.
        if let contentHandler = contentHandler, let bestAttemptContent =  bestAttemptContent {
            contentHandler(bestAttemptContent)
        }
    }

}

```