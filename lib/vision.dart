import 'dart:async';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'dart:typed_data';

class Vision {
  static const _channel = MethodChannel('vision');
  Future<String?> getPlatformVersion() {
    return Future.value("ANDROID TEST");
  }

  static Future<bool> initial() async {
    final init = await _channel.invokeMethod<bool?>('initial');
    if (init != null){
      return init ;
    } else {
      return false ;
    }
  }

  static Future<String> runModelOnByteArray(Uint8List uint8list, double confidence) async {
    final labels = await _channel.invokeMethod<String>('runOnBytesList', {
      "byteImage": uint8list,
      "confidence": confidence
    });
    return labels ?? "[]";
  }
}
