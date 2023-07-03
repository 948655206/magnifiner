package com.magnifiers.loupe.ui.fragment

import android.graphics.Color
import com.blankj.utilcode.util.LogUtils
import com.magnifiers.loupe.base.BaseFragment
import myapplication.com.databinding.FragmentSettingBinding

class SettingFragment :BaseFragment<FragmentSettingBinding>(){
    override fun initEvent() {
        LogUtils.d("SettingFragment")
    }

    override fun initView() {
        mBinding.QMUITopBar.apply {
            setSubTitle("12341234")
            setBackgroundColor(Color.WHITE)
        }
    }

    override fun getViewBinding(): FragmentSettingBinding = FragmentSettingBinding.inflate(layoutInflater)
}