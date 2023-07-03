package com.magnifiers.loupe.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.tbruyelle.rxpermissions3.RxPermissions

abstract class BaseViewModelActivity<VM:ViewModel,VB: ViewBinding> : AppCompatActivity() {

    lateinit var viewModel:VM
    lateinit var mBinding:VB

    abstract fun getVMClass(): Class<VM>

    open val rxPermission by lazy {
        RxPermissions(this)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding=getViewBinding()
        setContentView(mBinding.root)
        initViewModel()
    }

     abstract fun getViewBinding():VB

    private fun initViewModel() {
        viewModel= ViewModelProvider(this)[getVMClass()]
    }



}