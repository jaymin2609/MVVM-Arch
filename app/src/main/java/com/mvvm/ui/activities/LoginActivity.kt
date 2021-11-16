package com.mvvm.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mvvm.MainActivity
import com.mvvm.R
import com.mvvm.database.repository.UserRepository
import com.mvvm.databinding.ActivityLoginBinding
import com.mvvm.extensions.toastShort
import com.mvvm.rootmanager.BaseActivity
import com.mvvm.rootmanager.EventType
import com.mvvm.rootmanager.Injector
import com.mvvm.ui.viewmodels.LoginViewModel
import com.mvvm.ui.viewmodels.LoginViewModelFactory
import com.mvvm.utilities.LogUtils
import com.mvvm.utilities.ProgressBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class LoginActivity : BaseActivity() {
    private val classTag = MainActivity::class.java.simpleName
    private lateinit var binding: ActivityLoginBinding
    lateinit var mContext: Context
    private val coroutineScope = CoroutineScope(Dispatchers.Main + Job())
    private lateinit var userRepository: UserRepository
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
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
        loginViewModel.dataLoading.observe(this, Observer {
            if (it) progressBar.showLoader() else progressBar.hideLoader()
        })

        loginViewModel.eventResponse.observe(this, Observer { eventResponse ->
            when (eventResponse.eventType) {
                EventType.LOGIN_SUCCESS -> {
                    mContext.toastShort("Success. ${eventResponse.data}")
                    Intent(mContext, UserListActivity::class.java).apply {
                        startActivity(this)
                        finish()
                    }
                }
            }
        })
    }
}