package com.sunteckindia.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sunteckindia.MainActivity
import com.sunteckindia.R
import com.sunteckindia.databinding.ActLoginBinding
import com.sunteckindia.extensions.toastShort
import com.sunteckindia.rootmanager.BaseActivity
import com.sunteckindia.rootmanager.EventType
import com.sunteckindia.rootmanager.Injector
import com.sunteckindia.database.repository.UserRepository
import com.sunteckindia.ui.viewmodels.LoginViewModel
import com.sunteckindia.ui.viewmodels.LoginViewModelFactory
import com.sunteckindia.utilities.LogUtils
import com.sunteckindia.utilities.ProgressBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class DemoActivity : BaseActivity() {
    private val classTag = MainActivity::class.java.simpleName
    private lateinit var binding: ActLoginBinding
    lateinit var mContext: Context
    private val coroutineScope = CoroutineScope(Dispatchers.Main + Job())
    private lateinit var userRepository: UserRepository
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            binding = DataBindingUtil.setContentView(this, R.layout.act_login)
            mContext = this
            initViewModel()
            initViews()
        } catch (e: Exception) {
            LogUtils.logE(classTag, e)
        }
    }

    private fun initViewModel() {
        userRepository = UserRepository(Injector.userDao(mContext))
        val factory = LoginViewModelFactory()
        loginViewModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java)
        binding.viewModel = loginViewModel
        binding.lifecycleOwner = this
    }

    private fun initViews() {
        progressBar = ProgressBar(context = mContext)
        bindObserver()
    }

    private fun bindObserver() {
        loginViewModel.dataLoading.observe(this, Observer { isLoading ->
            if (isLoading) {
                progressBar.showLoader()
            } else {
                progressBar.hideLoader()
            }
        })

        loginViewModel.eventResponse.observe(this, Observer { eventResponse ->
            if (eventResponse.eventType == EventType.LOGIN_SUCCESS) {
                mContext.toastShort("Success. ${eventResponse.data}")
                startActivity(Intent(mContext, UserListActivity::class.java))
                finish()
            }
        })
    }

}