import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'onepait_method_channel.dart';

abstract class OnepaitPlatform extends PlatformInterface {
  /// Constructs a OnepaitPlatform.
  OnepaitPlatform() : super(token: _token);

  static final Object _token = Object();

  static OnepaitPlatform _instance = MethodChannelOnepait();

  /// The default instance of [OnepaitPlatform] to use.
  ///
  /// Defaults to [MethodChannelOnepait].
  static OnepaitPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [OnepaitPlatform] when
  /// they register themselves.
  static set instance(OnepaitPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
