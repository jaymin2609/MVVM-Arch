package com.mvvm.pojos


import com.google.gson.annotations.SerializedName

data class ResInspectionStages(
    @SerializedName("Errors")
    val errors: Any,
    @SerializedName("ExtraData")
    val extraData: List<Stage>,
    @SerializedName("Message")
    val message: Any,
    @SerializedName("Status")
    val status: String,
    @SerializedName("StatusCode")
    val statusCode: String
)

data class Stage(
    @SerializedName("Datetime")
    val datetime: String,
    @SerializedName("Stage")
    val stage: String,
    @SerializedName("Status")
    val status: String,
    @SerializedName("Sub_Stage")
    val subStage: String
)