package com.mvvm.ui.activities

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
import com.mvvm.MainActivity
import com.mvvm.R
import com.mvvm.database.entity.UserEntity
import com.mvvm.databinding.ActivityUserListBinding
import com.mvvm.extensions.toastShort
import com.mvvm.locationmanager.GPSResponder
import com.mvvm.locationmanager.LocationChangeListener
import com.mvvm.locationmanager.LocationFinderAware
import com.mvvm.rootmanager.BaseActivity
import com.mvvm.rootmanager.SealedResource
import com.mvvm.ui.adapters.UserListAdapter
import com.mvvm.ui.viewmodels.UserListViewModel
import com.mvvm.utilities.LogUtils
import com.mvvm.utilities.ProgressBar
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
//        getLocationUpdates()
    }

    private fun bindObserver() {
        /*Observe data from Room database*/
        userListViewModel.userListLocal.observe(this, Observer { userList ->
//            userListAdapter.setList(userList)
//            userListAdapter.notifyDataSetChanged()
        })
        /*Observe data from API call*/
        userListViewModel.userList.observe(this, Observer { userList ->
            when (userList) {
                is SealedResource.Success -> {
                    //This is handle inside the .xml using data binding
                    /*binding.pbUserList.makeGone()
                    binding.rvUserList.makeVisible()
                    binding.tvError.makeGone()*/
                    userListAdapter.setList(userList.data!!)
                    userListAdapter.notifyDataSetChanged()
                }
                is SealedResource.Error -> {
                    //This is handle inside the .xml using data binding
                    /*binding.pbUserList.makeGone()
                    binding.rvUserList.makeGone()
                    binding.tvError.makeVisible()
                    binding.tvError.text = userList.message*/
                }
                is SealedResource.Loading -> {
                    //This is handle inside the .xml using data binding
                    /*binding.pbUserList.makeVisible()
                    binding.rvUserList.makeGone()
                    binding.tvError.makeGone()*/
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