package com.sunteckindia.ui.activities

import android.content.Context
import android.location.Location
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.LocationSettingsResponse
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.sunteckindia.MainActivity
import com.sunteckindia.R
import com.sunteckindia.database.entity.UserEntity
import com.sunteckindia.databinding.ActivityUserListBinding
import com.sunteckindia.extensions.toastShort
import com.sunteckindia.locationmanager.GPSResponder
import com.sunteckindia.locationmanager.LocationChangeListener
import com.sunteckindia.locationmanager.LocationFinderAware
import com.sunteckindia.rootmanager.BaseActivity
import com.sunteckindia.rootmanager.Status
import com.sunteckindia.ui.adapters.UserListAdapter
import com.sunteckindia.ui.viewmodels.UserListViewModel
import com.sunteckindia.utilities.LogUtils
import com.sunteckindia.utilities.ProgressBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject

@AndroidEntryPoint
class UserListActivity : BaseActivity(), LocationChangeListener {
    private val classTag = MainActivity::class.java.simpleName
    private lateinit var binding: ActivityUserListBinding
    lateinit var mContext: Context
    private val coroutineScope = CoroutineScope(Dispatchers.Main + Job())
    private val userListViewModel: UserListViewModel by viewModels()
    private lateinit var userListAdapter: UserListAdapter
    private lateinit var progressBar: ProgressBar


    @Inject
    lateinit var locationFinderAware: LocationFinderAware

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            binding = DataBindingUtil.setContentView(this, R.layout.activity_user_list)
            mContext = this
            initViewModel()
            initViews()
        } catch (e: Exception) {
            LogUtils.logE(classTag, e)
        }
    }

    private fun initViewModel() {
        binding.viewModel = userListViewModel
        binding.lifecycleOwner = this
    }

    private fun initViews() {
        progressBar = ProgressBar(context = mContext)
        binding.rvUserList.layoutManager = LinearLayoutManager(mContext)
        userListAdapter =
            UserListAdapter { listItemClicked(it) }
        binding.rvUserList.adapter = userListAdapter
        bindObserver()
        getLocationUpdates()
    }

    private fun bindObserver() {
        /*Observe data from Room database*/
        userListViewModel.userListLocal.observe(this, Observer { userList ->
//            userListAdapter.setList(userList)
//            userListAdapter.notifyDataSetChanged()
        })
        /*Observe data from API call*/
        userListViewModel.userList.observe(this, Observer { userList ->
            when (userList.status) {
                Status.SUCCESS -> {
                    userListAdapter.setList(userList.data!!)
                    userListAdapter.notifyDataSetChanged()
                }
                Status.ERROR -> {

                }
            }

        })

        /*Observe data from viewModel for loader*/
        userListViewModel.dataLoading.observe(this, Observer {
            if (it) {
//                progressBar.showLoader()
            } else {
//                progressBar.hideLoader()
            }
        })
    }

    private fun getLocationUpdates() {
        Dexter.withContext(mContext)
            .withPermissions(getLocationPermission())
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report!!.areAllPermissionsGranted()) {
                        locationFinderAware.checkLocationSettings(object : GPSResponder {
                            override fun onSuccess(locationSettingsResponse: LocationSettingsResponse?) {

                            }

                            override fun onFailure(e: Exception) {
                                LogUtils.logE(classTag, e)
                            }
                        })
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            }).check()
    }

    private fun listItemClicked(userEntity: UserEntity) {
        mContext.toastShort("Selected User Name : ${userEntity.name} and Age : ${userEntity.age}")
    }

    override fun onLocationChanged(location: Location?) {
        mContext.toastShort("Lat -> ${location?.latitude} Long -> ${location?.longitude}")
    }


}