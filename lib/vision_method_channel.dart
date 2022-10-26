import 'dart:convert';
import 'dart:typed_data';

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'vision_platform_interface.dart';

/// An implementation of [VisionPlatform] that uses method channels.
class MethodChannelVision extends VisionPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('vision');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  @override
  Future<bool?> initial() async {
    final initialIsSuccess = await methodChannel.invokeMethod<bool?>('initial');
    return initialIsSuccess;
  }

  @override
  Future<String> runModelOnByteArray(Uint8List uint8list, double confidence) async {
    final labels = await methodChannel.invokeMethod<String>('runOnBytesList', {
      "byteImage": uint8list,
      "confidence": confidence
    });
    return labels.toString();
  }
}
