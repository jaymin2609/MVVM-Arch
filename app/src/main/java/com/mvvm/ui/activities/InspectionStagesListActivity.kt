package com.mvvm.ui.activities

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.mvvm.R
import com.mvvm.databinding.ActivityInspectionStagesBinding
import com.mvvm.extensions.toastShort
import com.mvvm.pojos.Stage
import com.mvvm.rootmanager.BaseActivity
import com.mvvm.rootmanager.SealedResource
import com.mvvm.ui.adapters.InspectionStageAdapter
import com.mvvm.ui.viewmodels.InspectionStagesViewModel
import com.mvvm.utilities.LogUtils
import com.mvvm.utilities.ProgressBar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class InspectionStagesListActivity : BaseActivity() {
    private val classTag = InspectionStagesListActivity::class.java.simpleName
    private lateinit var binding: ActivityInspectionStagesBinding
    lateinit var mContext: Context

    private val inspectionStagesViewModel: InspectionStagesViewModel by viewModels()

    private lateinit var inspectionStageAdapter: InspectionStageAdapter

    @Inject
    lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            binding = DataBindingUtil.setContentView(this, R.layout.activity_inspection_stages)
            mContext = this

            initViewModel()
            initViews()
        } catch (e: Exception) {
            LogUtils.logE(classTag, e)
        }
    }

    private fun initViewModel() {
        inspectionStagesViewModel
        binding.viewModel = inspectionStagesViewModel
        binding.lifecycleOwner = this
    }

    private fun initViews() {
        binding.rvUserList.layoutManager = LinearLayoutManager(mContext)
        inspectionStageAdapter =
            InspectionStageAdapter { listItemClicked(it) }
        binding.rvUserList.adapter = inspectionStageAdapter
        bindObserver()
    }

    private fun bindObserver() {

        /*Observe data from API call*/
        inspectionStagesViewModel.inspectionStageList.observe(this) { userList ->
            when (userList) {
                is SealedResource.Success -> {
                    //This is handle inside the .xml using data binding
                    inspectionStageAdapter.setList(userList.data!!)
                    inspectionStageAdapter.notifyDataSetChanged()
                }
                is SealedResource.Error -> {

                }
                is SealedResource.Loading -> {

                }
                else -> {
                }
            }

        }

        /*Observe data from viewModel for loader*/
        inspectionStagesViewModel.dataLoading.observe(this) {
            if (it) {
//                progressBar.showLoader()
            } else {
//                progressBar.hideLoader()
            }
        }
    }


    private fun listItemClicked(userEntity: Stage) {
        mContext.toastShort("Selected stage : ${userEntity.stage} and status : ${userEntity.status}")
    }


}