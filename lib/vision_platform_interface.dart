import 'dart:typed_data';

import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'vision_method_channel.dart';

abstract class VisionPlatform extends PlatformInterface {
  /// Constructs a VisionPlatform.
  VisionPlatform() : super(token: _token);

  static final Object _token = Object();

  static VisionPlatform _instance = MethodChannelVision();

  /// The default instance of [VisionPlatform] to use.
  ///
  /// Defaults to [MethodChannelVision].
  static VisionPlatform get instance => _instance;
  
  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [VisionPlatform] when
  /// they register themselves.
  static set instance(VisionPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }

  Future<bool?> initial() {
    throw UnimplementedError('initial() has not been implemented.');
  }

  Future<String> runModelOnByteArray(Uint8List uint8list, double confidence)  {
    throw UnimplementedError('runModelOnByteArray() has not been implemented.');
  }
}
