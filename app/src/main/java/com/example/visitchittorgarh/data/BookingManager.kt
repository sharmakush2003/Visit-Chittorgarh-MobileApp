package com.example.visitchittorgarh.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object BookingManager {
    private val _passes = MutableStateFlow<List<TravelPass>>(emptyList())
    val passes: StateFlow<List<TravelPass>> = _passes

    init {
        // Pre-populate with some sample mock bookings for testing/verification
        _passes.value = listOf(
            TravelPass(
                passCode = "CT1294",
                name = "Kush Sharma",
                email = "kush@example.com",
                phone = "+917597451057",
                date = "2026-07-01",
                arrivalTime = "09:00 AM",
                departureTime = "06:00 PM",
                transport = "Royal SUV (Innova)",
                transportPrice = 2000.0,
                hotel = "Heritage Palace",
                hotelPrice = 5000.0,
                guide = "History Scholar",
                guidePrice = 1500.0,
                pillarTitle = "Mewar Heritage",
                guideName = "Chandra Shekhar Rathore",
                driverName = "Ram Singh",
                paymentStatus = "Received",
                redeemed_taxi = true,
                redeemed_hotel = false,
                redeemed_guide = true
            ),
            TravelPass(
                passCode = "CT8832",
                name = "Aarav Mehta",
                email = "aarav@gmail.com",
                phone = "+919876543210",
                date = "2026-07-05",
                arrivalTime = "10:30 AM",
                departureTime = "08:00 PM",
                transport = "Luxury Sedan (Dzire)",
                transportPrice = 1500.0,
                hotel = "None",
                hotelPrice = 0.0,
                guide = "Storyteller",
                guidePrice = 1000.0,
                pillarTitle = "Fort Legacy",
                guideName = "",
                driverName = "",
                paymentStatus = "Pending",
                redeemed_taxi = false,
                redeemed_hotel = false,
                redeemed_guide = false
            )
        )
    }

    fun addPass(pass: TravelPass) {
        _passes.value = _passes.value + pass
    }

    fun getPass(passCode: String): TravelPass? {
        return _passes.value.find { it.passCode.equals(passCode, ignoreCase = true) }
    }

    fun updatePass(updatedPass: TravelPass) {
        _passes.value = _passes.value.map {
            if (it.passCode == updatedPass.passCode) updatedPass else it
        }
    }
}
