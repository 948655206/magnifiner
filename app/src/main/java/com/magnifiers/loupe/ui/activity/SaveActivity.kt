package com.magnifiers.loupe.ui.activity

import com.magnifiers.loupe.base.BaseViewModelActivity
import com.magnifiers.loupe.model.SaveModel
import myapplication.com.databinding.ActivitySaveBinding

class SaveActivity : BaseViewModelActivity<SaveModel, ActivitySaveBinding>() {
    override fun getVMClass(): Class<SaveModel> = SaveModel::class.java

    override fun getViewBinding(): ActivitySaveBinding = ActivitySaveBinding.inflate(layoutInflater)


}