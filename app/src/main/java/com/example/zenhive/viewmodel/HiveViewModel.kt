package com.example.zenhive.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zenhive.model.HiveModel
import com.example.zenhive.repository.HiveRepositoryImplementation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HiveViewModel : ViewModel() {

    private val repository = HiveRepositoryImplementation()
    private val _liveHives = MutableStateFlow<List<HiveModel>>(emptyList())
    val liveHives: StateFlow<List<HiveModel>> = _liveHives

    init {
        fetchLiveHives()
    }

    private fun fetchLiveHives() {
        viewModelScope.launch {
            val hives = repository.getLiveHives()
            _liveHives.value = hives
        }
    }
}
