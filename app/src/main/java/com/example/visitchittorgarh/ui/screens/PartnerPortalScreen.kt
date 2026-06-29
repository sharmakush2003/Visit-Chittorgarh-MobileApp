package com.example.visitchittorgarh.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Offset
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.visitchittorgarh.data.BookingManager
import com.example.visitchittorgarh.data.TravelPass
import com.example.visitchittorgarh.theme.CrimsonDark
import com.example.visitchittorgarh.theme.CrimsonSecondary
import com.example.visitchittorgarh.theme.GoldAccent
import com.example.visitchittorgarh.theme.SaffronPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartnerPortalScreen(
    isEnglish: Boolean,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    var isUnlocked by remember { mutableStateOf(false) }
    var pinInput by remember { mutableStateOf("") }
    var passCodeQuery by remember { mutableStateOf("") }
    var activePass by remember { mutableStateOf<TravelPass?>(null) }

    // Form inputs for updating active pass
    var driverName by remember { mutableStateOf("") }
    var guideName by remember { mutableStateOf("") }
    var paymentStatus by remember { mutableStateOf("Pending") }
    var redeemedTaxi by remember { mutableStateOf(false) }
    var redeemedHotel by remember { mutableStateOf(false) }
    var redeemedGuide by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isEnglish) "Partner Portal" else "साझेदार पोर्टल",
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = SaffronPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (!isUnlocked) {
                // PIN Lock screen
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .background(SaffronPrimary.copy(alpha = 0.12f), RoundedCornerShape(20.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = SaffronPrimary,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = if (isEnglish) "Verification PIN Required" else "सत्यापन पिन आवश्यक है",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        fontFamily = FontFamily.Serif
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (isEnglish) "Enter the 4-digit partner PIN to proceed" else "आगे बढ़ने के लिए 4-अंकीय साझेदार पिन दर्ज करें",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    OutlinedTextField(
                        value = pinInput,
                        onValueChange = {
                            if (it.length <= 4) pinInput = it
                        },
                        label = { Text(if (isEnglish) "Security PIN" else "सुरक्षा पिन") },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier.width(200.dp),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (pinInput == "1234") {
                                isUnlocked = true
                            } else {
                                Toast.makeText(context, "Incorrect PIN. Try again.", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.width(200.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CrimsonSecondary)
                    ) {
                        Text(
                            text = if (isEnglish) "Unlock Panel" else "पैनल अनलॉक करें",
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif
                        )
                    }
                }
            } else {
                // Administrative Dashboard
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(24.dp)
                ) {
                    // Search box
                    Text(
                        text = if (isEnglish) "Verify Travel Pass Code" else "ट्रैवल पास कोड सत्यापित करें",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        fontFamily = FontFamily.Serif,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = passCodeQuery,
                            onValueChange = { passCodeQuery = it },
                            placeholder = { Text(if (isEnglish) "e.g., CT1294" else "जैसे, CT1294") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )

                        Button(
                            onClick = {
                                val pass = BookingManager.getPass(passCodeQuery)
                                if (pass != null) {
                                    activePass = pass
                                    driverName = pass.driverName
                                    guideName = pass.guideName
                                    paymentStatus = pass.paymentStatus
                                    redeemedTaxi = pass.redeemed_taxi
                                    redeemedHotel = pass.redeemed_hotel
                                    redeemedGuide = pass.redeemed_guide
                                } else {
                                    Toast.makeText(context, "No pass found for this code.", Toast.LENGTH_SHORT).show()
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                            modifier = Modifier.height(56.dp)
                        ) {
                            Icon(Icons.Default.Search, contentDescription = "Search")
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    activePass?.let { pass ->
                        // Pass details ticket preview (royal pass layout)
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 20.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
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
                                        .padding(20.dp)
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
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = if (isEnglish) "Chittorgarh Tourism Portal" else "चित्तौड़गढ़ पर्यटन पोर्टल",
                                                color = Color.White,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold,
                                                fontFamily = FontFamily.Serif,
                                                modifier = Modifier.weight(1f)
                                            )
                                            Box(
                                                modifier = Modifier
                                                    .background(
                                                        if (paymentStatus == "Received") Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                                                        RoundedCornerShape(6.dp)
                                                    )
                                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                                            ) {
                                                Text(
                                                    text = if (paymentStatus == "Received") "PAID" else "PENDING",
                                                    color = if (paymentStatus == "Received") Color(0xFF2E7D32) else Color(0xFFC62828),
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 10.sp
                                                )
                                            }
                                        }
                                    }
                                }

                                // Dashed line
                                Canvas(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(1.dp)
                                        .background(MaterialTheme.colorScheme.surface)
                                ) {
                                    val pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(12f, 12f), 0f)
                                    drawLine(
                                        color = Color.Gray.copy(alpha = 0.5f),
                                        start = Offset(0f, 0f),
                                        end = Offset(size.width, 0f),
                                        pathEffect = pathEffect,
                                        strokeWidth = 2f
                                    )
                                }

                                // Pass details
                                Column(modifier = Modifier.padding(20.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text(text = if (isEnglish) "TRAVELER" else "यात्री", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                            Text(text = pass.name, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                        }
                                        Column(horizontalAlignment = Alignment.End) {
                                            Text(text = if (isEnglish) "PASS CODE" else "पास कोड", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                            Text(text = pass.passCode, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = SaffronPrimary, fontFamily = FontFamily.Serif)
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(14.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text(text = if (isEnglish) "ARRIVAL DATE" else "आगमन की तिथि", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                            Text(text = pass.date, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                        }
                                        Column(horizontalAlignment = Alignment.End) {
                                            Text(text = if (isEnglish) "ARRIVAL TIME" else "आगमन का समय", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                            Text(text = pass.arrivalTime, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(14.dp))
                                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
                                    Spacer(modifier = Modifier.height(12.dp))

                                    Text(
                                        text = if (isEnglish) "INCLUDED SERVICES" else "शामिल सेवाएं",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CrimsonSecondary
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))

                                    val tPrice = pass.transportPrice
                                    val hPrice = pass.hotelPrice
                                    val gPrice = pass.guidePrice
                                    val total = tPrice + hPrice + gPrice

                                    if (pass.transport != "None") {
                                        Row(
                                            modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(text = if (isEnglish) "Transport (${pass.transport})" else "परिवहन (${pass.transport})", fontSize = 13.sp)
                                            Text(text = "₹${tPrice.toInt()}", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }

                                    if (pass.hotel != "None") {
                                        Row(
                                            modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(text = if (isEnglish) "Stay (${pass.hotel})" else "होटल (${pass.hotel})", fontSize = 13.sp)
                                            Text(text = "₹${hPrice.toInt()}", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }

                                    if (pass.guide != "None") {
                                        Row(
                                            modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(text = if (isEnglish) "Guide (${pass.guide})" else "गाइड (${pass.guide})", fontSize = 13.sp)
                                            Text(text = "₹${gPrice.toInt()}", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }

                                    HorizontalDivider(
                                        modifier = Modifier.padding(vertical = 12.dp),
                                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                                    )

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = if (isEnglish) "TOTAL ESTIMATE" else "कुल अनुमान", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Text(
                                            text = "₹${total.toInt()}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp,
                                            color = SaffronPrimary,
                                            fontFamily = FontFamily.Serif
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))

                                    // Barcode
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(60.dp)
                                            .background(Color.White)
                                            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                            repeat(24) { i ->
                                                val w = if (i % 3 == 0) 4.dp else if (i % 2 == 0) 2.dp else 1.dp
                                                Box(
                                                    modifier = Modifier
                                                        .width(w)
                                                        .fillMaxHeight(0.6f)
                                                        .background(Color.Black)
                                                )
                                            }
                                        }
                                        Text(
                                            text = "* ${pass.passCode} *",
                                            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 2.dp),
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.DarkGray
                                        )
                                    }
                                }
                            }
                        }

                        // Administrative controls card (below ticket preview)
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            border = BorderStroke(1.dp, SaffronPrimary.copy(alpha = 0.2f)),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                // Operations Form
                                Text(
                                    text = if (isEnglish) "ASSIGNMENTS & REDEMPTION" else "असाइनमेंट और मोचन",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CrimsonSecondary
                                )
                                Spacer(modifier = Modifier.height(16.dp))

                                OutlinedTextField(
                                    value = driverName,
                                    onValueChange = { driverName = it },
                                    label = { Text(if (isEnglish) "Assign Driver" else "चालक का नाम") },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))

                                OutlinedTextField(
                                    value = guideName,
                                    onValueChange = { guideName = it },
                                    label = { Text(if (isEnglish) "Assign Guide Name" else "गाइड का नाम") },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp)
                                )

                                Spacer(modifier = Modifier.height(20.dp))

                                // Status Toggles
                                Text(text = "Payment Status", fontSize = 12.sp, color = Color.Gray)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        RadioButton(
                                            selected = paymentStatus == "Pending",
                                            onClick = { paymentStatus = "Pending" },
                                            colors = RadioButtonDefaults.colors(selectedColor = SaffronPrimary)
                                        )
                                        Text(text = "Pending", fontSize = 14.sp)
                                    }
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        RadioButton(
                                            selected = paymentStatus == "Received",
                                            onClick = { paymentStatus = "Received" },
                                            colors = RadioButtonDefaults.colors(selectedColor = SaffronPrimary)
                                        )
                                        Text(text = "Received", fontSize = 14.sp)
                                    }
                                }

                                Spacer(modifier = Modifier.height(20.dp))

                                // Redemption switches
                                Text(text = "Service Redemption", fontSize = 12.sp, color = Color.Gray)
                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = "Redeem Taxi (Cab option)", fontSize = 14.sp)
                                    Switch(
                                        checked = redeemedTaxi,
                                        onCheckedChange = { redeemedTaxi = it },
                                        colors = SwitchDefaults.colors(checkedThumbColor = SaffronPrimary)
                                    )
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = "Redeem Hotel Stay", fontSize = 14.sp)
                                    Switch(
                                        checked = redeemedHotel,
                                        onCheckedChange = { redeemedHotel = it },
                                        colors = SwitchDefaults.colors(checkedThumbColor = SaffronPrimary)
                                    )
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = "Redeem Guide service", fontSize = 14.sp)
                                    Switch(
                                        checked = redeemedGuide,
                                        onCheckedChange = { redeemedGuide = it },
                                        colors = SwitchDefaults.colors(checkedThumbColor = SaffronPrimary)
                                    )
                                }

                                Spacer(modifier = Modifier.height(24.dp))

                                Button(
                                    onClick = {
                                        val updated = pass.copy(
                                            driverName = driverName,
                                            guideName = guideName,
                                            paymentStatus = paymentStatus,
                                            redeemed_taxi = redeemedTaxi,
                                            redeemed_hotel = redeemedHotel,
                                            redeemed_guide = redeemedGuide
                                        )
                                        BookingManager.updatePass(updated)
                                        activePass = updated
                                        Toast.makeText(context, "Pass details updated successfully!", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonSecondary)
                                ) {
                                    Text(
                                        text = if (isEnglish) "Update Pass details" else "पास विवरण अपडेट करें",
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Serif
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
