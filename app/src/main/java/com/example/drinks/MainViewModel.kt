package com.example.drinks

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drinks.database.Drink
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel : ViewModel(){

    var selectedDrink by mutableStateOf<Drink?>(null)
        private set

    fun selectDrink(drink: Drink) {
        selectedDrink = drink
    }

    private val _current = mutableStateOf(60)
    val current: State<Int> = _current
    private val _isPaused = mutableStateOf(false)
    val isPaused: State<Boolean> = _isPaused
    private val _hasStarted = mutableStateOf(false)
    val hasStarted: State<Boolean> = _hasStarted
    private var timerJob: Job? = null

    fun stoperHandler(){
        if (_hasStarted.value){
            resetCountdown()
        }else{
            startCountdown()
        }
    }
    fun startCountdown() {
        if (timerJob?.isActive == true) return

        _hasStarted.value = true
        _isPaused.value = false
        timerJob = viewModelScope.launch {
            while (_current.value > 0) {
                if (_isPaused.value) {
                    delay(100L)
                    continue
                }
                delay(1000L)
                _current.value--
            }
        }
    }

    fun pauseOrResumeCountdown() {
        if (_isPaused.value) {
            // Wznów
            _isPaused.value = false
        } else {
            // Pauza
            _isPaused.value = true
        }
    }

    fun resetCountdown() {
        timerJob?.cancel()
        timerJob = null
        _current.value = 60
        _isPaused.value = false
        _hasStarted.value = false
    }
}
