package com.example.visitchittorgarh

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable data object Splash : NavKey
@Serializable data object Main : NavKey
@Serializable data object Auth : NavKey

@Serializable data class BookingPass(
    val pillarTitle: String = "Custom Tour",
    val transport: String = "None",
    val transportPrice: Double = 0.0,
    val hotel: String = "None",
    val hotelPrice: Double = 0.0,
    val guide: String = "None",
    val guidePrice: Double = 0.0
) : NavKey

@Serializable data object AboutDeveloper : NavKey

@Serializable data object AboutChittorgarh : NavKey

@Serializable data object HowToReach : NavKey

@Serializable data object EmergencyContacts : NavKey

@Serializable data object Weather : NavKey

@Serializable data object UPIGuide : NavKey

