
import 'onepait_platform_interface.dart';

class Onepait {
  Future<String?> getPlatformVersion() {
    return OnepaitPlatform.instance.getPlatformVersion();
  }
}
