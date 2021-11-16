package com.sunteckindia.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sunteckindia.MainActivity
import com.sunteckindia.R
import com.sunteckindia.databinding.ActivityRegisterBinding
import com.sunteckindia.extensions.toastShort
import com.sunteckindia.rootmanager.BaseActivity
import com.sunteckindia.rootmanager.EventType
import com.sunteckindia.ui.viewmodels.RegisterViewModel
import com.sunteckindia.ui.viewmodels.RegisterViewModelFactory
import com.sunteckindia.utilities.LogUtils
import com.sunteckindia.utilities.ProgressBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class RegisterActivity : BaseActivity() {
    private val classTag = MainActivity::class.java.simpleName
    private lateinit var binding: ActivityRegisterBinding
    lateinit var mContext: Context
    private val coroutineScope = CoroutineScope(Dispatchers.Main + Job())
    private lateinit var factory: RegisterViewModelFactory
    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            binding = DataBindingUtil.setContentView(this, R.layout.activity_register)
            mContext = this
            initViewModel()
            initViews()
        } catch (e: Exception) {
            LogUtils.logE(classTag, e)
        }
    }

    private fun initViewModel() {
        factory = RegisterViewModelFactory()
        registerViewModel = ViewModelProvider(this, factory).get(RegisterViewModel::class.java)
        binding.viewModel = registerViewModel
        binding.lifecycleOwner = this
    }

    private fun initViews() {
        progressBar = ProgressBar(context = mContext)
        bindObserver()
    }

    private fun bindObserver() {
        registerViewModel.dataLoading.observe(this, Observer { isLoading ->
            if (isLoading) {
                progressBar.showLoader()
            } else {
                progressBar.hideLoader()
            }
        })

        registerViewModel.eventResponse.observe(this, Observer { eventResponse ->
            if (eventResponse.eventType == EventType.REGISTER_SUCCESS) {
                mContext.toastShort("Success. ${eventResponse.data}")
                Intent(mContext, LoginActivity::class.java).apply {
                    startActivity(this)
                    finish()
                }
            }
        })
    }

}