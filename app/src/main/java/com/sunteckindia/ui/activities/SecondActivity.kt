package com.sunteckindia.ui.activities

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import com.sunteckindia.R
import com.sunteckindia.databinding.ActivitySecondBinding
import com.sunteckindia.rootmanager.BaseActivity
import com.sunteckindia.utilities.LogUtils

class SecondActivity : BaseActivity() {
    private val classTag = SecondActivity::class.java.simpleName
    private lateinit var binding: ActivitySecondBinding
    lateinit var mContext: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            binding = DataBindingUtil.setContentView(this, R.layout.activity_second)
            mContext = this
            initViews()
        } catch (e: Exception) {
            LogUtils.logE(classTag, e)
        }
    }

    private fun initViews() {
        setToolbarWithHome(binding.toolbar.tbMain, "Second Activity")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item);
    }
}