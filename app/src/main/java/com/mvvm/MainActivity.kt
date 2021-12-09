package com.mvvm

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
import com.mvvm.database.entity.UserEntity
import com.mvvm.database.repository.UserRepository
import com.mvvm.databinding.ActivityMainBinding
import com.mvvm.documentmanager.DocumentManager
import com.mvvm.documentmanager.DocumentType
import com.mvvm.documentmanager.OnDocumentSelectedListener
import com.mvvm.extensions.snack
import com.mvvm.locationmanager.GPSResponder
import com.mvvm.locationmanager.LocationChangeListener
import com.mvvm.locationmanager.LocationFinderAware
import com.mvvm.networking.ApiResponseCallBack
import com.mvvm.networking.ApiServiceProviderGeneric
import com.mvvm.networking.ReturnType
import com.mvvm.rootmanager.BaseActivity
import com.mvvm.rootmanager.Injector
import com.mvvm.ui.activities.SecondActivity
import com.mvvm.utilities.LogUtils
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
        loadWebView()
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
        binding.webView.loadUrl("https://upiqr.in/api/qr?name=jaymin%20patel&vpa=jaimin2305@okicici&amount=50.00&format=png")
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