package com.magnifiers.loupe.model

import android.app.Application
import android.content.ContentValues
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.camera.core.Camera
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.OutputFileOptions
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.TorchState
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import com.blankj.utilcode.util.LogUtils
import com.magnifiers.loupe.ui.activity.SaveActivity
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

class MainModel(
    private val context: Application
) : AndroidViewModel(context) {


    //打开摄像头
    fun openCamera() {

    }

    //打开手电筒
    fun toggleLight(camera: Camera) {
        camera.cameraControl.enableTorch(
            camera.cameraInfo.torchState.value!=TorchState.ON
        )
    }

    //伸缩摄像头
    fun setProgress(camera: Camera, progress: Int) {
        val zoom = progress / 100f
        camera.cameraControl.setLinearZoom(zoom)
        LogUtils.d("滑动改变了....$zoom")
    }


    //拍照
    fun takePhoto(imageCapture: ImageCapture) {
        val outputOptions: OutputFileOptions
        val photoFile: File
        // 创建文件名
        val filename = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US)
            .format(System.currentTimeMillis()) + ".jpg"

        //适配android10以下
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
            LogUtils.d("Android10以上拍照")
            // 创建一个存储照片的输出文件
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                put(MediaStore.MediaColumns.RELATIVE_PATH, "${Environment.DIRECTORY_PICTURES}/Magnifier")
            }

            val resolver = context.contentResolver
            val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            outputOptions = OutputFileOptions.Builder(
                resolver.openOutputStream(uri!!)!!
            ).build()
        }else{
            // 如果是Android 9及以下，使用传统方式保存图片
            val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            photoFile = File(imagesDir, filename)
            outputOptions = OutputFileOptions.Builder(photoFile).build()
        }

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    LogUtils.d("保存成功...")
                    val intent = Intent(context, SaveActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                }

                override fun onError(exception: ImageCaptureException) {
                    LogUtils.e("保存失败...")

                }

            }
        )
    }

    //打开相册
    fun openAlbum() {
        val intent = Intent()
        intent.action = Intent.ACTION_MAIN
        intent.addCategory(Intent.CATEGORY_APP_GALLERY)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
//        val intent = Intent(Intent.ACTION_VIEW)
//        intent.apply {
//            intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK
//            intent.type="image/*"
//        }

        context.startActivity(intent)
    }

}