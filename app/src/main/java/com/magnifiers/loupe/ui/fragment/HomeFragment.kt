package com.magnifiers.loupe.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Color
import android.view.ScaleGestureDetector
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.blankj.utilcode.util.LogUtils
import com.google.common.util.concurrent.ListenableFuture
import com.magnifiers.loupe.base.BaseFragment
import com.magnifiers.loupe.ui.activity.MainActivity
import myapplication.com.R
import myapplication.com.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    override fun initEvent() {

    }

    @SuppressLint("CheckResult")
    override fun initView() {
        LogUtils.d("主fragment")

        cameraProviderFuture = ProcessCameraProvider.getInstance(requireActivity())
        bindPreview(cameraProviderFuture.get())

    }

    private val viewModel by lazy {
        (activity as MainActivity).viewModel
    }

    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>

    //是否按下进度条
    private var isSeekDown = false
    private val preview: Preview by lazy {
        Preview.Builder()
            .build()
    }

    private val frontSelector: CameraSelector by lazy {
        CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
            .build()
    }

    private val backSelector: CameraSelector by lazy {
        CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()
    }

    private var lensFacing = CameraSelector.LENS_FACING_BACK
    private lateinit var camera: Camera

    private fun bindPreview(cameraProvider: ProcessCameraProvider) {


        val imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
            .build()

        preview.setSurfaceProvider(mBinding.PreviewView.surfaceProvider)

        cameraProvider.unbindAll()
        camera = cameraProvider.bindToLifecycle(
            this,
            backSelector,
            preview,
            imageCapture
        )

        camera.cameraInfo.zoomState.observe(this@HomeFragment, Observer { zoomState ->
            if (!isSeekDown) {
                val progress = (zoomState.zoomRatio / zoomState.maxZoomRatio) * 100
                LogUtils.d("当前进度$progress")
//                    mBinding.include.QMUISeekBar.currentProgress= progress.toInt()
            }

        })

        //手势缩放
        val scaleGestureDetector =
            ScaleGestureDetector(requireContext(), object : ScaleGestureDetector.OnScaleGestureListener {
                override fun onScale(detector: ScaleGestureDetector): Boolean {
                    LogUtils.d("手势缩放...")
                    val currentZoomRatio = camera.cameraInfo.zoomState.value?.zoomRatio ?: 1f
                    val delta = detector.scaleFactor
                    camera.cameraControl.setZoomRatio(currentZoomRatio * delta)
                    return true
                }

                override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
                    return true
                }

                override fun onScaleEnd(detector: ScaleGestureDetector) {
                }

            })

        mBinding.apply {
            settingBtn.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_settingFragment)
            }
            //翻转相机
            turnBtn.setOnClickListener {
                cameraProvider.unbindAll()
                if (lensFacing== CameraSelector.LENS_FACING_FRONT){
                    LogUtils.d("LENS_FACING_FRONT")
                    camera=cameraProvider.bindToLifecycle(
                        this@HomeFragment,
                        backSelector,
                        preview,
                        imageCapture
                    )
                    lensFacing= CameraSelector.LENS_FACING_BACK
                }else{
                    LogUtils.d("LENS_FACING_BACK")
                    camera=cameraProvider.bindToLifecycle(
                        this@HomeFragment,
                        frontSelector,
                        preview,
                        imageCapture
                    )
                    lensFacing= CameraSelector.LENS_FACING_FRONT
                }

            }
            //点击相册
            albumBtn.setOnClickListener {
                LogUtils.d("点击相册...")
                viewModel.openAlbum()
            }

            //拍照
            photoBtn.setOnClickListener {
                viewModel.takePhoto(imageCapture)

            }
            //手电筒
            flashLightBtn.setOnClickListener {
                viewModel.toggleLight(camera)
            }
            //画面
            PreviewView.apply {
                previewStreamState.observe(this@HomeFragment, Observer {
                    if (it == androidx.camera.view.PreviewView.StreamState.STREAMING) {
                        setOnTouchListener { v, event ->
                            scaleGestureDetector.onTouchEvent(event)
                        }
                    }
                })
            }
        }

        //进度条
//        mBinding.include.QMUISeekBar.apply {
//            //
//            setClickToChangeProgress(true)
//            //回调
//            setCallback(object : QMUISlider.Callback {
//                override fun onProgressChange(
//                    slider: QMUISlider?,
//                    progress: Int,
//                    tickCount: Int,
//                    fromUser: Boolean
//                ) {
//                    if (isSeekDown) {
//                        viewModel.setProgress(camera, progress)
//                    }
//                }
//
//                override fun onTouchDown(
//                    slider: QMUISlider?,
//                    progress: Int,
//                    tickCount: Int,
//                    hitThumb: Boolean
//                ) {
//                    isSeekDown = true
//                }
//
//                override fun onTouchUp(slider: QMUISlider?, progress: Int, tickCount: Int) {
//                    isSeekDown = false
//                }
//
//                override fun onStartMoving(slider: QMUISlider?, progress: Int, tickCount: Int) {
//                }
//
//                override fun onStopMoving(slider: QMUISlider?, progress: Int, tickCount: Int) {
//                }
//
//            })
//        }

    }

    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)
}