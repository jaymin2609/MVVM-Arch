package com.sunteckindia.ui.viewmodels

import android.util.Patterns
import android.view.View
import android.widget.AdapterView
import android.widget.CompoundButton
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sunteckindia.R
import com.sunteckindia.extensions.toastShort
import com.sunteckindia.rootmanager.BaseViewModel
import com.sunteckindia.rootmanager.EventResponse
import com.sunteckindia.rootmanager.EventType
import com.sunteckindia.rootmanager.ResourceProvider
import com.sunteckindia.utilities.Utils


class RegisterViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel() as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }

}

class RegisterViewModel : BaseViewModel(), Observable {

    private var itemValue: String = ""

    private val hobbies: MutableList<String> = ArrayList()

    @Bindable
    val rgGenderChecked = MutableLiveData<Int>()

    @Bindable
    val itemPos = MutableLiveData<Int>()

    @Bindable
    val isShowDetails = MutableLiveData<Boolean>().apply { value = false }

    @Bindable
    val showNotification = MutableLiveData<Boolean>().apply { value = false }

    @Bindable
    val inputEmail = MutableLiveData<String>()

    @Bindable
    val details = MutableLiveData<String>()

    @Bindable
    val inputPass = MutableLiveData<String>()

    @Bindable
    val inputName = MutableLiveData<String>()

    @Bindable
    val errorEmail = MutableLiveData<String>()

    @Bindable
    val errorPass = MutableLiveData<String>()

    @Bindable
    val errorName = MutableLiveData<String>()

    init {
        rgGenderChecked.value = (R.id.rbMale)
        itemPos.value = 3
        errorPass.value = ""
        errorEmail.value = ""
        errorName.value = ""
    }

    fun onRegisterClick() {
        if (Utils.isStringNull(inputName.value)) {
            errorName.value = ResourceProvider.applicationContext.getString(R.string.error_name)
        } else if (Utils.isStringNull(inputEmail.value)) {
            errorEmail.value = "Please Enter Valid Email Address"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.value!!).matches()) {
            errorEmail.value = "Please Enter Valid Email Address"
        } else if (Utils.isStringNull(inputPass.value)) {
            errorPass.value = "Please Enter Valid Password"
        } else if (inputPass.value!!.length < 5) {
            errorPass.value = "Please Enter Valid Password. Must be 5 Character Long."
        } else {
            eventResponse.value = EventResponse(EventType.REGISTER_SUCCESS, "Great", "Done")
            isShowDetails.value = true

            var result = "Gender : "
            result += if (rgGenderChecked.value == R.id.rbMale) {
                "Male\n"
            } else {
                "Female\n"
            }
            result += "User Name : ${inputName.value}\nUser Email : ${inputEmail.value}\nNotification(s) : ${showNotification.value}\n" +
                    "Selected Item : $itemValue\nHobbies : $hobbies"

            details.value = result

            ResourceProvider.applicationContext.toastShort(result)
        }


    }

    fun onSelectItem(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        //pos                                 get selected item position
        //view.getText()                      get lable of selected item
        //parent.getAdapter().getItem(pos)    get item by pos
        //parent.getAdapter().getCount()      get item count
        //parent.getCount()                   get item count
        //parent.getSelectedItem()            get selected item
        itemValue = parent!!.adapter.getItem(pos).toString()
        ResourceProvider.applicationContext.toastShort(itemValue)
        itemPos.value = pos
    }

    fun onCheckedChange(button: CompoundButton, check: Boolean) {
        if (check) {
            hobbies.add(button.text.toString())
        } else {
            hobbies.remove(button.text.toString())
        }
    }


    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

}