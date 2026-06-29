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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.visitchittorgarh.data.TourPackage
import com.example.visitchittorgarh.theme.CrimsonDark
import com.example.visitchittorgarh.theme.CrimsonSecondary
import com.example.visitchittorgarh.theme.GoldAccent
import com.example.visitchittorgarh.theme.SaffronPrimary
import java.net.URLEncoder

@Composable
fun PackagesScreen(
    packages: List<TourPackage>,
    isEnglish: Boolean,
    onBookingPassClick: (String, String, Double, String, Double, String, Double) -> Unit
) {
    var selectedPackageForBooking by remember { mutableStateOf<TourPackage?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // Top Header Title Card
        item {
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
                        text = if (isEnglish) "Royal Expeditions" else "शाही यात्राएं",
                        color = GoldAccent,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isEnglish) 
                            "Select an all-inclusive heritage package and experience Chittorgarh like royalty." 
                            else "एक सर्व-समावेशी विरासत पैकेज चुनें और चित्तौड़गढ़ का अनुभव राजाओं की तरह करें।",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 13.sp,
                        fontFamily = FontFamily.SansSerif
                    )
                }
            }
        }

        // Package Cards List (Flat, seamless layout)
        items(packages) { pkg ->
            PackageCard(
                pkg = pkg,
                isEnglish = isEnglish,
                onBookClick = { selectedPackageForBooking = pkg }
            )
        }
    }

    // Booking Dialog Form (Ticket Style)
    selectedPackageForBooking?.let { pkg ->
        BookingFormDialog(
            pkg = pkg,
            isEnglish = isEnglish,
            onBookingPassClick = onBookingPassClick,
            onDismiss = { selectedPackageForBooking = null }
        )
    }
}

@Composable
fun PackageCard(
    pkg: TourPackage,
    isEnglish: Boolean,
    onBookClick: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                AsyncImage(
                    model = pkg.imageUrl,
                    contentDescription = pkg.title.get(isEnglish),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                )
                // Left badge - Gold Royal Tag
                Box(
                    modifier = Modifier
                        .padding(12.dp)
                        .background(SaffronPrimary, shape = RoundedCornerShape(6.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                        .align(Alignment.TopStart)
                ) {
                    Text(
                        text = if (isEnglish) "HERITAGE TOUR" else "धरोहर यात्रा",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = pkg.title.get(isEnglish),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = pkg.duration.get(isEnglish),
                            color = SaffronPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Box(
                        modifier = Modifier
                            .background(CrimsonSecondary, shape = RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = pkg.price.get(isEnglish),
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = pkg.summary.get(isEnglish),
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Accordion Trigger for Detailed Itinerary
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isExpanded = !isExpanded }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isExpanded) {
                            if (isEnglish) "Hide Itinerary Details" else "यात्रा कार्यक्रम विवरण छिपाएं"
                        } else {
                            if (isEnglish) "Show Detailed Itinerary" else "विस्तृत यात्रा कार्यक्रम दिखाएं"
                        },
                        color = CrimsonSecondary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = CrimsonSecondary
                    )
                }

                // Itinerary Items list
                AnimatedVisibility(
                    visible = isExpanded,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(12.dp)
                    ) {
                        pkg.itinerary.forEachIndexed { i, step ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Box(
                                    modifier = Modifier
                                        .padding(top = 2.dp, end = 10.dp)
                                        .size(18.dp)
                                        .background(SaffronPrimary.copy(alpha = 0.15f), shape = RoundedCornerShape(4.dp))
                                        .border(0.5.dp, SaffronPrimary, shape = RoundedCornerShape(4.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = (i + 1).toString(),
                                        color = SaffronPrimary,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Text(
                                    text = step.get(isEnglish),
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Booking CTA
                Button(
                    onClick = onBookClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CrimsonSecondary,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                ) {
                    Text(
                        text = if (isEnglish) "Request Royal Booking" else "शाही बुकिंग का अनुरोध करें",
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        fontSize = 15.sp,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}

@Composable
fun BookingFormDialog(
    pkg: TourPackage,
    isEnglish: Boolean,
    onBookingPassClick: (String, String, Double, String, Double, String, Double) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var fullName by remember { mutableStateOf("") }
    var travelDate by remember { mutableStateOf("") }
    var numGuests by remember { mutableStateOf("1") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                // Ticket Header Section (Velvet Maroon)
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
                            text = if (isEnglish) "BOARDING PASS / EXPEDITION VOUCHER" else "बोर्डिंग पास / यात्रा वाउचर",
                            color = SaffronPrimary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp,
                            fontFamily = FontFamily.Serif
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = pkg.title.get(isEnglish),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = pkg.duration.get(isEnglish),
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = pkg.price.get(isEnglish),
                                color = GoldAccent,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif
                            )
                        }
                    }
                }

                // Dashed Tear Line Divider
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 12f), 0f)
                    drawLine(
                        color = SaffronPrimary.copy(alpha = 0.6f),
                        start = Offset(0f, 0f),
                        end = Offset(size.width, 0f),
                        pathEffect = pathEffect,
                        strokeWidth = 3f
                    )
                }

                // Ticket Content & Passenger Input fields
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = if (isEnglish) "PASSENGER DETAILS" else "यात्री विवरण",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                        fontFamily = FontFamily.Serif,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Form Fields
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text(if (isEnglish) "Lead Guest Full Name" else "मुख्य अतिथि का पूरा नाम") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Name", tint = SaffronPrimary) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SaffronPrimary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = travelDate,
                        onValueChange = { travelDate = it },
                        label = { Text(if (isEnglish) "Travel Date (e.g. 28-06-2026)" else "यात्रा की तारीख (उदा. 28-06-2026)") },
                        leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = "Date", tint = SaffronPrimary) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SaffronPrimary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = numGuests,
                        onValueChange = { numGuests = it },
                        label = { Text(if (isEnglish) "Number of Guests" else "अतिथियों की संख्या") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Guests", tint = SaffronPrimary) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SaffronPrimary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Actions: Customize Pass, WhatsApp Concierge or Call
                    Button(
                        onClick = {
                            val transport = when (pkg.id) {
                                "pkg_1" -> "Royal SUV (Innova)"
                                "pkg_2" -> "Luxury Sedan (Dzire)"
                                "pkg_3" -> "Royal SUV (Innova)"
                                else -> "None"
                            }
                            val transportPrice = when (pkg.id) {
                                "pkg_1" -> 2000.0
                                "pkg_2" -> 1500.0
                                "pkg_3" -> 2000.0
                                else -> 0.0
                            }
                            val hotel = when (pkg.id) {
                                "pkg_1" -> "None"
                                "pkg_2" -> "Boutique Hotel"
                                "pkg_3" -> "Heritage Palace"
                                else -> "None"
                            }
                            val hotelPrice = when (pkg.id) {
                                "pkg_1" -> 0.0
                                "pkg_2" -> 3000.0
                                "pkg_3" -> 5000.0
                                else -> 0.0
                            }
                            val guide = when (pkg.id) {
                                "pkg_1" -> "History Scholar"
                                "pkg_2" -> "History Scholar"
                                "pkg_3" -> "Storyteller"
                                else -> "None"
                            }
                            val guidePrice = when (pkg.id) {
                                "pkg_1" -> 1500.0
                                "pkg_2" -> 1500.0
                                "pkg_3" -> 1000.0
                                else -> 0.0
                            }
                            onDismiss()
                            onBookingPassClick(
                                pkg.title.get(isEnglish),
                                transport,
                                transportPrice,
                                hotel,
                                hotelPrice,
                                guide,
                                guidePrice
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SaffronPrimary,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = if (isEnglish) "Customize & Generate Royal Pass" else "अनुकूलित करें और शाही पास जनरेट करें",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            if (fullName.isBlank() || travelDate.isBlank()) {
                                Toast.makeText(
                                    context, 
                                    if (isEnglish) "Please fill out passenger details" else "कृपया यात्री का विवरण भरें", 
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@Button
                            }
                            val message = "Hello Visit Chittorgarh! I want to request a booking for *${pkg.title.get(isEnglish)}*.\n\n" +
                                    "*Guest Name:* $fullName\n" +
                                    "*Travel Date:* $travelDate\n" +
                                    "*Total Guests:* $numGuests\n\n" +
                                    "Please process this request. Thank you."
                            sendWhatsAppMessage(context, "+917597451057", message)
                            onDismiss()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF25D366), // WhatsApp Green
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = if (isEnglish) "Confirm via WhatsApp Concierge" else "व्हाट्सएप द्वारपाल द्वारा पुष्टि करें",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedButton(
                        onClick = {
                            val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                                 data = Uri.parse("tel:+917597451057")
                            }
                            context.startActivity(dialIntent)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, CrimsonSecondary),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = CrimsonSecondary
                        )
                    ) {
                        Icon(Icons.Default.Call, contentDescription = "Call", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isEnglish) "Call Guest Desk" else "गेस्ट डेस्क पर कॉल करें",
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = if (isEnglish) "Cancel Request" else "अनुरोध रद्द करें", 
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

private fun sendWhatsAppMessage(context: Context, phoneNumber: String, message: String) {
    try {
        val url = "https://api.whatsapp.com/send?phone=$phoneNumber&text=" + URLEncoder.encode(message, "UTF-8")
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "WhatsApp is not installed on this device.", Toast.LENGTH_LONG).show()
    }
}
