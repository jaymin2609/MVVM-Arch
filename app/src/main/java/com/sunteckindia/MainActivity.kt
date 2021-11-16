package com.sunteckindia

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.webkit.JavascriptInterface
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.gms.location.LocationSettingsResponse
import com.google.gson.JsonObject
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.sunteckindia.database.entity.UserEntity
import com.sunteckindia.database.repository.UserRepository
import com.sunteckindia.databinding.ActivityMainBinding
import com.sunteckindia.documentmanager.DocumentManager
import com.sunteckindia.documentmanager.DocumentType
import com.sunteckindia.documentmanager.OnDocumentSelectedListener
import com.sunteckindia.extensions.snack
import com.sunteckindia.locationmanager.GPSResponder
import com.sunteckindia.locationmanager.LocationChangeListener
import com.sunteckindia.locationmanager.LocationFinderAware
import com.sunteckindia.networking.ApiResponseCallBack
import com.sunteckindia.networking.ApiServiceProviderGeneric
import com.sunteckindia.networking.ReturnType
import com.sunteckindia.rootmanager.BaseActivity
import com.sunteckindia.rootmanager.Injector
import com.sunteckindia.ui.activities.SecondActivity
import com.sunteckindia.utilities.LogUtils
import kotlinx.coroutines.*
import java.io.File


class MainActivity : BaseActivity(), ApiResponseCallBack, LocationChangeListener,
    OnDocumentSelectedListener {

    private val classTag = MainActivity::class.java.simpleName
    private lateinit var binding: ActivityMainBinding
    lateinit var mContext: Context
    lateinit var apiServiceProviderGeneric: ApiServiceProviderGeneric
    lateinit var locationFinderAware: LocationFinderAware
    lateinit var documentManager: DocumentManager
    private val coroutineScope = CoroutineScope(Dispatchers.Main + Job())
    private lateinit var userRepository: UserRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
            mContext = this
            initViews()
        } catch (e: Exception) {
            LogUtils.logE(classTag, e)
        }
    }

    private fun initViews() {
        setToolbar(binding.toolbar.tbMain,"Home")
//        apiServiceProviderGeneric = ApiServiceProviderGeneric(this)
//        locationFinderAware = LocationFinderAware(mContext, lifecycle, this)
        documentManager = DocumentManager(mContext, DocumentType.DOCUMENT_CONTRACT, this)
        userRepository = UserRepository(Injector.userDao(mContext))
//        loadWebView()
        binding.btnChoosePhoto.setOnClickListener {
            getFileChooser()
        }
        binding.btnSecondActivity.setOnClickListener {
            startActivity(Intent(this, SecondActivity::class.java))
        }
//        getLocationUpdates()
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                userRepository
                    .insert(UserEntity(0, "Jaymin Patel", age = 28))
                withContext(Dispatchers.Main) {
                    getUsersFromDB()
                }
            }
        }
    }

    private fun getUsersFromDB() {
        userRepository.getUserData()
            .observe(this, Observer {
                LogUtils.logE(classTag, "Users Size is " + it.size)
                LogUtils.logE(classTag, "Users : $it")
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

    private fun loadWebView() {
        binding.webView.settings.javaScriptEnabled = true
//        binding.webView.loadData(html, "text/html", null)
        binding.webView.loadUrl("http://192.168.3.230:4200")
        binding.webView.addJavascriptInterface(WebAppInterface(this), "android")
    }

    private fun callAPI() {
        apiServiceProviderGeneric.postCall(
            JsonObject(),
            ReturnType.POST_LOGIN
        )
    }

    private fun getFileChooser() {
        Dexter.withContext(mContext)
            .withPermissions(getStoragePermission())
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report!!.areAllPermissionsGranted()) {
                        documentManager.showOptionsDialog()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (resultCode == Activity.RESULT_OK && data != null) {
                documentManager.onActivityResult(requestCode, resultCode, data)
            }
        } catch (ex: Exception) {
            LogUtils.logE(classTag, ex)
        }
    }

    inner class WebAppInterface(private val mContext: Context) {
        /** Show a toast from the web page  */
        @JavascriptInterface
        fun showToast(toast: String) {
//            mContext.toastShort(toast)
            binding.root.snack(toast) {}
        }

    }

    override fun onPreExecute(returnType: ReturnType) {
//        TODO("Not yet implemented")
    }

    override fun onSuccess(returnType: ReturnType, response: String) {
//        TODO("Not yet implemented")
    }

    override fun onError(returnType: ReturnType, error: String) {
//        TODO("Not yet implemented")
    }

    override fun onLocationChanged(location: Location?) {
        LogUtils.logE(
            classTag,
            "onLocationChanged : Lat -> ${location?.latitude} Long -> ${location?.longitude}"
        )
    }

    override fun onDocumentSelected(documentType: DocumentType?, imagePath: String?) {
        LogUtils.logE(classTag, "Image path : $imagePath")

        binding.ivPic.setImageURI(Uri.fromFile(File(imagePath)))
    }
}