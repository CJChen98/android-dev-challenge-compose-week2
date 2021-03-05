package com.example.androiddevchallenge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androiddevchallenge.ui.page.InputType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * @Author:       Chen
 * @Date:         2021/3/5 9:32
 * @Description:
 */
class TimeViewModel : ViewModel() {
    private val _timeFlow = MutableStateFlow<Long>(0)
    val time: StateFlow<Long> = _timeFlow
    private val _hour = MutableLiveData(0)
    val hour: LiveData<Int> = _hour
    private val _minute = MutableLiveData(0)
    val minute: LiveData<Int> = _minute
    private val _second = MutableLiveData(0)
    val second: LiveData<Int> = _second

    fun addTime(num: Int, type: InputType) {
        when (type) {
            is InputType.Hour -> {
                var value = (_hour.value ?: 0) * 10 + num
                if (value > 60) {
                    value = 60
                }
                _hour.postValue(value)
            }
            is InputType.Minute -> {
                var value = (_minute.value ?: 0) * 10 + num
                if (value > 60) {
                    value = 60
                }
                _minute.postValue(value)
            }
            is InputType.Second -> {
                var value = (_second.value ?: 0) * 10 + num
                if (value > 60) {
                    value = 60
                }
                _second.postValue(value)
            }
            is InputType.Auto -> {

            }
        }
    }

    fun setTime(time: Long) {
        viewModelScope.launch(Dispatchers.Default) {
            _timeFlow.emit(time)
        }
    }

}