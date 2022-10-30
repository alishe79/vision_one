# Vision

![Pub Version](https://img.shields.io/pub/v/vision) ![LICENSE](https://img.shields.io/badge/license-MIT%20License-green)

#### Offline image labeling

- +5000 Labels
- Offline
- Pre-train AI Model
- Support PNG, JPEG, JPG and GIF
- Powered by [TFLite](https://www.tensorflow.org/lite "TFLite")

#### Why was this plugin written?
**The strength of this plugin is tagging photos on the device without communicating with anything outside the user's device. Well, the conclusion is that the most important issue for all of us is privacy. Initially, this plugin started with this vision, but it focuses on three main themes. Speed, accuracy and privacy security**

#### What you need ?
**In this example you need add [Imager picker](https://pub.dev/packages/image_picker "Image picker") plugin**


#### How to use Vision?
**Simply**

```dart
// image_picker plugin
final ImagePicker picker = ImagePicker();
// Initial vision
var initialed = await Vision.initial();
// If successfully initialed, then you can use image labeling codes
if (initialed){
  // Run Image labeling code's
}
```

**Image labeling code's**

```dart
String jsonLabels = await Vision.runModelOnByteArray(uint8list, /** you can change confidence (Important) **/ 0.3);
```

here maybe you need convert bytes to uint8list for e.g. code

```dart
// also you can get bytes from file image by using
var bytes = await _fileImage.readAsBytes();
var uint8list = Uint8List.fromList(bytes.toList())
```

Then run Image labeling code ...

**Vision.runModelOnByteArray** is return json string items when confidence > 0.3, e.g.

```json
[
  {
    "index": 567,
    "confidence": 0.7956880331039429,
    "label": "/m/01gq53",
    "description": "Performance"
  },
  {
    "index": 2629,
    "confidence": 0.7695709466934204,
    "label": "/m/04_5hy",
    "description": "Stage"
  },
  {
    "index": 3131,
    "confidence": 0.6444675326347351,
    "label": "/m/05qjc",
    "description": "Performing arts"
  },
  {
    "index": 1613,
    "confidence": 0.583440899848938,
    "label": "/m/02jjt",
    "description": "Entertainment"
  },
  {
    "index": 643,
    "confidence": 0.5553570985794067,
    "label": "/m/01jddz",
    "description": "Concert"
  },
  {
    "index": 3361,
    "confidence": 0.49405911564826965,
    "label": "/m/06mg_j",
    "description": "Rock concert"
  },
  {
    "index": 2990,
    "confidence": 0.32111555337905884,
    "label": "/m/0557q",
    "description": "Musical theatre"
  }
]
```


you can convert it to 

```dart 
List<Map<String, dynamic>>
```
by usin below code

```dart
var labels = List<Map<String, dynamic>>.from(json.decode(jsonLabels));
```


#### Are you confused ?

**See here **

add this line of import to first of dart file

```dart
import 'dart:convert';
import 'dart:typed_data';
import 'dart:async';
import 'dart:developer' as dev;

import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';

import 'package:flutter/services.dart';
import 'package:image_picker/image_picker.dart';
import 'package:vision/vision.dart';
```

now, use below code, just this.

```dart
final ImagePicker picker = ImagePicker();
try {
  var initialed = await Vision.initial();
  if(initialed){
    final XFile? image = await picker.pickImage(source: ImageSource.gallery);
    if (image != null){
      var bytes = await image.readAsBytes();
      String jsonLabels = await Vision.runModelOnByteArray(Uint8List.fromList(bytes.toList()), 0.3);
      var labels = List<Map<String, dynamic>>.from(json.decode(jsonLabels));
      if (kDebugMode){
        dev.log(labels.toString());
      }
    }
  }
} on PlatformException {
  if (kDebugMode){
    print('Failed to initial');
  }
}
```

### Enjoy :)

##### Created with ❤️🍰☕ at [Sensifai](https://sensifai.com "Sensifai")
