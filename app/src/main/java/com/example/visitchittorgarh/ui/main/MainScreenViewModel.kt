package com.example.visitchittorgarh.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.visitchittorgarh.data.*
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class MainScreenViewModel(dataRepository: DataRepository) : ViewModel() {
    val attractions: StateFlow<List<Attraction>> = dataRepository.attractions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val packages: StateFlow<List<TourPackage>> = dataRepository.packages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cabs: StateFlow<List<CabOption>> = dataRepository.cabs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val guides: StateFlow<List<GuideOption>> = dataRepository.guides
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val stays: StateFlow<List<StayOption>> = dataRepository.stays
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val locals: StateFlow<List<LocalItem>> = dataRepository.locals
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
