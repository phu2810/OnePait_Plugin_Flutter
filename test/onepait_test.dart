import 'package:flutter_test/flutter_test.dart';
import 'package:onepait/onepait.dart';
import 'package:onepait/onepait_platform_interface.dart';
import 'package:onepait/onepait_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockOnepaitPlatform
    with MockPlatformInterfaceMixin
    implements OnepaitPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final OnepaitPlatform initialPlatform = OnepaitPlatform.instance;

  test('$MethodChannelOnepait is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelOnepait>());
  });

  test('getPlatformVersion', () async {
    Onepait onepaitPlugin = Onepait();
    MockOnepaitPlatform fakePlatform = MockOnepaitPlatform();
    OnepaitPlatform.instance = fakePlatform;

    expect(await onepaitPlugin.getPlatformVersion(), '42');
  });
}
