package com.magnifiers.loupe.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.ScaleGestureDetector
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.common.util.concurrent.ListenableFuture
import com.magnifiers.loupe.base.BaseViewModelActivity
import com.magnifiers.loupe.model.MainModel
import myapplication.com.databinding.ActivityMainBinding

class MainActivity : BaseViewModelActivity<MainModel, ActivityMainBinding>() {



    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rxPermission
            .requestEach(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
            )
            .subscribe { granted ->
                if (granted.granted) {
                    LogUtils.i("已获取相机权限")

                } else {
                    LogUtils.e("没有获取相机权限")
                }
            }

    }




    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun getVMClass(): Class<MainModel> = MainModel::class.java

}