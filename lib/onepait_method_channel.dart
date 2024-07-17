import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'onepait_platform_interface.dart';

/// An implementation of [OnepaitPlatform] that uses method channels.
class MethodChannelOnepait extends OnepaitPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('onepait');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
}
