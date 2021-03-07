package com.example.androiddevchallenge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androiddevchallenge.ui.page.Hour
import com.example.androiddevchallenge.ui.page.InputType
import com.example.androiddevchallenge.ui.page.Minute
import com.example.androiddevchallenge.ui.page.Second
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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
    val showFloatButton = MutableStateFlow(true)

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
                when {

                }
            }
        }
    }

    fun setTime() {
        val value = (_hour.value ?: 0) * Hour + (_minute.value ?: 0) * Minute + (_second.value
            ?: 0) * Second
        viewModelScope.launch {
            if (value >= 1000) {
                _timeFlow.emit(value)
                _currentTime.emit(value)
                showFloatButton.emit(false)
            } else {
                showFloatButton.emit(true)
            }
        }
    }

    private val _currentTime = MutableStateFlow<Long>(0)
    val currentTime: StateFlow<Long> get() = _currentTime
    private lateinit var timeCounter: Job

    fun startCountDownTime() {
        viewModelScope.launch {
            timeCounter = launch(Dispatchers.Default, start = CoroutineStart.LAZY) {
                while (_currentTime.value > 0) {
                    delay(Second)
                    _currentTime.emit(_currentTime.value - Second)
                }
            }
            timeCounter.start()
        }
    }

    fun pauseCountDownTime() {
        if (this::timeCounter.isInitialized && timeCounter.isActive) {
            timeCounter.cancel()
        }
    }

    fun resetTime(){
        pauseCountDownTime()
        viewModelScope.launch {
            showFloatButton.emit(true)
            _second.postValue(0)
            _minute.postValue(0)
            _hour.postValue(0)
        }
    }
}