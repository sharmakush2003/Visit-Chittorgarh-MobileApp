package com.example.visitchittorgarh.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.visitchittorgarh.data.BookingManager
import com.example.visitchittorgarh.data.TravelPass
import com.example.visitchittorgarh.theme.CrimsonDark
import com.example.visitchittorgarh.theme.CrimsonSecondary
import com.example.visitchittorgarh.theme.GoldAccent
import com.example.visitchittorgarh.theme.SaffronPrimary
import java.net.URLEncoder
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingPassScreen(
    initialPillarTitle: String = "Custom Tour",
    initialTransport: String = "None",
    initialTransportPrice: Double = 0.0,
    initialHotel: String = "None",
    initialHotelPrice: Double = 0.0,
    initialGuide: String = "None",
    initialGuidePrice: Double = 0.0,
    isEnglish: Boolean,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    var currentStep by remember { mutableStateOf(0) }

    // Form inputs state
    var date by remember { mutableStateOf("2026-07-01") }
    var arrivalTime by remember { mutableStateOf("09:00 AM") }
    var departureTime by remember { mutableStateOf("06:00 PM") }

    var transport by remember { mutableStateOf(initialTransport) }
    var transportPrice by remember { mutableStateOf(initialTransportPrice) }
    var hotel by remember { mutableStateOf(initialHotel) }
    var hotelPrice by remember { mutableStateOf(initialHotelPrice) }
    var guide by remember { mutableStateOf(initialGuide) }
    var guidePrice by remember { mutableStateOf(initialGuidePrice) }

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    var travelPass by remember { mutableStateOf<TravelPass?>(null) }

    val transportOptions = listOf(
        Triple("None", if (isEnglish) "Not Needed" else "आवश्यकता नहीं", 0.0),
        Triple("Luxury Sedan (Dzire)", if (isEnglish) "Luxury Sedan (₹11/km)" else "लक्जरी सेडान (₹11/किमी)", 1500.0),
        Triple("Royal SUV (Innova)", if (isEnglish) "Royal SUV (₹14/km)" else "रॉयल एसयूवी (₹14/किमी)", 2000.0),
        Triple("Mini Bus (Tempo)", if (isEnglish) "Mini Bus (₹20/km)" else "मिनी बस (₹20/किमी)", 3500.0)
    )

    val hotelOptions = listOf(
        Triple("None", if (isEnglish) "Not Needed" else "आवश्यकता नहीं", 0.0),
        Triple("Boutique Hotel", if (isEnglish) "Boutique Haveli (₹3,000/N)" else "बुटीक हवेली (₹3,000/रात)", 3000.0),
        Triple("Eco Resort", if (isEnglish) "Eco Resort (₹4,000/N)" else "इको रिसॉर्ट (₹4,000/रात)", 4000.0),
        Triple("Heritage Palace", if (isEnglish) "Heritage Palace (₹5,000/N)" else "हेरिटेज पैलेस (₹5,000/रात)", 5000.0)
    )

    val guideOptions = listOf(
        Triple("None", if (isEnglish) "Not Needed" else "आवश्यकता नहीं", 0.0),
        Triple("Storyteller", if (isEnglish) "Storyteller (₹1,000)" else "कहानीकार (₹1,000)", 1000.0),
        Triple("Photography Expert", if (isEnglish) "Photography Expert (₹1,200)" else "फोटोग्राफी विशेषज्ञ (₹1,200)", 1200.0),
        Triple("History Scholar", if (isEnglish) "History Scholar (₹1,500)" else "इतिहास विद्वान (₹1,500)", 1500.0)
    )

    val totalEstimate = transportPrice + hotelPrice + guidePrice

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (travelPass == null) {
                            if (isEnglish) "Royal Planner" else "शाही योजनाकार"
                        } else {
                            if (isEnglish) "Royal Tourism Pass" else "शाही पर्यटन पास"
                        },
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (travelPass != null) {
                            travelPass = null
                            currentStep = 0
                        } else if (currentStep > 0) {
                            currentStep--
                        } else {
                            onBackClick()
                        }
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = SaffronPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (travelPass == null) {
                // Step Progress Indicator
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(3) { step ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(6.dp)
                                .clip(CircleShape)
                                .background(
                                    if (currentStep >= step) SaffronPrimary else MaterialTheme.colorScheme.outline.copy(
                                        alpha = 0.2f
                                    )
                                )
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(24.dp)
                ) {
                    when (currentStep) {
                        0 -> {
                            // Step 1: Itinerary overview and Date/Time selection
                            Text(
                                text = if (isEnglish) "When shall we prepare for your arrival?" else "हम आपके आगमन की तैयारी कब करें?",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Spacer(modifier = Modifier.height(20.dp))

                            OutlinedTextField(
                                value = date,
                                onValueChange = { date = it },
                                label = { Text(if (isEnglish) "Arrival Date (YYYY-MM-DD)" else "आगमन की तिथि (YYYY-MM-DD)") },
                                leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null, tint = SaffronPrimary) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            OutlinedTextField(
                                value = arrivalTime,
                                onValueChange = { arrivalTime = it },
                                label = { Text(if (isEnglish) "Arrival Time" else "आगमन का समय") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            OutlinedTextField(
                                value = departureTime,
                                onValueChange = { departureTime = it },
                                label = { Text(if (isEnglish) "Departure Time" else "प्रस्थान का समय") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                        1 -> {
                            // Step 2: Choose Services
                            Text(
                                text = if (isEnglish) "Configure Concierge Options" else "द्वारपाल विकल्प कॉन्फ़िगर करें",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Spacer(modifier = Modifier.height(20.dp))

                            // Transport selection
                            Text(
                                text = if (isEnglish) "Transport Fleet" else "परिवहन बेड़ा",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = CrimsonSecondary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            transportOptions.forEach { option ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            transport = option.first
                                            transportPrice = option.third
                                        }
                                        .padding(vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = transport == option.first,
                                        onClick = {
                                            transport = option.first
                                            transportPrice = option.third
                                        },
                                        colors = RadioButtonDefaults.colors(selectedColor = SaffronPrimary)
                                    )
                                    Text(
                                        text = option.second,
                                        fontSize = 14.sp,
                                        modifier = Modifier.padding(start = 8.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // Stays selection
                            Text(
                                text = if (isEnglish) "Accommodation Stays" else "आवास एवं होटल",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = CrimsonSecondary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            hotelOptions.forEach { option ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            hotel = option.first
                                            hotelPrice = option.third
                                        }
                                        .padding(vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = hotel == option.first,
                                        onClick = {
                                            hotel = option.first
                                            hotelPrice = option.third
                                        },
                                        colors = RadioButtonDefaults.colors(selectedColor = SaffronPrimary)
                                    )
                                    Text(
                                        text = option.second,
                                        fontSize = 14.sp,
                                        modifier = Modifier.padding(start = 8.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // Guide selection
                            Text(
                                text = if (isEnglish) "Heritage Guide Historian" else "विरासत गाइड इतिहासकार",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = CrimsonSecondary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            guideOptions.forEach { option ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            guide = option.first
                                            guidePrice = option.third
                                        }
                                        .padding(vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = guide == option.first,
                                        onClick = {
                                            guide = option.first
                                            guidePrice = option.third
                                        },
                                        colors = RadioButtonDefaults.colors(selectedColor = SaffronPrimary)
                                    )
                                    Text(
                                        text = option.second,
                                        fontSize = 14.sp,
                                        modifier = Modifier.padding(start = 8.dp)
                                    )
                                }
                            }
                        }
                        2 -> {
                            // Step 3: Enter Details and Checkout
                            Text(
                                text = if (isEnglish) "Traveler Credentials" else "यात्री का विवरण",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Spacer(modifier = Modifier.height(20.dp))

                            OutlinedTextField(
                                value = name,
                                onValueChange = { name = it },
                                label = { Text(if (isEnglish) "Full Name" else "पूरा नाम") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            OutlinedTextField(
                                value = email,
                                onValueChange = { email = it },
                                label = { Text(if (isEnglish) "Email Address" else "ईमेल आईडी") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            OutlinedTextField(
                                value = phone,
                                onValueChange = { phone = it },
                                label = { Text(if (isEnglish) "WhatsApp Phone Number" else "व्हाट्सएप मोबाइल नंबर") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )

                            Spacer(modifier = Modifier.height(30.dp))

                            // Estimate Card
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                border = BorderStroke(1.dp, SaffronPrimary.copy(alpha = 0.2f)),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = if (isEnglish) "ROYAL TRIP ESTIMATE" else "शाही यात्रा अनुमान",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = CrimsonSecondary
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(text = if (isEnglish) "Transport Selection" else "परिवहन बेड़ा", fontSize = 14.sp)
                                        Text(text = "₹${transportPrice}", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(text = if (isEnglish) "Palace Stays" else "शाही होटल", fontSize = 14.sp)
                                        Text(text = "₹${hotelPrice}", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(text = if (isEnglish) "Heritage Guide" else "विरासत गाइड", fontSize = 14.sp)
                                        Text(text = "₹${guidePrice}", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                    }

                                    HorizontalDivider(
                                        modifier = Modifier.padding(vertical = 12.dp),
                                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                                    )

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = if (isEnglish) "Total Estimated Cost" else "कुल अनुमानित लागत",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp,
                                            color = MaterialTheme.colorScheme.onBackground
                                        )
                                        Text(
                                            text = "₹${totalEstimate}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 20.sp,
                                            color = SaffronPrimary,
                                            fontFamily = FontFamily.Serif
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    // Next/Generate button
                    Button(
                        onClick = {
                            if (currentStep < 2) {
                                currentStep++
                            } else {
                                // Form Validation
                                if (name.isBlank() || email.isBlank() || phone.isBlank()) {
                                    Toast.makeText(context, "Please fill in all traveler details.", Toast.LENGTH_SHORT).show()
                                } else {
                                    // Generate Pass
                                    val rCode = "CT" + Random.nextInt(1000, 9999).toString()
                                    val pass = TravelPass(
                                        passCode = rCode,
                                        name = name,
                                        email = email,
                                        phone = phone,
                                        date = date,
                                        arrivalTime = arrivalTime,
                                        departureTime = departureTime,
                                        transport = transport,
                                        transportPrice = transportPrice,
                                        hotel = hotel,
                                        hotelPrice = hotelPrice,
                                        guide = guide,
                                        guidePrice = guidePrice,
                                        pillarTitle = initialPillarTitle
                                    )
                                    BookingManager.addPass(pass)
                                    travelPass = pass
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CrimsonSecondary,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = if (currentStep < 2) {
                                if (isEnglish) "Continue Journey" else "यात्रा जारी रखें"
                            } else {
                                if (isEnglish) "Generate Royal Pass" else "शाही पास जनरेट करें"
                            },
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            fontSize = 15.sp
                        )
                        if (currentStep < 2) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            } else {
                // Pass Voucher Ticket rendering
                val pass = travelPass!!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
                    ) {
                        Column {
                            // Royal Velvet Header
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(CrimsonSecondary, CrimsonDark)
                                        )
                                    )
                                    .padding(24.dp)
                            ) {
                                Column {
                                    Text(
                                        text = if (isEnglish) "BOARDING PASS / TOURIST VOUCHER" else "बोर्डिंग पास / पर्यटक वाउचर",
                                        color = SaffronPrimary,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.5.sp,
                                        fontFamily = FontFamily.Serif
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = if (isEnglish) "Chittorgarh Tourism Portal" else "चित्तौड़गढ़ पर्यटन पोर्टल",
                                        color = Color.White,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Serif
                                    )
                                }
                            }

                            // Dashed teardown line
                            Canvas(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(MaterialTheme.colorScheme.surface)
                            ) {
                                val pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 12f), 0f)
                                drawLine(
                                    color = Color.Gray.copy(alpha = 0.5f),
                                    start = Offset(0f, 0f),
                                    end = Offset(size.width, 0f),
                                    pathEffect = pathEffect,
                                    strokeWidth = 2f
                                )
                            }

                            // Pass details
                            Column(modifier = Modifier.padding(24.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(text = if (isEnglish) "TRAVELER" else "यात्री", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                        Text(text = pass.name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(text = if (isEnglish) "PASS CODE" else "पास कोड", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                        Text(text = pass.passCode, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = SaffronPrimary, fontFamily = FontFamily.Serif)
                                    }
                                }

                                Spacer(modifier = Modifier.height(20.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(text = if (isEnglish) "ARRIVAL DATE" else "आगमन की तिथि", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                        Text(text = pass.date, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(text = if (isEnglish) "ARRIVAL TIME" else "आगमन का समय", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                        Text(text = pass.arrivalTime, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                Spacer(modifier = Modifier.height(20.dp))

                                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = if (isEnglish) "INCLUDED SERVICES" else "शामिल सेवाएं",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CrimsonSecondary
                                )
                                Spacer(modifier = Modifier.height(10.dp))

                                TicketServiceItem(
                                    label = if (isEnglish) "Transport" else "परिवहन",
                                    value = pass.transport,
                                    price = "₹" + pass.transportPrice.toInt().toString()
                                )
                                TicketServiceItem(
                                    label = if (isEnglish) "Stay Booking" else "होटल बुकिंग",
                                    value = pass.hotel,
                                    price = "₹" + pass.hotelPrice.toInt().toString()
                                )
                                TicketServiceItem(
                                    label = if (isEnglish) "Historian Guide" else "इतिहासकार गाइड",
                                    value = pass.guide,
                                    price = "₹" + pass.guidePrice.toInt().toString()
                                )

                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 16.dp),
                                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = if (isEnglish) "TOTAL ESTIMATE" else "कुल अनुमान", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text(
                                        text = "₹" + totalEstimate.toInt().toString(),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp,
                                        color = SaffronPrimary,
                                        fontFamily = FontFamily.Serif
                                    )
                                }

                                Spacer(modifier = Modifier.height(24.dp))

                                // QR Code placeholder drawn in Compose
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(80.dp)
                                        .background(Color.White)
                                        .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        // Draw pseudo barcode
                                        repeat(28) { i ->
                                            val width = if (i % 3 == 0) 4.dp else if (i % 2 == 0) 2.dp else 1.dp
                                            Box(
                                                modifier = Modifier
                                                    .width(width)
                                                    .fillMaxHeight(0.6f)
                                                    .background(Color.Black)
                                            )
                                        }
                                    }
                                    Text(
                                        text = "* ${pass.passCode} *",
                                        modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 6.dp),
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.DarkGray
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Booking options trigger (WhatsApp or Close)
                    Button(
                        onClick = {
                            val msg = "*👑 Chittorgarh Tourism Pass Generated!* \n\n" +
                                    "Namaste! My itinerary is planned:\n" +
                                    "• *Pass Code:* ${pass.passCode}\n" +
                                    "• *Traveler:* ${pass.name}\n" +
                                    "• *Date:* ${pass.date} (${pass.arrivalTime})\n" +
                                    "• *Transport:* ${pass.transport}\n" +
                                    "• *Stay:* ${pass.hotel}\n" +
                                    "• *Guide:* ${pass.guide}\n" +
                                    "• *Estimated Cost:* ₹${totalEstimate.toInt()}\n\n" +
                                    "Please confirm the availability. Thanks!"
                            sendWhatsApp(context, "+917597451057", msg)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary)
                    ) {
                        Text(
                            text = if (isEnglish) "Inquire on WhatsApp" else "व्हाट्सएप पर पूछताछ करें",
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    TextButton(
                        onClick = onBackClick,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (isEnglish) "Return to Dashboard" else "डैशबोर्ड पर लौटें",
                            fontWeight = FontWeight.Bold,
                            color = CrimsonSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TicketServiceItem(label: String, value: String, price: String) {
    if (value != "None") {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = label, fontSize = 11.sp, color = Color.Gray)
                Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
            Text(text = price, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = SaffronPrimary)
        }
    }
}

private fun sendWhatsApp(context: Context, phoneNumber: String, message: String) {
    try {
        val url = "https://api.whatsapp.com/send?phone=$phoneNumber&text=" + URLEncoder.encode(message, "UTF-8")
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "WhatsApp is not installed.", Toast.LENGTH_SHORT).show()
    }
}
