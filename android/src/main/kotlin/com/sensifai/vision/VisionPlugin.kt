package com.sensifai.vision

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.NonNull
import com.sensifai.vision.ml.Model
import com.sensifai.vision.Labels
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer


/** VisionPlugin */
class VisionPlugin : FlutterPlugin, MethodCallHandler {

    private lateinit var channel: MethodChannel
    private lateinit var context: Context
    private lateinit var flutterPluginBinding: FlutterPlugin.FlutterPluginBinding

    companion object {
        var model: Model? = null
    }

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        this.flutterPluginBinding = flutterPluginBinding
        this.context = flutterPluginBinding.applicationContext
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "vision")
        channel.setMethodCallHandler(this)
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {

    }


    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        when (call.method) {
            "getPlatformVersion" -> {
                result.success("Android ${android.os.Build.VERSION.RELEASE}")
            }
            "initial" -> {
                try {
                    model = Model.newInstance(context)
                    result.success(true)
                } catch (e: Exception) {
                    Log.e("SENSIFAI-Vision", e.message.toString())
                    result.success(false)
                }
            }
            "runOnBytesList" -> {
                Log.e("ERROR_TF", "1")
                val byteArrayImage: ByteArray = call.argument<ByteArray>("byteImage") ?: return
                Log.e("ERROR_TF", "2")
                val confidence: Double = call.argument<Double>("confidence") ?: return
                Log.e("ERROR_TF", "3")
                val inputFeature0 =
                    TensorBuffer.createFixedSize(intArrayOf(512, 512, 3), DataType.UINT8)
                Log.e("ERROR_TF", "4")
                val bitmap = BitmapFactory.decodeByteArray(byteArrayImage, 0, byteArrayImage.size)
                Log.e("ERROR_TF", "5")
                val image =
                    TensorImage.fromBitmap(Bitmap.createScaledBitmap(bitmap, 512, 512, true))
                Log.e("ERROR_TF", "5")
                inputFeature0.loadBuffer(image.buffer)
                Log.e("ERROR_TF", "6")

                if (model == null) {
                    result.error(
                        "-7",
                        "Model not initialed",
                        "before run model on image run await _vision.initial(); if response true can you work with this plugin perfectly"
                    )
                    return
                }
                Log.e("ERROR_TF", "7")
                val t = Thread {
                    Runnable {
                        Handler(Looper.getMainLooper())
                            .postDelayed({
                                val outputs = model?.process(inputFeature0)
                                if (outputs != null) {
                                    processOutput(outputs, confidence, result)
                                } else {
                                    result.success("[]")
                                }

                            }, 1000)
                    }.run()
                }
                processOutput(t)


            }
            "close" -> {
                try {
                    model?.close()
                    model = null
                    result.success(true)
                } catch (e: Exception) {
                    result.success(false)
                }
            }
            else -> {
                result.error(
                    "MissingPluginException",
                    "Unhandled Exception",
                    "No implementation found for method"
                )
            }
        }
    }

    private fun processOutput(thread: Thread) {
        thread.start()
    }

    private fun processOutput(outputs: Model.Outputs, confidence: Double, result: Result) {
        Log.e("ERROR_TF", "8")
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer
        val responses = outputFeature0.floatArray
        val x = ArrayList<Confidence>()
        val labels = Labels(context)
        for (i in responses.indices) {
            if (responses[i] > confidence.toFloat()) {
                val label = labels.getLabelByIndex(i)
                val description = labels.getDescriptionByLabel(label)
                x.add(Confidence(i, responses[i], label, description))
            }
        }

        x.sortWith { o1, o2 ->
            o2.rate.compareTo(o1.rate)
        }

        var response = "[\n"
        for (i in 0 until x.size) {
            response += "   {\n"
            response += "       \"index\": " + x[i].index + ",\n"
            response += "       \"confidence\": " + x[i].rate.toDouble() + ",\n"
            response += "       \"label\": \"" + x[i].label + "\",\n"
            response += "       \"description\": \"" + x[i].description + "\"\n"
            response += "   }"
            response += if ((x.size - 1) != i) {
                ",\n"
            } else {
                "\n"
            }
        }

        response += "]"

        if (x.isNotEmpty()) {
            result.success(response)
        } else {
            result.success("[]")
        }
    }
}
