package com.example.visitchittorgarh.data

import kotlinx.serialization.Serializable

@Serializable
data class Translation(
    val en: String,
    val hi: String
) {
    fun get(isEnglish: Boolean): String = if (isEnglish) en else hi
}

data class Attraction(
    val name: Translation,
    val category: Translation,
    val description: Translation,
    val entryFee: Translation,
    val timing: Translation,
    val rating: Double,
    val imageUrl: Any
)

data class TourPackage(
    val id: String,
    val title: Translation,
    val duration: Translation,
    val price: Translation,
    val summary: Translation,
    val itinerary: List<Translation>,
    val imageUrl: Any
)

data class CabOption(
    val vehicleName: String,
    val capacity: Translation,
    val ratePerKm: Translation,
    val description: Translation,
    val imageUrl: Any
)

data class GuideOption(
    val name: Translation,
    val experience: Translation,
    val languages: Translation,
    val specialty: Translation,
    val rating: Double,
    val imageUrl: Any
)

data class StayOption(
    val name: String,
    val type: Translation,
    val rating: Double,
    val priceRange: Translation,
    val description: Translation,
    val imageUrl: Any
)

data class LocalItem(
    val name: Translation,
    val category: Translation,
    val description: Translation,
    val origin: Translation,
    val imageUrl: Any,
    val minPriceInr: Double = 0.0,
    val maxPriceInr: Double = 0.0
)

@Serializable
data class TravelPass(
    val passCode: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val date: String = "",
    val arrivalTime: String = "",
    val departureTime: String = "",
    val transport: String = "",
    val transportPrice: Double = 0.0,
    val hotel: String = "",
    val hotelPrice: Double = 0.0,
    val guide: String = "",
    val guidePrice: Double = 0.0,
    val pillarTitle: String = "Custom Tour",
    var guideName: String = "",
    var driverName: String = "",
    var paymentStatus: String = "Pending",
    var redeemed_taxi: Boolean = false,
    var redeemed_hotel: Boolean = false,
    var redeemed_guide: Boolean = false,
    val userId: String = ""
)

